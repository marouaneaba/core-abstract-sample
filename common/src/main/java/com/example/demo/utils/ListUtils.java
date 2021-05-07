package com.example.demo.utils;

import java.util.Collections;
import java.util.List;

public class ListUtils {

  public static final int STOCK_SHOE_DUPLICATION_ELEMENT_MAX = 1;

  private ListUtils() {}

  public static <T> boolean areUnique(final List<T> list) {
    return list.stream().anyMatch(element -> Collections.frequency(list, element) > STOCK_SHOE_DUPLICATION_ELEMENT_MAX);
  }
}
