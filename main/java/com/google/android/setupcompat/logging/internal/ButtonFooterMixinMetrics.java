package com.google.android.setupcompat.logging.internal;

import static java.lang.annotation.RetentionPolicy.SOURCE;

import android.os.PersistableBundle;
import androidx.annotation.StringDef;
import androidx.annotation.VisibleForTesting;
import java.lang.annotation.Retention;

/** Uses to log internal event footer button metric */
public class ButtonFooterMixinMetrics {
  @VisibleForTesting
  public static final String EXTRA_PRIMARY_BUTTON_VISIBILITY = "PrimaryButtonVisibility";

  @VisibleForTesting
  public static final String EXTRA_SECONDARY_BUTTON_VISIBILITY = "SecondaryButtonVisibility";

  @Retention(SOURCE)
  @StringDef({
    FooterButtonVisibility.UNKNOW,
    FooterButtonVisibility.VISIBLE_USING_XML,
    FooterButtonVisibility.VISIBLE,
    FooterButtonVisibility.VISIBLE_USING_XML_TO_INVISIBLE,
    FooterButtonVisibility.VISIBLE_TO_INVISIBLE,
    FooterButtonVisibility.INVISIBLE_TO_VISIBLE,
    FooterButtonVisibility.INVISIBLE,
  })
  @VisibleForTesting
  public @interface FooterButtonVisibility {
    String UNKNOW = "Unknow";
    String VISIBLE_USING_XML = "VisibileUsingXml";
    String VISIBLE = "Visible";
    String VISIBLE_USING_XML_TO_INVISIBLE = "VisibileUsingXml_to_Invisible";
    String VISIBLE_TO_INVISIBLE = "Visible_to_Invisible";
    String INVISIBLE_TO_VISIBLE = "Invisible_to_Visible";
    String INVISIBLE = "Invisible";
  }

  @FooterButtonVisibility String primaryButtonVisibility = FooterButtonVisibility.UNKNOW;

  @FooterButtonVisibility String secondaryButtonVisibility = FooterButtonVisibility.UNKNOW;

  /** Creates a metric object for metric logging */
  public ButtonFooterMixinMetrics() {}

  /** Gets initial state visibility */
  @FooterButtonVisibility
  public String getInitialStateVisibility(boolean isVisible, boolean isUsingXml) {
    @FooterButtonVisibility String visibility;

    if (isVisible) {
      visibility =
          isUsingXml ? FooterButtonVisibility.VISIBLE_USING_XML : FooterButtonVisibility.VISIBLE;
    } else {
      visibility = FooterButtonVisibility.INVISIBLE;
    }

    return visibility;
  }

  /** Saves primary footer button visibility when initial state */
  public void logPrimaryButtonInitialStateVisibility(boolean isVisible, boolean isUsingXml) {
    primaryButtonVisibility =
        primaryButtonVisibility.equals(FooterButtonVisibility.UNKNOW)
            ? getInitialStateVisibility(isVisible, isUsingXml)
            : primaryButtonVisibility;
  }

  /** Saves secondary footer button visibility when initial state */
  public void logSecondaryButtonInitialStateVisibility(boolean isVisible, boolean isUsingXml) {
    secondaryButtonVisibility =
        secondaryButtonVisibility.equals(FooterButtonVisibility.UNKNOW)
            ? getInitialStateVisibility(isVisible, isUsingXml)
            : secondaryButtonVisibility;
  }

  /** Saves footer button visibility when finish state */
  public void updateButtonVisibility(
      boolean isPrimaryButtonVisiable, boolean isSecondaryButtonVisible) {
    primaryButtonVisibility =
        updateButtonVisibilityState(primaryButtonVisibility, isPrimaryButtonVisiable);
    secondaryButtonVisibility =
        updateButtonVisibilityState(secondaryButtonVisibility, isSecondaryButtonVisible);
  }

  @FooterButtonVisibility
  static String updateButtonVisibilityState(
      @FooterButtonVisibility String origionalVisibility, boolean isVisible) {
    if (!origionalVisibility.equals(FooterButtonVisibility.VISIBLE_USING_XML)
        && !origionalVisibility.equals(FooterButtonVisibility.VISIBLE)
        && !origionalVisibility.equals(FooterButtonVisibility.INVISIBLE)) {
      throw new IllegalStateException("Illegal visibility state:" + origionalVisibility);
    }

    if (isVisible && origionalVisibility.equals(FooterButtonVisibility.INVISIBLE)) {
      return FooterButtonVisibility.INVISIBLE_TO_VISIBLE;
    } else if (!isVisible) {
      if (origionalVisibility.equals(FooterButtonVisibility.VISIBLE_USING_XML)) {
        return FooterButtonVisibility.VISIBLE_USING_XML_TO_INVISIBLE;
      } else if (origionalVisibility.equals(FooterButtonVisibility.VISIBLE)) {
        return FooterButtonVisibility.VISIBLE_TO_INVISIBLE;
      }
    }
    return origionalVisibility;
  }

  /** Returns metrics data for logging */
  public PersistableBundle getMetrics() {
    PersistableBundle persistableBundle = new PersistableBundle();
    persistableBundle.putString(EXTRA_PRIMARY_BUTTON_VISIBILITY, primaryButtonVisibility);
    persistableBundle.putString(EXTRA_SECONDARY_BUTTON_VISIBILITY, secondaryButtonVisibility);
    return persistableBundle;
  }
}
