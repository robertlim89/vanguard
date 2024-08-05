package com.vanguard.codingtest.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QueryTest {
    @ParameterizedTest()
    @MethodSource("getTestQueries")
    void testQueryEvaluation(Query<?> query, List<Long> matchingIDs) {
        var events = List.of(
                new Event("AU_Bank", "AU_Bank", "AUD", 200.0, 1L),
                new Event("US_Bank", "US_Bank", "USD", 95.0, 2L),
                new Event("HK_Bank", "US_Bank", "USD", 100.0, 3L),
                new Event("HK_Bank", "AU_Bank", "HKD", 10000.0, 4L),
                new Event("AU_Bank", "AU_Bank", "AUD", 51.0, 5L),
                new Event("AU_Bank", "US_Bank", "AUD", 100.0, 6L),
                new Event("NZ_Bank", "AU_Bank", "NZD", 100.0, 7L)

        );
        var ids = events.stream().filter(query.evaluate()).map(Event::getId).toList();
        assertThat(ids, is(matchingIDs));
    }

    private static Stream<Arguments> getTestQueries() {
        return Stream.of(
                Arguments.of(new Query<>(null, Collections.emptyList()), List.of(1L,2L,3L,4L,5L,6L,7L)),
                Arguments.of(new Query<>("sellerParty", "AU_Bank", ComparisonOperation.EQUALS), List.of(1L, 5L, 6L)),
                Arguments.of(new Query<>("buyerParty", "US_Bank", ComparisonOperation.EQUALS),List.of(2L, 3L, 6L)),
                Arguments.of(new Query<>(LogicalOperation.AND, List.of(
                        new Query<>("sellerParty", "HK_Bank", ComparisonOperation.EQUALS),
                        new Query<>("premiumCurrency", "HKD", ComparisonOperation.EQUALS)
                )), List.of(4L)),
                Arguments.of(new Query<>(LogicalOperation.OR, List.of(
                        new Query<>("sellerParty", "US_Bank", ComparisonOperation.EQUALS),
                        new Query<>("premiumCurrency", "USD", ComparisonOperation.EQUALS)
                )), List.of(2L, 3L)),
                Arguments.of(new Query<>(LogicalOperation.OR, "sellerParty", "AU_Bank", ComparisonOperation.EQUALS,
                        List.of(new Query<>("buyerParty", "AU_Bank", ComparisonOperation.EQUALS))),
                       List.of(1L, 4L, 5L, 6L, 7L)),
                Arguments.of(new Query<>(LogicalOperation.AND, "premiumCurrency", "AUD", ComparisonOperation.EQUALS,
                        List.of(new Query<>("premiumAmount", 50.0, ComparisonOperation.GREATER_THAN),
                        new Query<>("premiumAmount", 200.0, ComparisonOperation.LESS_THAN))),
                       List.of(5L, 6L))
        );
    }

    private static Stream<Query<?>> getExceptionalQueries() {
        return Stream.of(
            new Query<>("unknown property", "", ComparisonOperation.EQUALS),
            new Query<>("sellerParty", 2, ComparisonOperation.GREATER_THAN),
            new Query<>("buyerParty", 3, ComparisonOperation.EQUALS),
            new Query<>("premiumCurrency", 4, ComparisonOperation.EQUALS),
            new Query<>("premiumAmount", 123, ComparisonOperation.GREATER_THAN),
            new Query<>("premiumAmount", 123, ComparisonOperation.LESS_THAN),
            new Query<>(null, List.of(new Query<>("sellerParty", "AU_Bank", ComparisonOperation.EQUALS)))
        );
    }

    @ParameterizedTest()
    @MethodSource("getExceptionalQueries")
    public void testGetExceptions(Query<?> query) {
        assertThrows(IllegalArgumentException.class, query::evaluate);
    }
}