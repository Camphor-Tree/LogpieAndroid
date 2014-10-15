package com.logpie.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.ActivityManager.ActivityCallback;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.ui.layout.LogpieRefreshLayout;
import com.logpie.android.ui.layout.LogpieRefreshLayout.PullToRefreshCallback;
import com.logpie.android.util.LogpieLog;

/**
 * Show the title part of the Square Page.
 * 
 * @author yilei
 * 
 */
public class ActivityListFragment extends ListFragment
{
    private static final String TAG = ActivityListFragment.class.getName();

    private LogpieRefreshLayout mRefreshableView;
    private ListView mListView;
    private ActivityAdapter mArrayAdapter;
    private Button mButton;

    private ActivityManager mActivityManager;
    private ArrayList<LogpieActivity> mActivityList;
    private User user;
    private String mCity;
    private String mCategory;
    private String mSubcategory;
    private String mTabName;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        user = NormalUser.getInstance(getActivity());
        mActivityManager = ActivityManager.getInstance(getActivity());

        ActionBar bar = getActivity().getActionBar();
        mTabName = bar.getSelectedTab().getText().toString();

        new FetchItemsTask().execute(mTabName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        if (inflater == null || parent == null)
        {
            LogpieLog.e(TAG, "Inflator or ViewGroup is null! It is impossible.");
            return null;
        }
        LogpieLog.i(TAG, "Starting handleOnCreateView() in ActivityListFragment");
        View v = inflater.inflate(R.layout.fragment_activity_list, parent, false);

        mRefreshableView = (LogpieRefreshLayout) v.findViewById(R.id.refreshable_view);
        mListView = (ListView) v.findViewById(android.R.id.list);
        TextView empty = (TextView) v.findViewById(R.id.activity_empty_text);
        empty.setText(LanguageHelper.getId(LanguageHelper.KEY_ACTIVITY_EMPTY, getActivity()));
        mArrayAdapter = new ActivityAdapter(mActivityList);

        mButton = (Button) v.findViewById(R.id.picker_button);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (mTabName.equals(LanguageHelper
                        .getString(LanguageHelper.KEY_CITY, getActivity())))
                {
                    DialogFragment dialog = new CityPickerDialog();
                    dialog.setTargetFragment(ActivityListFragment.this,
                            LogpieDialogHelper.REQUEST_CODE_CITY_DIALOG);
                    dialog.show(fm, LogpieDialogHelper.KEY_CITY_PICKER_DIALOG);
                }
                else if (mTabName.equals(LanguageHelper.getString(LanguageHelper.KEY_CATEGORY,
                        getActivity())))
                {
                    DialogFragment dialog = new CategoryPickerDialog();
                    dialog.setTargetFragment(ActivityListFragment.this,
                            LogpieDialogHelper.REQUEST_CODE_CATEGORY_DIALOG);
                    dialog.show(fm, LogpieDialogHelper.KEY_CATEGORY_PICKER_DIALOG);
                }
            }
        });

        setupAdapter(mArrayAdapter);

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

        return v;
    }

    void setupAdapter(ArrayAdapter adapter)
    {
        if (getActivity() == null || mListView == null)
        {
            return;
        }
        if (mActivityList != null)
        {
            LogpieLog.d(TAG, "List is not null!");
            mListView.setAdapter(adapter);
        }
        else
        {
            LogpieLog.d(TAG, "List is null!");
            mListView.setAdapter(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == LogpieDialogHelper.REQUEST_CODE_CITY_DIALOG)
        {
            mCity = data.getStringExtra(CityPickerDialog.KEY_CITY);
            new FetchItemsTask().execute(mTabName);
        }
        if (requestCode == LogpieDialogHelper.REQUEST_CODE_CATEGORY_DIALOG)
        {
            mCategory = data.getStringExtra(CategoryPickerDialog.KEY_CATEGORY_ID);
            mSubcategory = data.getStringExtra(CategoryPickerDialog.KEY_SUBCATEGORY_ID);
            new FetchItemsTask().execute(mTabName);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.square, menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_create_activity:

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Customize a new adapter working with Activity object
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
                v = getActivity().getLayoutInflater().inflate(R.layout.fragment_activity_list_item,
                        null);
            }

            LogpieActivity activity = getItem(position);
            LogpieLog.d(TAG, "Initialize the UI...");
            /**
             * set UI of each activity part
             */
            ImageView avatar = (ImageView) v.findViewById(R.id.activity_list_user_avatar);

            TextView nickname = (TextView) v.findViewById(R.id.activity_list_user_nickname);
            nickname.setText(activity.getUserName());

            TextView createTime = (TextView) v.findViewById(R.id.activity_list_create_time);
            createTime.setText(activity.getCreateTime().getDateTimeString());

            TextView description = (TextView) v.findViewById(R.id.activity_list_description);
            description.setText(activity.getDescription());

            TextView location = (TextView) v.findViewById(R.id.activity_list_location);
            location.setText(activity.getLocation().getAddress());

            TextView city = (TextView) v.findViewById(R.id.activity_list_city);
            city.setText(activity.getLocation().getCity());

            TextView startTime = (TextView) v.findViewById(R.id.activity_list_start_time);
            startTime.setText(activity.getStartTime().getDateTimeString());

            TextView endTime = (TextView) v.findViewById(R.id.activity_list_end_time);
            endTime.setText(activity.getEndTime().getDateTimeString());

            TextView like = (TextView) v.findViewById(R.id.activity_list_count_like);
            like.setText(String.valueOf(activity.getCountLike()));

            TextView dislike = (TextView) v.findViewById(R.id.activity_list_count_dislike);
            dislike.setText(String.valueOf(activity.getCountDislike()));

            TextView comment = (TextView) v.findViewById(R.id.activity_list_comment);

            ImageView ic_location = (ImageView) v.findViewById(R.id.activity_list_ic_location);
            ImageView ic_time = (ImageView) v.findViewById(R.id.activity_list_ic_time);
            ImageView ic_like = (ImageView) v.findViewById(R.id.activity_list_ic_count_like);
            ImageView ic_dislike = (ImageView) v.findViewById(R.id.activity_list_ic_count_dislike);

            ImageView ic_more = (ImageView) v.findViewById(R.id.activity_list_more_functions);

            ImageView ic_comment = (ImageView) v.findViewById(R.id.activity_list_ic_comment);

            return v;
        }
    }

    private class FetchItemsTask extends AsyncTask<String, Void, ArrayList<LogpieActivity>>
    {
        ArrayList<LogpieActivity> mList = new ArrayList<LogpieActivity>();

        @Override
        protected ArrayList<LogpieActivity> doInBackground(String... params)
        {
            String tab = params[0];

            ActivityCallback callback = new ActivityCallback()
            {
                @Override
                public void onSuccess(List<LogpieActivity> activityList)
                {
                    mList.addAll(activityList);
                }

                @Override
                public void onError(Bundle bundle)
                {

                }
            };

            String nearby = LanguageHelper.getString(LanguageHelper.KEY_NEARBY, getActivity());
            String city = LanguageHelper.getString(LanguageHelper.KEY_CITY, getActivity());
            String category = LanguageHelper.getString(LanguageHelper.KEY_CATEGORY, getActivity());

            if (tab.equals(nearby))
            {
                ActivityListFragment.this.mActivityManager.getNearbyActivityList(user, null, null,
                        ActivityManager.MODE_INITIAL,
                        ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                callback));
            }
            else if (tab.equals(city))
            {
                ActivityListFragment.this.mActivityManager.getActivityListByCity(user,
                        ActivityManager.MODE_INITIAL, mCity,
                        ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                callback));
            }
            else if (tab.equals(category))
            {
                ActivityListFragment.this.mActivityManager.getActivityListByCategory(user,
                        ActivityManager.MODE_INITIAL, mCategory, mSubcategory,
                        ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                callback));
            }
            else
            {
                LogpieLog.e(TAG, "Unsupported tab name.");
            }
            return mList;
        }

        @Override
        protected void onPostExecute(ArrayList<LogpieActivity> items)
        {
            LogpieLog.d(TAG, "Starting onPostExecute....");
            if (ActivityListFragment.this.mActivityList == null)
            {
                ActivityListFragment.this.mActivityList = new ArrayList<LogpieActivity>();
            }
            ActivityListFragment.this.mActivityList = items;
            mArrayAdapter = new ActivityAdapter(mActivityList);

            setupAdapter(mArrayAdapter);
            LogpieLog.d(TAG, "Finished Async Task.");
        }

    }

}
