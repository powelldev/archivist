package com.fireminder.archivist;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestUtil {

  public static String randomString(final int length) {
    final Random r = new Random();
    final StringBuilder sb = new StringBuilder();
    for(int i = 0; i < length; i++) {
      char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
      sb.append(c);
    }
    return sb.toString();
  }


  public static long currentTimeMillis() {
    return 1000000000000L;
  }

  public static long getDefaultDuration() {
    return TimeUnit.MINUTES.toMillis(3);
  }
}
