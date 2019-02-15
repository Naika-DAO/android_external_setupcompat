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

package com.google.android.setupcompat.internal;

import android.os.PersistableBundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Contains utility methods related to {@link PersistableBundle}. */
public final class PersistableBundles {

  /**
   * Merges two or more {@link PersistableBundle}. Ensures no conflict of keys occurred during
   * merge.
   *
   * @return Returns a new {@link PersistableBundle} that contains all the data from {@code
   *     firstBundle}, {@code nextBundle} and {@code others}.
   */
  public static PersistableBundle mergeBundles(
      PersistableBundle firstBundle, PersistableBundle nextBundle, PersistableBundle... others) {
    List<PersistableBundle> allBundles = new ArrayList<>();
    allBundles.addAll(Arrays.asList(firstBundle, nextBundle));
    Collections.addAll(allBundles, others);

    PersistableBundle result = new PersistableBundle();
    for (PersistableBundle bundle : allBundles) {
      for (String key : bundle.keySet()) {
        Preconditions.checkArgument(
            !result.containsKey(key),
            String.format("Found duplicate key [%s] while attempting to merge bundles.", key));
      }
      result.putAll(bundle);
    }

    return result;
  }

  private PersistableBundles() {
    throw new AssertionError("Should not be instantiated");
  }
}
