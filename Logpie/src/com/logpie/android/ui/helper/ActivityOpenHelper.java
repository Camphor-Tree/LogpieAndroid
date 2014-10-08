package com.logpie.android.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.logpie.android.util.LogpieLog;

public class ActivityOpenHelper
{
    private static final String TAG = ActivityOpenHelper.class.getName();

    public static void openActivityAndPassParameter(final Activity activity,
            final Class<? extends Activity> activityClass, final String key,
            final Parcelable parameter)
    {
        openActivityNormally(activity, activityClass, key, parameter);
    }

    public static void openActivityAndPassParameterAndFinishPreviousActivity(
            final Activity activity, final Class<? extends Activity> activityClass,
            final String key, final Parcelable parameter)
    {
        openActivityNormally(activity, activityClass, key, parameter);
        activity.finish();
    }

    public static void openActivity(final Activity activity,
            final Class<? extends Activity> activityClass)
    {
        openActivityNormally(activity, activityClass, null, null);
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
        openActivityNormally(activity, activityClass, null, null);
        activity.finish();
    }

    public static void restartApplication(Context context)
    {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    private static void openActivityNormally(final Activity activity,
            final Class<? extends Activity> activityClass, final String key,
            final Parcelable parameter)
    {
        if (activity == null || activityClass == null)
        {
            LogpieLog.e(TAG, "Context or ActivityClass cannot be null");
            throw new IllegalArgumentException("Context or ActivityClass cannot be null");
        }

        Intent startActivityIntent = getOpenActivityIntent(activity, activityClass);
        if (key != null)
        {
            startActivityIntent.putExtra(key, parameter);
        }
        activity.startActivity(startActivityIntent);
    }

    private static Intent getOpenActivityIntent(Context baseContext,
            Class<? extends Activity> activityClass)
    {
        Intent intent = new Intent(baseContext, activityClass);
        return intent;
    }
}
