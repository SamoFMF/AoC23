package com.adventofcode.day09;

import com.adventofcode.utils.Pair;
import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class Day09 {

    public static void main(String[] args) throws FileNotFoundException {
        var pairs = Utils.readFileStream("inputs/input09.txt")
            .map(line -> Arrays.stream(line.split(" "))
                .mapToLong(Long::parseLong)
                .toArray())
            .map(Day09::handleSequence)
            .toList();

        part01(pairs);
        part02(pairs);
    }

    private static void part01(List<Pair<Long>> pairs) {
        var result = pairs.stream()
            .mapToLong(Pair::second)
            .sum();

        System.out.println(result);
    }

    private static void part02(List<Pair<Long>> pairs) {
        var result = pairs.stream()
            .mapToLong(Pair::first)
            .sum();

        System.out.println(result);
    }

    private static Pair<Long> handleSequence(long[] sequence) {
        var next = 0L;
        var prev = 0L;
        var condition = true;
        var sign = 1;
        while (condition) {
            condition = false;
            next += sequence[sequence.length - 1];
            prev += sign * sequence[0];
            sign *= -1;
            var newSequence = new long[sequence.length - 1];
            for (int i = 0; i < sequence.length - 1; i++) {
                newSequence[i] = sequence[i + 1] - sequence[i];
                if (newSequence[i] != 0) {
                    condition = true;
                }
            }
            sequence = newSequence;
        }
        return new Pair<>(prev, next);
    }
}
