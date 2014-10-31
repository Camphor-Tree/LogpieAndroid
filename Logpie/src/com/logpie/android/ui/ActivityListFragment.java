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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.ActivityManager.ActivityCallback;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
import com.logpie.android.ui.helper.ActivityOpenHelper;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieColorHelper;
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

    private Activity mActivity;
    private LogpieRefreshLayout mRefreshableView;
    private View mLoadMoreView;
    private ListView mListView;
    private ActivityAdapter mArrayAdapter;
    private Button mButton;
    private ProgressBar mProgressBar;

    private ActivityManager mActivityManager;
    private ArrayList<LogpieActivity> mActivityList;
    private User user;
    private String mCity;
    private String mCategory;
    private String mSubcategory;
    private String mTabName;
    private int mLastVisibleIndex;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Set option menu visible
        setHasOptionsMenu(true);
        mActivity = getActivity();
        user = NormalUser.getInstance(getActivity());
        mActivityManager = ActivityManager.getInstance(mActivity);

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

        mLoadMoreView = inflater.inflate(R.layout.layout_load_more_view, null);
        mProgressBar = (ProgressBar) mLoadMoreView.findViewById(R.id.progressBar1);

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
                new FetchItemsTask().execute(new String[] { mTabName,
                        String.valueOf(ActivityManager.MODE_REFRESH) });
            }
        }, mTabName);

        mListView.setOnScrollListener(new OnScrollListener()
        {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (mListView.getFooterViewsCount() == 0)
                {
                    mListView.addFooterView(mLoadMoreView);
                }

                // Check if it is at the bottom of the whole page
                // Index is starting from 0; Count is starting from 1;
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                        && mLastVisibleIndex == mListView.getAdapter().getCount() - 1)
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    // The last position is progress bar, so the last activity
                    // should be lastPosition - 1;
                    int pos = mListView.getLastVisiblePosition() - 1;
                    LogpieActivity lastActivity = (LogpieActivity) mListView.getAdapter().getItem(
                            pos);
                    String lastActivityId = lastActivity.getActivityID();
                    new FetchItemsTask().execute(new String[] { mTabName,
                            String.valueOf(ActivityManager.MODE_LOAD_MORE), lastActivityId });
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount)
            {

                if (mProgressBar.getVisibility() == View.GONE)
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mLastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

            }
        });

        return v;
    }

    void setupAdapter(ArrayAdapter<LogpieActivity> adapter)
    {
        if (getActivity() == null || mListView == null)
        {
            return;
        }
        if (mActivityList != null)
        {
            LogpieLog.d(TAG, "List is not null!");
            mListView.setAdapter(adapter);
            mListView.removeFooterView(mLoadMoreView);
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
            mCity = data.getStringExtra(CityPickerDialog.KEY_CITY_ID);
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
    public void onListItemClick(final ListView listView, View view, int position, long id)
    {
        if (mArrayAdapter != null)
        {
            LogpieActivity activity = ((ActivityAdapter) mArrayAdapter).getItem(position);
            if (activity != null)
            {
                ActivityOpenHelper.openActivityAndPassParameter(mActivity,
                        LogpieActivityDetailActivity.class,
                        LogpieActivityDetailActivity.KEY_DETAIL_ACTIVITY, activity);
            }
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
                        parent, false);
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

            TextView category = (TextView) v.findViewById(R.id.activity_list_category);
            category.setText(activity.getCategoryString());
            int color = LogpieColorHelper.getColorByCategoryTag(activity.getCategoryId());
            category.setBackgroundResource(color);
            if (activity.getSubCategoryString() != null)
            {
                TextView subcategory = (TextView) v.findViewById(R.id.activity_list_subcategory);
                subcategory.setText(activity.getSubCategoryString());
                subcategory.setBackgroundResource(color);
            }

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

            ImageView ic_comment = (ImageView) v.findViewById(R.id.activity_list_ic_comment);

            return v;
        }
    }

    private class FetchItemsTask extends AsyncTask<String, Void, ArrayList<LogpieActivity>>
    {
        private String mMode;
        private ArrayList<LogpieActivity> mList = new ArrayList<LogpieActivity>();

        @Override
        protected ArrayList<LogpieActivity> doInBackground(String... params)
        {
            String tab = params[0];

            long lastActivityId = 0;

            if (params.length > 1)
            {
                mMode = params[1];
                if (mMode.equals(String.valueOf(ActivityManager.MODE_LOAD_MORE)))
                {
                    if (params.length == 3)
                    {
                        lastActivityId = Long.valueOf(params[2]);
                    }
                    else
                    {
                        LogpieLog
                                .e(TAG,
                                        "cannot find the last activity id when mode is LOAD_MORE_ACTIVITY.");
                    }
                }
            }

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
                if (mMode != null && mMode.equals(String.valueOf(ActivityManager.MODE_LOAD_MORE)))
                {
                    ActivityListFragment.this.mActivityManager.getNearbyActivityList(user, null,
                            null, ActivityManager.MODE_LOAD_MORE, lastActivityId,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
                else
                {
                    ActivityListFragment.this.mActivityManager.getNearbyActivityList(user, null,
                            null, ActivityManager.MODE_REFRESH, lastActivityId,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
            }
            else if (tab.equals(city))
            {
                if (mMode != null && mMode.equals(String.valueOf(ActivityManager.MODE_LOAD_MORE)))
                {
                    ActivityListFragment.this.mActivityManager.getActivityListByCity(user,
                            ActivityManager.MODE_LOAD_MORE, lastActivityId, mCity,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
                else
                {
                    ActivityListFragment.this.mActivityManager.getActivityListByCity(user,
                            ActivityManager.MODE_REFRESH, lastActivityId, mCity,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
            }
            else if (tab.equals(category))
            {
                if (mMode != null && mMode.equals(String.valueOf(ActivityManager.MODE_LOAD_MORE)))
                {
                    ActivityListFragment.this.mActivityManager.getActivityListByCategory(user,
                            ActivityManager.MODE_LOAD_MORE, lastActivityId, mCategory,
                            mSubcategory,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
                else
                {
                    ActivityListFragment.this.mActivityManager.getActivityListByCategory(user,
                            ActivityManager.MODE_REFRESH, lastActivityId, mCategory, mSubcategory,
                            ActivityListFragment.this.mActivityManager.new ActivityCallbackAdapter(
                                    callback));
                }
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
            if (mMode != null && mMode.equals(String.valueOf(ActivityManager.MODE_LOAD_MORE)))
            {
                if (ActivityListFragment.this.mActivityList == null)
                {
                    LogpieLog.e(TAG, "The original activity list is null.");
                    return;
                }
                LogpieLog.d(TAG, "Load more...");
                mArrayAdapter.addAll(items);

                // Hide progress bar
                mProgressBar.setVisibility(View.GONE);
                // Update the data of array adapter
                mArrayAdapter.notifyDataSetChanged();
                LogpieLog.d(TAG, "Finished Async Task of Load More.");
            }
            else
            {
                if (ActivityListFragment.this.mActivityList == null)
                {
                    ActivityListFragment.this.mActivityList = new ArrayList<LogpieActivity>();
                }
                ActivityListFragment.this.mActivityList = items;
                mArrayAdapter = new ActivityAdapter(items);

                setupAdapter(mArrayAdapter);
                LogpieLog.d(TAG, "Finished Async Task of Refreh.");
            }
        }

    }

}
