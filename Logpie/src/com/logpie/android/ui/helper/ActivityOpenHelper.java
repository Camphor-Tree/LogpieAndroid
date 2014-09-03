package com.logpie.android.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.logpie.android.util.LogpieLog;

public class ActivityOpenHelper
{
    private static final String TAG = ActivityOpenHelper.class.getName();

    public static void openActivity(Activity activity, Class<? extends Activity> activityClass)
    {
        openActivityNormally(activity, activityClass);
    }

    /**
     * Start the activity and close the previous one. If doing so, when user
     * click the back button on Android, it won't goto the previous activity
     * anymore.
     * 
     * @param activity
     * @param activityClass
     */
    public static void openActivityAndFinishPreviousActivity(Activity activity,
            Class<? extends Activity> activityClass)
    {
        openActivityNormally(activity, activityClass);
        activity.finish();
    }

    private static void openActivityNormally(Activity activity,
            Class<? extends Activity> activityClass)
    {
        if (activity == null || activityClass == null)
        {
            LogpieLog.e(TAG, "Context or ActivityClass cannot be null");
            throw new IllegalArgumentException("Context or ActivityClass cannot be null");
        }

        Intent startActivityIntent = getOpenActivityIntent(activity, activityClass);
        activity.startActivity(startActivityIntent);
    }

    private static Intent getOpenActivityIntent(Context baseContext,
            Class<? extends Activity> activityClass)
    {
        Intent intent = new Intent(baseContext, activityClass);
        return intent;
    }
}