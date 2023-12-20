package com.adventofcode.day16;

import com.adventofcode.utils.Point2D;

public record Beam(Point2D position, Point2D direction) {

    public Point2D move() {
        return new Point2D(position.x() + direction.x(), position.y() + direction.y());
    }
}
