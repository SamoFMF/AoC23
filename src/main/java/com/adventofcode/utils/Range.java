package com.adventofcode.utils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public record Range(
    int min,
    int max
) {

    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 4001;

    public Range(int value) {
        this(value, value);
    }

    public Optional<Range> overlap(Range range) {
        return Optional.of(new Range(Math.max(min, range.min()), Math.min(max, range.max())))
            .filter(Range::isValid);
    }

    public List<Range> minus(Range range) {
        if (range.max() <= min || range.min() >= max) {
            return List.of(this);
        }

        return Stream.of(
                new Range(min, range.min()),
                new Range(range.max(), max)
            )
            .filter(Range::isValid)
            .toList();
    }

    public int length() {
        return max - min;
    }

    public boolean isValid() {
        return min < max;
    }

    public boolean contains(int value) {
        return value >= min && value < max;
    }

    public static Range full() {
        return new Range(MIN_VALUE, MAX_VALUE);
    }

    public static Range greaterThan(int value) {
        return new Range(value + 1, MAX_VALUE);
    }

    public static Range lessThan(int value) {
        return new Range(MIN_VALUE, value);
    }
}
