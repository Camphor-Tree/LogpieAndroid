package com.logpie.android.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.logpie.android.R;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.layout.LogpieRefreshLayout;
import com.logpie.android.ui.layout.LogpieRefreshLayout.PullToRefreshCallback;
import com.logpie.android.util.LogpieLog;

/**
 * Show the title part of the Square Page.
 * 
 * @author yilei
 * 
 */
public class ActivityListFragment extends LogpieBaseFragment
{
    private static final String TAG = ActivityListFragment.class.getName();
    LogpieRefreshLayout refreshableView;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<LogpieActivity> activities;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {

    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        LogpieLog.i(TAG, "Starting handleOnCreateView() in ActivityListFragment");
        View v = inflater.inflate(R.layout.fragment_activity_list, parent, false);
        refreshableView = (LogpieRefreshLayout) v.findViewById(R.id.refreshable_view);
        listView = (ListView) v.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        refreshableView.setPullToRefreshCallback(new PullToRefreshCallback()
        {
            @Override
            public void onRefresh()
            {
                try
                {
                    Thread.sleep(2000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }, 0);
        return v;
    }

    /**
     * Customize a new adapter working with Crime object
     * 
     * @author xujiahang
     * 
     */
    private class ActivityAdapter extends ArrayAdapter<LogpieActivity>
    {
        public ActivityAdapter(ArrayList<LogpieActivity> activities)
        {
            super(getActivity(), 0, activities);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_activity_list_item, null);
            }

            LogpieActivity activity = getItem(position);

            /**
             * set UI of each activity part
             */

            return convertView;
        }
    }
}
