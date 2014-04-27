package com.logpie.android.testapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.datastorage.CentralDataService;
import com.logpie.android.datastorage.DataServiceCaller;
import com.logpie.android.util.LogpieLog;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG = MainActivity.class.getName();
    CentralDataService mDataService;

    DataServiceCaller mServiceCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        testBindService();
    }

    @Override
    protected void onResume()
    {
        LogpieLog.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onStop()
    {
        LogpieLog.d(TAG, "onStop");
        super.onStop();
        mServiceCaller.asyncConnectDataService();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {

        public PlaceholderFragment()
        {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    private void testBindService()
    {
        LogpieLog.i(TAG, "onStart try DataServiceCaller");
        mServiceCaller = new DataServiceCaller(this);
        mServiceCaller.asyncConnectDataService();
    }
}
