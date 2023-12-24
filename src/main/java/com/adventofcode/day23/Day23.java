package com.adventofcode.day23;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;

public class Day23 {

    public static void main(String[] args) throws FileNotFoundException {
        var grid = Grid.parseInput(Utils.readFileStream("inputs/input23.txt"));

        part01(grid);
        part02(grid);
    }

    private static void part01(Grid grid) {
        var graph = grid.toGraph(true);
        var result = graph.longestPath();

        System.out.println(result);
    }

    private static void part02(Grid grid) {
        var graph = grid.toGraph(false);
        var result = graph.longestPath();

        System.out.println(result);
    }
}
