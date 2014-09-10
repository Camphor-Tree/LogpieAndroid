package com.logpie.android.ui;

import android.support.v4.app.Fragment;

import com.logpie.android.ui.base.LogpieSingleFragmentActivity;

public class AuthActivity extends LogpieSingleFragmentActivity
{
    private static final String TAG = AuthActivity.class.getName();

    @Override
    protected Fragment createFragment()
    {
        return new LoginFragment();
    }

}
