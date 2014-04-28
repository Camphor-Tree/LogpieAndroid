package com.logpie.android.testapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.datastorage.CentralDataService;
import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.DataServiceCaller;
import com.logpie.android.datastorage.KeyValueStorage;
import com.logpie.android.exception.ThreadException;
import com.logpie.android.util.LogpieCallback;
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
        testKeyValueDataStorage();
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
        try
        {
            mServiceCaller.syncDisconnectDataService();
        } catch (ThreadException e)
        {
            LogpieLog.e(TAG, "cannot bind to save because of thread pool is full");
        }
        super.onStop();
    }

    @Override
    protected void onPause()
    {
        LogpieLog.d(TAG, "onPause");
        try
        {
            mServiceCaller.syncDisconnectDataService();
        } catch (ThreadException e)
        {
            LogpieLog.e(TAG, "cannot bind to save because of thread pool is full");
        }
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

    private void testBindService()
    {
        LogpieLog.i(TAG, "onStart try DataServiceCaller");
        mServiceCaller = new DataServiceCaller(this);
        try
        {
            mServiceCaller.asyncConnectDataService();
        } catch (ThreadException e)
        {
            LogpieLog.e(TAG, "cannot bind to save because of thread pool is full");
        }
    }

    private void testKeyValueDataStorage()
    {
        KeyValueStorage storage = KeyValueStorage.getInstance(this);
        storage.initialize();
        LogpieLog.d(TAG, storage.mDataMap.size() + "|" + DataLevel.values().length);
        Bundle data = new Bundle();
        data.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data.putString("test_key", "test_value");
        storage.insert(data, new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle result)
            {
                LogpieLog.d(TAG, result.toString());
            }

            @Override
            public void onError(Bundle errorMessagge)
            {
                LogpieLog.d(TAG, errorMessagge.toString());
            }
        });
        for (DataLevel dataLevel : DataLevel.values())
        {
            if (storage.mDataMap.get(dataLevel) != null)
                LogpieLog.d(TAG, dataLevel.name() + " is not null");
        }
    }
}
