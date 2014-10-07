package com.logpie.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.logpie.android.R;
import com.logpie.android.logic.AuthManager;
import com.logpie.android.logic.LogpieAccount;
import com.logpie.android.ui.helper.ActivityOpenHelper;
import com.logpie.android.ui.operation.BaseSquareMode;
import com.logpie.android.ui.operation.CategoryMode;
import com.logpie.android.ui.operation.CityMode;
import com.logpie.android.ui.operation.NearbyMode;
import com.logpie.android.util.LogpieLog;

/**
 * This activity shows the main Square page.
 * 
 * @author yilei
 * 
 */
public class SquareActivity extends ActionBarActivity
{
    private final static String TAG = SquareActivity.class.getName();
    private BaseSquareMode mCurrentMode;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /**
         * Call AuthManager to check if it already exists an account on Logpie
         */
        LogpieAccount account = AuthManager.getInstance(getApplicationContext())
                .getCurrentAccount();

        if (account == null)
        {
            ActivityOpenHelper.openActivityAndFinishPreviousActivity(SquareActivity.this,
                    AuthActivity.class);
        }
        else

        {
            LogpieLog.d(TAG, "Getting the action bar...");
            mActionBar = getSupportActionBar();

            // Setup the navigation.
            setUpNavigationTab(mActionBar);
            LogpieLog.d(TAG, "Finished setting navigation bar.");

            // Notice that setContentView() is not used, because we use the root
            // android.R.id.content as the container for each fragment
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        LogpieLog.d(TAG, "Call onCreateOptionsMenu!");
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.squre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle presses on the action bar items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        LogpieLog.d(TAG, "Call onOptionsItemSelected!");
        switch (item.getItemId())
        {
        case R.id.action_create_activity:
        {
            // TODO: open create activity page
            break;
        }
        case R.id.action_settings:
        {
            // Open the settings page
            ActivityOpenHelper.openActivity(this, LogpieSettingsActivity.class);
            break;
        }
        default:
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * Setup the Navigation tab. By default, we set NearbyMode first.
     * 
     * @param actionBar
     */
    private void setUpNavigationTab(final ActionBar actionBar)
    {
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayShowTitleEnabled(false);

        // Setup NearbyMode first.
        mCurrentMode = new NearbyMode(this);
        setupSingleTab(mCurrentMode);
        setupSingleTab(new CategoryMode(this));
        setupSingleTab(new CityMode(this));
    }

    private void setupSingleTab(final BaseSquareMode squareMode)
    {
        squareMode.setupTab();
    }

    public static class LogpieBaseTabListener<T extends Fragment> implements ActionBar.TabListener
    {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /**
         * Constructor used each time a new tab is created.
         * 
         * @param activity
         *            The host Activity, used to instantiate the fragment
         * @param tag
         *            The identifier tag for the fragment
         * @param clz
         *            The fragment's Class, used to instantiate the fragment
         */
        public LogpieBaseTabListener(Activity activity, String tag, Class<T> clz)
        {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(Tab tab, FragmentTransaction ft)
        {
            // Check if the fragment is already initialized
            if (mFragment == null)
            {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            }
            else
            {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft)
        {
            if (mFragment != null)
            {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft)
        {
            // User selected the already selected tab. Usually do nothing.
        }
    }

}
