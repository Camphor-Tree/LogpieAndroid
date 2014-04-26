package com.logpie.android.testapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.connection.ThreadHelper;
import com.logpie.android.datastorage.CentralDataService;
import com.logpie.android.datastorage.DataServiceCaller;
import com.logpie.android.util.LogpieLog;

public class MainActivity extends ActionBarActivity
{
    private static final String TAG = MainActivity.class.getName();
    CentralDataService mDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main Thread",
                String.valueOf(ThreadHelper.isRunningOnMainThread()));

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                Log.d("New Thread",
                        String.valueOf(ThreadHelper.isRunningOnMainThread()));
            }
        });
        thread.start();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        testBindService();
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
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            return rootView;
        }
    }

    private void testBindService()
    {
        LogpieLog.i(TAG, "try DataServiceCaller");
        DataServiceCaller serviceCaller = new DataServiceCaller(this);
        serviceCaller.syncGetDataPlatform();
        // bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

}
