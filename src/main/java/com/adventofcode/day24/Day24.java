package com.adventofcode.day24;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class Day24 {

    private static final int RANGE = 500;

    public static void main(String[] args) throws FileNotFoundException {
        var hailList = Utils.readFileStream("inputs/input24.txt")
            .map(Hail::parseLine)
            .toList();

        part01(hailList);
        part021(hailList);
    }

    private static void part01(List<Hail> hailList) {
        var pMin = 200000000000000L;
        var pMax = 400000000000000L;
        var counter = 0;
        for (int i = 0; i < hailList.size(); i++) {
            for (int j = i + 1; j < hailList.size(); j++) {
                if (hailList.get(i).intersects2D(hailList.get(j), pMin, pMax)) {
                    counter++;
                }
            }
        }

        System.out.println(counter);
    }

    private static void part021(List<Hail> hailList) {
        for (int i = -RANGE; i < RANGE; i++) {
            for (int j = -RANGE; j < RANGE; j++) {
                var velocity = new Vector(i, j, 0);
                var intersection = getIntersection2D(velocity, hailList)
                    .flatMap(intersection2D -> findZAxis(velocity, hailList));
                if (intersection.isPresent()) {
                    var vector = intersection.get();
                    System.out.println(vector.x() + vector.y() + vector.z());
                    return;
                }
            }
        }

        System.out.println("No solution found.");
    }

    private static Optional<Vector> getIntersection(Vector velocity, List<Hail> hailList) {
        hailList = hailList.stream()
            .map(hail -> hail.minusVelocity(velocity))
            .toList();

        var intersection = hailList.get(0).findIntegerIntersection2D(hailList.get(1));

        if (intersection.isEmpty()) {
            return Optional.empty();
        }

        for (int i = 2; i < hailList.size(); i++) {
            if (!hailList.get(i).containsPoint(intersection.get())) {
                return Optional.empty();
            }
        }

        return intersection;
    }

    private static Optional<Vector> getIntersection2D(Vector velocity, List<Hail> hailList) {
        hailList = hailList.stream()
            .map(hail -> hail.minusVelocity(velocity))
            .toList();

        var intersection = hailList.get(0).findIntegerIntersection2D(hailList.get(1));

        if (intersection.isEmpty()) {
            return Optional.empty();
        }

        for (int i = 2; i < hailList.size(); i++) {
            if (!hailList.get(i).containsPoint2D(intersection.get())) {
                return Optional.empty();
            }
        }

        return intersection;
    }

    private static Optional<Vector> findZAxis(Vector velocity2D, List<Hail> hailList) {
        for (int i = -RANGE; i < RANGE; i++) {
            var intersection = getIntersection(new Vector(velocity2D.x(), velocity2D.y(), i), hailList);
            if (intersection.isPresent()) {
                return intersection;
            }
        }

        return Optional.empty();
    }
}
