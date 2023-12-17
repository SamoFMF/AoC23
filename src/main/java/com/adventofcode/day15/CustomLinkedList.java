package com.adventofcode.day15;

import java.util.Optional;
import java.util.function.Predicate;

public class CustomLinkedList<T> {

    private Node<T> first;
    private Node<T> last;

    public void addLast(Node<T> node) {
        if (isEmpty()) {
            first = node;
            last = node;
            return;
        }

        last.setNext(node);
        node.setPrev(last);
        last = node;
    }

    public Node<T> getFirst() {
        return first;
    }

    public void removeIf(Predicate<Node<T>> predicate) {
        if (isEmpty()) {
            return;
        }

        var current = first;
        while (current != null) {
            if (predicate.test(current)) {
                var prev = current.getPrev();
                var next = current.getNext();

                if (current == first) {
                    first = next;
                }

                if (current == last) {
                    last = prev;
                }

                if (prev != null) {
                    prev.setNext(next);
                }

                if (next != null) {
                    next.setPrev(prev);
                }
            }

            current = current.getNext();
        }
    }

    public Optional<Node<T>> get(Predicate<Node<T>> predicate) {
        var current = first;
        while (current != null) {
            if (predicate.test(current)) {
                return Optional.of(current);
            }

            current = current.getNext();
        }

        return Optional.empty();
    }

    public boolean isEmpty() {
        return first == null;
    }
}
