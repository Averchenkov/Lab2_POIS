package ru.mirea.pois.data;

public class Mortgage {
    private String name;
    private int value;
    private double percent;
    private int time;

    public Mortgage(String name, int value, double percent, int time) {
        this.name = name;
        this.value = value;
        this.percent = percent;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }


}
