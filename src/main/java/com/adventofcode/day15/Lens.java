package com.adventofcode.day15;

public class Lens {

    private final String label;
    private int length;

    public Lens(String label, int length) {
        this.label = label;
        this.length = length;
    }

    public String getLabel() {
        return label;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
