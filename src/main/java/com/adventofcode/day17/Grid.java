package com.adventofcode.day17;

import com.adventofcode.utils.Point2D;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public record Grid(
    int[][] grid
) {

    public Grid(Stream<String> input) {
        this(
            input
                .map(line -> line.chars().map(c -> c - '0').toArray())
                .toArray(int[][]::new)
        );
    }

    public int aStar(int minStraight, int maxStraight) {
        var queue = new PriorityQueue<PriorityItem>(Comparator.comparingInt(a -> a.heat() + a.heuristic()));
        var seen = new HashSet<Crucible>();
        var end = new Point2D(grid[0].length - 1, grid.length - 1);
        Stream.of(
                new Point2D(1, 0),
                new Point2D(0, 1)
            ).map(dir -> new Crucible(new Point2D(0, 0), dir, 0))
            .map(crucible -> new PriorityItem(crucible, 0, getHeuristic(crucible.position())))
            .forEach(item -> {
                queue.add(item);
                seen.add(item.crucible());
            });

        while (!queue.isEmpty()) {
            var item = queue.poll();

            if (item.position().equals(end) && item.count() >= minStraight) {
                return item.heat();
            }

            Stream.of(
                    new Point2D(1, 0),
                    new Point2D(-1, 0),
                    new Point2D(0, 1),
                    new Point2D(0, -1)
                )
                .filter(dir -> dir.x() * item.direction().x() >= 0)
                .filter(dir -> dir.y() * item.direction().y() >= 0)
                .filter(dir -> dir.equals(item.direction()) || item.count() >= minStraight)
                .map(dir -> new Crucible(
                    item.position().add(dir),
                    dir,
                    item.direction().equals(dir)
                        ? item.count() + 1
                        : 1
                ))
                .filter(crucible -> crucible.count() <= maxStraight)
                .filter(crucible -> crucible.position().x() >= 0)
                .filter(crucible -> crucible.position().x() < grid[0].length)
                .filter(crucible -> crucible.position().y() >= 0)
                .filter(crucible -> crucible.position().y() < grid.length)
                .filter(crucible -> !seen.contains(crucible))
                .map(crucible -> new PriorityItem(
                    crucible,
                    item.heat() + grid[crucible.position().y()][crucible.position().x()],
                    getHeuristic(crucible.position())
                ))
                .forEach(it -> {
                    seen.add(it.crucible());
                    queue.add(it);
                });
        }

        return -1;
    }

    private int getHeuristic(Point2D position) {
        return (grid.length - position.y() - 1) + (grid[0].length - position.x() - 1);
    }
}
