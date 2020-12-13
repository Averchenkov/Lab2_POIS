package ru.mirea.pois;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import ru.mirea.pois.data.*;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TestAll {

    @Test
    public void test1() {
        try {
            InternalKnowledgeBase kBase = prepareKnowledgeBase(Arrays.asList(
                    new File("rules/payment.drl")));
            KieSession kSession = kBase.newKieSession();
            // typically we want to consider today but we may decide to schedule
            // emails in the future or we may want to run tests using a different date
            LocalDate dayToConsider = LocalDate.now();

            loadDataIntoSession(kSession, new Mortgage(
                    "Ilya",
                    1500000,
                    9.6,
                    20
            ));

            kSession.fireAllRules();
            Model model = (Model) kSession.getGlobal("model");

            Assert.assertEquals(12516.0, model.getPayment(), 1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            InternalKnowledgeBase kBase = prepareKnowledgeBase(Arrays.asList(
                    new File("rules/payment.drl")));
            KieSession kSession = kBase.newKieSession();
            // typically we want to consider today but we may decide to schedule
            // emails in the future or we may want to run tests using a different date
            LocalDate dayToConsider = LocalDate.now();

            loadDataIntoSession(kSession, new Mortgage(
                    "Maksim",
                    10000000,
                    11.5,
                    30
            ));

            kSession.fireAllRules();
            Model model = (Model) kSession.getGlobal("model");

            Assert.assertEquals(96147.0, model.getPayment(), 1.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadDataIntoSession(KieSession kieSession, Mortgage mortgage) {


        kieSession.insert(mortgage);

        kieSession.setGlobal("model", new Model());

    }

    private InternalKnowledgeBase prepareKnowledgeBase(List<File> files) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        files.forEach(i -> knowledgeBuilder.add(ResourceFactory.newFileResource(i), ResourceType.DRL));

        KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();

        if (!errors.isEmpty()) {
            for (KnowledgeBuilderError error : errors) {
                System.out.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(knowledgeBuilder.getKnowledgePackages());

        return kBase;
    }
}
