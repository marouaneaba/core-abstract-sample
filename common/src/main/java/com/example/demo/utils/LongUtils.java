package com.example.demo.utils;


import com.example.demo.error.ErrorMessage;

public class LongUtils {

    private LongUtils() {
    throw new IllegalCallerException(ErrorMessage.UTILITY_CLASS_INSTANTIATE_ERROR.getDescription());
    }

    public static Integer convertToInteger(Long value) {
        if (value != null) {
            return Math.toIntExact(value);
        }
        return Integer.valueOf(0);
    }
}
