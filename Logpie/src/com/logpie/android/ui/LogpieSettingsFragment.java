package com.logpie.android.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.ActivityOpenHelper;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpieEditTextDialogCallback;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpiePickerDialogCallback;
import com.logpie.android.util.BuildInfo;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.RequestKeys;

/**
 * This fragment will show the Logpie Settings page.
 * 
 * @author yilei
 * 
 */
public class LogpieSettingsFragment extends LogpieBaseFragment
{
    private User mUser;
    private LogpieSystemSetting mSystemSetting;
    private Context mContext;

    private SettingUnit mUserProfilePhotoUnit;
    private SettingUnit mUserNicknameUnit;
    private SettingUnit mUserEmailUnit;
    private SettingUnit mUserGenderUnit;
    private SettingUnit mUserCityUnit;
    private SettingUnit mSystemLanguageUnit;
    private SettingUnit mSystemVersionUnit;
    private SettingUnit mSystemAbountUnit;

    private static final String TAG = LogpieSettingsFragment.class.getName();

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mContext = getActivity();
        mUser = NormalUser.getInstance(mContext);
        mSystemSetting = LogpieSystemSetting.getInstance(mContext);
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, parent, false);
        // Setup all the setting unit
        setupSettingUnit(view);
        // Setup logout button
        setupLogoutButton(view);
        return view;
    }

    private void setupLogoutButton(View parent)
    {
        Button logoutButton = (Button) parent.findViewById(R.id.settings_logout);
        logoutButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mUser.logout();
                ActivityOpenHelper.restartApplication(mContext);
            }
        });
    }

    private static class SettingUnit
    {
        // The whole linear layout for one single setting unit
        RelativeLayout mLayout;
        // The label textview
        TextView mLabelTextView;
        // The value view. (For ProfilePhoto it is ImageView, for other fields
        // it is TextView)
        View mContent;
        // The Key to get the String from LanguageHelper. So that it can be
        // dynamically change based on the system language
        private String mLabelKey;
        // The onClickListener for the entire setting unit. Some setting unit
        // may not have a clickListener, such as: system version, user email.
        private View.OnClickListener mOnClickListener;

        public SettingUnit(final Context context, final View parentView,
                final int linearLayoutResource, final int labelResource, final int contentResource,
                final String labelNameKey, final Class<? extends View> resourceType)
        {

            mLayout = (RelativeLayout) parentView.findViewById(linearLayoutResource);
            mLabelTextView = (TextView) parentView.findViewById(labelResource);
            if (contentResource != -1)
            {
                mContent = parentView.findViewById(contentResource);
            }
            mLabelKey = labelNameKey;
            initLabel(context);
        }

        public void setTextValue(final String value)
        {
            TextView contentView = (TextView) mContent;
            contentView.setText(value);
        }

        private void initLabel(Context context)
        {
            // Use the label name key to find the corresponding string.
            mLabelTextView.setText(LanguageHelper.getString(mLabelKey, context));
        }

        /* package-private */void setOnClickListener(final View.OnClickListener onClickListener)
        {
            mOnClickListener = onClickListener;
            mLayout.setOnClickListener(mOnClickListener);
        }
    }

    // Setup all the setting units one by one.
    private void setupSettingUnit(View view)
    {
        // Setup the user profile photo setting unit.
        setupProfilePhotoUnit(view);
        // Setup the user nickname setting unit.
        setupNicknameUnit(view);
        // Setup the user email setting unit.
        setupEmailUnit(view);
        // Setup the user gender setting unit.
        setupGenderUnit(view);
        // Setup the user city setting unit.
        setupCityUnit(view);
        // Setup the system language setting unit.
        setupSystemLanguageUnit(view);
        // Setup the system version setting unit.
        setupSystemVersionUnit(view);
        // Setup the system about setting unit.
        setupSystemAboutUnit(view);
    }

    private void setupSystemAboutUnit(View view)
    {
        mSystemAbountUnit = new SettingUnit(mContext, view,
                R.id.settings_system_about_linear_layout,
                R.id.settings_system_about_label_text_view, -1,
                LanguageHelper.KEY_SYSTEM_ABOUT_SETTING_LABEL, TextView.class);
    }

    private void setupSystemVersionUnit(View view)
    {
        mSystemVersionUnit = new SettingUnit(mContext, view,
                R.id.settings_system_version_linear_layout,
                R.id.settings_system_version_label_text_view,
                R.id.settings_system_version_text_view,
                LanguageHelper.KEY_SYSTEM_VERSION_SETTING_LABEL, TextView.class);
        mSystemVersionUnit.setTextValue(BuildInfo.VERSION);
    }

    private void setupSystemLanguageUnit(View view)
    {
        mSystemLanguageUnit = new SettingUnit(mContext, view,
                R.id.settings_system_language_linear_layout,
                R.id.settings_system_language_label_text_view,
                R.id.settings_system_language_text_view,
                LanguageHelper.KEY_SYSTEM_LANGUAGE_SETTING_LABEL, TextView.class);

        mSystemLanguageUnit.setTextValue(LanguageHelper.getString(
                LanguageHelper.KEY_LANGUAGE_SHOWING_STRING, mContext));

        Resources resources = mContext.getResources();
        String[] languageStoreValue = new String[] { LanguageHelper.ENGLISH, LanguageHelper.CHINESE };
        String[] languageShowValue = new String[] {
                resources.getString(R.string.language_showing_string_us),
                resources.getString(R.string.language_showing_string_cn) };
        mSystemLanguageUnit.setOnClickListener(getPickerListener(mContext,
                LanguageHelper.KEY_SYSTEM_LANGUAGE_SETTING_LABEL, mSystemLanguageUnit,
                LanguageHelper.KEY_LANGUAGE, languageShowValue, languageStoreValue, true));
    }

    private void setupCityUnit(View view)
    {
        mUserCityUnit = new SettingUnit(mContext, view, R.id.settings_user_city_linear_layout,
                R.id.settings_user_city_label_text_view, R.id.settings_user_city_text_view,
                LanguageHelper.KEY_USER_CITY_SETTING_LABEL, TextView.class);
        mUserCityUnit.setTextValue(mUser.getUserProfile().getUserCity());
    }

    private void setupGenderUnit(View view)
    {
        mUserGenderUnit = new SettingUnit(mContext, view, R.id.settings_user_gender_linear_layout,
                R.id.settings_user_gender_label_text_view, R.id.settings_user_gender_text_view,
                LanguageHelper.KEY_USER_GENDER_SETTING_LABEL, TextView.class);
        boolean userGender = mUser.getUserProfile().getUserGender();
        if (userGender)
        {
            mUserGenderUnit.setTextValue(LanguageHelper.getString(LanguageHelper.KEY_GENDER_MALE,
                    mContext));
        }
        else
        {
            mUserGenderUnit.setTextValue(LanguageHelper.getString(LanguageHelper.KEY_GENDER_FEMALE,
                    mContext));
        }
        String[] genderShowChoice = new String[] {
                LanguageHelper.getString(LanguageHelper.KEY_GENDER_MALE, mContext),
                LanguageHelper.getString(LanguageHelper.KEY_GENDER_FEMALE, mContext) };
        String[] genderStoreValue = new String[] { String.valueOf(true), String.valueOf(false) };
        mUserGenderUnit.setOnClickListener(getPickerListener(mContext,
                LanguageHelper.KEY_USER_GENDER_SETTING_LABEL, mUserGenderUnit,
                RequestKeys.KEY_GENDER, genderShowChoice, genderStoreValue, false));
    }

    private void setupEmailUnit(View view)
    {
        mUserEmailUnit = new SettingUnit(mContext, view, R.id.settings_user_email_linear_layout,
                R.id.settings_user_email_label_text_view, R.id.settings_user_email_text_view,
                LanguageHelper.KEY_USER_EMAIL_SETTING_LABEL, TextView.class);
        mUserEmailUnit.setTextValue(mUser.getUserProfile().getUserEmail());
    }

    private void setupNicknameUnit(View view)
    {
        mUserNicknameUnit = new SettingUnit(mContext, view,
                R.id.settings_user_nickname_linear_layout,
                R.id.settings_user_nickname_label_text_view, R.id.settings_user_nickname_text_view,
                LanguageHelper.KEY_USER_NICKNAME_SETTING_LABEL, TextView.class);
        mUserNicknameUnit.setOnClickListener(getEditTextListener(mContext,
                LanguageHelper.KEY_USER_NICKNAME_SETTING_LABEL,
                LanguageHelper.KEY_SETTING_NICKNAME_DIALOG_HINT, mUserNicknameUnit,
                RequestKeys.KEY_NICKNAME, false));
        mUserNicknameUnit.setTextValue(mUser.getUserProfile().getUserName());
    }

    private void setupProfilePhotoUnit(View view)
    {
        mUserProfilePhotoUnit = new SettingUnit(mContext, view,
                R.id.settings_user_profile_photo_linear_layout,
                R.id.settings_user_profile_photo_label_text_view,
                R.id.settings_user_profile_photo_image_view,
                LanguageHelper.KEY_USER_PROFILE_PHOTO_SETTING_LABEL, ImageView.class);

        mUserProfilePhotoUnit.mContent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

            }
        });

        // TODO: Set photo
        // ((ImageView) mUserProfilePhotoUnit.mContent).setImageResource(null);
    }

    private View.OnClickListener getEditTextListener(final Context context,
            final String titleStringKey, final String hintStringKey, final SettingUnit settingUnit,
            final String updateKey, final boolean isSystemSetting)
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String titleString = LanguageHelper.getString(titleStringKey, context);
                final String hintString = LanguageHelper.getString(hintStringKey, context);
                LogpieDialogHelper.openEditTextDialog(mContext, titleString, hintString,
                        new LogpieEditTextDialogCallback()
                        {
                            @Override
                            public void onSelect(String text)
                            {
                                LogpieLog.i(TAG, "The " + titleString + " user just entered is "
                                        + text);
                                TextView textView = (TextView) settingUnit.mContent;
                                textView.setText(text);
                                if (isSystemSetting)
                                {
                                    mSystemSetting.setSystemSetting(updateKey, text);
                                }
                                else
                                {
                                    mUser.updateProfile(updateKey, text);
                                }
                            }

                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled input a new nickname");
                            }
                        });
            }
        };
    }

    /**
     * Get the picker dialog when click the layout.
     * 
     * @param context
     * @param titleStringKey
     * @param settingUnit
     * @param key
     * @param showChoice
     *            The choice use to show up.
     * @param storeValue
     *            The value used to store in the database.
     * @param isSystemSetting
     * @return
     */
    private View.OnClickListener getPickerListener(final Context context,
            final String titleStringKey, final SettingUnit settingUnit, final String key,
            final String[] showChoice, final String[] storeValue, final boolean isSystemSetting)
    {
        return new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String titleString = LanguageHelper.getString(titleStringKey, context);
                LogpieDialogHelper.openPickerDialog(mContext, titleString, showChoice,
                        new LogpiePickerDialogCallback()
                        {
                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled input a new nickname");
                            }

                            @Override
                            public void onSelect(int n)
                            {
                                LogpieLog.i(TAG, "The " + titleString + " user just choose is "
                                        + showChoice[n]);
                                TextView textView = (TextView) settingUnit.mContent;
                                textView.setText(showChoice[n]);
                                if (isSystemSetting)
                                {
                                    mSystemSetting.setSystemSetting(key, storeValue[n]);
                                    // If it is setting a system language, it
                                    // will restart the device.
                                    if (TextUtils.equals(key, LanguageHelper.KEY_LANGUAGE))
                                    {
                                        ActivityOpenHelper.restartApplication(mContext);
                                    }
                                }
                                else
                                {
                                    mUser.updateProfile(key, storeValue[n]);
                                }
                            }
                        });
            }
        };
    }
}
