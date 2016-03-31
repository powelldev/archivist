package com.fireminder.archivist.utils;

import android.content.res.Resources;
import android.util.TypedValue;

import com.fireminder.archivist.IvyApplication;

public class LayoutUtils {

  public static float dpToPx(int dp) {
    Resources r = IvyApplication.getAppContext().getResources();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
  }
}
