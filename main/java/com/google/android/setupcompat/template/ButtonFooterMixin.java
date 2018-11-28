/*
 * Copyright (C) 2017 The Android Open Source Project
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.XmlRes;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.setupcompat.R;
import com.google.android.setupcompat.TemplateLayout;
import com.google.android.setupcompat.item.FooterButton;
import com.google.android.setupcompat.item.FooterButtonInflater;
import com.google.android.setupcompat.util.PartnerConfig;
import com.google.android.setupcompat.util.PartnerConfigHelper;

/**
 * A {@link Mixin} for managing buttons. By default, the button bar expects that buttons on the
 * start (left for LTR) are "secondary" borderless buttons, while buttons on the end (right for LTR)
 * are "primary" accent-colored buttons.
 */
public class ButtonFooterMixin implements Mixin {

  private final Context context;

  @Nullable private final ViewStub footerStub;

  private LinearLayout buttonContainer;

  @VisibleForTesting final boolean applyPartnerResources;

  /**
   * Creates a mixin for managing buttons on the footer.
   *
   * @param layout The {@link TemplateLayout} containing this mixin.
   * @param applyPartnerResources determine applies partner resources or not.
   */
  public ButtonFooterMixin(
      TemplateLayout layout,
      @XmlRes int attrPrimaryButton,
      @XmlRes int attrSecondaryButton,
      boolean applyPartnerResources) {
    context = layout.getContext();
    footerStub = (ViewStub) layout.findManagedViewById(R.id.suc_layout_footer);
    this.applyPartnerResources = applyPartnerResources;

    FooterButtonInflater inflater = new FooterButtonInflater(context);

    if (attrPrimaryButton != 0) {
      setPrimaryButton(inflater.inflate(attrPrimaryButton));
    }

    if (attrSecondaryButton != 0) {
      setSecondaryButton(inflater.inflate(attrSecondaryButton));
    }
  }

  // TODO(b/119537553): The button position abnormal due to set button order different.
  private View addSpace() {
    LinearLayout buttonContainer = ensureFooterInflated();
    View space = new View(buttonContainer.getContext());
    space.setLayoutParams(new LayoutParams(0, 0, 1.0f));
    space.setVisibility(View.INVISIBLE);
    buttonContainer.addView(space);
    return space;
  }

  @NonNull
  private LinearLayout ensureFooterInflated() {
    if (buttonContainer == null) {
      if (footerStub == null) {
        throw new IllegalStateException("Footer stub is not found in this template");
      }
      footerStub.setLayoutResource(R.layout.suc_footer_button_bar);
      buttonContainer = (LinearLayout) footerStub.inflate();
    }
    return buttonContainer;
  }

