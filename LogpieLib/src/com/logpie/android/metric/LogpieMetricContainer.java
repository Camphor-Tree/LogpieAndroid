package com.logpie.android.metric;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.logpie.android.util.BuildInfo;

public final class LogpieMetricContainer
{
    private static Queue<LogpieMetric> sMetricList = new LinkedList<LogpieMetric>();
    private static int sBufferSize = BuildInfo.METRIC_BUFFER_SIZE;
    private static Context sContext;

    public synchronized static void insert(LogpieMetric metric, Context context)
    {
        sContext = context;
        sMetricList.add(metric);
        // when the currently bigger than the buffer size, start the
        // intentService to send.
        if (sMetricList.size() >= sBufferSize)
        {
            Bundle metricBundle = packAllMetrics(sMetricList.size());
            startMetricService(metricBundle);
        }

    }

    // when the buffer size is full, pack all the metric record into one bundle
    private static synchronized Bundle packAllMetrics(int currentSize)
    {
        Bundle metricBundle = new Bundle();
        String key = "key";
        for (int i = 0; i < currentSize; i++)
        {
            LogpieMetric metric = sMetricList.poll();
            String metricString = metric.getComponent() + "&" + metric.getAction() + "&"
                    + metric.getEndTime() + "&" + metric.getLatency();
            metricBundle.putString(key + String.valueOf(i), metricString);
        }
        return metricBundle;
    }

    // start the MetricService
    // MetricService is an IntentService, just post the sending task to it, once
    // it done, it will automatically close.
    private static synchronized void startMetricService(Bundle metricBundle)
    {
        Intent metricServiceIntent = new Intent(sContext, MetricService.class);
        metricServiceIntent.putExtra(MetricService.KEY_METRIC_BUNDLE, metricBundle);
        sContext.startService(metricServiceIntent);
    }
}
