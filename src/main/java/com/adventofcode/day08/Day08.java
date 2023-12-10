package com.adventofcode.day08;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day08 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input08.txt");

        var instructions = input.get(0);
        var nodes = input.stream()
            .skip(2)
            .map(Node::parseLine)
            .collect(Collectors.toMap(Node::current, node -> node));

        part01(instructions, nodes);
        part02(instructions, nodes);
    }

    private static void part01(String instructions, Map<String, Node> nodes) {
        var counter = 0;
        var i = 0;
        var node = nodes.get("AAA");
        while (!"ZZZ".equals(node.current())) {
            node = switch (instructions.charAt(i)) {
                case 'L' -> nodes.get(node.left());
                case 'R' -> nodes.get(node.right());
                default -> throw new IllegalArgumentException();
            };
            counter++;
            i = (i + 1) % instructions.length();
        }

        System.out.println(counter);
    }

    /**
     * Assumes that all end nodes are reached on the last instruction in the instruction set.
     */
    private static void part02(String instructions, Map<String, Node> nodes) {
        var result = nodes.entrySet().stream()
            .filter(entry -> entry.getKey().endsWith("A"))
            .map(Map.Entry::getValue)
            .map(node -> part02Helper(instructions, nodes, node))
            .reduce(
                new ArrayList<>(Collections.singleton(new long[0])),
                (acc, values) -> {
                    var combinations = new ArrayList<long[]>();
                    for (var a : acc) {
                        for (var b : values) {
                            var ab = new long[a.length + 1];
                            System.arraycopy(a, 0, ab, 0, a.length);
                            ab[a.length] = b;
                            combinations.add(ab);
                        }
                    }
                    return combinations;
                },
                (u, v) -> v
            )
            .stream()
            .mapToLong(Utils::lcm)
            .min()
            .orElseThrow();

        System.out.println(result);
    }

    /**
     * Simplifies part02 by additionally assuming only a single solution exists for each starting node.
     */
    private static void part02Simple(String instructions, Map<String, Node> nodes) {
        var result = nodes.entrySet().stream()
            .filter(entry -> entry.getKey().endsWith("A"))
            .map(Map.Entry::getValue)
            .map(node -> part02Helper(instructions, nodes, node))
            .mapToLong(col -> col.iterator().next())
            .toArray();

        System.out.println(Utils.lcm(result));
    }

    private static Collection<Integer> part02Helper(String instructions, Map<String, Node> nodes, Node startingNode) {
        var counter = 0;
        var i = 0;
        var state = new NodeState(-1, startingNode);
        var visitedStates = new HashMap<NodeState, Integer>();
        while (!visitedStates.containsKey(state)) {
            var node = switch (instructions.charAt(i)) {
                case 'L' -> nodes.get(state.node().left());
                case 'R' -> nodes.get(state.node().right());
                default -> throw new IllegalArgumentException();
            };

            counter++;
            state = new NodeState(i, node);
            if (node.current().endsWith("Z")) {
                visitedStates.put(state, counter);
            }

            i = (i + 1) % instructions.length();
        }

        return visitedStates.values();
    }
}
