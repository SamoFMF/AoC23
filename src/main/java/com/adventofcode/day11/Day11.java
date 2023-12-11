package com.adventofcode.day11;

import com.adventofcode.utils.Point2D;
import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Day11 {

    private static final char GALAXY = '#';

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input11.txt");
        var galaxies = new ArrayList<Point2D>();
        for (int i = 0; i < input.size(); i++) {
            var line = input.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == GALAXY) {
                    galaxies.add(new Point2D(j, i));
                }
            }
        }

        part01(galaxies);
        part02(galaxies);
    }

    private static void part01(List<Point2D> galaxies) {
        solve(galaxies, 2);
    }

    private static void part02(List<Point2D> galaxies) {
        solve(galaxies, 1_000_000);
    }

    private static void solve(List<Point2D> galaxies, int width) {
        galaxies = updateDimension(galaxies, Point2D::x, (x, point) -> new Point2D(x, point.y()), width);
        galaxies = updateDimension(galaxies, Point2D::y, (y, point) -> new Point2D(point.x(), y), width);
        var result = getDistances(galaxies);

        System.out.println(result);
    }

    private static List<Point2D> updateDimension(
        List<Point2D> galaxies,
        Function<Point2D, Integer> get,
        BiFunction<Integer, Point2D, Point2D> create,
        int width
    ) {
        galaxies.sort(Comparator.comparingInt(get::apply));
        var galaxiesX = new ArrayList<Point2D>();
        var prev = get.apply(galaxies.get(0));
        var current = prev;
        for (var galaxy : galaxies) {
            var cur = get.apply(galaxy);
            var delta = cur - prev;
            current += Math.min(delta, 1) + Math.max(0, delta - 1) * width;
            galaxiesX.add(create.apply(current, galaxy));
            prev = cur;
        }

        return galaxiesX;
    }

    private static long getDistances(List<Point2D> galaxies) {
        var distance = 0L;
        for (int i = 0; i < galaxies.size() - 1; i++) {
            var galaxy = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                distance += getDistance(galaxy, galaxies.get(j));
            }
        }

        return distance;
    }

    private static long getDistance(Point2D galaxy1, Point2D galaxy2) {
        return Math.abs(galaxy1.x() - galaxy2.x()) + Math.abs(galaxy1.y() - galaxy2.y());
    }
}
