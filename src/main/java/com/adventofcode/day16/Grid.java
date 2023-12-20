package com.adventofcode.day16;

import com.adventofcode.utils.Point2D;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Grid(
    char[][] grid
) {

    public Grid(Stream<String> input) {
        this(
            input
                .map(String::toCharArray)
                .toArray(char[][]::new)
        );
    }

    public Set<Beam> simulate(Beam startBeam) {
        var beams = new HashSet<Beam>();
        beams.add(startBeam);
        var seen = new HashSet<Beam>();
        while (!beams.isEmpty()) {
            var beamsNew = new HashSet<Beam>();
            for (var beam : beams) {
                seen.add(beam);
                beamsNew.addAll(moveBeam(beam, seen));
            }

            beams = beamsNew;
        }

        return seen;
    }

    private List<Beam> moveBeam(Beam beam, Set<Beam> seen) {
        var newPosition = beam.move();

        return Optional.of(newPosition)
            .filter(p -> p.x() >= 0)
            .filter(p -> p.x() < grid[0].length)
            .filter(p -> p.y() >= 0)
            .filter(p -> p.y() < grid.length)
            .stream()
            .flatMap(p -> createBeam(p, beam.direction()))
            .filter(Predicate.not(seen::contains))
            .toList();
    }

    private Stream<Beam> createBeam(Point2D position, Point2D inDirection) {
        return (switch (grid[position.y()][position.x()]) {
            case '/' -> Stream.of(new Point2D(-inDirection.y(), -inDirection.x()));
            case '\\' -> Stream.of(new Point2D(inDirection.y(), inDirection.x()));
            case '-' -> Stream.of(new Point2D(1, 0), new Point2D(-1, 0))
                .filter(dir -> inDirection.x() == 0 || inDirection.equals(dir));
            case '|' -> Stream.of(new Point2D(0, 1), new Point2D(0, -1))
                .filter(dir -> inDirection.y() == 0 || inDirection.equals(dir));
            default -> Stream.of(inDirection);
        }).map(dir -> new Beam(position, dir));
    }
}
