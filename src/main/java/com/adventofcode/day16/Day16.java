package com.adventofcode.day16;

import com.adventofcode.utils.Point2D;
import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 {

    public static void main(String[] args) throws FileNotFoundException {
        var grid = new Grid(Utils.readFileStream("inputs/input16.txt"));

        part01(grid);
        part02(grid);
    }

    private static void part01(Grid grid) {
        var result = solve(grid, new Beam(new Point2D(-1, 0), new Point2D(1, 0)));

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var width = grid.grid()[0].length;
        var height = grid.grid().length;
        var result = IntStream.range(0, Math.max(height, width))
            .boxed()
            .<Beam>mapMulti((i, consumer) -> {
                if (i < height) {
                    consumer.accept(new Beam(new Point2D(-1, i), new Point2D(1, 0)));
                    consumer.accept(new Beam(new Point2D(width, i), new Point2D(-1, 0)));
                }

                if (i < width) {
                    consumer.accept(new Beam(new Point2D(i, -1), new Point2D(0, 1)));
                    consumer.accept(new Beam(new Point2D(i, height), new Point2D(0, -1)));
                }
            })
            .parallel()
            .mapToInt(beam -> solve(grid, beam))
            .max()
            .orElseThrow();

        System.out.println(result);
    }

    private static int solve(Grid grid, Beam beam) {
        return grid.simulate(beam).stream()
            .map(Beam::position)
            .collect(Collectors.toSet())
            .size() - 1;
    }
}
