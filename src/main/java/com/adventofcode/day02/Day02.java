package com.adventofcode.day02;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.List;

public class Day02 {

    public static void main(String[] args) throws IOException {
        var input = Utils.readAllLines("inputs/input02.txt");

        part01(input);
        part02(input);
    }

    private static void part01(List<String> input) {
        var availableCubes = new CubeSet(12, 13, 14);

        var result = input.stream()
            .map(Game::parseLine)
            .filter(game -> game.isValid(availableCubes))
            .mapToInt(Game::id)
            .sum();

        System.out.println(result);
    }

    private static  void part02(List<String> input) {
        var result = input.stream()
            .map(Game::parseLine)
            .mapToInt(Game::getPower)
            .sum();

        System.out.println(result);
    }
}
