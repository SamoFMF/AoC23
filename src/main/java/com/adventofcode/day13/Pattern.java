package com.adventofcode.day13;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public record Pattern(
    List<String> pattern,
    Set<Integer> bannedHorizontal,
    Set<Integer> bannedVertical
) {

    public Pattern(List<String> pattern) {
        this(pattern, new HashSet<>(), new HashSet<>());
    }

    public int getMirrorHorizontal(int validDifference) {
        return getMirror(pattern.size(), this::countDifferencesHorizontal, validDifference, bannedHorizontal);
    }

    public int getMirrorVertical(int validDifference) {
        return getMirror(pattern.get(0).length(), this::countDifferencesVertical, validDifference, bannedVertical);
    }

    private int getMirror(
        int size,
        BiFunction<Integer, Integer, Integer> countDifference,
        int validDifference,
        Set<Integer> banned
    ) {
        for (int i = 0; i < size - 1; i++) {
            if (banned.contains(i)) {
                continue;
            }
            var differences = 0;
            for (int j = 0; j < Math.min(i + 1, size - i - 1); j++) {
                differences += countDifference.apply(i - j, i + j + 1);
                if (differences > validDifference) break;
            }
            if (differences == validDifference) {
                banned.add(i);
                return i + 1;
            }
        }

        return 0;
    }

    private int countDifferencesHorizontal(int i, int j) {
        var line1 = pattern.get(i);
        var line2 = pattern.get(j);

        return IntStream.range(0, line1.length())
            .map(k -> line1.charAt(k) == line2.charAt(k) ? 0 : 1)
            .sum();
    }

    private int countDifferencesVertical(int i, int j) {
        return pattern.stream()
            .mapToInt(line -> line.charAt(i) == line.charAt(j) ? 0 : 1)
            .sum();
    }
}
