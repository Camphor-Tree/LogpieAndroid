package com.logpie.android.components;

import android.app.Application;
import android.content.Context;

import com.logpie.android.datastorage.DataPlatform;
import com.logpie.android.ui.helper.LanguageHelper;

public class LogpieApplication extends
		Application
{
	Context mContext;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mContext = getApplicationContext();
		logpieInit();
	}

	private void logpieInit()
	{
		DataPlatform dataPlatform = DataPlatform
				.getInstance(mContext);
		LogpieSystemSetting setting = LogpieSystemSetting
				.getInstance(mContext);
		setting.initialize();
		LanguageHelper
				.initialSystemLocale(mContext);
	}
}
