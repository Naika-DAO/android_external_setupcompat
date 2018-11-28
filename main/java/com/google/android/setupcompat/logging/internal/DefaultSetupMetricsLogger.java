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

package com.google.android.setupcompat.logging.internal;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.setupcompat.logging.internal.SetupMetricsLoggingConstants.MetricType;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for safely publishing log events to SetupWizard. To avoid memory issues
 * due to backed up queues, an upper bound of {@link #MAX_QUEUED} is set on the executor service's
 * queue. Once the upper bound is reached, metrics published after this event are dropped silently.
 *
 * <p>NOTE: This class is not meant to be used directly. Please use {@link
 * com.google.android.setupcompat.logging.SetupMetricsLogger} for publishing metric events.
 */
public class DefaultSetupMetricsLogger {

  @SuppressWarnings("FutureReturnValueIgnored")
  public void logEventSafely(@MetricType int metricType, Bundle args) {
    try {
      executorService.submit(() -> invokeService(metricType, args));
    } catch (RejectedExecutionException e) {
      Log.e(TAG, String.format("Metric of type %d dropped since queue is full.", metricType), e);
    }
  }

  private void invokeService(@MetricType int metricType, @SuppressWarnings("unused") Bundle args) {
    // TODO(b/117984473): Invoke service.
    Log.w(
        TAG,
        String.format("invokeService not implemented yet. No action taken for: %d", metricType));
  }

  @VisibleForTesting
  DefaultSetupMetricsLogger(Context context, int maxSize) {
    this(context, createBoundedExecutor(maxSize));
  }

  private DefaultSetupMetricsLogger(Context context) {
    this(context, MAX_QUEUED);
  }

  private DefaultSetupMetricsLogger(Context context, ExecutorService executorService) {
    this.context = context;
    this.executorService = executorService;
  }

  @SuppressWarnings("unused")
  private final Context context;

  private final ExecutorService executorService;

  private static ExecutorService createBoundedExecutor(int maxSize) {
    return new ThreadPoolExecutor(
        /* corePoolSize= */ 1,
        /* maximumPoolSize= */ 1,
        /* keepAliveTime= */ 0,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(maxSize),
        runnable -> new Thread(runnable, "DefaultSetupMetricsLogger"));
  }

  public static synchronized DefaultSetupMetricsLogger get(Context context) {
    if (instance == null) {
      instance = new DefaultSetupMetricsLogger(context);
    }

    return instance;
  }

  private static DefaultSetupMetricsLogger instance;
  private static final int MAX_QUEUED = 50;
  private static final String TAG = "SetupCompat.SetupMetricsLogger";
}
