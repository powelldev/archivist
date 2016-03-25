package com.fireminder.archivist.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static String getTimestamp() {
    return sdf.format(new Date(System.currentTimeMillis()));
  }

  public static String getTimestamp(long currentTimeMillis) {
    return sdf.format(new Date(currentTimeMillis));
  }
}
