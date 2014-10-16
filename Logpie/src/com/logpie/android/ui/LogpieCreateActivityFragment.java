package com.logpie.android.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.exception.ThreadException;
import com.logpie.android.gis.GISManager;
import com.logpie.android.gis.GisAPIHelper;
import com.logpie.android.logic.ActivityManager;
import com.logpie.android.logic.AuthManager;
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
import com.logpie.android.util.ThreadHelper;

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
    private AuthManager mAuthManager;
    private LogpieActivity mLogpieActivity;
    private CreateActivityUIHolder mUiHolder;

    private LogpieDateTime mStartDateTime;
    private LogpieDateTime mEndDateTime;

    // This is to make sure the current location address doesn't get changed. If
    // changed, shouldn't use current location for the activity
    private volatile String mCurrentLocationAddress;
    private volatile boolean mIsUsingCurrentLocation;
    private Double mCurrentLat;
    private Double mCurrentLon;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        // Show the menu item in the action bar
        this.setHasOptionsMenu(true);
        // Initialize a LogpieActivity. This is used to store the activity
        // attributes.
        mContext = getActivity();

        mAuthManager = AuthManager.getInstance(mContext);

        mLogpieActivity = new LogpieActivity();
        // Setup creater information
        String uid = mAuthManager.getUID();
        String nickName = mAuthManager.getCurrentAccount().getNickname();
        mLogpieActivity.setUserID(uid);
        mLogpieActivity.setUserName(nickName);

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
        setupAddressListener();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.create_activity, menu);
    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_confirm_create_activity:
        {
            CreateActivityTask task = new CreateActivityTask();
            task.execute();
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void setupAddressListener()
    {
        mUiHolder.mAddressEditText.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                LogpieLocation currentLocation = GISManager.getInstance(mContext)
                        .getCurrentLocation();
                if (currentLocation == null)
                {
                    LogpieToastHelper.showShortMessage(mContext,
                            "Sorry, the current location is not available!");
                    return false;
                }
                final Double lat = currentLocation.getLatitude();
                final Double lon = currentLocation.getLongitude();
                if (lat != null && lon != null)
                {
                    try
                    {
                        ThreadHelper.runOffMainThread(false, new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                // try to revser geocoding first.
                                mCurrentLocationAddress = GisAPIHelper.getAddressFromLatLon(lat,
                                        lon);
                                if (mCurrentLocationAddress == null)
                                {
                                    LogpieLog
                                            .d(TAG, "Rever geocoding fail! use lat lon as address");
                                    mCurrentLocationAddress = "latitude:" + lat + " longitude:"
                                            + lon;
                                }
                                mIsUsingCurrentLocation = true;
                                mCurrentLat = lat;
                                mCurrentLon = lon;
                                ThreadHelper.runOnMainThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        LogpieToastHelper.showShortMessage(mContext,
                                                "Using current location as the activity location");
                                        mUiHolder.mAddressEditText.setText(mCurrentLocationAddress);
                                    }
                                });
                            }
                        });
                    } catch (ThreadException e)
                    {
                        e.printStackTrace();
                    }

                    return true;
                }
                else
                {
                    LogpieToastHelper.showShortMessage(mContext,
                            "Sorry, the current location is not available!");
                    return false;
                }
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
                                LogpieToastHelper.showLongMessage(mContext,
                                        "Cannot set the start time before current time");
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

    /**
     * handle the create activity.
     */
    private void createActivityAction()
    {
        // Read the information from the ui.
        boolean isParameterValid = handleCreateActivity();
        if (!isParameterValid)
        {
            return;
        }

        LogpieLocation activityLocation = mLogpieActivity.getLocation();
        String city = activityLocation.getCity();
        String address = activityLocation.getAddress();

        // If user long-click the address and didn't change the auto-generated
        // address, then just use the lat lon.
        if (mIsUsingCurrentLocation && TextUtils.equals(mCurrentLocationAddress.trim(), address))
        {
            activityLocation.setLatitude(mCurrentLat);
            activityLocation.setLongitude(mCurrentLon);
        }
        else
        {
            // If user use his own address, or modified the address, then try to
            // geocoding to get the coordinates for the activity
            if (!TextUtils.isEmpty(address))
            {
                LogpieLocation location = GisAPIHelper.getLatLonFromAddressAndCity(address, city);
                Double lat = location.getLatitude();
                Double lon = location.getLongitude();
                if (lat != null && lon != null)
                {
                    activityLocation.setLatitude(lat);
                    activityLocation.setLatitude(lon);
                }
                else
                {
                    LogpieLog.e(TAG, "Geocoding fail!");
                }
            }
            else
            {
                LogpieLog.e(TAG, "The addreess cannot be null");
                return;
            }
        }

        ActivityManager.getInstance(mContext).createActivity(mLogpieActivity, new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle result)
            {
                LogpieLog.d(TAG, "Create Activity succeed!");
                LogpieToastHelper.showShortMessage(mContext, "Create activity succeed!");
                getActivity().finish();
            }

            @Override
            public void onError(Bundle errorMessage)
            {
                LogpieLog.d(TAG, "Create Activity error!");
                LogpieToastHelper.showShortMessage(mContext,
                        "Error happend when creating activity, please try later!");
            }
        });
    }

    /**
     * Get all the parameters from UI and check whether all the parameters are
     * legel.
     * 
     * @return whether the parameters are legel
     */
    private boolean handleCreateActivity()
    {
        String description = mUiHolder.mDescriptionEditText.getText().toString().trim();
        String address = mUiHolder.mAddressEditText.getText().toString().trim();
        LogpieDateTime startTime = mStartDateTime;
        LogpieDateTime endTime = mEndDateTime;

        if (TextUtils.isEmpty(description))
        {
            LogpieToastHelper.showShortMessage(mContext, "The description can not be empty");
            LogpieLog.d(TAG, "The description can not be empty");
            return false;
        }
        if (TextUtils.isEmpty(address))
        {
            LogpieToastHelper.showShortMessage(mContext, "The address can not be empty");
            LogpieLog.d(TAG, "The address can not be empty");
            return false;
        }

        if (startTime.before(new LogpieDateTime()))
        {
            LogpieToastHelper.showShortMessage(mContext,
                    "Cannot create activity before current time");
            LogpieLog.d(TAG, "Cannot create activity before current time");
            return false;
        }

        if (mLogpieActivity.getCategoryString() == null)
        {
            LogpieToastHelper.showShortMessage(mContext, "Please choose a category!");
            LogpieLog.d(TAG, "Category cannot be null!");
            return false;
        }

        LogpieLocation location = mLogpieActivity.getLocation();
        if (location == null || TextUtils.isEmpty(location.getCity()))
        {
            LogpieToastHelper.showShortMessage(mContext, "Please choose a city!");
            LogpieLog.d(TAG, "City cannot be null");
            return false;
        }
        mLogpieActivity.setDescription(description);
        mLogpieActivity.setAddress(address);
        mLogpieActivity.setStartTime(startTime);
        mLogpieActivity.setEndTime(endTime);
        mLogpieActivity.setCreateTime();
        return true;
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

    class CreateActivityTask extends AsyncTask<Object, Object, Object>
    {
        @Override
        protected Object doInBackground(Object... params)
        {
            createActivityAction();
            return null;
        }
    }

}
