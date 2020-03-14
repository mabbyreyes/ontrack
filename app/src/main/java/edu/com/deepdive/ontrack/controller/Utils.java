package edu.com.deepdive.ontrack.controller;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

class Utils {
  Utils() {
  }

  static int convertToPx(int dp, Resources resources) {
    DisplayMetrics dm = resources.getDisplayMetrics();
    return (int) TypedValue.applyDimension(1, (float)dp, dm);
  }

}
