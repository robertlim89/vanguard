package com.vanguard.codingtest.model;

import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public record Query<T extends Comparable<T>> (LogicalOperation logicalOperation, String property, T value, ComparisonOperation comparison, List<Query<?>> queries) {
    public static final Query<String> BASIC_QUERY = new Query<>(LogicalOperation.OR,
            List.of(
                    new Query<String>(LogicalOperation.AND, List.of(
                            new Query<>("sellerParty", "EMU_BANK", ComparisonOperation.EQUALS),
                            new Query<>("premiumCurrency", "AUD", ComparisonOperation.EQUALS)
                    )),
                    new Query<String>(LogicalOperation.AND, List.of(
                            new Query<>("sellerParty", "BISON_BANK", ComparisonOperation.EQUALS),
                            new Query<>("premiumCurrency", "USD", ComparisonOperation.EQUALS)
                    ))
            ));

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
                case "sellerParty" -> {
                    if (!(value instanceof String)) throw new IllegalArgumentException("Type of T (%s) does not match String".formatted(value.getClass()));
                    yield event -> comparison.toPredicate(value).test((T) event.getSellerParty());
                }
                case "buyerParty" -> {
                    if (!(value instanceof String)) throw new IllegalArgumentException("Type of T (%s) does not match String".formatted(value.getClass()));
                    yield event -> comparison.toPredicate(value).test((T) event.getBuyerParty());
                }
                case "premiumCurrency" -> {
                    if (!(value instanceof String)) throw new IllegalArgumentException("Type of T (%s) does not match String".formatted(value.getClass()));
                    yield event -> comparison.toPredicate(value).test((T) event.getPremiumCurrency());
                }
                case "premiumAmount" -> {
                    if (!(value instanceof Double)) throw new IllegalArgumentException("Type of T (%s) does not match Double".formatted(value.getClass()));
                    yield event -> comparison.toPredicate(value).test((T) Double.valueOf(event.getPremiumAmount()));
                }
                default -> throw new IllegalArgumentException("Unknown property: %s".formatted(property));
            };
        } else {
            identity = switch (logicalOperation) {
                case OR -> a -> false;
                case AND -> a -> true;
                case null -> a -> true;
            };
        }

        if (!queries.isEmpty()) {
            BinaryOperator<Predicate<Event>> reducer = switch (logicalOperation) {
                case OR -> Predicate::or;
                case AND -> Predicate::and;
                case null -> throw new IllegalArgumentException("Operator not specified");
            };
            return queries.stream()
                    .map(Query::evaluate)
                    .reduce(identity, reducer);
        }
        return identity;
    }


    @Override
    public String toString() {
        var sb = new StringBuilder();
        if (property != null && comparison != null && value != null) {
            sb.append("(%s %s %s)".formatted(property, comparison.toString(), value));
        }
        if (!queries.isEmpty()) {
            var joiner = " %s ".formatted(logicalOperation.toString());
            if (!sb.isEmpty()) sb.append(joiner);
            var format = queries.size() == 1 ? "%s" : "(%s)";
            sb.append(format.formatted(String.join(joiner, queries.stream().map(Query::toString).toList())));
        }
        return sb.toString();
    }
}

