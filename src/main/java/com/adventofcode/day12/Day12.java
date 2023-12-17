package com.adventofcode.day12;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.List;

public class Day12 {

    public static void main(String[] args) throws FileNotFoundException {
        var rows = Utils.readFileStream("inputs/input12.txt")
            .map(Row::parseLine)
            .toList();

        part01(rows);
        part02(rows);
    }

    private static void part01(List<Row> rows) {
        var result = rows.stream()
            .mapToLong(row -> row.getCombinations(1))
            .sum();

        System.out.println(result);
    }

    private static void part02(List<Row> rows) {
        var result = rows.stream()
            .mapToLong(row -> row.getCombinations(5))
            .sum();

        System.out.println(result);
    }
}
