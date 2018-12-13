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
import android.os.RemoteException;
import androidx.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.internal.ExecutorProvider;
import com.google.android.setupcompat.internal.SetupCompatServiceProvider;
import com.google.android.setupcompat.logging.internal.SetupMetricsLoggingConstants.MetricType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class is responsible for safely publishing log events to SetupWizard. To avoid memory issues
 * due to backed up queues, an upper bound of {@link
 * ExecutorProvider#SETUP_METRICS_LOGGER_MAX_QUEUED} is set on the executor service's queue. Once
 * the upper bound is reached, metrics published after this event are dropped silently.
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
    try {
      ISetupCompatService setupCompatService =
          SetupCompatServiceProvider.get(
              context, waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
      if (setupCompatService != null) {
        setupCompatService.logMetric(metricType, args, Bundle.EMPTY);
      } else {
        Log.w(TAG, "logMetric failed since service reference is null. Are the permissions valid?");
      }
    } catch (InterruptedException | TimeoutException | RemoteException e) {
      Log.e(TAG, String.format("Exception occurred while trying to log metric = [%s]", args), e);
    }
  }

  private DefaultSetupMetricsLogger(Context context) {
    this.context = context;
    this.executorService = ExecutorProvider.setupMetricsLoggerExecutor.get();
    this.waitTimeInMillisForServiceConnection = MAX_WAIT_TIME_FOR_CONNECTION_MS;
  }

  @SuppressWarnings("unused")
  private final Context context;

  private final ExecutorService executorService;
  private final long waitTimeInMillisForServiceConnection;

  public static synchronized DefaultSetupMetricsLogger get(Context context) {
    if (instance == null) {
      instance = new DefaultSetupMetricsLogger(context);
    }

    return instance;
  }

  @VisibleForTesting
  static void setInstanceForTesting(DefaultSetupMetricsLogger testInstance) {
    instance = testInstance;
  }

  private static DefaultSetupMetricsLogger instance;
  private static final long MAX_WAIT_TIME_FOR_CONNECTION_MS = TimeUnit.SECONDS.toMillis(10);
  private static final String TAG = "SetupCompat.SetupMetricsLogger";
}
