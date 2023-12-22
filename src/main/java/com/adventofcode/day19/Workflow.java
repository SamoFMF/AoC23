package com.adventofcode.day19;

import com.adventofcode.utils.Range;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Workflow(
    String name,
    List<Rule> rules
) {

    public String ratePart(Map<String, Integer> part) {
        for (var rule : rules) {
            if (rule.predicate().test(part)) {
                return rule.goTo();
            }
        }

        throw new IllegalArgumentException("Failed to rate part.");
    }

    public long ratePartRange(Map<String, Range> part, int iRule, Map<String, Workflow> workflows) {
        if (name.equals("R")) return 0;
        else if (name.equals("A")) {
            return part.values().stream()
                .mapToLong(Range::length)
                .reduce(1, (a, b) -> a * b);
        }

        var result = 0L;
        var rule = rules.get(iRule);
        result += part.get(rule.field()).overlap(rule.acceptableRange())
            .map(r -> {
                var newPart = new HashMap<>(part);
                newPart.put(rule.field(), r);
                return workflows.get(rule.goTo()).ratePartRange(newPart, 0, workflows);
            })
            .orElse(0L);

        result += part.get(rule.field()).minus(rule.acceptableRange()).stream()
            .mapToLong(r -> {
                var newPart = new HashMap<>(part);
                newPart.put(rule.field(), r);
                return ratePartRange(newPart, iRule + 1, workflows);
            })
            .sum();

        return result;
    }

    public static Workflow parseLine(String line) {
        var parts = line.split("\\{");

        var rules = Arrays.stream(parts[1].substring(0, parts[1].length() - 1).split(","))
            .map(Rule::parseRule)
            .toList();

        return new Workflow(parts[0], rules);
    }
}
