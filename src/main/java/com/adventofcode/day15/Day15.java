package com.adventofcode.day15;

import com.adventofcode.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day15 {

    public static void main(String[] args) throws IOException {
        var inputs = Utils.readString("inputs/input15.txt").trim().split(",");

        part01(inputs);
        part02(inputs);
    }

    private static void part01(String[] input) {
        var result = Arrays.stream(input)
            .mapToInt(Day15::hash)
            .sum();

        System.out.println(result);
    }

    private static void part02(String[] input) {
        var boxes = new ArrayList<CustomLinkedList<Lens>>(256);
        IntStream.range(0, 256).forEach(i -> boxes.add(new CustomLinkedList<>()));
        for (var instruction : input) {
            var iDash = instruction.indexOf('-');
            if (iDash < 0) {
                var split = instruction.split("=");
                var label = split[0];
                var length = Integer.parseInt(split[1]);
                var hash = hash(label);
                boxes.get(hash).get(node -> node.getData().getLabel().equals(label))
                    .ifPresentOrElse(
                        node -> node.getData().setLength(length),
                        () -> boxes.get(hash).addLast(new Node<>(new Lens(label, length)))
                    );
            } else {
                var label = instruction.substring(0, iDash);
                var hash = hash(label);
                boxes.get(hash).removeIf(node -> node.getData().getLabel().equals(label));
            }
        }

        var result = 0L;
        for (var boxNumber = 0; boxNumber < 256; boxNumber++) {
            var box = boxes.get(boxNumber);
            var slotNumber = 1;
            var current = box.getFirst();
            while (current != null) {
                result += (long) (boxNumber + 1) * (slotNumber++) * current.getData().getLength();
                current = current.getNext();
            }
        }

        System.out.println(result);
    }

    private static int hash(String str) {
        return str.chars().reduce(0, (acc, val) -> ((acc + val) * 17) % 256);
    }
}
