package com.logpie.android.ui.helper;

import android.content.Context;

/**
 * This class used to help time related method.
 * 
 * @author yilei
 * 
 */
public class TimeHelper
{
    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_MONTH = 30 * ONE_DAY;
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    /**
     * Get time elapsed String. Like: updated at 20 days ago.
     * 
     * @param lastUpdateTime
     * @param context
     * @return
     */
    public static String getElapsedTimeString(final long lastUpdateTime, final Context context)
    {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastUpdateTime;
        long timeIntoFormat;
        String updateAtValue;
        String updateAtString = LanguageHelper.getString(LanguageHelper.KEY_UPDATED_AT, context);
        if (lastUpdateTime == -1)
        {
            updateAtValue = LanguageHelper.getString(LanguageHelper.KEY_NOT_UPDATED_YET, context);
        }
        else if (timePassed < 0)
        {
            updateAtValue = LanguageHelper.getString(LanguageHelper.KEY_TIME_ERROR, context);
        }
        else if (timePassed < ONE_MINUTE)
        {
            updateAtValue = LanguageHelper.getString(LanguageHelper.KEY_UPDATED_JUST_NOW, context);
        }
        else if (timePassed < ONE_HOUR)
        {
            timeIntoFormat = timePassed / ONE_MINUTE;
            // value = number + timeunit
            String value = timeIntoFormat
                    + LanguageHelper.getString(LanguageHelper.KEY_MINUTE, context);
            updateAtValue = String.format(updateAtString, value);
        }
        else if (timePassed < ONE_DAY)
        {
            timeIntoFormat = timePassed / ONE_HOUR;
            String value = timeIntoFormat
                    + LanguageHelper.getString(LanguageHelper.KEY_HOUR, context);

            updateAtValue = String.format(updateAtString, value);
        }
        else if (timePassed < ONE_MONTH)
        {
            timeIntoFormat = timePassed / ONE_DAY;
            String value = timeIntoFormat
                    + LanguageHelper.getString(LanguageHelper.KEY_DAY, context);
            updateAtValue = String.format(updateAtString, value);
        }
        else if (timePassed < ONE_YEAR)
        {
            timeIntoFormat = timePassed / ONE_MONTH;
            String value = timeIntoFormat
                    + LanguageHelper.getString(LanguageHelper.KEY_MONTH, context);
            ;
            updateAtValue = String.format(updateAtString, value);
        }
        else
        {
            timeIntoFormat = timePassed / ONE_YEAR;
            String value = timeIntoFormat
                    + LanguageHelper.getString(LanguageHelper.KEY_YEAR, context);
            ;
            updateAtValue = String.format(updateAtString, value);
        }
        return updateAtValue;
    }
}
