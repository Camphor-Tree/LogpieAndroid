package com.logpie.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.logic.User;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper;
import com.logpie.android.ui.helper.LogpieDialogHelper.LogpieEditTextDialogCallback;
import com.logpie.android.util.LogpieLog;

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
    private SettingsUIHolder mUiHolder;

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
        // setupUI(view);
        setupSettingUnit(view);
        // setupSettingLogic();
        return view;
    }

    private void setupSettingLogic()
    {
        mUiHolder.mUserNicknameLinearLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                LogpieDialogHelper.openEditTextDialog(mContext, "Change Nickname",
                        "Choose a name easy to remember", new LogpieEditTextDialogCallback()
                        {
                            @Override
                            public void onSelect(String text)
                            {
                                LogpieLog.i(TAG, "The new nickname user just entered is " + text);
                                mUiHolder.mUserNickNameTextView.setText(text);
                            }

                            @Override
                            public void onCancel()
                            {
                                LogpieLog.i(TAG, "User canceled input a new nickname");
                            }
                        });
            }
        });
    }

    private void setupUI(View view)
    {
        mUiHolder = new SettingsUIHolder();
        mUiHolder.mUserProfilePhotoLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_user_profile_photo_linear_layout);

        mUiHolder.mUserNicknameLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_user_nickname_linear_layout);
        mUiHolder.mUserEmailLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_user_email_linear_layout);
        mUiHolder.mUserGenderLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_user_gender_linear_layout);
        mUiHolder.mUserCityLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_user_city_linear_layout);
        mUiHolder.mSystemLanguageLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_system_language_linear_layout);
        mUiHolder.mSystemVersionLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_system_version_linear_layout);
        mUiHolder.mSystemAboutLinearLayout = (LinearLayout) view
                .findViewById(R.id.settings_system_about_linear_layout);

        mUiHolder.mUserNickNameTextView = (TextView) view
                .findViewById(R.id.settings_user_nickname_text_view);
    }

    private static class SettingsUIHolder
    {
        LinearLayout mUserProfilePhotoLinearLayout;
        LinearLayout mUserNicknameLinearLayout;
        LinearLayout mUserEmailLinearLayout;
        LinearLayout mUserGenderLinearLayout;
        LinearLayout mUserCityLinearLayout;
        LinearLayout mSystemLanguageLinearLayout;
        LinearLayout mSystemVersionLinearLayout;
        LinearLayout mSystemAboutLinearLayout;

        TextView mUserProfilePhotoLabelImageView;
        TextView mUserNickNameLabelTextView;
        TextView mUserEmailLabelTextView;
        TextView mUserGenderLabelTextView;
        TextView mUserCityLabelTextView;
        TextView mSystemLanguageLabelTextView;
        TextView mSystemVersionLabelTextView;

        ImageView mUserProfilePhotoImageView;
        TextView mUserNickNameTextView;
        TextView mUserEmailTextView;
        TextView mUserGenderTextView;
        TextView mUserCityTextView;
        TextView mSystemLanguageTextView;
        TextView mSystemVersionTextView;
    }

    private static class SettingUnit
    {
        private Context mContext;
        LinearLayout mLinearLayout;
        TextView mLabelTextView;
        View mContent;
        private boolean mIsClickable;
        private String mLabelKey;
        private View.OnClickListener mOnClickListener;

        public SettingUnit(final Context context, final View parentView, final boolean isClickable,
                final int linearLayoutResource, final int labelResource, final int contentResource,
                final String labelNameKey, final Class<? extends View> resourceType)
        {
            mContext = context;
            mIsClickable = isClickable;
            mLinearLayout = (LinearLayout) parentView.findViewById(linearLayoutResource);
            mLabelTextView = (TextView) parentView.findViewById(labelResource);
            if (contentResource != -1)
            {
                mContent = parentView.findViewById(contentResource);
            }
            mLabelKey = labelNameKey;
            initLabel();
        }

        private void initLabel()
        {
            // Use the label name key to find the corresponding string.
            mLabelTextView.setText(LanguageHelper.getString(mLabelKey, mContext));
        }

        /* package-private */void setOnClickListener(final View.OnClickListener onClickListener)
        {
            mOnClickListener = onClickListener;
            if (!mIsClickable)
            {
                LogpieLog.e(TAG, "This setting unit is not clickable!");
                throw new UnsupportedOperationException("This setting unit is not clickable!");
            }
            mLinearLayout.setOnClickListener(mOnClickListener);
        }
    }

    private void setupSettingUnit(View view)
    {
        mUserProfilePhotoUnit = new SettingUnit(mContext, view, true,
                R.id.settings_user_profile_photo_linear_layout,
                R.id.settings_user_profile_photo_label_text_view,
                R.id.settings_user_profile_photo_image_view,
                LanguageHelper.KEY_USER_PROFILE_PHOTO_SETTING_LABEL, ImageView.class);

        mUserNicknameUnit = new SettingUnit(mContext, view, true,
                R.id.settings_user_nickname_linear_layout,
                R.id.settings_user_nickname_label_text_view, R.id.settings_user_nickname_text_view,
                LanguageHelper.KEY_USER_NICKNAME_SETTING_LABEL, TextView.class);
        mUserNicknameUnit.setOnClickListener(getEditTextListener(mContext,
                LanguageHelper.KEY_USER_NICKNAME_SETTING_LABEL,
                LanguageHelper.KEY_SETTING_NICKNAME_DIALOG_HINT, mUserNicknameUnit,
                User.KEY_USER_NICKNAME, false));

        mUserEmailUnit = new SettingUnit(mContext, view, false,
                R.id.settings_user_email_linear_layout, R.id.settings_user_email_label_text_view,
                R.id.settings_user_email_text_view, LanguageHelper.KEY_USER_EMAIL_SETTING_LABEL,
                TextView.class);
        mUserGenderUnit = new SettingUnit(mContext, view, true,
                R.id.settings_user_gender_linear_layout, R.id.settings_user_gender_label_text_view,
                R.id.settings_user_gender_text_view, LanguageHelper.KEY_USER_GENDER_SETTING_LABEL,
                TextView.class);
        mUserCityUnit = new SettingUnit(mContext, view, true,
                R.id.settings_user_city_linear_layout, R.id.settings_user_city_label_text_view,
                R.id.settings_user_city_text_view, LanguageHelper.KEY_USER_CITY_SETTING_LABEL,
                TextView.class);

        mSystemLanguageUnit = new SettingUnit(mContext, view, true,
                R.id.settings_system_language_linear_layout,
                R.id.settings_system_language_label_text_view,
                R.id.settings_system_language_text_view,
                LanguageHelper.KEY_SYSTEM_LANGUAGE_SETTING_LABEL, TextView.class);

        mSystemVersionUnit = new SettingUnit(mContext, view, false,
                R.id.settings_system_version_linear_layout,
                R.id.settings_system_version_label_text_view,
                R.id.settings_system_version_text_view,
                LanguageHelper.KEY_SYSTEM_VERSION_SETTING_LABEL, TextView.class);

        mSystemAbountUnit = new SettingUnit(mContext, view, true,
                R.id.settings_system_about_linear_layout,
                R.id.settings_system_about_label_text_view, -1,
                LanguageHelper.KEY_SYSTEM_ABOUT_SETTING_LABEL, TextView.class);

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
}
