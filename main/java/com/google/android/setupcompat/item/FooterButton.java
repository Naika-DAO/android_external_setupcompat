/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.setupcompat.item;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.template.ButtonFooterMixin;

/**
 * Definition of a footer button. Clients can use this class to customize attributes like text and
 * click listener, and ButtonFooterMixin will inflate a corresponding Button view.
 */
public class FooterButton {
  private final String text;
  private final OnClickListener listener;
  private int theme;

  public FooterButton(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SucFooterButton);
    this.text = a.getString(R.styleable.SucFooterButton_android_text);
    this.listener = null;
    this.theme = a.getResourceId(R.styleable.SucFooterButton_android_theme, 0);
    a.recycle();
  }

  /**
   * Allows client customize text, click listener and theme for footer button
   * before Button has been created. The {@link ButtonFooterMixin} will inflate a corresponding
   * Button view.
   *
   * @param context The context of application.
   * @param text The text for button.
   * @param listener The listener for button.
   * @param theme The theme for button.
   */
  public FooterButton(
      Context context,
      @StringRes int text,
      @Nullable OnClickListener listener,
      @StyleRes int theme) {
    this(context.getString(text), listener, theme);
  }

  public FooterButton(
      String text, @Nullable OnClickListener listener, @StyleRes int theme) {
    this.text = text;
    this.listener = listener;
    this.theme = theme;
  }

  public String getText() {
    return text;
  }

  public OnClickListener getListener() {
    return listener;
  }

  @StyleRes
  public int getTheme() {
    return theme;
  }

  /**
   * Sets the default theme for footer button, the method only for internal use in {@link
   * ButtonFooterMixin} and there will have no influence during setup wizard flow.
   *
   * @param theme The theme for footer button.
   */
  @VisibleForTesting
  public void setTheme(@StyleRes int theme) {
    this.theme = theme;
  }
}
