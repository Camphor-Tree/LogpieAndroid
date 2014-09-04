package com.logpie.android.ui.helper;

import com.logpie.android.R;

public class LanguageHelper
{
    private static final boolean sInChinese = true;

    public static int getNearybyModeTagString()
    {
        return switchLanguage(R.string.navigation_nearby_mode_cn,
                R.string.navigation_nearby_mode_us);
    }

    public static int getCategoryModeTabString()
    {
        return switchLanguage(R.string.navigation_category_mode_cn,
                R.string.navigation_category_mode_us);
    }

    public static int getCityModeTabString()
    {
        return switchLanguage(R.string.navigation_city_mode_cn, R.string.navigation_city_mode_us);
    }

    /**
     * Just return resource id
     * 
     * @return
     */
    private static int switchLanguage(int chineseVersion, int englishVersion)
    {
        if (sInChinese)
        {
            return chineseVersion;
        }
        else
        {
            return englishVersion;
        }
    }

}
