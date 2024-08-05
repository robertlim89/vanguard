package com.vanguard.codingtest.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComparisonOperationTest {
    @ParameterizedTest
    @MethodSource("getComparisonOperationTestArguments")
    public <T extends Comparable<T>> void testComparisonOperation(ComparisonOperation operation, T first, T second, boolean result) {
        assertEquals(result, operation.toPredicate(second).test(first));
    }

    private static Stream<Arguments> getComparisonOperationTestArguments() {
        return Stream.of(
                Arguments.of(ComparisonOperation.EQUALS, "This", "That", false),
                Arguments.of(ComparisonOperation.EQUALS, "This", "This", true),
                Arguments.of(ComparisonOperation.EQUALS, "That", "This", false),
                Arguments.of(ComparisonOperation.EQUALS, 2.0, 2.0, true),
                Arguments.of(ComparisonOperation.GREATER_THAN, "Aa", "Bb", false),
                Arguments.of(ComparisonOperation.GREATER_THAN, "Bb", "Aa", true),
                Arguments.of(ComparisonOperation.GREATER_THAN, "Aa", "Aa", false),
                Arguments.of(ComparisonOperation.GREATER_THAN, 2.0, 1.0, true),
                Arguments.of(ComparisonOperation.LESS_THAN, "Aa", "Bb", true),
                Arguments.of(ComparisonOperation.LESS_THAN, "Bb", "Aa", false),
                Arguments.of(ComparisonOperation.LESS_THAN, "Aa", "Aa", false),
                Arguments.of(ComparisonOperation.LESS_THAN, 1.0, 2.0, true)
        );
    }
}
