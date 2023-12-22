package com.adventofcode.day20;

import java.util.Deque;

public class Output extends Module {

    public Output(String name, String previous) {
        super(name, new String[0]);
        addInput(previous);
    }

    @Override
    public void handlePulse(String from, boolean pulse, Deque<Pulse> pulses) {
        if (!pulse) System.out.println("PRESSED ME");
    }

    @Override
    public String toString() {
        return "D{" + toStringNoBrackets() + "}";
    }
}
