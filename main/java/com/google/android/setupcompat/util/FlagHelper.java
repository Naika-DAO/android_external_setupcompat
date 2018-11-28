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

import android.content.Context;
import android.content.res.TypedArray;
import com.google.android.setupcompat.R;

/** Helper utilities to get flags that enable/disable features of this library. */
public final class FlagHelper {

  private FlagHelper() {}

  /**
   * Returns whether customization styles should be loaded from the partner overlay APK. Default
   * value is currently false, but will be changed to true once the feature is ready for general
   * consumption. Outside of setup wizard flow, this flag should default to false.
   */
  public static final boolean isPartnerStyleEnabled(Context context) {
    TypedArray typedArray = context.obtainStyledAttributes(new int[] {R.attr.sucEnablePartnerStyle});
    boolean enablePartnerStyle = typedArray.getBoolean(0, false);
    typedArray.recycle();
    return enablePartnerStyle;
  }
}
