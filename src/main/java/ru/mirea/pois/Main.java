package ru.mirea.pois;


import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
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
public class Main {

    private static void loadDataIntoSession(KieSession kieSession, LocalDate dayToConsider) {

        Mortgage mortgage1 = new Mortgage(
                "Ilya",
                1500000,
                9.6,
                20
        );
        Mortgage mortgage2 = new Mortgage(
                "Maksim",
                10000000,
                11.5,
                30
        );
        kieSession.insert(mortgage1);
        kieSession.insert(mortgage2);

        kieSession.setGlobal("model", new Model());

    }

    private static InternalKnowledgeBase readKnoledgeBase(List<File> files) {
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        files.forEach(i -> knowledgeBuilder.add(ResourceFactory.newFileResource(i), ResourceType.DRL));

        KnowledgeBuilderErrors errors = knowledgeBuilder.getErrors();

        if (!errors.isEmpty()) {
            for (KnowledgeBuilderError error : errors) {
                log.error("{}", error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }

        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(knowledgeBuilder.getKnowledgePackages());

        return kBase;
    }


    public static void showSending(KieSession kieSession) {
        log.info("Showing results");
        log.info("{}", kieSession);
    }

    public static void main(String[] args) {
        try {
            InternalKnowledgeBase kBase = readKnoledgeBase(Arrays.asList(
                    new File("rules/payment.drl")));
            KieSession kSession = kBase.newKieSession();
            // typically we want to consider today but we may decide to schedule
            // emails in the future or we may want to run tests using a different date
            LocalDate dayToConsider = LocalDate.now();
            loadDataIntoSession(kSession, dayToConsider);

            kSession.fireAllRules();

            showSending(kSession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
