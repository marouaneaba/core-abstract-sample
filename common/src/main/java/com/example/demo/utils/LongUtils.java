package com.example.demo.utils;

public class LongUtils {

    private LongUtils() {
        throw new AssertionError("Utility class must not be instantiated.");
    }

    public static Integer convertToInteger(Long value) {
        try{
            return value.intValue();
        }catch (NullPointerException e){
            return Integer.valueOf(0);
        }
    }
}
