package com.logpie.android.testapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.metric.LogpieMetric;
import com.logpie.android.util.LogpieLog;

public class MainActivity extends FragmentActivity
{
    private static final String TAG = MainActivity.class.getName();

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
    }

    @Override
    protected void onResume()
    {
        LogpieLog.d(TAG, "onResume");
        testMetric();
        super.onResume();
    }

    @Override
    protected void onStop()
    {
        LogpieLog.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        LogpieLog.d(TAG, "onPause");

        super.onStop();
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

    private void testMetric()
    {
        LogpieMetric metric = new LogpieMetric("MainActivity", "testMetric", this);
        metric.stopTimer();
    }

}
