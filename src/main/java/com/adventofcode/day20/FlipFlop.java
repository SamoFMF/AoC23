package com.adventofcode.day20;

import java.util.Deque;
import java.util.Objects;

public class FlipFlop extends Module {

    private boolean state;

    public FlipFlop(String name, String[] destinations) {
        super(name, destinations);
        state = false;
    }

    @Override
    public String getSignedName() {
        return "%" + super.getSignedName();
    }

    @Override
    public void handlePulse(String from, boolean pulse, Deque<Pulse> pulses) {
        if (pulse) {
            return;
        }

        state = !state;
        broadcast(state, pulses);
    }

    @Override
    public String toString() {
        return "%{" +
            toStringNoBrackets() + "," +
            "state:" + state +
            "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, state);
    }
}
