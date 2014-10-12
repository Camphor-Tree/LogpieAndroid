package com.logpie.android.ui;

import java.util.Date;

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
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_activity, parent, false);
        initilizeUI(view);
        setupDatePicker();
        return view;
    }

    private void setupDatePicker()
    {
        mUiHolder.mStartDateEditText.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieDialogHelper.openDatePickerDialog(mContext, new Date(),
                        new LogpieDatePickerDialogCallback()
                        {
                            @Override
                            public void onSelectDate(LogpieDateTime date)
                            {
                                mLogpieActivity.setStartTime(date);
                                mUiHolder.mStartDateEditText.setText(date.getDateString());
                            }

                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled");
                            }
                        });

            }
        });
    }

    private void initilizeUI(View parentView)
    {
        mUiHolder = new CreateActivityUIHolder();
        mUiHolder.mDescriptionEditText = (EditText) parentView
                .findViewById(R.id.create_activity_description_edit_text);

        mUiHolder.mAddressEditText = (EditText) parentView
                .findViewById(R.id.create_activity_address_edit_text);

        mUiHolder.mStartDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_date_edit_text);
        mUiHolder.mStartTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_time_edit_text);
        mUiHolder.mEndDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_date_edit_text);
        mUiHolder.mEndTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_time_edit_text);

        mUiHolder.mCityTextView = (TextView) parentView
                .findViewById(R.id.create_activity_city_text_view);
        mUiHolder.mCategoryTextView = (TextView) parentView
                .findViewById(R.id.create_activity_category_text_view);

    }

    private void initializeTime()
    {

    }

    private static class CreateActivityUIHolder
    {
        private EditText mDescriptionEditText;

        private EditText mAddressEditText;

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
