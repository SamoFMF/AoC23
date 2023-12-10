package com.adventofcode.day07;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public record Hand(
    int[] cards,
    int bet,
    boolean isPart02
) {

    public int getHandValue() {
        var countMap = new HashMap<Integer, Integer>();
        for (var card : cards) {
            var count = countMap.getOrDefault(card, 0);
            countMap.put(card, count + 1);
        }

        if (isPart02) {
            countMap.entrySet().stream()
                .filter(entry -> entry.getKey() != JOKER)
                .min((x, y) -> Integer.compare(y.getValue(), x.getValue()))
                .ifPresent(best -> {
                    countMap.put(best.getKey(), best.getValue() + countMap.getOrDefault(JOKER, 0));
                    countMap.put(JOKER, 0);
                });
        }

        var counts = countMap.values().stream()
            .sorted((x, y) -> Integer.compare(y, x))
            .mapToInt(i -> i)
            .toArray();

        return switch (counts[0]) {
            case 5 -> 6;
            case 4 -> 5;
            case 3 -> counts[1] == 2 ? 4 : 3;
            case 2 -> counts[1] == 2 ? 2 : 1;
            case 1 -> 0;
            default -> throw new IllegalStateException("Unexpected value: " + counts[0]);
        };
    }

    public static int compare(Hand hand1, Hand hand2) {
        var valueCompare = Integer.compare(hand1.getHandValue(), hand2.getHandValue());
        if (valueCompare != 0) {
            return valueCompare;
        }

        for (int i = 0; i < hand1.cards().length; i++) {
            var cardCompare = Integer.compare(hand1.cards()[i], hand2.cards()[i]);
            if (cardCompare != 0) {
                return cardCompare;
            }
        }

        return 0;
    }

    public static Hand parseLine(String line, boolean isPart02) {
        var parts = line.split(" ");
        var cards = Arrays.stream(parts[0].split(""))
            .mapToInt(card -> isPart02 ? CARDS2.get(card) : CARDS1.get(card))
            .toArray();
        var bet = Integer.parseInt(parts[1]);
        return new Hand(cards, bet, isPart02);
    }

    private static final Map<String, Integer> CARDS1;
    private static final Map<String, Integer> CARDS2;
    private static final int JOKER = 1;

    static {
        CARDS1 = new HashMap<>();
        CARDS1.put("A", 14);
        CARDS1.put("K", 13);
        CARDS1.put("Q", 12);
        CARDS1.put("J", 11);
        CARDS1.put("T", 10);
        IntStream.range(2, 10)
            .forEach(i -> CARDS1.put(String.valueOf(i), i));

        CARDS2 = new HashMap<>();
        CARDS2.put("A", 14);
        CARDS2.put("K", 13);
        CARDS2.put("J", JOKER);
        CARDS2.put("T", 10);
        CARDS2.put("Q", 12);
        IntStream.range(2, 10)
            .forEach(i -> CARDS2.put(String.valueOf(i), i));
    }
}
