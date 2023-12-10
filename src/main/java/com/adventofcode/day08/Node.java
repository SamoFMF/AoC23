package com.adventofcode.day08;

public record Node(
    String current,
    String left,
    String right
) {

    public static Node parseLine(String line) {
        var parts = line.split(" = ");
        var dirs = parts[1].substring(1, parts[1].length() - 1).split(", ");
        return new Node(parts[0], dirs[0], dirs[1]);
    }
}
