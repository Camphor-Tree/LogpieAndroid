package com.logpie.android.ui;

import android.support.v4.app.Fragment;

import com.logpie.android.ui.base.LogpieSingleFragmentActivity;
import com.logpie.android.ui.helper.LanguageHelper;

public class LogpieActivityDetailActivity extends LogpieSingleFragmentActivity
{
    @Override
    protected Fragment createFragment()
    {
        return new LogpieActivityDetailFragment();
    }

    @Override
    protected String getTitleBarString()
    {
        return LanguageHelper.getString(LanguageHelper.KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL,
                getBaseContext());
    }

}
