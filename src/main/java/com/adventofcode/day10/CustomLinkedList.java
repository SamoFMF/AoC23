package com.adventofcode.day10;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T> {

    private final List<Node<T>> originalList;

    public CustomLinkedList() {
        this.originalList = new ArrayList<>();
    }

    public Node<T> get(int i) {
        return originalList.get(i);
    }
}
