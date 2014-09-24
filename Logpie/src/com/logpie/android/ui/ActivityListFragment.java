package com.logpie.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.ActivityManager.ActivityCallback;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
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

    private LogpieRefreshLayout mRefreshableView;
    private ListView mListView;
    private ArrayAdapter<LogpieActivity> mArrayAdapter;

    private ActivityManager mActivityManager;
    private ArrayList<LogpieActivity> mActivityList;
    private User user;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        user = NormalUser.getInstance(getActivity());
        mActivityManager = ActivityManager.getInstance(getActivity());
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        LogpieLog.i(TAG, "Starting handleOnCreateView() in ActivityListFragment");
        View v = inflater.inflate(R.layout.fragment_activity_list, parent, false);
        mRefreshableView = (LogpieRefreshLayout) v.findViewById(R.id.refreshable_view);
        mListView = (ListView) v.findViewById(R.id.list_view);

        ActivityCallback callback = new ActivityCallback()
        {

            @Override
            public void onSuccess(List<LogpieActivity> activityList)
            {
                onSucceed(activityList);
            }

            @Override
            public void onError(Bundle bundle)
            {
                onFailed(bundle);
            }
        };

        mActivityManager.getActivityListByCity(user, ActivityManager.MODE_INITIAL, null,
                mActivityManager.new ActivityCallbackAdapter(callback));
        return new View(getActivity());
    }

    private void onSucceed(List<LogpieActivity> activityList)
    {
        mActivityList = (ArrayList<LogpieActivity>) activityList;
        mArrayAdapter = new ArrayAdapter<LogpieActivity>(getActivity(),
                android.R.layout.simple_list_item_1, mActivityList);
        mListView.setAdapter(mArrayAdapter);

        mRefreshableView.setPullToRefreshCallback(new PullToRefreshCallback()
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
    }

    private void onFailed(Bundle bundle)
    {
        mArrayAdapter = new ArrayAdapter<LogpieActivity>(getActivity(),
                android.R.layout.simple_list_item_1, mActivityList);
        mListView.setAdapter(mArrayAdapter);

        mRefreshableView.setPullToRefreshCallback(new PullToRefreshCallback()
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
        LogpieLog.e(TAG, "Failed to get latest activity list.");
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
        public View getView(int position, View v, ViewGroup parent)
        {
            if (v == null)
            {
                v = getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_activity_list_item, null);
            }

            LogpieActivity activity = getItem(position);
            /**
             * set UI of each activity part
             */
            ImageView avatar = (ImageView) v.findViewById(R.id.activity_list_user_avatar);

            TextView nickname = (TextView) v
                    .findViewById(R.id.activity_list_user_nickname);
            nickname.setText(activity.getmUserName());

            TextView createTime = (TextView) v
                    .findViewById(R.id.activity_list_create_time);
            createTime.setText(LogpieActivity.getFormatDate(activity.getmCreateTime()));

            TextView description = (TextView) v
                    .findViewById(R.id.activity_list_description);
            description.setText(activity.getmDescription());

            TextView location = (TextView) v.findViewById(R.id.activity_list_location);
            location.setText(activity.getmLocation().getAddress());

            TextView city = (TextView) v.findViewById(R.id.activity_list_city);
            city.setText(activity.getmLocation().getCity());

            TextView startTime = (TextView) v.findViewById(R.id.activity_list_start_time);
            startTime.setText(LogpieActivity.getFormatDate(activity.getmStartTime()));

            TextView endTime = (TextView) v.findViewById(R.id.activity_list_end_time);
            endTime.setText(LogpieActivity.getFormatDate(activity.getmEndTime()));

            TextView like = (TextView) v.findViewById(R.id.activity_list_count_like);
            like.setText(activity.getmCountLike());

            TextView dislike = (TextView) v
                    .findViewById(R.id.activity_list_count_dislike);
            dislike.setText(activity.getmCountDislike());

            ImageView ic_location = (ImageView) v
                    .findViewById(R.id.activity_list_ic_location);
            ImageView ic_time = (ImageView) v.findViewById(R.id.activity_list_ic_time);
            ImageView ic_like = (ImageView) v
                    .findViewById(R.id.activity_list_ic_count_like);
            ImageView ic_dislike = (ImageView) v
                    .findViewById(R.id.activity_list_ic_count_dislike);

            ImageView ic_more = (ImageView) v
                    .findViewById(R.id.activity_list_more_functions);

            ImageView ic_comment = (ImageView) v
                    .findViewById(R.id.activity_list_ic_comment);

            return v;
        }
    }
}
