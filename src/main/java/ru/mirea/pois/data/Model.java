package ru.mirea.pois.data;

public class Model {
    Mortgage mortgage;
    double payment;

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Mortgage getMortgage() {
        return mortgage;
    }

    public void setMortgage(Mortgage mortgage) {
        this.mortgage = mortgage;
    }


    public void printResult(double payment){
        System.out.println("Name: " + mortgage.getName() + "\nMortgage loan amount: " + mortgage.getValue() + "\nLoan terms: " + mortgage.getTime() + "\nInterest rate: " + mortgage.getPercent() + "\nMonthly payment: " + payment + "\n\n\n\n\n\n\n");
    }


}
