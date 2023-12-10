package com.adventofcode.day07;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class Day07 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input07.txt");

        part01(input);
        part02(input);
    }

    private static void part01(List<String> input) {
        solve(input, false);
    }

    private static void part02(List<String> input) {
        solve(input, true);
    }

    private static void solve(List<String> input, boolean isPart02) {
        var hands = input.stream()
            .map(line -> Hand.parseLine(line, isPart02))
            .sorted(Hand::compare)
            .toList();

        var result = IntStream.range(0, hands.size())
            .map(i -> (i + 1) * hands.get(i).bet())
            .sum();
        System.out.println(result);
    }
}
