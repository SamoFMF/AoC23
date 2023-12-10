package com.adventofcode.day10;

import com.adventofcode.utils.Utils;

import java.io.IOException;

public class Day10 {

    public static void main(String[] args) throws IOException {
        var grid = new Grid(Utils.readAllLines("inputs/input10.txt"));

        part01(grid);
        part02(grid);
    }

    private static void part01(Grid grid) {
        var result = grid.getLoop().size() / 2;

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var expandedGrid = grid.expandGrid();
        var result = expandedGrid.countEnclosedTiles();

        System.out.println(result);
    }
}
