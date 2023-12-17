package com.adventofcode.day14;

import com.adventofcode.utils.Utils;

import java.io.IOException;

public class Day14 {

    public static void main(String[] args) throws IOException {
        var lines = Utils.readAllLines("inputs/input14.txt");

        part01(new Grid(lines));
        part02(new Grid(lines));
    }

    private static void part01(Grid grid) {
        grid.tilt(0);
        System.out.println(grid.evaluate());
    }

    private static void part02(Grid grid) {
        grid.doCycles(1_000_000_000);
        System.out.println(grid.evaluate());
    }
}
