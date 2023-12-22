package com.adventofcode.day20;

import com.adventofcode.utils.Pair;
import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day20 {

    public static void main(String[] args) throws IOException {
        var modules = Utils.readFileStream("inputs/input20.txt")
            .map(line -> line.split(" -> "))
            .map(parts -> {
                var destinations = parts[1].split(", ");
                return switch (parts[0].charAt(0)) {
                    case '%' -> new FlipFlop(parts[0].substring(1), destinations);
                    case '&' -> new Conjunction(parts[0].substring(1), destinations);
                    default -> new Broadcast(parts[0], destinations);
                };
            })
            .collect(Collectors.toMap(Module::getName, module -> module));

        var outputs = new ArrayList<Module>();
        for (var module : modules.values()) {
            for (var destination : module.getDestinations()) {
                if (!modules.containsKey(destination)) {
                    outputs.add(new Output(destination, module.getName()));
                    continue;
                }

                modules.get(destination).addInput(module.getName());
            }
        }

        outputs.forEach(module -> modules.put(module.getName(), module));

        part01(modules);
        part02(modules);
    }

    private static void part01(Map<String, Module> modules) {
        var seen = new HashMap<List<Integer>, Pair<Long>>();
        var hashes = new ArrayList<List<Integer>>();
        while (true) {
            var pulses = pressButton(modules);
            var hash = hashModules(modules);
            if (seen.containsKey(hash)) {
                System.out.println("CYCLE: " + hashes.indexOf(hash));
                break;
            }

            seen.put(hash, pulses);
            hashes.add(hash);

            if (hashes.size() == 1000) {
                break;
            }
        }

        var pairResult = seen.values().stream()
            .reduce(new Pair<>(0L, 0L), (a, b) -> new Pair<>(a.first() + b.first(), a.second() + b.second()));

        var result = pairResult.first() * pairResult.second();

        System.out.println(result);
    }

    private static void part02(Map<String, Module> modules) {
        var cycles = modules.get("rx").getInputs().stream()
            .map(modules::get)
            .flatMap(module -> module.getInputs().stream())
            .map(modules::get)
            .flatMap(module -> module.getInputs().stream())
            .map(modules::get)
            .mapToLong(module -> getCycleLength(module, modules))
            .toArray();

        var result = Utils.lcm(cycles);

        System.out.println(result);
    }

    private static List<Integer> hashModules(Map<String, Module> modules) {
        return modules.values().stream().map(Module::hashCode).toList();
    }

    private static Pair<Long> pressButton(Map<String, Module> modules) {
        var pulses = new ArrayDeque<Pulse>();
        pulses.addLast(new Pulse("button", "broadcaster", false));
        var low = 0L;
        var high = 0L;
        while (!pulses.isEmpty()) {
            var pulse = pulses.poll();
            var module = modules.get(pulse.destination());
            module.handlePulse(pulse.source(), pulse.pulse(), pulses);
            if (pulse.pulse()) {
                high++;
            } else {
                low++;
            }
        }

        return new Pair<>(low, high);
    }

    private static int getCycleLength(Module criticalModule, Map<String, Module> modules) {
        var criticalName = criticalModule.getName();
        var start = modules.get("broadcaster").getDestinations().stream()
            .map(modules::get)
            .filter(module -> module.getDestinations().contains(criticalName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Incorrect format."));

        var binary = new StringBuilder();
        var module = start;
        while (module != null) {
            if (module.getDestinations().contains(criticalName)) {
                binary.append("1");
            } else {
                binary.append("0");
            }

            module = module.getDestinations().stream()
                .filter(destination -> !destination.equals(criticalName))
                .findFirst()
                .map(modules::get)
                .orElse(null);
        }

        return Integer.parseInt(binary.reverse().toString(), 2);
    }
}
