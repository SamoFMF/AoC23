package com.adventofcode.day19;

import com.adventofcode.utils.Range;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public record Rule(
    Predicate<Map<String, Integer>> predicate,
    String goTo,
    Range acceptableRange,
    String field
) {

    public static Rule parseRule(String rule) {
        if (!rule.contains(":")) {
            return new Rule(v -> true, rule, Range.full(), "a");
        }

        var parts = rule.split(":");

        var pattern = Pattern.compile("[=><]");
        var matcher = pattern.matcher(parts[0]);
        var comparator = matcher.find() ? matcher.group() : "";
        var predicates = parts[0].split(comparator);
        var value = Integer.parseInt(predicates[1]);
        var range = createRange(comparator, value);

        return new Rule(
            part -> range.contains(part.get(predicates[0])),
            parts[1],
            range,
            predicates[0]
        );
    }

    private static Range createRange(String comparator, Integer value) {
        return switch (comparator) {
            case "=" -> new Range(value);
            case ">" -> Range.greaterThan(value);
            case "<" -> Range.lessThan(value);
            default -> throw new IllegalArgumentException("Unknown comparator.");
        };
    }
}
