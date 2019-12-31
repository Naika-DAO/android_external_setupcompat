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

package com.google.android.setupcompat.util;

import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Resource names that can be customized by partner overlay APK. */
@Retention(RetentionPolicy.SOURCE)
@StringDef({
  PartnerConfigKey.KEY_STATUS_BAR_BACKGROUND,
  PartnerConfigKey.KEY_WINDOW_LIGHT_STATUS_BAR,
  PartnerConfigKey.KEY_NAVIGATION_BAR_BG_COLOR,
  PartnerConfigKey.KEY_WINDOW_LIGHT_NAVIGATION_BAR,
  PartnerConfigKey.KEY_FOOTER_BUTTON_FONT_FAMILY,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_ADD_ANOTHER,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CANCEL,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CLEAR,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_DONE,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_NEXT,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_OPT_IN,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_SKIP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_STOP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_TOP,
  PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_BOTTOM,
  PartnerConfigKey.KEY_FOOTER_BUTTON_RADIUS,
  PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR,
  PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_SIZE,
  PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR,
  PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR,
  PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_SIZE,
})
// TODO(121371322): can be removed and always reference PartnerConfig.getResourceName()?
public @interface PartnerConfigKey {
  // Status bar background color or illustration.
  String KEY_STATUS_BAR_BACKGROUND = "setup_compat_status_bar_background";

  // The same as "WindowLightStatusBar". If set true, the status bar icons will be drawn such
  // that it is compatible with a light status bar background
  String KEY_WINDOW_LIGHT_STATUS_BAR = "setup_compat_window_light_status_bar";

  // Navigation bar background color
  String KEY_NAVIGATION_BAR_BG_COLOR = "setup_compat_navigation_bar_bg_color";

  // The same as "windowLightNavigationBar". If set true, the navigation bar icons will be drawn
  // such that it is compatible with a light navigation bar background.
  String KEY_WINDOW_LIGHT_NAVIGATION_BAR = "setup_compat_window_light_navigation_bar";

  // The font face used in footer buttons. This must be a string reference to a font that is
  // available in the system. Font references (@font or @xml) are not allowed.
  String KEY_FOOTER_BUTTON_FONT_FAMILY = "setup_compat_footer_button_font_family";

  // The icon for "add another" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_ADD_ANOTHER = "setup_compat_footer_button_icon_add_another";

  // The icon for "cancel" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_CANCEL = "setup_compat_footer_button_icon_cancel";

  // The icon for "clear" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_CLEAR = "setup_compat_footer_button_icon_clear";

  // The icon for "done" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_DONE = "setup_compat_footer_button_icon_done";

  // The icon for "next" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_NEXT = "setup_compat_footer_button_icon_next";

  // The icon for "opt-in" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_OPT_IN = "setup_compat_footer_button_icon_opt_in";

  // The icon for "skip" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_SKIP = "setup_compat_footer_button_icon_skip";

  // The icon for "stop" action. Can be "@null" for no icon.
  String KEY_FOOTER_BUTTON_ICON_STOP = "setup_compat_footer_button_icon_stop";

  // Top padding of the footer buttons
  String KEY_FOOTER_BUTTON_PADDING_TOP = "setup_compat_footer_button_padding_top";

  // Bottom padding of the footer buttons
  String KEY_FOOTER_BUTTON_PADDING_BOTTOM = "setup_compat_footer_button_padding_bottom";

  // Corner radius of the footer buttons
  String KEY_FOOTER_BUTTON_RADIUS = "setup_compat_footer_button_radius";

  // Background color of the primary footer button
  String KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR = "setup_compat_footer_primary_button_bg_color";

  // Text color of the primary footer button
  String KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR = "setup_compat_footer_primary_button_text_color";

  // Text size of the primary footer button
  String KEY_FOOTER_PRIMARY_BUTTON_TEXT_SIZE = "setup_compat_footer_primary_button_text_size";

  // Background color of the secondary footer button
  String KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR = "setup_compat_footer_secondary_button_bg_color";

  // Text color of the secondary footer button
  String KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR = "setup_compat_footer_secondary_button_text_color";

  // Text size of the secondary footer button
  String KEY_FOOTER_SECONDARY_BUTTON_TEXT_SIZE = "setup_compat_footer_secondary_button_text_size";
}
