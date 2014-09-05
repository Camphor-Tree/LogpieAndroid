package com.logpie.android.ui.helper;

import java.util.Locale;

import android.content.Context;

import com.logpie.android.R;
import com.logpie.android.components.LogpieSystemSetting;
import com.logpie.android.util.LogpieLog;

public class LanguageHelper
{
	private final static String TAG = LanguageHelper.class
			.getName();
	private static boolean sIsChinese;

	/**
	 * Key for every Logpie system setting
	 */
	public final static String KEY_LANGUAGE = "com.logpie.android.language";
	public final static String ENGLISH = "english";
	public final static String CHINESE = "chinese";

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

	/**
	 * Initialize the language of Logpie
	 */
	public static void initialSystemLocale(
			Context context)
	{
		LogpieSystemSetting setting = LogpieSystemSetting
				.getInstance(context);
		if (setting
				.getSystemSetting(KEY_LANGUAGE) == null)
		{
			String mLanguage = Locale
					.getDefault().getLanguage();
			if (mLanguage.equals(Locale.CHINA)
					|| mLanguage
							.equals(Locale.CHINESE))
			{
				setting.setSystemSetting(
						KEY_LANGUAGE, CHINESE);
			} else
			{
				setting.setSystemSetting(
						KEY_LANGUAGE, ENGLISH);
			}
		}
	}

	public static int getId(String key,
			Context context)
	{
		LogpieSystemSetting setting = LogpieSystemSetting
				.getInstance(context);
		if (setting
				.getSystemSetting(KEY_LANGUAGE)
				.equals(CHINESE))
		{
			sIsChinese = true;
		} else
		{
			sIsChinese = false;
		}

		switch (key) {
		case KEY_WELCOME:
			return switchLanguage(
					R.string.welcome_cn,
					R.string.welcome_us);
		case KEY_LOGIN:
			return switchLanguage(
					R.string.login_cn,
					R.string.login_us);
		case KEY_REGISTER:
			return switchLanguage(
					R.string.register_cn,
					R.string.register_us);
		case KEY_BACK:
			return switchLanguage(
					R.string.back_cn,
					R.string.back_us);
		case KEY_EMAIL:
			return switchLanguage(
					R.string.email_cn,
					R.string.email_us);
		case KEY_PASSWORD:
			return switchLanguage(
					R.string.password_cn,
					R.string.password_us);
		case KEY_CONFIRM_PASSWORD:
			return switchLanguage(
					R.string.confirm_password_cn,
					R.string.confirm_password_us);
		case KEY_NICKNAME:
			return switchLanguage(
					R.string.nickname_cn,
					R.string.nickname_us);
		case KEY_CITY:
			return switchLanguage(
					R.string.navigation_city_mode_cn,
					R.string.navigation_city_mode_us);
		case KEY_CATEGORY:
			return switchLanguage(
					R.string.navigation_category_mode_cn,
					R.string.navigation_category_mode_us);
		case KEY_NEARBY:
			return switchLanguage(
					R.string.navigation_nearby_mode_cn,
					R.string.navigation_nearby_mode_us);
		default:
			LogpieLog
					.d(TAG,
							"Failed to find the key when setting the language.");
			return -1;
		}
	}

	/**
	 * Just return resource id
	 * 
	 * @return
	 */
	private static int switchLanguage(
			int chineseVersion, int englishVersion)
	{
		if (sIsChinese)
		{
			return chineseVersion;
		} else
		{
			return englishVersion;
		}
	}
}
