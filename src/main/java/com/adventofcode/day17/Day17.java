package com.adventofcode.day17;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;

public class Day17 {

    public static void main(String[] args) throws FileNotFoundException {
        var grid = new Grid(Utils.readFileStream("inputs/input17.txt"));

        part01(grid);
        part02(grid);
    }

    private static void part01(Grid grid) {
        var result = grid.aStar(1, 3);

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var result = grid.aStar(4, 10);

        System.out.println(result);
    }
}
