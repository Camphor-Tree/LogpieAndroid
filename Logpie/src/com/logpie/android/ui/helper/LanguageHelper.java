package com.logpie.android.ui.helper;

import java.util.Locale;

import android.content.Context;

import com.logpie.android.R;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.util.LogpieLog;

public class LanguageHelper
{
    private final static String TAG = LanguageHelper.class.getName();
    private static boolean sIsChinese;

    /**
     * Key for every Logpie system setting
     */
    public final static String KEY_LANGUAGE = "com.logpie.android.language";
    public final static String ENGLISH = "english";
    public final static String CHINESE = "chinese";

    // For Login Fragement
    public final static String KEY_WELCOME = "welcome";
    public final static String KEY_EMAIL = "email";
    public final static String KEY_PASSWORD = "password";
    public final static String KEY_NICKNAME = "nickname";
    public final static String KEY_CONFIRM_PASSWORD = "confirm_password";
    public final static String KEY_LOGIN = "login";
    public final static String KEY_REGISTER = "register";
    public final static String KEY_BACK = "back";
    public final static String KEY_NEARBY = "nearby";
    public final static String KEY_CATEGORY = "category";
    public final static String KEY_CITY = "city";

    // For Activity List Fragment
    public final static String KEY_ACTIVITY_EMPTY = "activity_empty";

    // For Header
    public final static String KEY_PULL_TO_REFRESH = "pull_to_refresh";
    public final static String KEY_RELEASE_TO_REFRESH = "release_to_refresh";
    public final static String KEY_REFRESHING = "refreshing";
    public final static String KEY_NOT_UPDATED_YET = "not_updated_yet";
    public final static String KEY_UPDATED_JUST_NOW = "updated_just_now_cn";
    public final static String KEY_TIME_ERROR = "time_error";
    public final static String KEY_UPDATED_AT = "updated_at";

    // For TimeUnit
    public final static String KEY_MINUTE = "minute";
    public final static String KEY_HOUR = "hour";
    public final static String KEY_DAY = "day";
    public final static String KEY_MONTH = "month";
    public final static String KEY_YEAR = "year";

    // ActionBar String
    public final static String KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL = "action_bar_string_activity_detail";
    public final static String KEY_ACTION_BAR_STRING_SETTINGS = "action_bar_string_settings";

    // For Settings page
    public final static String KEY_USER_PROFILE_PHOTO_SETTING_LABEL = "settings_user_profile_photo_setting_label";
    public final static String KEY_USER_NICKNAME_SETTING_LABEL = "settings_user_nickname_setting_label";
    public final static String KEY_USER_EMAIL_SETTING_LABEL = "settings_user_email_setting_label";
    public final static String KEY_USER_GENDER_SETTING_LABEL = "settings_user_gender_setting_label";
    public final static String KEY_USER_CITY_SETTING_LABEL = "settings_user_city_setting_label";
    public final static String KEY_SYSTEM_LANGUAGE_SETTING_LABEL = "settings_system_language_setting_label";
    public final static String KEY_SYSTEM_VERSION_SETTING_LABEL = "settings_system_version_setting_label";
    public final static String KEY_SYSTEM_ABOUT_SETTING_LABEL = "settings_system_about_setting_label";
    // For dialog edit text hint
    public final static String KEY_SETTING_NICKNAME_DIALOG_HINT = "settings_nickname_dialog_hint";
    // For gender string
    public final static String KEY_GENDER_MALE = "gender_male";
    public final static String KEY_GENDER_FEMALE = "gender_female";
    // For language showing string
    public final static String KEY_LANGUAGE_SHOWING_STRING = "language_showing_string";

    /**
     * Initialize the language of Logpie
     */
    public static void initialSystemLocale(Context context)
    {
        LogpieSystemSetting setting = LogpieSystemSetting.getInstance(context);
        if (setting.getSystemSetting(KEY_LANGUAGE) == null)
        {
            String mLanguage = Locale.getDefault().getLanguage();
            if (mLanguage.equals(Locale.CHINA) || mLanguage.equals(Locale.CHINESE))
            {
                setting.setSystemSetting(KEY_LANGUAGE, CHINESE);
            }
            else
            {
                setting.setSystemSetting(KEY_LANGUAGE, ENGLISH);
            }
        }
    }

