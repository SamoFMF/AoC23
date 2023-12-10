package com.adventofcode.day02;

import java.util.Arrays;
import java.util.List;

public record Game(
    int id,
    List<CubeSet> sets
) {

    public boolean isValid(CubeSet availableCubes) {
        return sets.stream().allMatch(cubeSet -> cubeSet.isValid(availableCubes));
    }

    public int getPower() {
        var minRequired = sets.stream()
            .reduce(new CubeSet(0, 0, 0), CubeSet::minRequired);

        return minRequired.red() * minRequired.green() * minRequired.blue();
    }

    public static Game parseLine(String line) {
        var parts = line.split(": ");
        var id = parts[0].split(" ")[1];

        var sets = Arrays.stream(parts[1].split("; "))
            .map(CubeSet::parseString)
            .toList();

        return new Game(Integer.parseInt(id), sets);
    }
}
