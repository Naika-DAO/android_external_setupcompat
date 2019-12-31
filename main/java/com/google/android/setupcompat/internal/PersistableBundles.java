package com.google.android.setupcompat.internal;

import android.os.PersistableBundle;
import com.google.common.base.Preconditions;
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
            "Found duplicate key [%s] while attempting to merge bundles.",
            key);
      }
      result.putAll(bundle);
    }

    return result;
  }

  private PersistableBundles() {
    throw new AssertionError("Should not be instantiated");
  }
}
