package com.adventofcode.day17;

import com.adventofcode.utils.Point2D;

public record Crucible(
    Point2D position,
    Point2D direction,
    int count
) {
}
