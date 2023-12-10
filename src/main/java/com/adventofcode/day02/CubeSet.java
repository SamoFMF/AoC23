package com.adventofcode.day02;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record CubeSet(
    int red,
    int green,
    int blue
) {

    public boolean isValid(CubeSet availableCubes) {
        return availableCubes.red() >= red
            && availableCubes.green() >= green
            && availableCubes.blue() >= blue;
    }

    public static CubeSet minRequired(CubeSet cubeSet1, CubeSet cubeSet2) {
        return new CubeSet(
            Math.max(cubeSet1.red(), cubeSet2.red()),
            Math.max(cubeSet1.green(), cubeSet2.green()),
            Math.max(cubeSet1.blue(), cubeSet2.blue())
        );
    }

    public static CubeSet parseString(String str) {
        var pattern = Pattern.compile("(\\d+) (red|green|blue)");
        var matcher = pattern.matcher(str);

        var results = matcher.results()
            .collect(Collectors.toMap(match -> match.group(2), match -> Integer.parseInt(match.group(1))));

        return new CubeSet(
            results.getOrDefault("red", 0),
            results.getOrDefault("green", 0),
            results.getOrDefault("blue", 0)
        );
    }
}
