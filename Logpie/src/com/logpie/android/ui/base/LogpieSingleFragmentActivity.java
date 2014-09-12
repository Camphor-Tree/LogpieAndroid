package com.logpie.android.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;

import com.logpie.android.util.LogpieLog;

public abstract class LogpieSingleFragmentActivity extends ActionBarActivity
{
    private static final String TAG = LogpieSingleFragmentActivity.class.getName();

    private static final String DEFAULT_TITLE_BAR = "Logpie";

    /**
     * Subclass should implement this abstract function to create it own
     * fragment
     * 
     * @return
     */
    protected abstract Fragment createFragment();

    protected abstract String getTitleBarString();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        handleTitleBarString();
        // setContentView(R.layout.activity_auth);

        FragmentManager fm = getSupportFragmentManager();
        // Fragment fragment = fm.findFragmentById(R.id.container);

        // if (fragment == null)
        // {
        // fragment = createFragment();
        // fm.beginTransaction().add(R.id.container, fragment).commit();
        // }
    }

    private void handleTitleBarString()
    {
        String titleBarString = getTitleBarString();
        if (TextUtils.isEmpty(titleBarString))
        {
            LogpieLog
                    .i(TAG,
                            "The titile string is null or empty, setting the defuault title bar string: Logpie");
            this.setTitle(DEFAULT_TITLE_BAR);
            return;
        }
        this.setTitle(titleBarString);
    }
}
