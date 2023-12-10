package com.adventofcode.day01;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day01 {

    private static final Map<String, String> numbers = Map.of(
        "one", "1",
        "two", "2",
        "three", "3",
        "four", "4",
        "five", "5",
        "six", "6",
        "seven", "7",
        "eight", "8",
        "nine", "9"
    );

    private static final String regex1 = "\\d";
    private static final String regex2;

    static {
        var builder = new StringBuilder();
        builder.append("(\\d)");
        numbers.keySet()
            .forEach(key -> builder.append("|(").append(key).append(")"));
        regex2 = builder.toString();
    }

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input01.txt");

        part01(input);
        part02(input);
    }

    private static void part01(List<String> input) {
        var pattern = Pattern.compile(regex1);
        var result = input.stream()
            .map(pattern::matcher)
            .map(Day01::parseMatcher)
            .mapToInt(Day01::parseGroups)
            .sum();

        System.out.println(result);
    }

    private static void part02(List<String> input) {
        var pattern = Pattern.compile(regex2);
        var result = input.stream()
            .map(pattern::matcher)
            .map(Day01::parseMatcher)
            .mapToInt(Day01::parseGroups)
            .sum();

        System.out.println(result);
    }

    private static Stream<String> parseMatcher(Matcher matcher) {
        int i = 0;
        var groups = new ArrayList<String>();
        while (matcher.find(i)) {
            var group = matcher.group();
            groups.add(group);
            i = matcher.start() + 1;
        }
        return groups.stream();
    }

    private static int parseGroups(Stream<String> groups) {
        var results = groups
            .map(value -> numbers.getOrDefault(value, value))
            .mapToInt(Integer::parseInt)
            .toArray();

        return 10 * results[0] + results[results.length - 1];
    }
}
