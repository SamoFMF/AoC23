package com.adventofcode.day10;

import com.adventofcode.utils.Pair;
import com.adventofcode.utils.Point2D;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid {

    private static final char EMPTY = '.';
    private static final char FLOODED = 'O';
    private static final char GAP = ' ';

    private final List<String> gridOld;
    private final char[][] grid;
    private final Point2D dimensions;
    private final Point2D startPosition;
    private final Point2D startDirection;
    private final char startPipe;

    private Set<Point2D> loop;

    public Grid(List<String> grid) {
        this.gridOld = grid;
        this.grid = grid.stream()
            .map(String::toCharArray)
            .toArray(char[][]::new);
        dimensions = new Point2D(grid.get(0).length(), grid.size());
        startPosition = findStart();
        startDirection = findStartDirection(startPosition);
        startPipe = findStartPipe();
    }

    public Grid(char[][] grid) {
        this.gridOld = null;
        this.grid = grid;
        dimensions = new Point2D(grid[0].length, grid.length);
        startPosition = null;
        startDirection = null;
        startPipe = ' ';
    }

    public Set<Point2D> getLoop() {
        if (this.loop == null) {
            var loop = new HashSet<Point2D>();
            var move = new Move(startPosition.x(), startPosition.y(), startDirection);
            do {
                loop.add(new Point2D(move.x(), move.y()));
                move = doMove(move);
            } while (move.x() != startPosition.x() || move.y() != startPosition.y());

            this.loop = loop;
        }

        return this.loop;
    }

    public long countEnclosedTiles() {
        floodGrid();

        return Arrays.stream(grid)
            .mapToLong(chars -> IntStream.range(0, chars.length)
                .filter(i -> chars[i] == '.')
                .count())
            .sum();
    }

    public Grid expandGrid() {
        removeOutsideLoop();
        var gaps = findGaps();

        grid[startPosition.y()][startPosition.x()] = startPipe;
        var temp = fillColumnGaps(gaps.first());
        var grid1 = fillRowGaps(gaps.second(), temp);

        return new Grid(grid1);
    }

    private void removeOutsideLoop() {
        getLoop();

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (!loop.contains(new Point2D(x, y))) {
                    grid[y][x] = EMPTY;
                }
            }
        }
    }

    private Pair<HashSet<Integer>> findGaps() {
        var gaps = new Pair<HashSet<Integer>>(new HashSet<>(), new HashSet<>());
        for (int i = 0; i < dimensions.y(); i++) {
            var row = grid[i];
            for (int j = 0; j < dimensions.x() - 1; j++) {
                var left = row[j];
                var right = row[j + 1];
                if ((left == '|' || left == 'J' || left == '7') && (right == '|' || right == 'L' || right == 'F')) {
                    gaps.first().add(j);
                }

                if (i < dimensions.y() - 1) {
                    var up = grid[i][j];
                    var down = grid[i + 1][j];
                    if ((up == '-' || up == 'L' || up == 'J') && (down == '-' || down == '7' || down == 'F')) {
                        gaps.second().add(i);
                    }
                }
            }
        }

        return gaps;
    }

    private char[][] fillColumnGaps(Collection<Integer> gaps) {
        var tempGrid = Arrays.stream(grid)
            .map(chars -> {
                var originalNodes = new ArrayList<Node<Character>>();
                var prev = new Node<>(chars[0]);
                originalNodes.add(prev);
                for (int i = 1; i < chars.length; i++) {
                    var node = new Node<>(chars[i]);
                    originalNodes.add(node);
                    prev.setNext(node);
                    prev = node;
                }
                return originalNodes;
            })
            .toList();

        gaps.forEach(gap -> {
            for (var row : tempGrid) {
                var node = row.get(gap);
                var inserted = switch (node.getData()) {
                    case '-', 'L', 'F' -> '-';
                    default -> switch (row.get(gap + 1).getData()) {
                        case '-', 'J', '7' -> '-';
                        default -> GAP;
                    };
                };
                node.insertNext(new Node<>(inserted));
            }
        });

        return tempGrid.stream()
            .map(row -> {
                var newRow = new char[row.size() + gaps.size()];
                var current = row.get(0);
                var i = 0;
                do {
                    newRow[i++] = current.getData();
                    current = current.getNext();
                } while (current != null);

                return newRow;
            })
            .toArray(char[][]::new);
    }

    private char[][] fillRowGaps(Collection<Integer> gaps, char[][] tempGrid) {
        var originalNodes = new ArrayList<Node<char[]>>();
        var prev = new Node<>(tempGrid[0]);
        originalNodes.add(prev);
        for (int i = 1; i < tempGrid.length; i++) {
            var node = new Node<>(tempGrid[i]);
            originalNodes.add(node);
            prev.setNext(node);
            prev = node;
        }

        gaps.forEach(gap -> {
            var node = originalNodes.get(gap);
            var row = node.getData();
            var newRow = new char[row.length];
            for (int i = 0; i < row.length; i++) {
                var inserted = switch (row[i]) {
                    case '|', '7', 'F' -> '|';
                    default -> switch (originalNodes.get(gap + 1).getData()[i]) {
                        case '|', 'L', 'J' -> '|';
                        default -> GAP;
                    };
                };
                newRow[i] = inserted;
            }
            node.insertNext(new Node<>(newRow));
        });

        var newGrid = new char[tempGrid.length + gaps.size()][];
        var current = originalNodes.get(0);
        var i = 0;
        do {
            newGrid[i++] = current.getData();
            current = current.getNext();
        } while (current != null);

        return newGrid;
    }

    private void floodGrid() {
        var queue = IntStream.range(1, dimensions.x() - 1)
            .boxed()
            .flatMap(i -> Stream.of(
                new Point2D(i, -1),
                new Point2D(i, dimensions.y()),
                new Point2D(-1, i),
                new Point2D(dimensions.x(), i)
            ))
            .collect(Collectors.toCollection(ArrayDeque::new));

        while (!queue.isEmpty()) {
            var position = queue.removeFirst();
            Stream.of(
                    new Point2D(position.x() + 1, position.y()),
                    new Point2D(position.x() - 1, position.y()),
                    new Point2D(position.x(), position.y() + 1),
                    new Point2D(position.x(), position.y() - 1)
                )
                .filter(point -> point.x() >= 0)
                .filter(point -> point.y() >= 0)
                .filter(point -> point.x() < dimensions.x())
                .filter(point -> point.y() < dimensions.y())
                .filter(point -> grid[point.y()][point.x()] == EMPTY || grid[point.y()][point.x()] == GAP)
                .forEach(point -> {
                    grid[point.y()][point.x()] = FLOODED;
                    queue.addLast(point);
                });
        }
    }

    private Move doMove(Move origin) {
        var position = new Point2D(origin.x() + origin.direction().x(), origin.y() + origin.direction().y());
        var direction = nextDirection(position, origin.direction());

        return new Move(position.x(), position.y(), direction);
    }

    private Point2D findStart() {
        for (int y = 0; y < gridOld.size(); y++) {
            var x = gridOld.get(y).indexOf('S');
            if (x >= 0) {
                return new Point2D(x, y);
            }
        }

        throw new IllegalArgumentException("Grid does not contain start");
    }

    private Point2D findStartDirection(Point2D start) {
        if (start.y() > 0 && Set.of('|', '7', 'F').contains(gridOld.get(start.y() - 1).charAt(start.x()))) {
            return new Point2D(0, -1);
        } else if (start.y() < gridOld.size() - 1 && Set.of('|', 'L', 'J').contains(gridOld.get(start.y() + 1).charAt(start.x()))) {
            return new Point2D(0, 1);
        } else if (start.x() > 0 && Set.of('-', 'L', 'F').contains(gridOld.get(start.y()).charAt(start.x() - 1))) {
            return new Point2D(-1, 0);
        } else if (start.x() < gridOld.get(0).length() - 1 && Set.of('-', 'J', '7').contains(gridOld.get(start.y()).charAt(start.x() + 1))) {
            return new Point2D(1, 0);
        } else {
            throw new UnsupportedOperationException("No where to go from start.");
        }
    }

    private char findStartPipe() {
        if (startPosition == null) throw new IllegalArgumentException("Start position not found");

        var value = 0;
        if (startPosition.y() > 0 && Set.of('|', '7', 'F').contains(gridOld.get(startPosition.y() - 1).charAt(startPosition.x()))) {
            value += 1;
        }
        if (startPosition.y() < gridOld.size() - 1 && Set.of('|', 'L', 'J').contains(gridOld.get(startPosition.y() + 1).charAt(startPosition.x()))) {
            value += 2;
        }
        if (startPosition.x() > 0 && Set.of('-', 'L', 'F').contains(gridOld.get(startPosition.y()).charAt(startPosition.x() - 1))) {
            value += 4;
        }
        if (startPosition.x() < gridOld.get(0).length() - 1 && Set.of('-', 'J', '7').contains(gridOld.get(startPosition.y()).charAt(startPosition.x() + 1))) {
            value += 8;
        }

        return switch (value) {
            case 3 -> '|';
            case 5 -> 'J';
            case 6 -> '7';
            case 9 -> 'L';
            case 10 -> 'F';
            case 12 -> '-';
            default -> throw new UnsupportedOperationException("No where to go from start.");
        };
    }

    private Point2D nextDirection(Point2D position, Point2D direction) {
        return nextDirection(gridOld.get(position.y()).charAt(position.x()), direction);
    }

    private Point2D nextDirection(char c, Point2D direction) {
        return switch (c) {
            case '|', '-' -> new Point2D(direction.x(), direction.y());
            case 'L', '7' -> new Point2D(direction.y(), direction.x());
            case 'J', 'F' -> new Point2D(-direction.y(), -direction.x());
            case 'S' -> nextDirection(startPipe, direction);
            default -> throw new IllegalArgumentException("Illegal argument: " + c);
        };
    }
}
