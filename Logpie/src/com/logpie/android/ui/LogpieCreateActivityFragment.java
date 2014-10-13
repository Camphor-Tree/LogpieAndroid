package com.logpie.android.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpieDatePickerDialogCallback;
import com.logpie.android.util.LogpieDateTime;
import com.logpie.android.util.LogpieLog;

public class LogpieCreateActivityFragment extends LogpieBaseFragment
{
    private static final String TAG = LogpieCreateActivityFragment.class.getName();
    private Context mContext;
    private LogpieActivity mLogpieActivity;
    private CreateActivityUIHolder mUiHolder;

    private LogpieDateTime mStartDateTime;
    private LogpieDateTime mEndDateTime;
    // indicate start/end date/time is editing
    private int tag = 4;
    private int first_time_in = 0;
    private int visibility_choice = 0;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        // Initialize a LogpieActivity. This is used to store the activity
        // attributes.
        mContext = getActivity();

        mLogpieActivity = new LogpieActivity();

        // Initialize the start/end date time
        mStartDateTime = new LogpieDateTime();
        mEndDateTime = new LogpieDateTime();
        // mEndDateTime.setLogpieDateTime(mStartDateTime);

        mLogpieActivity.setStartTime(mStartDateTime);
        mLogpieActivity.setEndTime(mEndDateTime);
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_activity, parent, false);
        initilizeUI(view);
        syncStartTimeEndTimeEditText(true);
        setupDatePicker();

        return view;
    }

    private void setupDatePicker()
    {
        setUpDatePicker(mUiHolder.mStartDateEditText, true, mStartDateTime);
        setUpDatePicker(mUiHolder.mEndDateEditText, false, mEndDateTime);
    }

    private void setUpDatePicker(final View view, final boolean isStartDate,
            final LogpieDateTime logpieDateTime)
    {
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieDialogHelper.openDatePickerDialog(mContext, new LogpieDateTime(),
                        new LogpieDatePickerDialogCallback()
                        {
                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled");
                            }

                            @Override
                            public void onSelectDate(int year, int month, int day)
                            {

                                logpieDateTime.setDate(year, month, day);
                                // If the start date is before currentTime,
                                // setback the start time to currentTime
                                LogpieDateTime currentTime = new LogpieDateTime();
                                if (logpieDateTime.before(currentTime))
                                {
                                    logpieDateTime.setLogpieDateTime(currentTime);
                                }
                                // TODO: show a toast to give user a hint.
                                syncStartTimeEndTimeEditText(isStartDate);
                            }
                        });

            }
        });
    }

    private void syncStartTimeEndTimeEditText(boolean isChangingStartTime)
    {
        if (mStartDateTime.after(mEndDateTime))
        {
            if (isChangingStartTime)
            {
                // Here we always make sure the end time is always after the
                // start time.
                mEndDateTime.setLogpieDateTime(mStartDateTime);
            }
            else
            {
                mStartDateTime.setLogpieDateTime(mEndDateTime);
            }
        }
        // Update the UI
        mUiHolder.mStartDateEditText.setText(mStartDateTime.getDateString());
        mUiHolder.mStartTimeEditText.setText(mStartDateTime.getTimeString());

        mUiHolder.mEndDateEditText.setText(mEndDateTime.getDateString());
        mUiHolder.mEndTimeEditText.setText(mEndDateTime.getTimeString());
    }

    private void initilizeUI(View parentView)
    {
        mUiHolder = new CreateActivityUIHolder();
        mUiHolder.mDescriptionEditText = (EditText) parentView
                .findViewById(R.id.create_activity_description_edit_text);
        mUiHolder.mDescriptionEditText.setHint(LanguageHelper.getId(
                LanguageHelper.KEY_DESCRIPTION_HINT, mContext));

        mUiHolder.mAddressEditText = (EditText) parentView
                .findViewById(R.id.create_activity_address_edit_text);
        mUiHolder.mAddressEditText.setHint(LanguageHelper.getId(LanguageHelper.KEY_LOCATION_HINT,
                mContext));

        mUiHolder.mStartTimeLabel = (TextView) parentView
                .findViewById(R.id.create_activity_start_time_label_text_view);
        mUiHolder.mStartTimeLabel.setText(LanguageHelper.getId(LanguageHelper.KEY_START_TIME_HINT,
                mContext));
        mUiHolder.mStartDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_date_edit_text);
        mUiHolder.mStartTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_time_edit_text);

        mUiHolder.mEndTimeLabel = (TextView) parentView
                .findViewById(R.id.create_activity_end_time_label_text_view);
        mUiHolder.mEndTimeLabel.setText(LanguageHelper.getId(LanguageHelper.KEY_END_TIME_HINT,
                mContext));
        mUiHolder.mEndDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_date_edit_text);
        mUiHolder.mEndTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_time_edit_text);

        mUiHolder.mCityTextView = (TextView) parentView
                .findViewById(R.id.create_activity_city_text_view);
        mUiHolder.mCityTextView.setHint(LanguageHelper.getId(LanguageHelper.KEY_CITY_PICKER_TITLE,
                mContext));
        mUiHolder.mCategoryTextView = (TextView) parentView
                .findViewById(R.id.create_activity_category_text_view);
        mUiHolder.mCategoryTextView.setHint(LanguageHelper.getId(
                LanguageHelper.KEY_CATEGORY_PICKER_TITLE, mContext));

    }

    private void initializeTime()
    {

    }

    private static class CreateActivityUIHolder
    {
        private EditText mDescriptionEditText;

        private EditText mAddressEditText;

        private TextView mStartTimeLabel;
        private TextView mEndTimeLabel;

        private EditText mStartDateEditText;
        private EditText mStartTimeEditText;
        private EditText mEndDateEditText;
        private EditText mEndTimeEditText;

        private TextView mCityTextView;
        private TextView mCategoryTextView;

        private DatePickerDialog dpd;
        private TimePickerDialog tpd;
    }

}
