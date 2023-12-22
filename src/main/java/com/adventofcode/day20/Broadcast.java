package com.adventofcode.day20;

import java.util.Deque;

public class Broadcast extends Module {

    public Broadcast(String name, String[] destinations) {
        super(name, destinations);
    }

    @Override
    public void handlePulse(String from, boolean pulse, Deque<Pulse> pulses) {
        broadcast(pulse, pulses);
    }

    @Override
    public String toString() {
        return "{" + toStringNoBrackets() + "}";
    }
}
