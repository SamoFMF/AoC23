package com.adventofcode.day14;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Grid {

    private static final char ROUNDED = 'O';
    private static final char CUBED = '#';
    private static final char EMPTY = '.';

    private final char[][] grid;
    private final int[][] moveTo;

    public Grid(List<String> lines) {
        grid = new char[lines.size()][];
        IntStream.range(0, grid.length)
            .forEach(i -> grid[i] = lines.get(i).toCharArray());

        moveTo = new int[][]{new int[grid[0].length], new int[grid.length], new int[grid[0].length], new int[grid.length]};
    }

    public long evaluate() {
        var result = 0L;
        for (int i = 0; i < grid.length; i++) {
            var row = grid[i];
            for (var col : row) {
                if (col == ROUNDED) {
                    result += grid.length - i;
                }
            }
        }

        return result;
    }

    public void doCycles(int n) {
        var seen = new HashMap<String, Integer>();
        var reverseSeen = new HashMap<Integer, String>();
        for (int i = 0; i < n; i++) {
            cycle();
            var str = stringifyGrid();
            if (seen.containsKey(str)) {
                // Found a cycle of cycles
                calculateCycles(n, seen.get(str), reverseSeen);
                break;
            }

            seen.put(str, i);
            reverseSeen.put(i, str);
        }
    }

    public void tilt(int direction) {
        resetMoveTo(direction);
        switch (direction) {
            case 0 -> {
                for (int i = 0; i < grid.length; i++) {
                    var row = grid[i];
                    for (int j = 0; j < row.length; j++) {
                        if (row[j] == ROUNDED) {
                            var available = moveTo[direction][j]++;
                            row[j] = EMPTY;
                            grid[available][j] = ROUNDED;
                        } else if (row[j] == CUBED) {
                            moveTo[direction][j] = i + 1;
                        }
                    }
                }
            }
            case 1 -> {
                for (int i = 0; i < grid[0].length; i++) {
                    for (int j = 0; j < grid.length; j++) {
                        if (grid[j][i] == ROUNDED) {
                            var available = moveTo[direction][j]++;
                            grid[j][i] = EMPTY;
                            grid[j][available] = ROUNDED;
                        } else if (grid[j][i] == CUBED) {
                            moveTo[direction][j] = i + 1;
                        }
                    }
                }
            }
            case 2 -> {
                for (int i = grid.length - 1; i >= 0; i--) {
                    var row = grid[i];
                    for (int j = 0; j < row.length; j++) {
                        if (row[j] == ROUNDED) {
                            var available = moveTo[direction][j]--;
                            row[j] = EMPTY;
                            grid[available][j] = ROUNDED;
                        } else if (row[j] == CUBED) {
                            moveTo[direction][j] = i - 1;
                        }
                    }
                }
            }
            case 3 -> {
                for (int i = grid[0].length - 1; i >= 0; i--) {
                    for (int j = 0; j < grid.length; j++) {
                        if (grid[j][i] == ROUNDED) {
                            var available = moveTo[direction][j]--;
                            grid[j][i] = EMPTY;
                            grid[j][available] = ROUNDED;
                        } else if (grid[j][i] == CUBED) {
                            moveTo[direction][j] = i - 1;
                        }
                    }
                }
            }
            default -> throw new IllegalArgumentException("Invalid direction.");
        }
    }

    private void resetMoveTo(int direction) {
        var defaultValue = switch (direction) {
            case 2 -> grid.length - 1;
            case 3 -> grid[0].length - 1;
            default -> 0;
        };
        Arrays.fill(moveTo[direction], defaultValue);
    }

    private void cycle() {
        for (int i = 0; i < 4; i++) {
            tilt(i);
        }
    }

    private void calculateCycles(int n, int start, Map<Integer, String> map) {
        var period = map.size() - start;
        var step = (n - 1 - start) % period;
        var str = map.get(start + step);
        stringToGrid(str);
    }

    private String stringifyGrid() {
        var builder = new StringBuilder();
        for (var row : grid) {
            builder.append(row);
        }
        return builder.toString();
    }

    private void stringToGrid(String str) {
        int i = 0, j = 0;
        for (var c : str.toCharArray()) {
            grid[i][j] = c;
            j++;
            if (j == grid[i].length) {
                j = 0;
                i++;
            }
        }
    }
}
