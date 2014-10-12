package com.logpie.android.ui;

import android.support.v4.app.Fragment;

import com.logpie.android.ui.base.LogpieSingleFragmentActivity;
import com.logpie.android.ui.helper.LanguageHelper;

/**
 * This activity is used contain the create activity fragment
 * 
 * @author yilei
 * 
 */
public class LogpieCreateActivityActivity extends LogpieSingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        Fragment createActivityFragment = new LogpieCreateActivityFragment();
        return createActivityFragment;
    }

    @Override
    protected String getTitleBarString()
    {
        return LanguageHelper.getString(LanguageHelper.KEY_ACTION_BAR_STRING_CREATE_ACTIVITY,
                getBaseContext());
    }

}
