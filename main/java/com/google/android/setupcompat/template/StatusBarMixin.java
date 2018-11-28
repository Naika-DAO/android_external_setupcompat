/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.google.android.setupcompat.template;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.util.PartnerConfig;
import com.google.android.setupcompat.util.PartnerConfigHelper;
import com.google.android.setupcompat.view.StatusBarBackgroundLayout;

/**
 * A {@link Mixin} for setting and getting background color, and window compatible light/dark theme
 * of status bar.
 */
public class StatusBarMixin implements Mixin {

  private final PartnerCustomizationLayout partnerCustomizationLayout;
  private final StatusBarBackgroundLayout statusBarLayout;
  private final View decorView;
  @VisibleForTesting final boolean applyPartnerResources;

  /**
   * Creates a mixin for managing status bar.
   *
   * @param layout The layout this Mixin belongs to.
   * @param window The window this activity of Mixin belongs to.
   * @param attrs XML attributes given to the layout.
   * @param defStyleAttr The default style attribute as given to the constructor of the layout.
   * @param applyPartnerResources determine applies partner resources or not.
   */
  public StatusBarMixin(
      @NonNull PartnerCustomizationLayout layout,
      @NonNull Window window,
      @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr,
      boolean applyPartnerResources) {
    partnerCustomizationLayout = layout;
    statusBarLayout = partnerCustomizationLayout.findManagedViewById(R.id.suc_layout_status);
    decorView = window.getDecorView();
    this.applyPartnerResources = applyPartnerResources;

    if (statusBarLayout == null) {
      throw new NullPointerException("StatusBarBackgroundLayout cannot be null in StatusBarMixin");
    }

    // Override the color of status bar to transparent such that the color of
    // StatusBarBackgroundLayout can be seen.
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      window.setStatusBarColor(Color.TRANSPARENT);
    }

    TypedArray a =
        layout
            .getContext()
            .obtainStyledAttributes(attrs, R.styleable.SucStatusBarMixin, defStyleAttr, 0);
    setStatusBarWindowLight(
        a.getBoolean(
            R.styleable.SucStatusBarMixin_sucStatusBarWindowLight, isStatusBarWindowLight()));
    setStatusBarBackground(a.getDrawable(R.styleable.SucStatusBarMixin_sucStatusBarBackground));
    a.recycle();
  }

  /**
   * Sets the background color of status bar. The color will be overridden by partner resource if
   * the activity is running in setup wizard flow.
   *
   * @param color The background color of status bar.
   */
  public void setStatusBarBackground(int color) {
    setStatusBarBackground(new ColorDrawable(color));
  }

  /**
   * Sets the background image of status bar. The drawable will be overridden by partner resource if
   * the activity is running in setup wizard flow.
   *
   * @param background The drawable of status bar.
   */
  public void setStatusBarBackground(Drawable background) {
    if (applyPartnerResources) {
      Context context = partnerCustomizationLayout.getContext();
      background =
          PartnerConfigHelper.get(context)
              .getDrawable(context, PartnerConfig.CONFIG_STATUS_BAR_BACKGROUND);
    }

    statusBarLayout.setStatusBarBackground(background);
  }

  /** Returns the background of status bar. */
  public Drawable getStatusBarBackground() {
    return statusBarLayout.getStatusBarBackground();
  }

  /**
   * Sets the status bar to draw in a mode that is compatible with light or dark status bar
   * backgrounds. The status bar drawing mode will be overridden by partner resource if the activity
   * is running in setup wizard flow.
   *
   * @param isLight true means compatible with light theme, otherwise compatible with dark theme
   */
  public void setStatusBarWindowLight(boolean isLight) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
      if (applyPartnerResources) {
        Context context = partnerCustomizationLayout.getContext();
        isLight =
            PartnerConfigHelper.get(context)
                .getBoolean(context, PartnerConfig.CONFIG_WINDOW_LIGHT_STATUS_BAR, false);
      }

      if (isLight) {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      } else {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() & ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      }
    }
  }

  /**
   * Returns true if status bar icons should be drawn on light background, false if the icons should
   * be light-on-dark.
   */
  public boolean isStatusBarWindowLight() {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
      return (decorView.getSystemUiVisibility() & SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
          == SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    }
    return true;
  }
}
