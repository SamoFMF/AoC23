package com.adventofcode.day04;

import com.adventofcode.utils.Utils;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

public class Day04 {

    public static void main(String[] args) throws FileNotFoundException {
        var cards = Utils.readFileStream("inputs/input04.txt")
            .map(Card::new)
            .sorted(Comparator.comparingInt(Card::getId))
            .toList();

        part01(cards);
        part02(cards);
    }

    private static void part01(List<Card> cards) {
        var result = cards.stream()
            .mapToLong(Card::getScore)
            .sum();

        System.out.println(result);
    }

    private static void part02(List<Card> cards) {
        long result = 0;
        for (int i = 0; i < cards.size(); i++) {
            var card = cards.get(i);
            result += card.getCopies();
            var winning = card.getWinningCount();
            for (int j = 0; j < winning; j++) {
                cards.get(i + j + 1).addCopies(card.getCopies());
            }
        }

        System.out.println(result);
    }
}
