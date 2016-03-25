package com.fireminder.archivist.model;

import java.util.UUID;

public class SqlUtil {



  public static String[] toArray(UUID item) {
    return toArray(item.toString());
  }

  public static String[] toArray(Object item) {
    return new String[] {String.valueOf(item)};
  }

  public static String[] toArray(String item) {
    return new String[] {item};
  }
}
