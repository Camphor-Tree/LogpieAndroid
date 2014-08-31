package com.logpie.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.logpie.android.datastorage.CentralDataService;
import com.logpie.android.datastorage.DataServiceCaller;
import com.logpie.android.datastorage.KeyValueStorage;
import com.logpie.android.datastorage.SQLStorage;
import com.logpie.android.exception.ThreadException;
import com.logpie.android.util.LogpieLog;

public class MainActivity extends
		FragmentActivity
{
	private static final String TAG = MainActivity.class
			.getName();
	CentralDataService mDataService;
	DataServiceCaller mServiceCaller;

	@Override
	protected void onStart()
	{
		super.onStart();
		mServiceCaller = new DataServiceCaller(
				this);
		try
		{
			mServiceCaller
					.asyncConnectDataService();
		} catch (ThreadException e)
		{
			LogpieLog
					.e(TAG,
							"cannot bind to save because of thread pool is full");
		}
		Context context = getBaseContext();
		SQLStorage sqlStorage = SQLStorage
				.getInstance(context);
		sqlStorage.initialize();
		KeyValueStorage kvStorage = KeyValueStorage
				.getInstance(context);
		kvStorage.initialize();
	}

	@Override
	protected void onCreate(
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm
				.findFragmentById(R.id.container);

		if (fragment == null)
		{
			fragment = new LoginFragment();
			fm.beginTransaction()
					.add(R.id.container, fragment)
					.commit();
		}
	}

}
