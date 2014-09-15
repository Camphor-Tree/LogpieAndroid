package com.logpie.android.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.ui.base.LogpieSingleFragmentActivity;
import com.logpie.android.ui.helper.LanguageHelper;

/**
 * This activity used to show the activity detail page
 * 
 * Note: to open this activity, please pass in a valid LogpieActivity including
 * valid parameters inside the LogpieActivity.
 * 
 * Please use ActivityOpenHelper.openActivityAndPassParameter(), or
 * intent.put(String, Parcelable); LogpieActivity implements the Parcelable
 * interface, so it is able to pass in the intent as a Parcelable Object.
 * 
 * @author yilei
 * 
 */
public class LogpieActivityDetailActivity extends LogpieSingleFragmentActivity
{
    public static final String KEY_DETAIL_ACTIVITY = LogpieActivityDetailFragment.KEY_DETAIL_ACTIVITY;

    @Override
    protected Fragment createFragment()
    {
        Fragment detailFragment = new LogpieActivityDetailFragment();
        // Get the LogpieActivity from the previous activity
        LogpieActivity detailActivity = getLogpieActivityFromOriginActivity();
        // Pass the LogpieActivity to the LogpieActivityDetailFragment
        setLogpieActivityInArugments(detailFragment, detailActivity);
        return detailFragment;
    }

    @Override
    protected String getTitleBarString()
    {
        return LanguageHelper.getString(LanguageHelper.KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL,
                getBaseContext());
    }

    private LogpieActivity getLogpieActivityFromOriginActivity()
    {
        Parcelable pacelableActivity = this.getIntent().getParcelableExtra(KEY_DETAIL_ACTIVITY);
        if (pacelableActivity == null)
        {
            throw new IllegalArgumentException(
                    "Cannot open this detail page without a LogpieActivity");
        }
        if (pacelableActivity instanceof LogpieActivity)
        {
            return (LogpieActivity) pacelableActivity;
        }
        else
        {
            throw new IllegalArgumentException("The activty must be an instance of LogpieActivity");
        }
    }

    /**
     * Set logpieActivity instance into the argumetns to pass to the fragment.
     * 
     * @param detailFragment
     */
    private void setLogpieActivityInArugments(Fragment detailFragment, LogpieActivity logpieActivity)
    {
        Bundle activityBundle = new Bundle();
        activityBundle.putParcelable(KEY_DETAIL_ACTIVITY, logpieActivity);
        detailFragment.setArguments(activityBundle);
    }
}
