package com.google.android.setupcompat.internal;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/** Utility methods for detecting the build API version. */
public final class BuildCompat {

  private BuildCompat() {}

  // TODO(b/127925696): remove the code for pre-release version of Android Q
  public static boolean isAtLeastQ() {
    return (VERSION.SDK_INT > VERSION_CODES.P)
        || (VERSION.CODENAME.length() == 1
            && VERSION.CODENAME.charAt(0) >= 'Q'
            && VERSION.CODENAME.charAt(0) <= 'Z');
  }
}
