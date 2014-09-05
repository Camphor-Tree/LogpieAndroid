package com.logpie.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.logpie.android.R;

public class AuthActivity extends
		FragmentActivity
{
	private static final String TAG = AuthActivity.class
			.getName();

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