    public static int getId(String key, Context context)
    {

        LogpieSystemSetting setting = LogpieSystemSetting.getInstance(context);
        if (setting.getSystemSetting(KEY_LANGUAGE).equals(CHINESE))
        {
            sIsChinese = true;
        }
        else
        {
            sIsChinese = false;
        }

        switch (key)
        {
        // Login Fragment
        case KEY_WELCOME:
            return switchLanguage(R.string.welcome_cn, R.string.welcome_us);
        case KEY_LOGIN:
            return switchLanguage(R.string.login_cn, R.string.login_us);
        case KEY_REGISTER:
            return switchLanguage(R.string.register_cn, R.string.register_us);
        case KEY_BACK:
            return switchLanguage(R.string.back_cn, R.string.back_us);
        case KEY_EMAIL:
            return switchLanguage(R.string.email_cn, R.string.email_us);
        case KEY_PASSWORD:
            return switchLanguage(R.string.password_cn, R.string.password_us);
        case KEY_CONFIRM_PASSWORD:
            return switchLanguage(R.string.confirm_password_cn, R.string.confirm_password_us);
        case KEY_NICKNAME:
            return switchLanguage(R.string.nickname_cn, R.string.nickname_us);
        case KEY_CITY:
            return switchLanguage(R.string.navigation_city_mode_cn,
                    R.string.navigation_city_mode_us);
        case KEY_CATEGORY:
            return switchLanguage(R.string.navigation_category_mode_cn,
                    R.string.navigation_category_mode_us);
        case KEY_NEARBY:
            return switchLanguage(R.string.navigation_nearby_mode_cn,
                    R.string.navigation_nearby_mode_us);
        case KEY_ACTIVITY_EMPTY:
            return switchLanguage(R.string.activity_empty_cn, R.string.activity_empty_us);

            // Pull to refresh Header
        case KEY_PULL_TO_REFRESH:
            return switchLanguage(R.string.pull_to_refresh_cn, R.string.pull_to_refresh_us);
        case KEY_RELEASE_TO_REFRESH:
            return switchLanguage(R.string.release_to_refresh_cn, R.string.release_to_refresh_us);
        case KEY_REFRESHING:
            return switchLanguage(R.string.refreshing_cn, R.string.refreshing_us);
        case KEY_NOT_UPDATED_YET:
            return switchLanguage(R.string.not_updated_yet_cn, R.string.not_updated_yet_us);
        case KEY_UPDATED_AT:
            return switchLanguage(R.string.updated_at_cn, R.string.updated_at_us);
        case KEY_UPDATED_JUST_NOW:
            return switchLanguage(R.string.updated_just_now_cn, R.string.updated_just_now_us);
        case KEY_TIME_ERROR:
            return switchLanguage(R.string.time_error_cn, R.string.time_error_us);
        case KEY_MINUTE:
            return switchLanguage(R.string.minute_cn, R.string.minute_us);
        case KEY_HOUR:
            return switchLanguage(R.string.hour_cn, R.string.hour_us);
        case KEY_DAY:
            return switchLanguage(R.string.day_cn, R.string.day_us);
        case KEY_MONTH:
            return switchLanguage(R.string.month_cn, R.string.month_us);
        case KEY_YEAR:
            return switchLanguage(R.string.year_cn, R.string.year_us);
            // Action Bar String

            // LogpieActivityDetailActivity
        case KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL:
            return switchLanguage(R.string.action_bar_string_activity_detail_cn,
                    R.string.action_bar_string_activity_detail_us);
            // LogpieSettingsActivity
        case KEY_ACTION_BAR_STRING_SETTINGS:
            return switchLanguage(R.string.action_bar_string_settings_cn,
                    R.string.action_bar_string_settings_us);
            // LogpieSettingsPageLabels:
        case KEY_USER_PROFILE_PHOTO_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_user_profile_photo_cn,
                    R.string.settings_label_user_profile_photo_us);
        case KEY_USER_NICKNAME_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_user_nickname_cn,
                    R.string.settings_label_user_nickname_us);
        case KEY_USER_EMAIL_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_user_email_cn,
                    R.string.settings_label_user_email_us);
        case KEY_USER_GENDER_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_user_gender_cn,
                    R.string.settings_label_user_gender_us);
        case KEY_USER_CITY_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_user_city_cn,
                    R.string.settings_label_user_city_us);
        case KEY_SYSTEM_LANGUAGE_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_system_language_cn,
                    R.string.settings_label_system_language_us);
        case KEY_SYSTEM_VERSION_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_system_version_cn,
                    R.string.settings_label_system_version_us);
        case KEY_SYSTEM_ABOUT_SETTING_LABEL:
            return switchLanguage(R.string.settings_label_system_about_cn,
                    R.string.settings_label_system_about_us);
            // Setting Page nickname Dialog edit text hint
        case KEY_SETTING_NICKNAME_DIALOG_HINT:
            return switchLanguage(R.string.settings_dialog_user_nickname_hint_cn,
                    R.string.settings_dialog_user_nickname_hint_us);
            // For gender string
        case KEY_GENDER_MALE:
            return switchLanguage(R.string.gender_male_cn, R.string.gender_male_us);
        case KEY_GENDER_FEMALE:
            return switchLanguage(R.string.gender_female_cn, R.string.gender_female_us);
            // For language showing string
        case KEY_LANGUAGE_SHOWING_STRING:
            return switchLanguage(R.string.language_showing_string_cn,
                    R.string.language_showing_string_us);
        default:
            LogpieLog.d(TAG, "Failed to find the key when setting the language.");
            return -1;
        }
    }

    public static String getString(String key, Context context)
    {
        return context.getResources().getString(getId(key, context));
    }

    /**
     * Just return resource id
     * 
     * @return
     */
    private static int switchLanguage(int chineseVersion, int englishVersion)
    {
        if (sIsChinese)
        {
            return chineseVersion;
        }
        else
        {
            return englishVersion;
        }
    }
}
