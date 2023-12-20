package com.adventofcode.utils;

public record Point2D(
    int x,
    int y
) {

    public Point2D add(Point2D point) {
        return new Point2D(x + point.x(), y + point.y());
    }
}
