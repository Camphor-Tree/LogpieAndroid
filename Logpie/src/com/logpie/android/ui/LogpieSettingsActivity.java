package com.logpie.android.ui;

import android.support.v4.app.Fragment;

import com.logpie.android.ui.base.LogpieSingleFragmentActivity;
import com.logpie.android.ui.helper.LanguageHelper;

/**
 * This activity is used to show the settings page. (Including system settings
 * and user profile settings)
 * 
 * @author yilei
 * 
 */
public class LogpieSettingsActivity extends LogpieSingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        Fragment detailFragment = new LogpieSettingsFragment();
        return detailFragment;
    }

    @Override
    protected String getTitleBarString()
    {
        return LanguageHelper.getString(LanguageHelper.KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL,
                getBaseContext());
    }

}
