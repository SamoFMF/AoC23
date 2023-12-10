package com.adventofcode.day03;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Day03 {

    private static Set<String> numbers = new HashSet<>();

    static {
        IntStream.range(0, 10)
            .mapToObj(String::valueOf)
            .forEach(numbers::add);
    }

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input03.txt");
        var grid = new Grid(input);

        part01(grid);
        part02(grid);
    }

    private static void part01(Grid grid) {
        var result = grid.getAdjacentNumbers()
            .stream()
            .mapToInt(Number::value)
            .sum();

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var result = grid.getGearRatios().sum();

        System.out.println(result);
    }
}
