package com.adventofcode.day20;

import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Conjunction extends Module {

    private final Set<String> highStates;

    public Conjunction(String name, String[] destinations) {
        super(name, destinations);
        highStates = new HashSet<>();
    }

    @Override
    public String getSignedName() {
        return "&" + super.getSignedName();
    }

    @Override
    public void handlePulse(String from, boolean pulse, Deque<Pulse> pulses) {
        if (pulse) {
            highStates.add(from);
        } else {
            highStates.remove(from);
        }

        broadcast(highStates.size() != inputs.size(), pulses);
    }

    @Override
    public String toString() {
        return "&{" +
            toStringNoBrackets() + "," +
            "highStates:" + highStates +
            "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, highStates);
    }
}
