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
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.annotation.VisibleForTesting;
import android.util.AttributeSet;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.template.ButtonFooterMixin;

/**
 * Definition of a footer button. Clients can use this class to customize attributes like text,
 * button type and click listener, and ButtonFooterMixin will inflate a corresponding Button view.
 */
public final class FooterButton {

  private final ButtonType buttonType;
  private CharSequence text;
  private boolean enabled;
  private int visibility;
  private int theme;
  @IdRes private int id;
  private OnClickListener onClickListener;
  private OnButtonEventListener buttonListener;

  public FooterButton(Context context, AttributeSet attrs) {
    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SucFooterButton);
    text = a.getString(R.styleable.SucFooterButton_android_text);
    onClickListener = null;
    buttonType =
        ButtonType.valueOf(
            a.getInt(
                R.styleable.SucFooterButton_sucButtonType,
                /* defValue= */ ButtonType.OTHER.getEnumValue()));
    theme = a.getResourceId(R.styleable.SucFooterButton_android_theme, /* defValue= */ 0);
    a.recycle();
  }

  /**
   * Allows client customize text, click listener and theme for footer button before Button has been
   * created. The {@link ButtonFooterMixin} will inflate a corresponding Button view.
   *
   * @param context The context of application.
   * @param text The text for button.
   * @param listener The listener for button.
   * @param buttonType The type of button.
   * @param theme The theme for button.
   * @deprecated use {@link #FooterButton.Builder(Context)} instead.
   */
  @Deprecated
  public FooterButton(
      Context context,
      @StringRes int text,
      @Nullable OnClickListener listener,
      @Nullable ButtonType buttonType,
      @StyleRes int theme) {
    this(context.getString(text), listener, buttonType, theme);
  }

  /** @deprecated use {@link #FooterButton.Builder(Context)} instead. */
  @Deprecated
  public FooterButton(
      Context context,
      @StringRes int text,
      @Nullable OnClickListener listener,
      @StyleRes int theme) {
    this(context.getString(text), listener, ButtonType.OTHER, theme);
  }

  /** @deprecated use {@link #FooterButton.Builder(Context)} instead. */
  @Deprecated
  public FooterButton(String text, @Nullable OnClickListener listener, @StyleRes int theme) {
    this(text, listener, ButtonType.OTHER, theme);
  }

  /** @deprecated use {@link #FooterButton.Builder(Context)} instead. */
  @Deprecated
  public FooterButton(
      CharSequence text,
      @Nullable OnClickListener listener,
      @Nullable ButtonType buttonType,
      @StyleRes int theme) {
    this.text = text;
    onClickListener = listener;
    this.buttonType = buttonType;
    this.theme = theme;
  }

  /** Sets the resource id of footer button. */
  @VisibleForTesting
  public void setId(@IdRes int id) {
    this.id = id;
  }

  /** Returns the text that this footer button is displaying. */
  public CharSequence getText() {
    return text;
  }

  /** Returns an {@link OnClickListener} of this footer button. */
  @VisibleForTesting
  public OnClickListener getOnClickListener() {
    return onClickListener;
  }

  /**
   * Registers a callback to be invoked when this view of footer button is clicked.
   *
   * @param listener The callback that will run
   */
  public void setOnClickListener(@Nullable OnClickListener listener) {
    onClickListener = listener;
    if (buttonListener != null && id != 0) {
      buttonListener.onClickListenerChanged(listener, id);
    }
  }

  /**
   * Registers a callback to be invoked when touch event footer button has reacted.
   *
   * @param listener The callback that will run
   */
  public void setOnTouchListener(@Nullable OnTouchListener listener) {
    if (buttonListener != null && id != 0) {
      buttonListener.onTouchListenerChanged(listener, id);
    }
  }

  /** Returns the type of this footer button icon. */
  public ButtonType getButtonType() {
    return buttonType;
  }

  /** Returns the theme of this footer button. */
  @StyleRes
  public int getTheme() {
    return theme;
  }

  /**
   * Sets the enabled state of this footer button.
   *
   * @param enabled True if this view is enabled, false otherwise.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    if (buttonListener != null && id != 0) {
      buttonListener.onEnabledChanged(enabled, id);
    }
  }

  /** Returns the enabled status for this footer button. */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the visibility state of this footer button.
   *
   * @param visibility one of {@link #VISIBLE}, {@link #INVISIBLE}, or {@link #GONE}.
   */
  public void setVisibility(int visibility) {
    this.visibility = visibility;
    if (buttonListener != null && id != 0) {
      buttonListener.onVisibilityChanged(visibility, id);
    }
  }

  /** Returns the visibility status for this footer button. */
  public int getVisibility() {
    return visibility;
  }

  /** Sets the text to be displayed using a string resource identifier. */
  public void setText(Context context, @IdRes int resid) {
    setText(context.getText(resid));
  }

  /** Sets the text to be displayed on footer button. */
  public void setText(CharSequence text) {
    this.text = text;
    if (buttonListener != null && id != 0) {
      buttonListener.onTextChanged(text, id);
    }
  }

  /**
   * Registers a callback to be invoked when footer button API has set.
   *
   * @param listener The callback that will run
   */
  @VisibleForTesting
  public void setOnButtonEventListener(@Nullable OnButtonEventListener listener) {
    if (listener != null) {
      buttonListener = listener;
    }
  }

  /** Interface definition for a callback to be invoked when footer button API has set. */
  @VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
  public interface OnButtonEventListener {

    void onClickListenerChanged(@Nullable OnClickListener listener, @IdRes int id);

    void onTouchListenerChanged(@Nullable OnTouchListener listener, @IdRes int id);

    void onEnabledChanged(boolean enabled, @IdRes int id);

    void onVisibilityChanged(int visibility, @IdRes int id);

    void onTextChanged(CharSequence text, @IdRes int id);
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

  /**
   * Types for footer button. The button appearance and behavior may change based on its type. In
   * order to be backward compatible with application built with old version of setupcompat; the
   * {@code enumValue} of each ButtonType should not be changed.
   */
  public enum ButtonType {
    /** A type of button that doesn't fit into any other categories. */
    OTHER(0),
    /**
     * A type of button that will set up additional elements of the ongoing setup step(s) when
     * clicked.
     */
    ADD_ANOTHER(1),
    /** A type of button that will cancel the ongoing setup step(s) and exit setup when clicked. */
    CANCEL(2),
    /** A type of button that will clear the progress when clicked. (eg: clear PIN code) */
    CLEAR(3),
    /** A type of button that will exit the setup flow when clicked. */
    DONE(4),
    /** A type of button that will go to the next screen, or next step in the flow when clicked. */
    NEXT(5),
    /** A type of button to opt-in or agree to the features described in the current screen. */
    OPT_IN(6),
    /** A type of button that will skip the current step when clicked. */
    SKIP(7),
    /** A type of button that will stop the ongoing setup step(s) and skip forward when clicked. */
    STOP(8);

    private final int enumValue;

    public int getEnumValue() {
      return enumValue;
    }

    ButtonType(int enumValue) {
      this.enumValue = enumValue;
    }

    public static ButtonType valueOf(int enumValue) {
      for (ButtonType type : ButtonType.values()) {
        if (type.getEnumValue() == enumValue) {
          return type;
        }
      }
      return OTHER;
    }
  }
  // LINT.ThenChange(
  // //depot/google3/third_party/java_src/android_libs/setupcompat/main/java/com/google/android/setupcompat/template/ButtonFooterMixin.java,
  // //depot/google3/third_party/java_src/android_libs/setupcompat/main/res/values/attrs.xml)

  /**
   * Builder class for constructing {@code FooterButton} objects.
   *
   * <p>Allows client customize text, click listener and theme for footer button before Button has
   * been created. The {@link ButtonFooterMixin} will inflate a corresponding Button view.
   *
   * <p>Example:
   *
   * <pre class="prettyprint">
   * FooterButton primaryButton =
   *     new FooterButton.Builder(mContext)
   *         .setText(R.string.primary_button_label)
   *         .setListener(primaryButton)
   *         .setButtonType(ButtonType.NEXT)
   *         .setTheme(R.style.SuwGlifButton_Primary)
   *         .build();
   * </pre>
   */
  public static class Builder {
    private final Context context;
    private String text = "";
    private OnClickListener onClickListener = null;
    private ButtonType buttonType = ButtonType.OTHER;
    private int theme = 0;

    public Builder(@NonNull Context context) {
      this.context = context;
    }

    /** Sets the {@code text} of FooterButton. */
    public Builder setText(String text) {
      this.text = text;
      return this;
    }

    /** Sets the {@code text} of FooterButton by resource. */
    public Builder setText(@StringRes int text) {
      this.text = context.getString(text);
      return this;
    }

    /** Sets the {@code listener} of FooterButton. */
    public Builder setListener(@Nullable OnClickListener listener) {
      onClickListener = listener;
      return this;
    }

    /** Sets the {@code buttonType} of FooterButton. */
    public Builder setButtonType(ButtonType buttonType) {
      this.buttonType = buttonType;
      return this;
    }

    /** Sets the {@code theme} for applying FooterButton. */
    public Builder setTheme(@StyleRes int theme) {
      this.theme = theme;
      return this;
    }

    public FooterButton build() {
      return new FooterButton(text, onClickListener, buttonType, theme);
    }
  }
}
