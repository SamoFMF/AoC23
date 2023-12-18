package com.adventofcode.day03;

import com.adventofcode.utils.Point2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Grid {

    private static final Set<Character> ignore = new HashSet<>();

    static {
        IntStream.range(0, 10)
            .mapToObj(i -> Character.forDigit(i, 10))
            .forEach(ignore::add);

        ignore.add('.');
    }

    private final List<String> grid;
    private final Map<Point2D, Number> numbers;

    public Grid(List<String> grid) {
        this.grid = grid;
        numbers = new HashMap<>();

        parseGrid();
    }

    public Set<Number> getAdjacentNumbers() {
        var adjacentNumbers = new HashSet<Number>();
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.size(); col++) {
                if (ignore.contains(grid.get(row).charAt(col))) continue;
                checkAround(row, col, adjacentNumbers);
            }
        }

        return adjacentNumbers;
    }

    public IntStream getGearRatios() {
        var gearRatios = IntStream.builder();
        for (int row = 0; row < grid.size(); row++) {
            for (int col = 0; col < grid.size(); col++) {
                if (ignore.contains(grid.get(row).charAt(col))) continue;
                var adjacentNumbers = new HashSet<Number>();
                checkAround(row, col, adjacentNumbers);
                if (adjacentNumbers.size() == 2) {
                    var gearRatio = adjacentNumbers.stream()
                        .mapToInt(Number::value)
                        .reduce(1, (x, y) -> x * y);
                    gearRatios.add(gearRatio);
                }
            }
        }

        return gearRatios.build();
    }

    private void checkAround(int row, int col, Set<Number> adjacentNumbers) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isOutsideBounds(row + i, col + j)) continue;
                var point = new Point2D(col + j, row + i);
                if (numbers.containsKey(point)) {
                    adjacentNumbers.add(numbers.get(point));
                }
            }
        }
    }

    private boolean isOutsideBounds(int row, int col) {
        return row < 0
            || row >= grid.size()
            || col < 0
            || col >= grid.get(row).length();
    }

    private void parseGrid() {
        for (int i = 0; i < grid.size(); i++) {
            parseLine(i);
        }
    }

    private void parseLine(int idx) {
        var line = grid.get(idx);
        var pattern = Pattern.compile("\\d+");
        pattern.matcher(line)
            .results()
            .forEach(match -> addNumber(idx, match));
    }

    private void addNumber(int y, MatchResult match) {
        var number = Integer.parseInt(match.group());
        var start = new Point2D(match.start(), y);
        IntStream.range(match.start(), match.end())
            .forEach(x -> numbers.put(
                new Point2D(x, y),
                new Number(start, number)
            ));
    }
}
