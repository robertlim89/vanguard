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
        if (!queries.isEmpty()) {
            Predicate<Event> identity;
            BinaryOperator<Predicate<Event>> reducer;
            switch (logicalOperation) {
                default:
                case OR: {
                    identity = a -> false;
                    reducer = Predicate::or;
                    break;
                }
                case AND: {
                    identity = a -> true;
                    reducer = Predicate::and;
                }
            }
            return queries.stream()
                    .map(Query::evaluate)
                    .reduce(identity, reducer);
        } else if (property != null && value != null && comparison != null) {
            return switch(property) {
                case "sellerParty" -> event -> comparison.toPredicate(value).test((T) event.getSellerParty());
                case "buyerParty" -> event -> comparison.toPredicate(value).test((T) event.getBuyerParty());
                case "premiumCurrency" -> event -> comparison.toPredicate(value).test((T) event.getPremiumCurrency());
                case "premiumAmount" -> event -> comparison.toPredicate(value).test((T) Double.valueOf(event.getPremiumAmount()));
                default -> throw new IllegalArgumentException("Unknown property: %s".formatted(property));
            };
        }
        throw new IllegalArgumentException("Unknown query: %s".formatted(this));
    }
}

