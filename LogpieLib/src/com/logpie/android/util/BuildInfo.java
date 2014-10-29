package com.logpie.android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

public class BuildInfo
{

    public static String APPLICATION = "Logpie Android";

    public static String OS_VERSION = String.valueOf(Build.VERSION.SDK_INT);
    public static String MANUFACTURER = Build.MANUFACTURER;

    public static String OS_TYPE = "android";
    public static String PLATFORM = "java";
    public static String ENVIRONMENT = "debug";

    // metric buffer size
    public static int METRIC_BUFFER_SIZE = ENVIRONMENT.equals("debug") ? 1 : 5;

    private static String KEY_META_DATA_VERSION = "com.logpie.version";
    private static String KEY_META_DATA_ENVIRONMENT = "com.logpie.environment";

    private static final String TAG = BuildInfo.class.getName();

    private static String sVersionString = null;

    // TODO: we should automate the release process add the version number into
    // one file, each time do a release, it will auto increase.
    public synchronized static String getLogpieVersion(Context context)
    {
        if (sVersionString == null)
        {

            String logpieVersion = null;
            String logpieEnvironment = null;
            try
            {
                ApplicationInfo app = context.getPackageManager().getApplicationInfo(
                        context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = app.metaData;
                // currVersion = Integer.valueOf(bundle.getString("dbVersion"));
                logpieVersion = bundle.getString(KEY_META_DATA_VERSION);
                logpieEnvironment = bundle.getString(KEY_META_DATA_ENVIRONMENT);
            } catch (NameNotFoundException e)
            {
                LogpieLog.e(TAG, "Meta data not found", e);
            } catch (NullPointerException e)
            {
                LogpieLog.e(TAG, "NullPointerException", e);
            }

            if (!TextUtils.isEmpty(logpieVersion) && !TextUtils.isEmpty(logpieEnvironment))
            {
                sVersionString = logpieVersion + " " + logpieEnvironment;
            }
            else
            {
                sVersionString = "Unknown";
            }
        }

        return sVersionString;
    }
}
