package com.adventofcode.day24;

public record Vector(
    long x,
    long y,
    long z
) {

    public Vector(long[] values) {
        this(values[0], values[1], values[2]);
    }

    public long cross2D(Vector vector) {
        return x * vector.y() - y * vector.x();
    }

    public Vector minus(Vector vector) {
        return new Vector(x - vector.x(), y - vector.y(), z - vector.z());
    }
}
