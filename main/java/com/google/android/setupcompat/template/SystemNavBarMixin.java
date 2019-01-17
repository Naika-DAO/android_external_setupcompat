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

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

/**
 * A {@link Mixin} for setting and getting background color and window compatible with light theme
 * of system navigation bar.
 */
public class SystemNavBarMixin implements Mixin {

  private final PartnerCustomizationLayout partnerCustomizationLayout;
  private final Window windowOfActivity;
  private final View decorView;
  @VisibleForTesting final boolean applyPartnerResources;

  /**
   * Creates a mixin for managing system navigation bar.
   *
   * @param layout The layout this Mixin belongs to.
   * @param window The window this activity of Mixin belongs to.
   * @param attrs XML attributes given to the layout.
   * @param defStyleAttr The default style attribute as given to the constructor of the layout.
   * @param applyPartnerResources determine applies partner resources or not.
   */
  public SystemNavBarMixin(
      @NonNull PartnerCustomizationLayout layout,
      @NonNull Window window,
      @Nullable AttributeSet attrs,
      @AttrRes int defStyleAttr,
      boolean applyPartnerResources) {
    partnerCustomizationLayout = layout;
    windowOfActivity = window;
    decorView = window.getDecorView();
    this.applyPartnerResources = applyPartnerResources;

    TypedArray a =
        partnerCustomizationLayout
            .getContext()
            .obtainStyledAttributes(attrs, R.styleable.SucSystemNavBarMixin, defStyleAttr, 0);
    int navigationBarBackground =
        a.getColor(R.styleable.SucSystemNavBarMixin_sucSystemNavBarBackgroundColor, 0);
    setSystemNavBarBackground(navigationBarBackground);
    setLightSystemNavBar(
        a.getBoolean(R.styleable.SucSystemNavBarMixin_sucLightSystemNavBar, isLightSystemNavBar()));
    a.recycle();
  }

  /**
   * Sets the background color of navigation bar. The color will be overridden by partner resource
   * if the activity is running in setup wizard flow.
   *
   * @param color The background color of navigation bar.
   */
  public void setSystemNavBarBackground(int color) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      if (applyPartnerResources) {
        Context context = partnerCustomizationLayout.getContext();
        color =
            PartnerConfigHelper.get(context)
                .getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_BG_COLOR);
      }

      windowOfActivity.setNavigationBarColor(color);
    }
  }

  /** Returns the background color of navigation bar. */
  public int getSystemNavBarBackground() {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      return windowOfActivity.getNavigationBarColor();
    }
    return Color.BLACK;
  }

  /**
   * Sets the navigation bar to draw in a mode that is compatible with light or dark navigation bar
   * backgrounds. The navigation bar drawing mode will be overridden by partner resource if the
   * activity is running in setup wizard flow.
   *
   * @param isLight true means compatible with light theme, otherwise compatible with dark theme
   */
  public void setLightSystemNavBar(boolean isLight) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
      if (applyPartnerResources) {
        Context context = partnerCustomizationLayout.getContext();
        isLight =
            PartnerConfigHelper.get(context)
                .getBoolean(context, PartnerConfig.CONFIG_LIGHT_NAVIGATION_BAR, false);
      }

      if (isLight) {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() | SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
      } else {
        decorView.setSystemUiVisibility(
            decorView.getSystemUiVisibility() & ~SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
      }
    }
  }

  /**
   * Returns true if the navigation bar icon should be drawn on light background, false if the icons
   * should be drawn light-on-dark.
   */
  public boolean isLightSystemNavBar() {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
      return (decorView.getSystemUiVisibility() & SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
          == SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
    }
    return true;
  }
}
