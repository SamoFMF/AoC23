package com.adventofcode.day10;

import com.adventofcode.utils.Point2D;

public record Move(
    int x,
    int y,
    Point2D direction
) {
}
