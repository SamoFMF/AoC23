package com.adventofcode.day21;

import com.adventofcode.utils.Point2D;
import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day21 {

    public static void main(String[] args) throws FileNotFoundException {
        var grid = Grid.parseInput(Utils.readFileStream("inputs/input21.txt"));

        part01(grid);
        part02(grid);

    }

    private static void part01(Grid grid) {
        var result = getPositionCount(grid, grid.start(), 64);

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var steps = 26501365L;
        var lineEnds = Stream.of(
                new Point2D(0, grid.start().y()),
                new Point2D(grid.grid()[0].length - 1, grid.start().y()),
                new Point2D(grid.start().x(), 0),
                new Point2D(grid.start().x(), grid.grid().length - 1)
            ).mapToLong(start -> getPositionCount(grid, start, 130))
            .sum();

        var outerDiagonals = Stream.of(
                new Point2D(0, 0),
                new Point2D(grid.grid()[0].length - 1, 0),
                new Point2D(grid.grid()[0].length - 1, grid.grid().length - 1),
                new Point2D(0, grid.grid().length - 1)
            ).mapToLong(start -> getPositionCount(grid, start, 64))
            .sum();

        var outerDiagonalsCount = (steps - 65) / grid.grid().length;

        var lastInnerDiagonals = Stream.of(
                new Point2D(0, 0),
                new Point2D(grid.grid()[0].length - 1, 0),
                new Point2D(grid.grid()[0].length - 1, grid.grid().length - 1),
                new Point2D(0, grid.grid().length - 1)
            ).mapToLong(start -> getPositionCount(grid, start, 195))
            .sum();

        var lastInnerDiagonalsCounts = (steps - 65) / grid.grid().length - 1;

        var values = Stream.of(grid.start())
            .<Long>mapMulti((start, consumer) -> {
                consumer.accept(getPositionCount(grid, start, 129));
                consumer.accept(getPositionCount(grid, start, 130));
            })
            .mapToLong(i -> i)
            .toArray();
        var innerDiagonals = LongStream.range(0, (steps - 65) / grid.grid().length - 2)
            .map(i -> values[(int) i % 2] * (i + 1))
            .sum();
        var innerDiagonalsCount = 4;

        var wholeLines = ((steps - 65) / grid.grid().length / 2 - 1) * (values[0] + values[1]) + values[1];

        var result = values[0] + // starting grid
            4 * wholeLines + // grids that are on vertical or horizontal line from start grid & fully filled
            lineEnds + // end grids on vertical and horizontal lines from start grid
            outerDiagonals * outerDiagonalsCount + // outer diagonal, the least filled grids
            lastInnerDiagonals * lastInnerDiagonalsCounts + // diagonal 1 away from the outer, not fully filled grids
            innerDiagonals * innerDiagonalsCount; // inner diagonals, fully filled grids

        System.out.println(result);
    }

    private static long getPositionCount(Grid grid, Point2D start, int steps) {
        var positions = Set.of(start);
        for (int i = 0; i < steps; i++) {
            positions = grid.makeStep(positions);
        }

        return positions.size();
    }
}
