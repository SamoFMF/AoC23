package com.adventofcode.day24;

import java.util.Arrays;
import java.util.Optional;

public record Hail(
    Vector position,
    Vector velocity
) {

    public Hail minusVelocity(Vector vector) {
        return new Hail(position, velocity.minus(vector));
    }

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

    public boolean containsPoint(Vector vector) {
        var t = velocity.x() != 0
            ? (vector.x() - position.x()) / velocity.x()
            : velocity.y() != 0
            ? (vector.y() - position.y()) / velocity.y()
            : velocity.z() != 0
            ? (vector.z() - position.z()) / vector.z()
            : 0;

        return t > 0
            && position.x() + velocity.x() * t == vector.x()
            && position.y() + velocity.y() * t == vector.y()
            && position.z() + velocity.z() * t == vector.z();
    }

    public boolean containsPoint2D(Vector vector) {
        var t = velocity.x() != 0
            ? (vector.x() - position.x()) / velocity.x()
            : velocity.y() != 0
            ? (vector.y() - position.y()) / velocity.y()
            : 0;

        return t > 0
            && position.x() + velocity.x() * t == vector.x()
            && position.y() + velocity.y() * t == vector.y();
    }

    public Optional<Vector> findIntegerIntersection2D(Hail hail) {

        var rs = velocity.cross2D(hail.velocity());
        var diff = hail.position().minus(position);
        var qpr = diff.cross2D(velocity);

        if (rs == 0) {
            if (qpr == 0) {
                // TODO: on the same line - this is currently not supported
                return Optional.empty();
            } else {
                // Parallel on different lines
                return Optional.empty();
            }
        }

        var qps = diff.cross2D(hail.velocity());
        var t = qps / rs;
        var u = qpr / rs;

        var vu = new Vector(
            hail.position.x() + hail.velocity.x() * u,
            hail.position.y() + hail.velocity.y() * u,
            0
        );

        return Optional.of(new Vector(
                position.x() + velocity.x() * t,
                position.y() + velocity.y() * t,
                position.z() + velocity.z() * t
            ))
            .filter(v -> t > 0)
            .filter(v -> u > 0)
            .filter(v -> v.x() == vu.x())
            .filter(v -> v.y() == vu.y());
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
