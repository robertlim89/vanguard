package com.vanguard.codingtest.model;

import java.util.function.Predicate;

public enum ComparisonOperation {
    EQUALS("=="),
    GREATER_THAN(">"),
    LESS_THAN("<");

    private final String value;

    ComparisonOperation(String value) {
        this.value = value;
    }

    public <T extends Comparable<T>> Predicate<T> toPredicate(T value) {
        return switch(this) {
            case EQUALS -> a -> a.equals(value);
            case GREATER_THAN -> a -> a.compareTo(value) > 0;
            case LESS_THAN -> a -> a.compareTo(value) < 0;
        };
    }

    @Override
    public String toString() {
        return value;
    }
}
