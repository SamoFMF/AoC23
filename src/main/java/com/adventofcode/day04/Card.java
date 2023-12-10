package com.adventofcode.day04;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Card {

    private final int id;
    private final Set<Integer> winningCards;
    private final Set<Integer> cards;

    private long copies = 1;

    public Card(String line) {
        var parts = line.trim().replaceAll(" +", " ").split(": ");
        id = Integer.parseInt(parts[0].split(" ")[1]);

        var cardPacks = parts[1].split(" \\| ");
        winningCards = parseCards(cardPacks[0]);
        cards = parseCards(cardPacks[1]);
    }

    public int getId() {
        return id;
    }

    public long getCopies() {
        return copies;
    }

    public void addCopies(long n) {
        copies += n;
    }

    public long getWinningCount() {
        return cards.stream()
            .filter(winningCards::contains)
            .count();
    }

    public long getScore() {
        var winning = getWinningCount();

        if (winning == 0) return 0;
        long score = 1;
        for (int i = 1; i < winning; i++) {
            score *= 2;
        }
        return score;
    }

    private static Set<Integer> parseCards(String cardsString) {
        return Arrays.stream(cardsString.split(" "))
            .map(Integer::parseInt)
            .collect(Collectors.toSet());
    }
}
