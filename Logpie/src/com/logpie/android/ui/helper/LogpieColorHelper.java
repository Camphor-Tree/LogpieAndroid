package com.logpie.android.ui.helper;

import com.logpie.android.R;

public class LogpieColorHelper
{
    private static final String TAG = LogpieColorHelper.class.getName();

    public static int getColorByCategoryTag(String categoryId)
    {
        switch (categoryId)
        {
        case "1":
            return R.color.LogpieGreen;
        case "2":
            return R.color.LogpieRed;
        case "3":
            return R.color.LogpiePurple;
        case "4":
            return R.color.LogpieBlue;
        case "5":
            return R.color.LogpieYellow;
        case "6":
            return R.color.LogpieGray;
        default:
            return R.color.LogpieGray;
        }
    }

}
