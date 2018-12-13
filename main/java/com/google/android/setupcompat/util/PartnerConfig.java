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

/** Resources that can be customized by partner overlay APK. */
public enum PartnerConfig {

  // Status bar background color or illustration.
  CONFIG_STATUS_BAR_BACKGROUND(PartnerConfigKey.KEY_STATUS_BAR_BACKGROUND, ResourceType.DRAWABLE),

  // The same as "WindowLightStatusBar". If set true, the status bar icons will be drawn such
  // that it is compatible with a light status bar background
  CONFIG_WINDOW_LIGHT_STATUS_BAR(PartnerConfigKey.KEY_WINDOW_LIGHT_STATUS_BAR, ResourceType.BOOL),

  // Navigation bar background color
  CONFIG_NAVIGATION_BAR_BG_COLOR(PartnerConfigKey.KEY_NAVIGATION_BAR_BG_COLOR, ResourceType.COLOR),

  // The same as "windowLightNavigationBar". If set true, the navigation bar icons will be drawn
  // such that it is compatible with a light navigation bar background.
  CONFIG_WINDOW_LIGHT_NAVIGATION_BAR(
      PartnerConfigKey.KEY_WINDOW_LIGHT_NAVIGATION_BAR, ResourceType.BOOL),

  // The font face used in footer buttons. This must be a string reference to a font that is
  // available in the system. Font references (@font or @xml) are not allowed.
  CONFIG_FOOTER_BUTTON_FONT_FAMILY(
      PartnerConfigKey.KEY_FOOTER_BUTTON_FONT_FAMILY, ResourceType.STRING),

  // The icon for "next" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_NEXT(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_NEXT, ResourceType.DRAWABLE),

  // The icon for "skip" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_SKIP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_SKIP, ResourceType.DRAWABLE),

  // The icon for "cancel" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_CANCEL(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_CANCEL, ResourceType.DRAWABLE),

  // The icon for "stop" action. Can be "@null" for no icon.
  CONFIG_FOOTER_BUTTON_ICON_STOP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_ICON_STOP, ResourceType.DRAWABLE),

  // Top padding of the footer buttons
  CONFIG_FOOTER_BUTTON_PADDING_TOP(
      PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_TOP, ResourceType.DIMENSION),

  // Bottom padding of the footer buttons
  CONFIG_FOOTER_BUTTON_PADDING_BOTTOM(
      PartnerConfigKey.KEY_FOOTER_BUTTON_PADDING_BOTTOM, ResourceType.DIMENSION),

  // Corner radius of the footer buttons
  CONFIG_FOOTER_BUTTON_RADIUS(PartnerConfigKey.KEY_FOOTER_BUTTON_RADIUS, ResourceType.DIMENSION),

  // Background color of the primary footer button
  CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR(
      PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_BG_COLOR, ResourceType.COLOR),

  // Text color of the primary footer button
  CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR(
      PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_COLOR, ResourceType.COLOR),

  // Text size of the primary footer button
  CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_SIZE(
      PartnerConfigKey.KEY_FOOTER_PRIMARY_BUTTON_TEXT_SIZE, ResourceType.DIMENSION),

  // Background color of the secondary footer button
  CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR(
      PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_BG_COLOR, ResourceType.COLOR),

  // Text color of the secondary footer button
  CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR(
      PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_COLOR, ResourceType.COLOR),

  // Text size of the secondary footer button
  CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_SIZE(
      PartnerConfigKey.KEY_FOOTER_SECONDARY_BUTTON_TEXT_SIZE, ResourceType.DIMENSION);

  public enum ResourceType {
    BOOL,
    COLOR,
    DRAWABLE,
    STRING,
    DIMENSION;
  }

  private final String resourceName;
  private final ResourceType resourceType;

  public ResourceType getResourceType() {
    return resourceType;
  }

  public String getResourceName() {
    return resourceName;
  }

  PartnerConfig(@PartnerConfigKey String resourceName, ResourceType type) {
    this.resourceName = resourceName;
    this.resourceType = type;
  }
}
