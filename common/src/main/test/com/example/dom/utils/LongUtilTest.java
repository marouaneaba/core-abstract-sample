package com.example.dom.utils;

import com.example.demo.utils.LongUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LongUtilTest {

    @Test
    void shouldReturnIntegerValueWhenLongArgsIsNotNull() {
        // When
        Integer actual = LongUtil.convertToInteger(Long.valueOf(50));

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
        Integer actual = LongUtil.convertToInteger(null);

        // Then
        assertThat(actual)
                .isNotNull()
                .isNotNegative()
                .isNotInstanceOf(Long.class)
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(0);
    }
}
