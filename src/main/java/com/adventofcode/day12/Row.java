package com.adventofcode.day12;

import com.adventofcode.utils.Point2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public record Row(
    char[] springs,
    int[] groups
) {

    private static final char OPERATIONAL = '.';
    private static final char DAMAGED = '#';
    private static final char UNKNOWN = '?';

    public static Row parseLine(String line) {
        var parts = line.split(" ");
        var groups = Arrays.stream(parts[1].split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
        var springs = new char[parts[0].length() + 1];
        System.arraycopy(parts[0].toCharArray(), 0, springs, 0, parts[0].length());
        springs[parts[0].length()] = '?';
        return new Row(springs, groups);
    }

    public long getCombinations(int repeats) {
        var combinations = new HashMap<Point2D, Long>();
        combinations.put(new Point2D(0, 0), 1L);
        for (int i = 0; i < repeats; i++) {
            if (i == repeats - 1) {
                setLastSpring(OPERATIONAL);
            }

            var combinationsNew = new HashMap<Point2D, Long>();
            for (var entry : combinations.entrySet()) {
                var state = entry.getKey();
                getCombinationsRecursive(state.x() % springs.length, state.y(), entry.getValue(), combinationsNew);
            }
            combinations = combinationsNew;
        }

        setLastSpring(UNKNOWN);
        return combinations.entrySet().stream()
            .filter(entry -> entry.getKey().y() == repeats * groups.length)
            .mapToLong(Map.Entry::getValue)
            .sum();
    }

    private void setLastSpring(char value) {
        springs[springs.length - 1] = value;
    }

    private void getCombinationsRecursive(
        int iSprings,
        int iGroups,
        long factor,
        Map<Point2D, Long> combinations
    ) {
        if (iSprings >= springs.length) {
            var state = new Point2D(iSprings, iGroups);
            var count = combinations.getOrDefault(state, 0L);
            combinations.put(state, count + factor);
            return;
        }

        var group = groups[iGroups % groups.length];
        var canBeEmpty = true;
        for (int i = iSprings; i < springs.length; i++) {
            var isGroup = true;
            for (int j = 0; j < group; j++) {
                if (springs[(i + j) % springs.length] == OPERATIONAL) {
                    isGroup = false;
                    break;
                }
            }

            if (isGroup && springs[(i + group) % springs.length] != DAMAGED) {
                // Valid group from [iSprings, iSprings + group) - can be it overlaps to next springs array
                getCombinationsRecursive(i + group + 1, iGroups + 1, factor, combinations);
            }

            if (springs[i] == DAMAGED) {
                canBeEmpty = false;
                break;
            }
        }

        if (canBeEmpty) {
            getCombinationsRecursive(springs.length, iGroups, factor, combinations);
        }
    }
}
