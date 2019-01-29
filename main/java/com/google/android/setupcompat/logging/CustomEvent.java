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

package com.google.android.setupcompat.logging;

import static com.google.android.setupcompat.internal.Validations.assertLengthInRange;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import androidx.annotation.VisibleForTesting;
import com.google.android.setupcompat.internal.ClockProvider;
import com.google.android.setupcompat.internal.Preconditions;
import java.util.Objects;

/**
 * This class represents a interesting event at a particular point in time. The event is identified
 * by {@link MetricKey} along with {@code timestamp}. It can include additional key-value pairs
 * providing more attributes associated with the given event. Only primitive values are supported
 * for now (int, long, double, float, String).
 */
public final class CustomEvent implements Parcelable {

  /** Creates a new instance of {@code CustomEvent}. Null arguments are not allowed. */
  public static CustomEvent create(
      MetricKey metricKey, PersistableBundle bundle, PersistableBundle piiValues) {
    return new CustomEvent(ClockProvider.timeInMillis(), metricKey, bundle, piiValues);
  }

  /** Creates a new instance of {@code CustomEvent}. Null arguments are not allowed. */
  public static CustomEvent create(MetricKey metricKey, PersistableBundle bundle) {
    return create(metricKey, bundle, PersistableBundle.EMPTY);
  }

  public static final Creator<CustomEvent> CREATOR =
      new Creator<CustomEvent>() {
        @Override
        public CustomEvent createFromParcel(Parcel in) {
          return new CustomEvent(
              in.readLong(),
              in.readParcelable(MetricKey.class.getClassLoader()),
              in.readPersistableBundle(),
              in.readPersistableBundle());
        }

        @Override
        public CustomEvent[] newArray(int size) {
          return new CustomEvent[size];
        }
      };

  /** Returns the timestamp of when the event occurred. */
  public long timestampMillis() {
    return timestampMillis;
  }

  /** Returns the identifier of the event. */
  public MetricKey metricKey() {
    return this.metricKey;
  }

  /** Returns the non PII values describing the event. Only primitive values are supported. */
  public PersistableBundle values() {
    return new PersistableBundle(this.persistableBundle);
  }

  /**
   * Returns the PII(Personally identifiable information) values describing the event. These values
   * will not be included in the aggregated logs. Only primitive values are supported.
   */
  public PersistableBundle piiValues() {
    return this.piiValues;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeLong(timestampMillis);
    parcel.writeParcelable(metricKey, i);
    parcel.writePersistableBundle(persistableBundle);
    parcel.writePersistableBundle(piiValues);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CustomEvent)) {
      return false;
    }
    CustomEvent that = (CustomEvent) o;
    return timestampMillis == that.timestampMillis
        && Objects.equals(metricKey, that.metricKey)
        && Objects.equals(persistableBundle, that.persistableBundle)
        && Objects.equals(piiValues, that.piiValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampMillis, metricKey, persistableBundle, piiValues);
  }

  private CustomEvent(
      long timestampMillis,
      MetricKey metricKey,
      PersistableBundle bundle,
      PersistableBundle piiValues) {
    Preconditions.checkArgument(timestampMillis >= 0, "Timestamp cannot be negative.");
    Preconditions.checkNotNull(metricKey, "MetricKey cannot be null.");
    Preconditions.checkNotNull(bundle, "Bundle cannot be null.");
    Preconditions.checkArgument(!bundle.isEmpty(), "Bundle cannot be empty.");
    Preconditions.checkNotNull(piiValues, "piiValues cannot be null.");
    assertPersistableBundleIsValid(bundle);
    this.timestampMillis = timestampMillis;
    this.metricKey = metricKey;
    this.persistableBundle = new PersistableBundle(bundle);
    this.piiValues = new PersistableBundle(piiValues);
  }

  private final long timestampMillis;
  private final MetricKey metricKey;
  private final PersistableBundle persistableBundle;
  private final PersistableBundle piiValues;

  private static void assertPersistableBundleIsValid(PersistableBundle bundle) {
    for (String key : bundle.keySet()) {
      assertLengthInRange(key, "bundle key", MIN_BUNDLE_KEY_LENGTH, MAX_STR_LENGTH);
      Object value = bundle.get(key);
      boolean valid = false;
      for (Class<?> clazz : CUSTOM_EVENT_ALLOWED_DATA_TYPES) {
        if (clazz.isInstance(value)) {
          valid = true;
          break;
        }
      }
      Preconditions.checkArgument(
          valid,
          String.format(
              "Invalid data type for key='%s'. Expected values of type %s, but found [%s].",
              key, CUSTOM_EVENT_ALLOWED_DATA_TYPES, value));

      if (value instanceof String) {
        Preconditions.checkArgument(
            ((String) value).length() <= MAX_STR_LENGTH,
            String.format(
                "Maximum length of string value for key='%s' cannot exceed %s.",
                key, MAX_STR_LENGTH));
      }
    }
  }

  private static final Class<?>[] CUSTOM_EVENT_ALLOWED_DATA_TYPES =
      new Class<?>[] {Integer.class, Long.class, Double.class, String.class, Boolean.class};
  @VisibleForTesting static final int MAX_STR_LENGTH = 50;
  @VisibleForTesting static final int MIN_BUNDLE_KEY_LENGTH = 3;
}
