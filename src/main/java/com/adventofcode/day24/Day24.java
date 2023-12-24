package com.adventofcode.day24;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.List;

public class Day24 {

    public static void main(String[] args) throws FileNotFoundException {
        var hailList = Utils.readFileStream("inputs/input24.txt")
            .map(Hail::parseLine)
            .toList();

        part01(hailList);
    }

    private static void part01(List<Hail> hailList) {
        var pMin = 200000000000000L;
        var pMax = 400000000000000L;
        var counter = 0;
        for (int i = 0; i < hailList.size(); i++) {
            for (int j = i+1; j < hailList.size(); j++) {
                if (hailList.get(i).intersects2D(hailList.get(j), pMin, pMax)) {
                    counter++;
                }
            }
        }

        System.out.println(counter);
    }
}
