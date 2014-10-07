package com.logpie.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.util.LogpieLog;

/**
 * This fragment will show the activity detail page.
 * 
 * @author yilei
 * 
 */
public class LogpieActivityDetailFragment extends LogpieBaseFragment
{
    public static final String KEY_DETAIL_ACTIVITY = "com.logpie.detail.activity";

    private static final String TAG = LogpieActivityDetailFragment.class.getName();
    private Activity mActivity;
    private LogpieActivity mLogpieActivity;
    private DetailActivityUIHolder mUIHolder;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mActivity = getActivity();
        mLogpieActivity = getDetailActivity();
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activity_detail, parent, false);
        // Set up the UI components, store them in mUIHolder
        setUpUIHolder(view);
        setupView();

        return view;
    }

    private LogpieActivity getDetailActivity()
    {
        Parcelable activity = getArguments().getParcelable(KEY_DETAIL_ACTIVITY);
        if (activity == null)
        {
            LogpieLog.e(TAG, "Must pass a LogpieActivity instance to this fragment!");
        }
        if (activity instanceof LogpieActivity)
        {
            return (LogpieActivity) activity;
        }
        else
        {
            throw new IllegalArgumentException(
                    "Must pass a LogpieActivity instance to this fragment!");
        }
    }

    private void setupView()
    {
        // TODO: Setup the user icon
        // Set user name
        mUIHolder.mUserNameTextView.setText(mLogpieActivity.getmUserName());
        // Set activity descryption
        mUIHolder.mActivityDescryptionTextView.setText(mLogpieActivity.getmDescription());
        // Set the activity time
        String startTime = LogpieActivity.getFormatDate(mLogpieActivity.getmStartTime());
        String endTime = LogpieActivity.getFormatDate(mLogpieActivity.getmEndTime());
        mUIHolder.mActivityTimeTextView.setText(startTime + " ~ " + endTime);
        // Set the activity location
        mUIHolder.mActivityLocationTextView.setText(mLogpieActivity.getmLocation().getAddress());
        // Set the activity count like
        mUIHolder.mActivityCountLikeTextView.setText(Integer.toString(mLogpieActivity
                .getmCountLike()));
        // Set the activity count dislike
        mUIHolder.mActivityCountDislikeTextView.setText(Integer.toString(mLogpieActivity
                .getmCountDislike()));
    }

    private void setUpUIHolder(View view)
    {
        mUIHolder = new DetailActivityUIHolder();
        mUIHolder.mUserNameTextView = (TextView) view
                .findViewById(R.id.textview_activity_detail_user_name);
        mUIHolder.mUserIconImageView = (ImageView) view
                .findViewById(R.id.imageview_activity_detail_user_icon);
        mUIHolder.mActivityDescryptionTextView = (TextView) view
                .findViewById(R.id.textview_activity_detail_description);
        mUIHolder.mActivityTimeTextView = (TextView) view
                .findViewById(R.id.textview_activity_detail_time);
        mUIHolder.mActivityLocationTextView = (TextView) view
                .findViewById(R.id.textivew_detail_activity_location);
        mUIHolder.mActivityCountLikeTextView = (TextView) view
                .findViewById(R.id.textview_detail_activity_count_like);
        mUIHolder.mActivityCountDislikeTextView = (TextView) view
                .findViewById(R.id.textview_detail_activity_count_dislike);
        mUIHolder.mActivityCommentsLinearLayout = (LinearLayout) view
                .findViewById(R.id.linearlayout_detail_activity_comments);

    }

    private static class DetailActivityUIHolder
    {
        TextView mUserNameTextView;
        ImageView mUserIconImageView;
        TextView mActivityDescryptionTextView;
        TextView mActivityTimeTextView;
        TextView mActivityLocationTextView;
        TextView mActivityCountLikeTextView;
        TextView mActivityCountDislikeTextView;
        LinearLayout mActivityCommentsLinearLayout;
    }
}
