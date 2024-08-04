package com.vanguard.codingtest.model;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public record Query<T extends Comparable<T>> (LogicalOperation logicalOperation, String property, T value, ComparisonOperation comparison, List<Query<?>> queries) {
    public Query(LogicalOperation logicalOperation, List<Query<?>> queries) {
        this(logicalOperation, null, null, null, queries);
    }

    public Query(String property, T value, ComparisonOperation comparison) {
        this(LogicalOperation.OR, property, value, comparison, Collections.emptyList());
    }

    public Predicate<Event> evaluate() {
        Predicate<Event> identity;
        if (property != null && value != null && comparison != null) {
            identity = switch(property) {
                case "sellerParty" -> event -> comparison.toPredicate(value).test((T) event.getSellerParty());
                case "buyerParty" -> event -> comparison.toPredicate(value).test((T) event.getBuyerParty());
                case "premiumCurrency" -> event -> comparison.toPredicate(value).test((T) event.getPremiumCurrency());
                case "premiumAmount" -> event -> comparison.toPredicate(value).test((T) Double.valueOf(event.getPremiumAmount()));
                default -> throw new IllegalArgumentException("Unknown property: %s".formatted(property));
            };
        } else {
            identity = switch (logicalOperation) {
                case AND -> a -> true;
                case OR -> a -> false;
                case null -> a -> false;
            };
        }

        if (!queries.isEmpty()) {
            BinaryOperator<Predicate<Event>> reducer = switch (logicalOperation) {
                case AND -> Predicate::and;
                case OR -> Predicate::or;
                case null -> Predicate::or;
            };
            return queries.stream()
                    .map(Query::evaluate)
                    .reduce(identity, reducer);
        }
        return identity;
    }
}

