package com.logpie.android.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.CommentManager;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.LogpieToastHelper;
import com.logpie.android.util.LogpieDateTime;
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
    private ActivityManager mActivityManager;
    private CommentManager mCommentManager;
    private User mUser;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mActivity = getActivity();
        mLogpieActivity = getDetailActivity();
        mActivityManager = ActivityManager.getInstance(mActivity.getApplicationContext());
        mCommentManager = CommentManager.getInstance(mActivity.getApplicationContext());
        mUser = NormalUser.getInstance(mActivity.getApplicationContext());
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activity_detail, parent, false);
        // Set up the UI components, store them in mUIHolder
        setUpUIHolder(view);
        setupView();
        setupCommentButton();

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

    private void setupCommentButton()
    {
        mUIHolder.mCommentButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String comment = mUIHolder.mCommentEditText.getText().toString();
                if (comment != null)
                {
                    // TODO should check the comment content if legal.
                    comment = comment.trim();
                }
                if (TextUtils.isEmpty(comment.trim()))
                {
                    LogpieToastHelper.showShortMessage(mActivity, "Comment cannot be null");
                    return;
                }

                // Lock the edit text and button
                disableCommentView();

                AddCommentTask addCommentTask = new AddCommentTask();
                addCommentTask.execute(comment);
            }
        });
    }

    private void setupView()
    {
        // TODO: Setup the user icon
        // Set user name
        mUIHolder.mUserNameTextView.setText(mLogpieActivity.getUserName());
        // Set activity descryption
        mUIHolder.mActivityDescryptionTextView.setText(mLogpieActivity.getDescription());
        // Set the activity time
        String startTimeString;
        LogpieDateTime startLogpieDateTime = mLogpieActivity.getStartTime();
        if (startLogpieDateTime != null)
        {
            startTimeString = startLogpieDateTime.getDateTimeString();
        }
        else
        {
            startTimeString = "-";
        }
        String endTimeString;
        LogpieDateTime endLogpieDateTime = mLogpieActivity.getEndTime();
        if (endLogpieDateTime != null)
        {
            endTimeString = endLogpieDateTime.getDateTimeString();
        }
        else
        {
            endTimeString = "-";
        }
        mUIHolder.mActivityTimeTextView.setText(startTimeString + " ~ " + endTimeString);
        // Set the activity location
        mUIHolder.mActivityLocationTextView.setText(mLogpieActivity.getLocation().getAddress());
        // Set the activity count like
        mUIHolder.mActivityCountLikeTextView.setText(Integer.toString(mLogpieActivity
                .getCountLike()));
        // Set the activity count dislike
        mUIHolder.mActivityCountDislikeTextView.setText(Integer.toString(mLogpieActivity
                .getCountDislike()));
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

        mUIHolder.mCommentEditText = (EditText) view.findViewById(R.id.edittext_comment);
        mUIHolder.mCommentButton = (Button) view.findViewById(R.id.button_comment);

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

        EditText mCommentEditText;
        Button mCommentButton;
    }

    private class AddCommentTask extends AsyncTask<String, Object, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... comment)
        {
            if (mUser == null || mUser.getUserProfile() == null)
            {
                LogpieLog.e(TAG, "User or user profile is null");
                return false;
            }
            if (mLogpieActivity == null)
            {
                LogpieLog.e(TAG, "Activity is null");
                return false;
            }

            if (comment == null)
            {
                LogpieLog.e(TAG, "Comment is null");
                return false;
            }
            return mCommentManager.writeComment(mUser.getUserProfile().getUserId(),
                    mLogpieActivity.getActivityID(), comment[0]);
        }

        protected void onPostExecute(Boolean success)
        {
            if (success.booleanValue())
            {
                LogpieLog.d(TAG, "Add comment success");
                enableCommentView();
            }
            else
            {
                LogpieLog.d(TAG, "Add comment fail");
                enableCommentView();
            }

        }

    }

    private void disableCommentView()
    {
        mUIHolder.mCommentEditText.setEnabled(false);
        mUIHolder.mCommentButton.setClickable(false);
    }

    private void enableCommentView()
    {
        mUIHolder.mCommentEditText.setEnabled(true);
        mUIHolder.mCommentButton.setClickable(true);
    }
}
