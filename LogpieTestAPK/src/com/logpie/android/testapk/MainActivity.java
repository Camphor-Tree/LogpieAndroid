package com.logpie.android.testapk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.KeyValueStorage;
import com.logpie.android.metric.LogpieMetric;
import com.logpie.android.util.LogpieCallback;
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
        testKeyValueDataStorage();
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

    private void testKeyValueDataStorage()
    {
        KeyValueStorage storage = KeyValueStorage.getInstance(this);
        storage.initialize();
        LogpieLog.d(TAG, storage.getDataMap().size() + "|" + DataLevel.values().length);
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
            if (storage.getDataMap().get(dataLevel) != null)
                LogpieLog.d(TAG, dataLevel.name() + " is not null");
        }
    }

    private void testMetric()
    {
        LogpieMetric metric = new LogpieMetric("MainActivity", "testMetric", this);
        metric.stopTimer();
    }

}
