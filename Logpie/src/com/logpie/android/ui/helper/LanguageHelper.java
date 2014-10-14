package com.logpie.android.ui.helper;

import java.util.Locale;

import android.content.Context;

import com.logpie.android.R;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.logic.CategoryManager;
import com.logpie.android.util.LogpieLog;

/**
 * This class is used to get the UI string based on the system setting for
 * language. The format shoule be:
 * LanguageHelper.getString(LanguageHelper.KEY_XXXXX, context);
 * 
 * @author yilei
 * 
 */
public class LanguageHelper
{
    private final static String TAG = LanguageHelper.class.getName();
    public static boolean sIsChinese;

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
    public final static String KEY_ACTION_BAR_STRING_CREATE_ACTIVITY = "action_bar_string_create_activity";

    // For Settings Page
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

    // Dialog
    public final static String KEY_CITY_PICKER_TITLE = "city_picker_title";
    public final static String KEY_CATEGORY_PICKER_TITLE = "category_picker_title";
    public final static String KEY_CATEGORY_GROUP = "category_group";
    public final static String KEY_SUBCATEGORY_GROUP = "subcategory_group";

    // Common Button
    public final static String KEY_BUTTON_CANCEL = "button_cancel";
    public final static String KEY_BUTTON_OK = "button_ok";
    // For Date Picker dialog title string
    public final static String KEY_DATE_PICKER_DIALOG_TITLE_STRING = "date_picker_dialog_title_string";
    public final static String KEY_TIME_PICKER_DIALOG_TITLE_STRING = "time_picker_dialog_title_string";
    // For CreateActivity Page
    public final static String KEY_CREATE_ACTIVTY_DESCRIPTION_HINT = "create_activity_description_hint";
    public final static String KEY_CREATE_ACTIVTY_ADDRESS_HINT = "create_activity_address_hint";
    public final static String KEY_CREATE_ACTIVTY_START_TIME_LABEL = "create_activity_start_time_label";
    public final static String KEY_CREATE_ACTIVTY_END_TIME_LABEL = "create_activity_end_time_label";
    public final static String KEY_CREATE_ACTIVTY_CITY_LABEL = "create_activity_city_label";
    public final static String KEY_CREATE_ACTIVTY_CATEGORY_LABEL = "create_activity_category_label";

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

            // Logpie Activity Detail Activity
        case KEY_ACTION_BAR_STRING_ACTIVITY_DETAIL:
            return switchLanguage(R.string.action_bar_string_activity_detail_cn,
                    R.string.action_bar_string_activity_detail_us);

            // Logpie Settings Activity
        case KEY_ACTION_BAR_STRING_SETTINGS:
            return switchLanguage(R.string.action_bar_string_settings_cn,
                    R.string.action_bar_string_settings_us);
        case KEY_ACTION_BAR_STRING_CREATE_ACTIVITY:
            return switchLanguage(R.string.action_bar_string_create_activity_cn,
                    R.string.action_bar_string_create_activity_us);
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

            // Picker Title
        case KEY_CITY_PICKER_TITLE:
            return switchLanguage(R.string.title_city_picker_cn, R.string.title_city_picker_us);
        case KEY_CATEGORY_PICKER_TITLE:
            return switchLanguage(R.string.title_category_picker_cn,
                    R.string.title_category_picker_us);

            // Common Buttons
        case KEY_BUTTON_CANCEL:
            return switchLanguage(R.string.button_cancel_cn, R.string.button_cancel_us);
        case KEY_BUTTON_OK:
            return switchLanguage(R.string.button_ok_cn, R.string.button_ok_us);
            // For Date Picker title string
        case KEY_DATE_PICKER_DIALOG_TITLE_STRING:
            return switchLanguage(R.string.date_picker_title_string_cn,
                    R.string.date_picker_title_string_us);
        case KEY_TIME_PICKER_DIALOG_TITLE_STRING:
            return switchLanguage(R.string.time_picker_title_string_cn,
                    R.string.time_picker_title_string_us);
        case KEY_CREATE_ACTIVTY_DESCRIPTION_HINT:
            return switchLanguage(R.string.create_activity_description_hint_cn,
                    R.string.create_activity_description_hint_us);
        case KEY_CREATE_ACTIVTY_ADDRESS_HINT:
            return switchLanguage(R.string.create_activity_address_hint_cn,
                    R.string.create_activity_address_hint_us);
        case KEY_CREATE_ACTIVTY_START_TIME_LABEL:
            return switchLanguage(R.string.create_activity_start_time_label_cn,
                    R.string.create_activity_start_time_label_us);
        case KEY_CREATE_ACTIVTY_END_TIME_LABEL:
            return switchLanguage(R.string.create_activity_end_time_label_cn,
                    R.string.create_activity_end_time_label_us);
        case KEY_CREATE_ACTIVTY_CITY_LABEL:
            return switchLanguage(R.string.create_activity_city_label_cn,
                    R.string.create_activity_city_label_us);
        case KEY_CREATE_ACTIVTY_CATEGORY_LABEL:
            return switchLanguage(R.string.create_activity_category_label_cn,
                    R.string.create_activity_category_label_us);

        default:
            LogpieLog.d(TAG, "Failed to find the key when setting the language.");
            return -1;
        }
    }

    public static String getString(String key, Context context)
    {
        switch (key)
        {
        case KEY_CATEGORY_GROUP:
            if (sIsChinese)
            {
                return CategoryManager.KEY_CATEGORY_GROUP_CN;
            }
            else
            {
                return CategoryManager.KEY_CATEGORY_GROUP_US;
            }
        case KEY_SUBCATEGORY_GROUP:
            if (sIsChinese)
            {
                return CategoryManager.KEY_SUBCATEGORY_GROUP_CN;
            }
            else
            {
                return CategoryManager.KEY_SUBCATEGORY_GROUP_US;
            }
        }
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
