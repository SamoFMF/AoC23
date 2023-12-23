package com.adventofcode.day21;

import com.adventofcode.utils.Point2D;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Grid(
    char[][] grid,
    Point2D start
) {
    private static final char PLOT = '.';
    private static final char START = 'S';

    public Set<Point2D> makeStep(Set<Point2D> positions) {
        return positions.stream()
            .<Point2D>mapMulti((position, consumer) -> {
                consumer.accept(position.add(new Point2D(1, 0)));
                consumer.accept(position.add(new Point2D(-1, 0)));
                consumer.accept(position.add(new Point2D(0, 1)));
                consumer.accept(position.add(new Point2D(0, -1)));
            })
            .filter(p -> p.x() >= 0)
            .filter(p -> p.y() >= 0)
            .filter(p -> p.x() < grid[0].length)
            .filter(p -> p.y() < grid.length)
            .filter(p -> grid[p.y()][p.x()] == PLOT)
            .collect(Collectors.toSet());
    }

    public static Grid parseInput(Stream<String> input) {
        var grid = input
            .map(String::toCharArray)
            .toArray(char[][]::new);

        var start = findStart(grid);
        grid[start.y()][start.x()] = PLOT;

        return new Grid(grid, start);
    }

    private static Point2D findStart(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == START) {
                    return new Point2D(j, i);
                }
            }
        }

        throw new IllegalArgumentException("Missing start!");
    }
}
