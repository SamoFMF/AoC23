package com.adventofcode.day13;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day13 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input13.txt");
        var patterns = new ArrayList<Pattern>();
        var pattern = new ArrayList<String>();
        for (var line : input) {
            if (line.trim().isEmpty()) {
                patterns.add(new Pattern(pattern));
                pattern = new ArrayList<>();
            } else {
                pattern.add(line);
            }
        }
        patterns.add(new Pattern(pattern));

        part01(patterns);
        part02(patterns);
    }

    private static void part01(List<Pattern> patterns) {
        var result = patterns.stream()
            .mapToInt(pattern -> pattern.getMirrorVertical(0) + 100 * pattern.getMirrorHorizontal(0))
            .sum();

        System.out.println(result);
    }

    private static void part02(List<Pattern> patterns) {
        var result = patterns.stream()
            .mapToInt(pattern -> pattern.getMirrorVertical(1) + 100 * pattern.getMirrorHorizontal(1))
            .sum();

        System.out.println(result);
    }
}
