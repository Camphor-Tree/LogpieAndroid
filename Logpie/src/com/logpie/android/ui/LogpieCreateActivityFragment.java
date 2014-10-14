package com.logpie.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.LogpieActivity;
import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpieDatePickerDialogCallback;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpieTimePickerDialogCallback;
import com.logpie.android.ui.helper.LogpieToastHelper;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieDateTime;
import com.logpie.android.util.LogpieLog;

/**
 * This fragment is used to show the CreateActivity page.
 * 
 * @author yilei
 * 
 */
public class LogpieCreateActivityFragment extends LogpieBaseFragment
{
    private static final String TAG = LogpieCreateActivityFragment.class.getName();
    private Context mContext;
    private LogpieActivity mLogpieActivity;
    private CreateActivityUIHolder mUiHolder;

    private LogpieDateTime mStartDateTime;
    private LogpieDateTime mEndDateTime;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        // Initialize a LogpieActivity. This is used to store the activity
        // attributes.
        mContext = getActivity();

        mLogpieActivity = new LogpieActivity();

        initializeTime();
    }

    private void initializeTime()
    {
        // Initialize the start/end date time
        mStartDateTime = new LogpieDateTime();
        mEndDateTime = new LogpieDateTime();
        // Make sure the time consistent.
        mEndDateTime.setLogpieDateTime(mStartDateTime);
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
        setupCityPicker();
        setupCategoryPicker();
        return view;
    }

    private void createActivityAction()
    {

        ActivityManager.getInstance(mContext).createActivity(mLogpieActivity, new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle result)
            {
                LogpieLog.d(TAG, "Create Activity succeed!");
            }

            @Override
            public void onError(Bundle errorMessage)
            {
                LogpieLog.d(TAG, "Create Activity error!");
            }
        });

    }

    private void setupCategoryPicker()
    {
        setupCategoryPicker(mUiHolder.mCategoryTextView, (FragmentActivity) mContext);
    }

    private void setupCityPicker()
    {
        setupCityPicker(mUiHolder.mCityTextView, (FragmentActivity) mContext);
    }

    private void setupDatePicker()
    {
        setUpDatePicker(mUiHolder.mStartDateEditText, true, mStartDateTime);
        setUpDatePicker(mUiHolder.mEndDateEditText, false, mEndDateTime);
        setUpTimePicker(mUiHolder.mStartTimeEditText, true, mStartDateTime);
        setUpTimePicker(mUiHolder.mEndTimeEditText, false, mEndDateTime);
    }

    private void setUpDatePicker(final View view, final boolean isStartDate,
            final LogpieDateTime logpieDateTime)
    {
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieDialogHelper.openDatePickerDialog(mContext, logpieDateTime,
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

    private void setUpTimePicker(final View view, final boolean isStartTime,
            final LogpieDateTime logpieDateTime)
    {
        view.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieDialogHelper.openTimePickerDialog(mContext, logpieDateTime,
                        new LogpieTimePickerDialogCallback()
                        {
                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled");
                            }

                            @Override
                            public void onSelectTime(int hour, int minute)
                            {

                                logpieDateTime.setTime(hour, minute);
                                // If the start date is before currentTime,
                                // setback the start time to currentTime
                                LogpieDateTime currentTime = new LogpieDateTime();
                                if (logpieDateTime.before(currentTime))
                                {
                                    logpieDateTime.setLogpieDateTime(currentTime);
                                }
                                // TODO: show a toast to give user a hint.
                                syncStartTimeEndTimeEditText(isStartTime);

                            }
                        });

            }
        });
    }

    private void setupCityPicker(final View view, final FragmentActivity activity)
    {
        view.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                FragmentManager fm = activity.getSupportFragmentManager();

                DialogFragment dialog = new CityPickerDialog();
                dialog.setTargetFragment(LogpieCreateActivityFragment.this,
                        LogpieDialogHelper.REQUEST_CODE_CITY_DIALOG);
                dialog.show(fm, LogpieDialogHelper.KEY_CITY_PICKER_DIALOG);
            }
        });
    }

    private void setupCategoryPicker(final View view, final FragmentActivity activity)
    {
        view.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                FragmentManager fm = activity.getSupportFragmentManager();
                DialogFragment dialog = new CategoryPickerDialog();
                dialog.setTargetFragment(LogpieCreateActivityFragment.this,
                        LogpieDialogHelper.REQUEST_CODE_CATEGORY_DIALOG);
                dialog.show(fm, LogpieDialogHelper.KEY_CATEGORY_PICKER_DIALOG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK)
        {
            LogpieLog.e(TAG, "Error or cancel from City");
            return;
        }
        if (requestCode == LogpieDialogHelper.REQUEST_CODE_CITY_DIALOG)
        {
            String city = data.getStringExtra(CityPickerDialog.KEY_CITY);
            if (!TextUtils.isEmpty(city))
            {
                mUiHolder.mCityTextView.setText(city);
                mLogpieActivity.setCity(city);
            }
            else
            {
                LogpieLog.e(TAG, "Error, the city choose is empty!");
            }
        }

        if (requestCode == LogpieDialogHelper.REQUEST_CODE_CATEGORY_DIALOG)
        {
            String categoryId = data.getStringExtra(CategoryPickerDialog.KEY_CATEGORY_ID);
            String subCategoryId = data.getStringExtra(CategoryPickerDialog.KEY_SUBCATEGORY_ID);
            String categoryString = data.getStringExtra(CategoryPickerDialog.KEY_CATEGORY_STRING);
            String subCategoryString = data
                    .getStringExtra(CategoryPickerDialog.KEY_SUBCATEGORY_STRING);
            if (!TextUtils.isEmpty(categoryId) && !TextUtils.isEmpty(categoryString))
            {
                mUiHolder.mCategoryTextView.setText(categoryString + ":" + subCategoryString);
                mLogpieActivity.setCategoryId(categoryId);
                mLogpieActivity.setSubCategoryId(subCategoryId);
                mLogpieActivity.setCategoryString(categoryString);
                mLogpieActivity.setSubCategoryString(subCategoryString);
            }
        }
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

        mUiHolder.mDescriptionEditText.setHint(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_DESCRIPTION_HINT, mContext));

        mUiHolder.mAddressEditText = (EditText) parentView
                .findViewById(R.id.create_activity_address_edit_text);
        mUiHolder.mAddressEditText.setHint(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_ADDRESS_HINT, mContext));

        // Set up start time label
        mUiHolder.mStartDateTimeTextView = (TextView) parentView
                .findViewById(R.id.create_activity_start_time_label_text_view);
        mUiHolder.mStartDateTimeTextView.setText(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_START_TIME_LABEL, mContext));
        mUiHolder.mStartDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_date_edit_text);
        mUiHolder.mStartTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_start_time_edit_text);

        // Set up end time label
        mUiHolder.mEndDateTimeTextView = (TextView) parentView
                .findViewById(R.id.create_activity_end_time_label_text_view);
        mUiHolder.mEndDateTimeTextView.setText(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_END_TIME_LABEL, mContext));

        mUiHolder.mEndDateEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_date_edit_text);
        mUiHolder.mEndTimeEditText = (EditText) parentView
                .findViewById(R.id.create_activity_end_time_edit_text);

        mUiHolder.mCityTextView = (TextView) parentView
                .findViewById(R.id.create_activity_city_text_view);

        mUiHolder.mCityTextView.setText(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_CITY_LABEL, mContext));
        mUiHolder.mCategoryTextView = (TextView) parentView
                .findViewById(R.id.create_activity_category_text_view);
        mUiHolder.mCategoryTextView.setText(LanguageHelper.getString(
                LanguageHelper.KEY_CREATE_ACTIVTY_CATEGORY_LABEL, mContext));

    }

    private void handleCreateActivity()
    {
        String description = mUiHolder.mDescriptionEditText.getText().toString().trim();
        String address = mUiHolder.mAddressEditText.getText().toString().trim();
        LogpieDateTime startTime = mStartDateTime;
        LogpieDateTime endTime = mEndDateTime;
        String city = mUiHolder.mCityTextView.getText().toString().trim();
        String category = mUiHolder.mCategoryTextView.getText().toString().trim();

        if (TextUtils.isEmpty(description))
        {
            LogpieToastHelper.showLongMessage(mContext, "The description can not be empty");
            LogpieLog.d(TAG, "The description can not be empty");
            return;
        }
        if (TextUtils.isEmpty(address))
        {
            LogpieToastHelper.showLongMessage(mContext, "The address can not be empty");
            LogpieLog.d(TAG, "The address can not be empty");
            return;
        }
        mLogpieActivity.setDescription(description);
        mLogpieActivity.setLocation(new LogpieLocation(address));
        mLogpieActivity.setStartTime(startTime);
        mLogpieActivity.setEndTime(endTime);
        mLogpieActivity.setCreateTime();

    }

    private static class CreateActivityUIHolder
    {
        private EditText mDescriptionEditText;

        private EditText mAddressEditText;

        private TextView mStartDateTimeTextView;
        private EditText mStartDateEditText;
        private EditText mStartTimeEditText;
        private TextView mEndDateTimeTextView;
        private EditText mEndDateEditText;
        private EditText mEndTimeEditText;

        private TextView mCityTextView;
        private TextView mCategoryTextView;
    }

}
