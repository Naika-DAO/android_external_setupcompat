package com.google.android.setupcompat.logging.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.VisibleForTesting;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.SetupMetricsLogger;

/** Uses to log internal event customization resource list. */
public class PartnerCustomizedResourceListMetric {

  public static void logMetrics(Context context, String screenName, Bundle bundle) {
    PersistableBundle logBundle =
        buildLogBundleFromResourceConfigBundle(context.getPackageName(), bundle);
    if (!logBundle.isEmpty()) {
      SetupMetricsLogger.logCustomEvent(
          context,
          CustomEvent.create(MetricKey.get("PartnerCustomizationResource", screenName), logBundle));
    }
  }

  @VisibleForTesting
  public static PersistableBundle buildLogBundleFromResourceConfigBundle(
      String defaultPackageName, Bundle resourceConfigBundle) {
    PersistableBundle persistableBundle = new PersistableBundle();
    for (String key : resourceConfigBundle.keySet()) {
      Bundle resourceExtra = resourceConfigBundle.getBundle(key);
      if (!resourceExtra.getString("packageName", defaultPackageName).equals(defaultPackageName)) {
        persistableBundle.putBoolean(resourceExtra.getString("resourceName", key), true);
      }
    }

    return persistableBundle;
  }
}
