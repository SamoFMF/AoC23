package com.adventofcode.day23;

import com.adventofcode.utils.Point2D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record Grid(
    char[][] grid,
    int iStart,
    int iEnd
) {

    private static final char PATH = '.';
    private static final char FOREST = '#';

    public Graph toGraph(boolean includeSlopes) {
        var start = new Point2D(iStart, 0);

        final List<Map<Integer, Long>> graph = new ArrayList<>();
        graph.add(new HashMap<>());

        var nodes = new HashMap<Point2D, Integer>();
        nodes.put(start, 0);

        var queue = new ArrayDeque<QueueItem>();
        queue.add(new QueueItem(0, start, start.add(new Point2D(0, 1))));

        while (!queue.isEmpty()) {
            var item = queue.pollLast();

            var prev = item.prev();
            var current = item.current();
            var distance = 0L;
            List<Point2D> nextMoves;
            boolean isOneWay = false;
            do {
                var direction = !includeSlopes || grid[current.y()][current.x()] == PATH
                    ? Stream.of(
                    new Point2D(1, 0),
                    new Point2D(-1, 0),
                    new Point2D(0, 1),
                    new Point2D(0, -1)
                )
                    : switch (grid[current.y()][current.x()]) {
                    case '>' -> {
                        isOneWay = true;
                        yield Stream.of(new Point2D(1, 0));
                    }
                    case '<' -> {
                        isOneWay = true;
                        yield Stream.of(new Point2D(-1, 0));
                    }
                    case '^' -> {
                        isOneWay = true;
                        yield Stream.of(new Point2D(0, -1));
                    }
                    case 'v' -> {
                        isOneWay = true;
                        yield Stream.of(new Point2D(0, 1));
                    }
                    default -> throw new IllegalArgumentException("Unknown floor: " + grid[current.y()][current.x()]);
                };
                var foo = direction.toList();

                nextMoves = foo.stream()
                    .map(current::add)
                    .filter(p -> p.x() >= 0)
                    .filter(p -> p.y() >= 0)
                    .filter(p -> p.x() < grid[0].length)
                    .filter(p -> p.y() < grid.length)
                    .filter(p -> grid[p.y()][p.x()] != FOREST)
                    .filter(Predicate.not(prev::equals))
                    .toList();

                prev = current;
                if (nextMoves.size() == 1) {
                    current = nextMoves.get(0);
                }

                distance++;
            } while (nextMoves.size() == 1);

            var isNewNode = !nodes.containsKey(current);

            int node;
            if (isNewNode) {
                node = graph.size();
                graph.add(new HashMap<>());
                nodes.put(current, node);
            } else {
                node = nodes.get(current);
            }

            if (!isOneWay) graph.get(node).put(item.prevNode(), distance);
            graph.get(item.prevNode()).put(node, distance);

            var currentFinal = current;
            if (isNewNode) {
                nextMoves.forEach(p -> queue.add(new QueueItem(node, currentFinal, p)));
            }

        }

        return new Graph(graph, nodes.get(start), nodes.get(new Point2D(iEnd, grid.length - 1)));
    }

    // Legacy code - alternative solution, only fast enough for part 1
    public long dfs(boolean includeSlopes) {
        var maxLength = 0L;
        var queue = new ArrayList<DfsItem>();
        queue.add(new DfsItem(new Point2D(iStart, 0), new HashSet<>()));

        while (!queue.isEmpty()) {
            var item = queue.remove(queue.size() - 1);

            if (item.position().y() == grid.length - 1) {
                if (item.visited().size() > maxLength) {
                    maxLength = item.visited().size();
                }
                continue;
            }

            if (item.visited().contains(item.position())) {
                continue;
            }

            var visited = new HashSet<>(item.visited());
            visited.add(item.position());

            if (includeSlopes) {
                switch (grid[item.position().y()][item.position().x()]) {
                    case '>' -> {
                        queue.add(new DfsItem(item.position().add(new Point2D(1, 0)), visited));
                        continue;
                    }
                    case '<' -> {
                        queue.add(new DfsItem(item.position().add(new Point2D(-1, 0)), visited));
                        continue;
                    }
                    case 'v' -> {
                        queue.add(new DfsItem(item.position().add(new Point2D(0, 1)), visited));
                        continue;
                    }
                    case '^' -> {
                        queue.add(new DfsItem(item.position().add(new Point2D(0, -1)), visited));
                        continue;
                    }
                }
            }

            Stream.of(
                    new Point2D(1, 0),
                    new Point2D(-1, 0),
                    new Point2D(0, 1),
                    new Point2D(0, -1)
                )
                .map(item.position()::add)
                .filter(p -> p.x() >= 0)
                .filter(p -> p.y() >= 0)
                .filter(p -> p.x() < grid[0].length)
                .filter(p -> p.y() < grid.length)
                .filter(p -> grid[p.y()][p.x()] != FOREST)
                .filter(Predicate.not(visited::contains))
                .forEach(p -> queue.add(new DfsItem(p, visited)));
        }

        return maxLength;
    }

    public static Grid parseInput(Stream<String> input) {
        var grid = input
            .map(String::toCharArray)
            .toArray(char[][]::new);

        return new Grid(
            grid,
            findPathInLine(grid[0]),
            findPathInLine(grid[grid.length - 1])
        );
    }

    private static int findPathInLine(char[] line) {
        for (int i = 0; i < line.length; i++) {
            if (line[i] == PATH) {
                return i;
            }
        }

        throw new IllegalArgumentException("Missing path.");
    }
}
