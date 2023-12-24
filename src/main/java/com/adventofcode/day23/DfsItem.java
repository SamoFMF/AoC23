package com.adventofcode.day23;

import com.adventofcode.utils.Point2D;

import java.util.Set;

public record DfsItem(
    Point2D position,
    Set<Point2D> visited
) {
}
