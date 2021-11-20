package br.com.drbandrade.dscatalog.activities.junitvanilla;

import lombok.Getter;

@Getter
public class Financing {

    private double totalAmount;
    private double income;
    private int months;

    public Financing(double totalAmount, double income, int months) {
        this.totalAmount = totalAmount;
        this.income = income;
        this.months = months;
    }

    public static Financing generateFinancing(double totalAmount, double income, int months) {
        validateValues(totalAmount,income,months);
        return new Financing(totalAmount,income,months);
    }

    private static void validateValues(double totalAmount, double income, int months) {
        if(totalAmount<0||income<0||months<0) throw new IllegalArgumentException();
        if(totalAmount*0.8/months>income/2) throw new IllegalArgumentException();
    }


    public double entry() {
        return totalAmount*0.2;
    }

    public double quota(){
        return (totalAmount-entry())/months;
    }

    public void setTotalAmount(double totalAmount) {
        validateValues(totalAmount,income,months);
        this.totalAmount = totalAmount;
    }

    public void setIncome(double income) {
        validateValues(totalAmount,income,months);
        this.income = income;
    }

    public void setMonths(int months) {
        validateValues(totalAmount,income,months);
        this.months = months;
    }
}
