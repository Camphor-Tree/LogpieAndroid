package com.logpie.android.util;

import android.os.Build;

public class BuildInfo
{
    public static String APPLICATION = "Logpie Android";
    // TODO: we should automate the release process
    // add the version number into one file, each time do a release, it will
    // auto increase.
    private static String BIG_VERSION = "1";
    private static String MEDIUM_VERSION = "0";
    private static String SMALL_VERSION = "0";
    public static String VERSION = BIG_VERSION + "." + MEDIUM_VERSION + "." + SMALL_VERSION;

    public static String OS_VERSION = String.valueOf(Build.VERSION.SDK_INT);
    public static String MANUFACTURER = Build.MANUFACTURER;

    public static String OS_TYPE = "android";
    public static String PLATFORM = "java";
    public static String ENVIRONMENT = "debug";

    // metric buffer size
    public static int METRIC_BUFFER_SIZE = ENVIRONMENT.equals("debug") ? 1 : 5;
}
