package com.adventofcode.day05;

public record Interval(
    long start,
    long end
) {

    public boolean isEmpty() {
        return start == end;
    }
}
