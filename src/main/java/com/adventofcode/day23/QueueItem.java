package com.adventofcode.day23;

import com.adventofcode.utils.Point2D;

public record QueueItem(
    int prevNode,
    Point2D prev,
    Point2D current
) {
}
