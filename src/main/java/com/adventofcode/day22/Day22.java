package com.adventofcode.day22;

import com.adventofcode.utils.Point2D;
import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

public class Day22 {

    public static void main(String[] args) throws FileNotFoundException {
        var bricks = Utils.readFileStream("inputs/input22.txt")
            .map(Brick::new)
            .sorted(Comparator.comparingInt(b -> b.getZ().x()))
            .toList();

        var xMax = bricks.stream().map(Brick::getX).mapToInt(Point2D::y).max().orElseThrow() + 1;
        var yMax = bricks.stream().map(Brick::getY).mapToInt(Point2D::y).max().orElseThrow() + 1;
        var zMax = bricks.stream().map(Brick::getZ).mapToInt(Point2D::y).max().orElseThrow() + 1;

        // Assuming xMin = yMin = 0 & zMin = 1
        var grid = new Brick[zMax][yMax][xMax];

        // Insert bricks into grid
        bricks.forEach(brick -> {
            brick.setGrid(grid);
            brick.insertInGrid();
        });

        // Fall into positions
        bricks.forEach(Brick::fall);

        // Update supports / supportedBy
        bricks.forEach(Brick::updateSupports);

        part01(bricks);
        part02(bricks);
    }

    private static void part01(List<Brick> bricks) {
        var result = bricks.stream()
            .filter(Brick::checkForDisintegration)
            .count();

        System.out.println(result);
    }

    private static void part02(List<Brick> bricks) {
        var result = bricks.stream()
            .mapToLong(Brick::fallCount)
            .sum();

        System.out.println(result);
    }
}
