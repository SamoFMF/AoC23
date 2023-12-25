package com.adventofcode.day25;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Karger(
    Map<String, List<Edge>> graph,
    List<Edge> edges,
    Map<String, Integer> nodeSizes
) {

    public int minCut() {
        contract();

        return graph().values().iterator().next().size();
    }

    private void contract() {
        if (graph.size() == 2) {
            return;
        }

        var edge = edges.remove(edges.size() - 1);
        if (!edge.getFirst().equals(edge.getSecond())) {
            mergeNodes(edge.getFirst(), edge.getSecond());
        }

        contract();
    }

    private void mergeNodes(String u, String v) {
        var nu = graph.get(u);
        var nv = graph.get(v);

        nu.addAll(nv);
        nv.forEach(e -> {
            if (e.getFirst().equals(v)) {
                e.setFirst(u);
            } else {
                e.setSecond(u);
            }
        });

        graph.remove(v);
        graph.put(u, nu.stream()
            .filter(edge -> !edge.getFirst().equals(edge.getSecond()))
            .collect(Collectors.toCollection(ArrayList::new))
        );

        nodeSizes.put(u, nodeSizes.get(u) + nodeSizes.get(v));
        nodeSizes.remove(v);
    }

    public static Karger randomized(Collection<Edge> E) {
        Map<String, List<Edge>> graph = new HashMap<>();
        Map<String, Integer> nodeSizes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();
        E.forEach(edge -> edges.add(new Edge(edge)));
        Collections.shuffle(edges);

        for (var edge : edges) {
            if (!graph.containsKey(edge.getFirst())) {
                graph.put(edge.getFirst(), new ArrayList<>());
                nodeSizes.put(edge.getFirst(), 1);
            }

            if (!graph.containsKey(edge.getSecond())) {
                graph.put(edge.getSecond(), new ArrayList<>());
                nodeSizes.put(edge.getSecond(), 1);
            }

            graph.get(edge.getFirst()).add(edge);
            graph.get(edge.getSecond()).add(edge);
        }

        return new Karger(graph, edges, nodeSizes);
    }
}
