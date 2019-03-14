/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupcompat.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * A helper class to manage the system navigation bar and status bar. This will add various
 * systemUiVisibility flags to the given Window or View to make them follow the Setup Wizard style.
 *
 * <p>When the useImmersiveMode intent extra is true, a screen in Setup Wizard should hide the
 * system bars using methods from this class. For Lollipop, {@link
 * #hideSystemBars(android.view.Window)} will completely hide the system navigation bar and change
 * the status bar to transparent, and layout the screen contents (usually the illustration) behind
 * it.
 */
public class SystemBarBaseHelper {

  private static final String TAG = "SystemBarBaseHelper";

  @SuppressLint("InlinedApi")
  public static final int DEFAULT_IMMERSIVE_FLAGS =
      View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

  @SuppressLint("InlinedApi")
  public static final int DIALOG_IMMERSIVE_FLAGS =
      View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

  /**
   * The maximum number of retries when peeking the decor view. When polling for the decor view,
   * waiting it to be installed, set a maximum number of retries.
   */
  private static final int PEEK_DECOR_VIEW_RETRIES = 3;

  /** Convenience method to add a visibility flag in addition to the existing ones. */
  public static void addVisibilityFlag(final View view, final int flag) {
    final int vis = view.getSystemUiVisibility();
    view.setSystemUiVisibility(vis | flag);
  }

  /** Convenience method to add a visibility flag in addition to the existing ones. */
  public static void addVisibilityFlag(final Window window, final int flag) {
    WindowManager.LayoutParams attrs = window.getAttributes();
    attrs.systemUiVisibility |= flag;
    window.setAttributes(attrs);
  }

  /**
   * Convenience method to remove a visibility flag from the view, leaving other flags that are not
   * specified intact.
   */
  public static void removeVisibilityFlag(final View view, final int flag) {
    final int vis = view.getSystemUiVisibility();
    view.setSystemUiVisibility(vis & ~flag);
  }

  /**
   * Convenience method to remove a visibility flag from the window, leaving other flags that are
   * not specified intact.
   */
  public static void removeVisibilityFlag(final Window window, final int flag) {
    WindowManager.LayoutParams attrs = window.getAttributes();
    attrs.systemUiVisibility &= ~flag;
    window.setAttributes(attrs);
  }

  /**
   * Add the specified immersive flags to the decor view of the window, because {@link
   * View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN} only takes effect when it is added to a view instead of
   * the window.
   */
  public static void addImmersiveFlagsToDecorView(final Window window, final int vis) {
    getDecorView(
        window,
        new OnDecorViewInstalledListener() {
          @Override
          public void onDecorViewInstalled(View decorView) {
            addVisibilityFlag(decorView, vis);
          }
        });
  }

  public static void removeImmersiveFlagsFromDecorView(final Window window, final int vis) {
    getDecorView(
        window,
        new OnDecorViewInstalledListener() {
          @Override
          public void onDecorViewInstalled(View decorView) {
            removeVisibilityFlag(decorView, vis);
          }
        });
  }

  private static void getDecorView(Window window, OnDecorViewInstalledListener callback) {
    new DecorViewFinder().getDecorView(window, callback, PEEK_DECOR_VIEW_RETRIES);
  }

  private static class DecorViewFinder {

    private final Handler handler = new Handler();
    private Window window;
    private int retries;
    private OnDecorViewInstalledListener callback;

    private final Runnable checkDecorViewRunnable =
        new Runnable() {
          @Override
          public void run() {
            // Use peekDecorView instead of getDecorView so that clients can still set window
            // features after calling this method.
            final View decorView = window.peekDecorView();
            if (decorView != null) {
              callback.onDecorViewInstalled(decorView);
            } else {
              retries--;
              if (retries >= 0) {
                // If the decor view is not installed yet, try again in the next loop.
                handler.post(checkDecorViewRunnable);
              } else {
                Log.w(TAG, "Cannot get decor view of window: " + window);
              }
            }
          }
        };

    public void getDecorView(Window window, OnDecorViewInstalledListener callback, int retries) {
      this.window = window;
      this.retries = retries;
      this.callback = callback;
      checkDecorViewRunnable.run();
    }
  }

  private interface OnDecorViewInstalledListener {

    void onDecorViewInstalled(View decorView);
  }
}
