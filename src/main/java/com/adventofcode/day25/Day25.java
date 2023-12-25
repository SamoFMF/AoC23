package com.adventofcode.day25;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Day25 {

    public static void main(String[] args) throws FileNotFoundException {
        List<Edge> edges = new ArrayList<>();
        Utils.readFileStream("inputs/input25.txt")
            .forEach(line -> {
                var parts = line.split(": ");
                var left = parts[0];

                for (var right : parts[1].split(" ")) {
                    edges.add(new Edge(left, right));
                }
            });

        part01(edges);
        part02();
    }

    private static void part01(List<Edge> edges) {
        while (true) {
            var karger = Karger.randomized(edges);
            if (karger.minCut() <= 3) {
                var result = karger.nodeSizes().values().stream()
                    .mapToInt(i -> i)
                    .reduce(1, (a, b) -> a * b);
                System.out.println(result);
                break;
            }
        }
    }

    private static void part02() {
        System.out.println("End of AoC23!");
    }
}
