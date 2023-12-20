package com.adventofcode.day17;

import com.adventofcode.utils.Point2D;

public record PriorityItem(
    Crucible crucible,
    int heat,
    int heuristic
) {

    public Point2D position() {
        return crucible.position();
    }

    public Point2D direction() {
        return crucible.direction();
    }

    public int count() {
        return crucible.count();
    }
}
