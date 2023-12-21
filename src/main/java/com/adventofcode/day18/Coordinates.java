package com.adventofcode.day18;

public record Coordinates(
    long x,
    long y
) {

    public Coordinates add(Coordinates coordinates) {
        return new Coordinates(x + coordinates.x(), y + coordinates.y());
    }
}
