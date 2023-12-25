package com.adventofcode.day25;

public class Edge {

    private String first;
    private String second;

    public Edge(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public Edge(Edge edge) {
        this.first = edge.first;
        this.second = edge.second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + "," + second + ")";
    }
}
