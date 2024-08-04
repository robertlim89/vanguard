package com.vanguard.codingtest.model;

public enum LogicalOperation {
    OR,
    AND;

    @Override
    public String toString() {
        return switch(this) {
            case OR -> "||";
            case AND -> "&&";
        };
    }
}
