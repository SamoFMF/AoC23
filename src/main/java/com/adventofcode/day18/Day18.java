package com.adventofcode.day18;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.List;

public class Day18 {

    public static void main(String[] args) throws IOException {
        var inputLines = Utils.readFileStream("inputs/input18.txt")
            .map(InputLine::parseLine)
            .toList();

        part01(inputLines);
        part02(inputLines);
    }

    private static void part01(List<InputLine> inputLines) {
        var result = getArea(inputLines);

        System.out.println(result);
    }

    private static void part02(List<InputLine> inputLines) {
        var result = getArea(inputLines.stream()
            .map(InputLine::color)
            .map(InputLine::parseHex)
            .toList());

        System.out.println(result);
    }

    private static long getArea(List<InputLine> inputLines) {
        var area = 0L;
        var boundary = 0L;
        var prev = new Coordinates(0, 0);
        for (var inputLine : inputLines) {
            var current = switch (inputLine.direction()) {
                case "R" -> prev.add(new Coordinates(inputLine.length(), 0));
                case "L" -> prev.add(new Coordinates(-inputLine.length(), 0));
                case "U" -> prev.add(new Coordinates(0, inputLine.length()));
                case "D" -> prev.add(new Coordinates(0, -inputLine.length()));
                default -> throw new IllegalArgumentException("Unsupported direction.");
            };

            area += prev.x() * current.y() - prev.y() * current.x(); // Shoelace Formula
            boundary += inputLine.length();
            prev = current;
        }

        var inside = (Math.abs(area) - boundary) / 2 + 1; // Pick's Theorem
        return inside + boundary;
    }
}
