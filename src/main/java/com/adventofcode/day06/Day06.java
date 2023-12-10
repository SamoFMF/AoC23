package com.adventofcode.day06;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day06 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input06.txt");

        part01(input);
        part02(input);
    }

    private static void part01(List<String> input) {
        solve(input, " ");
    }

    private static void part02(List<String> input) {
        solve(input, "");
    }

    private static void solve(List<String> input, String delimiter) {
        var times = parseLine(input.get(0), delimiter);
        var distances = parseLine(input.get(1), delimiter);
        var result = IntStream.range(0, times.length)
            .mapToLong(i -> getResult(times[i], distances[i]))
            .reduce(1, (x, y) -> x * y);

        System.out.println(result);
    }

    private static long[] parseLine(String line, String delimiter) {
        return Arrays.stream(line.split(":")[1]
                .trim()
                .replaceAll(" +", delimiter)
                .split(" "))
            .mapToLong(Long::parseLong)
            .toArray();
    }

    private static long getResult(long time, long distance) {
        var D = time * time - 4 * distance;
        var sqrtD = Math.sqrt(D);

        var delta = sqrtD == (int) sqrtD
            ? -1
            : 1;

        return (long) (Math.floor((time + sqrtD) / 2) - Math.ceil((time - sqrtD) / 2) + delta);
    }
}