  @SuppressLint("InflateParams")
  private Button createThemedButton(Context context, @StyleRes int theme) {
    // Inflate a single button from XML, which when using support lib, will take advantage of
    // the injected layout inflater and give us AppCompatButton instead.
    LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(context, theme));
    return (Button) inflater.inflate(R.layout.suc_button, null, false);
  }

  /** Sets primary button for footer. */
  public void setPrimaryButton(FooterButton footerButton) {
    addSpace();
    LinearLayout buttonContainer = ensureFooterInflated();

    // Set the default theme if theme is not set, or when running in setup flow.
    if (footerButton.getTheme() == 0 || applyPartnerResources) {
      footerButton.setTheme(R.style.SucPartnerCustomizationButton_Primary);
    }
    // TODO(b/120055778): Make sure customize attributes in theme can be applied during setup flow.
    // If sets background color to full transparent, the button changes to colored borderless ink
    // button style.
    if (applyPartnerResources
        && PartnerConfigHelper.get(context)
                .getColor(context, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR)
            == Color.TRANSPARENT) {
      footerButton.setTheme(R.style.SucPartnerCustomizationButton_Secondary);
    }

    Button button = inflateButton(footerButton, R.id.suc_customization_primary_button);
    buttonContainer.addView(button);
  }

  public Button getPrimaryButton() {
    return buttonContainer.findViewById(R.id.suc_customization_primary_button);
  }

  /** Sets secondary button for footer. */
  public void setSecondaryButton(FooterButton footerButton) {
    LinearLayout buttonContainer = ensureFooterInflated();

    // Set the default theme if theme is not set, or when running in setup flow.
    if (footerButton.getTheme() == 0 || applyPartnerResources) {
      footerButton.setTheme(R.style.SucPartnerCustomizationButton_Secondary);
    }
    // TODO(b/120055778): Make sure customize attributes in theme can be applied during setup flow.
    // If doesn't set background color to full transparent, the button changes to colored bordered
    // ink button style.
    if (applyPartnerResources
        && PartnerConfigHelper.get(context)
                .getColor(context, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR)
            != Color.TRANSPARENT) {
      footerButton.setTheme(R.style.SucPartnerCustomizationButton_Primary);
    }

    Button button = inflateButton(footerButton, R.id.suc_customization_secondary_button);
    buttonContainer.addView(button);
    addSpace();
  }

  public Button getSecondaryButton() {
    return buttonContainer.findViewById(R.id.suc_customization_secondary_button);
  }

  private Button inflateButton(FooterButton footerButton, @IdRes int id) {
    Button button = createThemedButton(context, footerButton.getTheme());
    button.setId(id);
    button.setText(footerButton.getText());
    button.setOnClickListener(footerButton.getListener());
    if (applyPartnerResources) {
      updateButtonAttrsWithPartnerConfig(button, id);
    }
    return button;
  }

  // TODO(b/120055778): Make sure customize attributes in theme can be applied during setup flow.
  private void updateButtonAttrsWithPartnerConfig(Button button, @IdRes int id) {
    updateButtonTextColorWithPartnerConfig(button, id);
    updateButtonTextSizeWithPartnerConfig(button, id);
    updateButtonTypeFaceWithPartnerConfig(button);
    updateButtonBackgroundWithPartnerConfig(button, id);
    updateButtonRadiusWithPartnerConfig(button);
  }

  private void updateButtonTextColorWithPartnerConfig(Button button, @IdRes int id) {
    @ColorInt int color = 0;
    if (id == R.id.suc_customization_primary_button) {
      color =
          PartnerConfigHelper.get(context)
              .getColor(context, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR);
    } else if (id == R.id.suc_customization_secondary_button) {
      color =
          PartnerConfigHelper.get(context)
              .getColor(context, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR);
    }
    button.setTextColor(color);
  }

  private void updateButtonTextSizeWithPartnerConfig(Button button, @IdRes int id) {
    float size = 0.0f;
    if (id == R.id.suc_customization_primary_button) {
      size =
          PartnerConfigHelper.get(context)
              .getDimension(context, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_SIZE);
    } else if (id == R.id.suc_customization_secondary_button) {
      size =
          PartnerConfigHelper.get(context)
              .getDimension(context, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_SIZE);
    }
    button.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
  }

  private void updateButtonTypeFaceWithPartnerConfig(Button button) {
    String fontFamilyName =
        PartnerConfigHelper.get(context)
            .getString(context, PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY);
    Typeface font = Typeface.create(fontFamilyName, Typeface.NORMAL);
    if (font != null) {
      button.setTypeface(font);
    }
  }

  private void updateButtonBackgroundWithPartnerConfig(Button button, @IdRes int id) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
      if (id == R.id.suc_customization_primary_button) {
        int color =
            PartnerConfigHelper.get(context)
                .getColor(context, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR);
        if (color != Color.TRANSPARENT) {
          button.getBackground().setColorFilter(color, Mode.MULTIPLY);
        }
      } else if (id == R.id.suc_customization_secondary_button) {
        int color =
            PartnerConfigHelper.get(context)
                .getColor(context, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR);
        if (color != Color.TRANSPARENT) {
          button.getBackground().setColorFilter(color, Mode.MULTIPLY);
        }
      }
    }
  }

  private void updateButtonRadiusWithPartnerConfig(Button button) {
    if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
      float defaultRadius =
          context.getResources().getDimension(R.dimen.suc_customization_button_corner_radius);
      float radius =
          PartnerConfigHelper.get(context)
              .getDimension(context, PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS, defaultRadius);
      GradientDrawable gradientDrawable = getGradientDrawable(button);
      if (gradientDrawable != null) {
        gradientDrawable.setCornerRadius(radius);
      }
    }
  }

  GradientDrawable getGradientDrawable(Button button) {
    Drawable drawable = button.getBackground();
    if (drawable instanceof InsetDrawable) {
      LayerDrawable layerDrawable = (LayerDrawable) ((InsetDrawable) drawable).getDrawable();
      return (GradientDrawable) layerDrawable.getDrawable(0);
    } else if (drawable instanceof RippleDrawable) {
      InsetDrawable insetDrawable = (InsetDrawable) ((RippleDrawable) drawable).getDrawable(0);
      return (GradientDrawable) insetDrawable.getDrawable();
    }
    return null;
  }

  protected View inflateFooter(@LayoutRes int footer) {
    footerStub.setLayoutResource(footer);
    return footerStub.inflate();
  }
}
