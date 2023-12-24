package com.adventofcode.day23;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record Graph(
    List<Map<Integer, Long>> graph,
    int start,
    int end
) {

    public long longestPath() {
        return dfsRecursive(start, 0, new HashSet<>());
    }

    private long dfsRecursive(int node, long steps, Set<Integer> visited) {
        if (node == end) {
            return steps;
        }

        visited.add(node);
        var result = graph.get(node).entrySet().stream()
            .filter(entry -> !visited.contains(entry.getKey()))
            .mapToLong(entry -> dfsRecursive(entry.getKey(), steps + entry.getValue(), visited))
            .max()
            .orElse(0L);
        visited.remove(node);

        return result;
    }
}
