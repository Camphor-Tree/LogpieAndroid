package com.logpie.android.metric;

import android.content.Context;

public class LogpieMetric
{
    private long mStartTime;
    private long mEndTime;
    private long mLatency;
    private String mComponent;
    private String mAction;
    private Context mContext;

    public LogpieMetric(String component, String action, Context context)
    {
        mStartTime = System.currentTimeMillis();
        mComponent = component;
        mAction = action;
        mContext = context;
    }

    public void stopTimer()
    {
        mEndTime = System.currentTimeMillis();
        mLatency = mEndTime - mStartTime;
        sendMetricToContainer(this, mContext);
    }

    private void sendMetricToContainer(LogpieMetric metric, Context context)
    {
        LogpieMetricContainer.insert(this, context);
    }

    public long getLatency()
    {
        return mLatency;
    }

    public long getStartTime()
    {
        return mStartTime;
    }

    public long getEndTime()
    {
        return mEndTime;
    }

    public String getComponent()
    {
        return mComponent;
    }

    public String getAction()
    {
        return mAction;
    }

}
