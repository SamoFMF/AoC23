package com.adventofcode.day19;

import com.adventofcode.utils.Range;
import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input19.txt");

        var workflows = new HashMap<String, Workflow>();
        var parts = new ArrayList<Map<String, Integer>>();
        var isWorkflow = true;
        for (var line : input) {
            if (line.isEmpty()) {
                isWorkflow = false;
            } else if (isWorkflow) {
                var workflow = Workflow.parseLine(line);
                workflows.put(workflow.name(), workflow);
            } else {
                parts.add(parsePart(line));
            }
        }
        workflows.put("A", new Workflow("A", null));
        workflows.put("R", new Workflow("R", null));

        part01(workflows, parts);
        part02(workflows);
    }

    private static void part01(Map<String, Workflow> workflows, List<Map<String, Integer>> parts) {
        var start = workflows.get("in");
        var result = parts.stream()
            .filter(part -> handlePartRecursive(part, start, workflows).equals("A"))
            .flatMap(part -> part.values().stream())
            .mapToInt(i -> i)
            .sum();

        System.out.println(result);
    }

    private static void part02(Map<String, Workflow> workflows) {
        var part = Map.of("x", Range.full(), "m", Range.full(), "a", Range.full(), "s", Range.full());
        var result = workflows.get("in").ratePartRange(part, 0, workflows);

        System.out.println(result);
    }

    private static String handlePartRecursive(Map<String, Integer> part, Workflow workflow, Map<String, Workflow> workflows) {
        var next = workflow.ratePart(part);
        return switch (next) {
            case "A", "R" -> next;
            default -> handlePartRecursive(part, workflows.get(next), workflows);
        };
    }

    private static Map<String, Integer> parsePart(String line) {
        var part = new HashMap<String, Integer>();
        Arrays.stream(line.substring(1, line.length() - 1).split(","))
            .map(r -> r.split("="))
            .forEach(r -> part.put(r[0], Integer.parseInt(r[1])));

        return part;
    }
}
