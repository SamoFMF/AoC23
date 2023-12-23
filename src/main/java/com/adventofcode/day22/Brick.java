package com.adventofcode.day22;

import com.adventofcode.utils.Point2D;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Brick {

    private final Set<Brick> supportedBy;
    private final Set<Brick> supports;

    private final Point2D x;
    private final Point2D y;
    private Point2D z;
    private Brick[][][] grid;

    public Brick(String line) {
        this.supportedBy = new HashSet<>();
        this.supports = new HashSet<>();

        var ends = line.split("~");
        var left = Arrays.stream(ends[0].split(","))
            .mapToInt(Integer::parseInt)
            .toArray();
        var right = Arrays.stream(ends[1].split(","))
            .mapToInt(Integer::parseInt)
            .toArray();

        x = new Point2D(Math.min(left[0], right[0]), Math.max(left[0], right[0]));
        y = new Point2D(Math.min(left[1], right[1]), Math.max(left[1], right[1]));
        z = new Point2D(Math.min(left[2], right[2]) - 1, Math.max(left[2], right[2]) - 1);
    }

    public Point2D getX() {
        return x;
    }

    public Point2D getY() {
        return y;
    }

    public Point2D getZ() {
        return z;
    }

    public void setGrid(Brick[][][] grid) {
        this.grid = grid;
    }

    public void insertInGrid() {
        insertInGrid(this);
    }

    public void fall() {
        var moveDelta = z.x() - getLowestLevel();
        if (moveDelta == 0) {
            return;
        }

        insertInGrid(null);
        z = z.add(new Point2D(-moveDelta, -moveDelta));
        insertInGrid();
    }

    public void updateSupports() {
        supports.clear();
        for (int yi = y.x(); yi <= y.y(); yi++) {
            for (int xi = x.x(); xi <= x.y(); xi++) {
                if (z.y() < grid.length - 1) {
                    Optional.ofNullable(grid[z.y() + 1][yi][xi])
                        .ifPresent(brick -> {
                            supports.add(brick);
                            brick.addSupportedBy(this);
                        });
                }
            }
        }
    }

    public void addSupportedBy(Brick brick) {
        supportedBy.add(brick);
    }

    public boolean checkForDisintegration() {
        return supports.size() == 0
            || supports.stream().allMatch(brick -> brick.supportedBy.size() > 1);
    }

    public long fallCount() {
        var disintegrated = new HashSet<Brick>();
        disintegrated.add(this);
        Set<Brick> current = new HashSet<>();
        current.add(this);
        while (!current.isEmpty()) {
            disintegrated.addAll(current);
            current = current.stream()
                .flatMap(brick -> brick.supports.stream())
                .filter(Predicate.not(disintegrated::contains))
                .filter(brick -> disintegrated.containsAll(brick.supportedBy))
                .collect(Collectors.toSet());
        }

        return disintegrated.size() - 1;
    }

    private int getLowestLevel() {
        for (int zi = z.x() - 1; zi >= 0; zi--) {
            if (isLevelOccupied(zi)) {
                return zi + 1;
            }
        }

        return 0;
    }

    private boolean isLevelOccupied(int iLevel) {
        var level = grid[iLevel];
        for (int yi = y.x(); yi <= y.y(); yi++) {
            for (int xi = x.x(); xi <= x.y(); xi++) {
                if (level[yi][xi] != null) {
                    return true;
                }
            }
        }

        return false;
    }

    private void insertInGrid(Brick brick) {
        for (int zi = z.x(); zi <= z.y(); zi++) {
            for (int yi = y.x(); yi <= y.y(); yi++) {
                for (int xi = x.x(); xi <= x.y(); xi++) {
                    grid[zi][yi][xi] = brick;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "{" + x.toString() + ", " + y.toString() + ", " + z.toString() + "}";
    }
}
