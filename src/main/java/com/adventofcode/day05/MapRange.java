package com.adventofcode.day05;

public record MapRange(
    long startSource,
    long startDestination,
    long range
) {

    public long endSource() {
        return startSource + range;
    }

    public static MapRange parseLine(String line) {
        var values = line.split(" ");
        return new MapRange(
            Long.parseLong(values[1]),
            Long.parseLong(values[0]),
            Long.parseLong(values[2])
        );
    }
}
