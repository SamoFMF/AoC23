package com.adventofcode.day20;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

public abstract class Module {

    protected final String name;
    protected final List<String> inputs;
    private final Set<String> destinations;

    public Module(String name, String[] destinations) {
        this.name = name;

        this.destinations = Set.of(destinations);
        inputs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getSignedName() {
        return name;
    }

    public Set<String> getDestinations() {
        return destinations;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public void addInput(String input) {
        inputs.add(input);
    }

    public void broadcast(boolean pulse, Deque<Pulse> pulses) {
        for (var destination : destinations) {
            pulses.addLast(new Pulse(name, destination, pulse));
        }
    }

    public abstract void handlePulse(String from, boolean pulse, Deque<Pulse> pulses);

    protected String toStringNoBrackets() {
        return "name:" + name + "," +
            "destinations:" + destinations + "," +
            "inputs:" + inputs;
    }
}
