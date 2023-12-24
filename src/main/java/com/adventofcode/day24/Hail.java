package com.adventofcode.day24;

import java.util.Arrays;

public record Hail(
    Vector position,
    Vector velocity
) {

    public boolean intersects2D(Hail hail, long pMin, long pMax) {

        var rs = velocity.cross2D(hail.velocity());
        var diff = hail.position().minus(position);
        var qpr = diff.cross2D(velocity);

        if (rs == 0) {
            if (qpr == 0) {
                // TODO: on the same line
                System.out.println("WE ARE HERE");
                return true;
            } else {
                // Parallel on different lines
                return false;
            }
        }

        var qps = diff.cross2D(hail.velocity());
        var t = (double) qps / rs;
        var u = (double) qpr / rs;

        return t >= 0
            && u >= 0
            && position.x() + velocity.x() * t >= pMin
            && position.y() + velocity.y() * t >= pMin
            && position.x() + velocity.x() * t <= pMax
            && position.y() + velocity.y() * t <= pMax;
    }

    public static Hail parseLine(String line) {
        var parts = line.split(" @ ");
        var position = Arrays.stream(parts[0].split(", "))
            .map(String::trim)
            .mapToLong(Long::parseLong)
            .toArray();

        var velocity = Arrays.stream(parts[1].split(", "))
            .map(String::trim)
            .mapToLong(Long::parseLong)
            .toArray();

        return new Hail(new Vector(position), new Vector(velocity));
    }
}
