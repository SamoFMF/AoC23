package com.adventofcode.day05;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day05 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input05.txt");

        var seeds = Arrays.stream(input.get(0).split(" "))
            .skip(1)
            .mapToLong(Long::parseLong)
            .toArray();

        var maps = parseMaps(input);

        part01(seeds, maps);
        part02(seeds, maps);
    }

    private static void part01(long[] seeds, Map<String, ConversionMap> maps) {
        var intervals = Arrays.stream(seeds)
            .mapToObj(seed -> new Interval(seed, seed + 1))
            .toList();

        solve(intervals, maps);
    }

    private static void part02(long[] seeds, Map<String, ConversionMap> maps) {
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < seeds.length; i += 2) {
            intervals.add(new Interval(seeds[i], seeds[i] + seeds[i + 1]));
        }

        solve(intervals, maps);
    }

    private static void solve(List<Interval> intervals, Map<String, ConversionMap> maps) {
        var source = "seed";
        while (!"location".equals(source)) {
            var map = maps.get(source);
            intervals = intervals.stream()
                .flatMap(interval -> map.mapRangeToDestination(interval).stream())
                .toList();

            source = map.getDestination();
        }

        var result = intervals.stream()
            .filter(Predicate.not(Interval::isEmpty))
            .mapToLong(Interval::start)
            .min()
            .orElse(Long.MAX_VALUE);

        System.out.println(result);
    }

    private static Map<String, ConversionMap> parseMaps(List<String> input) {
        var conversionMaps = new HashMap<String, ConversionMap>();
        for (int i = 2; i < input.size(); i++) {
            var conversionMap = new ConversionMap(input.get(i));
            for (i++; i < input.size(); i++) {
                var line = input.get(i);
                if (line.trim().isEmpty()) break;
                conversionMap.parseRangeLine(line);
            }
            conversionMaps.put(conversionMap.getSource(), conversionMap);
        }

        return conversionMaps;
    }
}
