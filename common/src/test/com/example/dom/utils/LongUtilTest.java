package com.example.dom.utils;


import com.example.demo.utils.ListUtils;
import com.example.demo.utils.LongUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LongUtilTest {

    @Test
    void shouldReturnIntegerValueWhenLongArgsIsNotNull() {
        // When
        Integer actual = LongUtils.convertToInteger(Long.valueOf(50));

        // Then
        assertThat(actual)
                .isNotNull()
                .isNotNegative()
                .isNotInstanceOf(Long.class)
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(50);
    }

    @Test
    void shouldReturnZeroWhenLongArgsIsNull() {
        // When
        Integer actual = LongUtils.convertToInteger(null);

        // Then
        assertThat(actual)
                .isNotNull()
                .isNotNegative()
                .isNotInstanceOf(Long.class)
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(0);
    }

    @Test
    void shouldReturnTrueWhenListContainsDuplicateElement() {
        // Given
        Point x = new Point(1,2);
        Point y = new Point(3,4);
        Point z = new Point(5,6);

        // When
        boolean actual = ListUtils.areUnique(List.of(x,y,z,x));

        // Then
        assertThat(actual).isNotNull()
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenListNotContainsDuplicateElement() {
        // Given
        Point x = new Point(1,2);
        Point y = new Point(3,4);
        Point z = new Point(5,6);

        // When
        boolean actual = ListUtils.areUnique(List.of(x,y,z));

        // Then
        assertThat(actual).isNotNull()
                .isFalse();
    }
}
