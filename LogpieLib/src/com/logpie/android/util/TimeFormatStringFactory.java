package com.logpie.android.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeFormatStringFactory
{
    private static String TAG = TimeFormatStringFactory.class.getName();

    public static String createCommentTimeFormatString(Date date)
    {
        if (date == null)
        {
            LogpieLog.e(TAG, "The date cannot be null");
            return null;
        }
        SimpleDateFormat dateFormat;
        // TODO check current Locale to change the format
        if (Locale.US == Locale.US)
        {
            // US Time Format
            dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.US);
        }
        else
        {
            // China Time Format
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        }
        return dateFormat.format(date);
    }
}
