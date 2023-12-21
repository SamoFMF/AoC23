package com.adventofcode.day18;

public record InputLine(
    String direction,
    int length,
    String color
) {

    public static InputLine parseLine(String line) {
        var parts = line.split(" ");

        return new InputLine(
            parts[0],
            Integer.parseInt(parts[1]),
            parts[2].substring(2, parts[2].length() - 1)
        );
    }

    public static InputLine parseHex(String hex) {
        var length = Integer.parseInt(hex.substring(0, 5), 16);
        var direction = switch (hex.charAt(5)) {
            case '0' -> "R";
            case '1' -> "D";
            case '2' -> "L";
            case '3' -> "U";
            default -> throw new IllegalArgumentException("Illegal direction in hex.");
        };

        return new InputLine(direction, length, null);
    }
}
