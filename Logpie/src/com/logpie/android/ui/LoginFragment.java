package com.logpie.android.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.components.LogpieSystemSetting;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.ActivityOpenHelper;
import com.logpie.android.ui.helper.LanguageHelper;

public class LoginFragment extends
		LogpieBaseFragment
{
	private final static String TAG = LoginFragment.class
			.getName();

	private ImageView mChinese;
	private ImageView mEnglish;
	private EditText mEmail;
	private EditText mPassword;
	private TextView mRegister;
	private TextView mLogin;

	private Activity mActivity;

	@SuppressLint("NewApi")
	private void changeLanguage(String lan)
	{
		if (lan.equals(LanguageHelper.CHINESE))
		{
			mEnglish.setAlpha(0.3f);
			mChinese.setAlpha(1.0f);
		} else
		{
			mEnglish.setAlpha(1.0f);
			mChinese.setAlpha(0.3f);
		}
		mEmail.setHint(LanguageHelper
				.getId(LanguageHelper.KEY_EMAIL,
						mActivity
								.getApplicationContext()));
		mPassword
				.setHint(LanguageHelper
						.getId(LanguageHelper.KEY_PASSWORD,
								mActivity
										.getApplicationContext()));
		mLogin.setText(LanguageHelper
				.getId(LanguageHelper.KEY_LOGIN,
						mActivity
								.getApplicationContext()));
		mRegister
				.setText(LanguageHelper
						.getId(LanguageHelper.KEY_REGISTER,
								mActivity
										.getApplicationContext()));
	}

	@Override
	public void handleOnCreate(
			Bundle savedInstanceState)
	{
		mActivity = getActivity();
	}

	@Override
	public View handleOnCreateView(
			LayoutInflater inflater,
			ViewGroup parent,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(
				R.layout.fragment_login, parent,
				false);

		mEnglish = (ImageView) v
				.findViewById(R.id.login_language_us);
		mChinese = (ImageView) v
				.findViewById(R.id.login_language_cn);

		// TODO: store to the key-value storage
		mEmail = (EditText) v
				.findViewById(R.id.login_email);
		mPassword = (EditText) v
				.findViewById(R.id.login_password);
		mLogin = (TextView) v
				.findViewById(R.id.login_login_button);
		mRegister = (TextView) v
				.findViewById(R.id.login_register_button);

		mEnglish.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogpieSystemSetting
						.getInstance(mActivity)
						.setSystemSetting(
								LanguageHelper.KEY_LANGUAGE,
								LanguageHelper.ENGLISH);
				changeLanguage(LanguageHelper.ENGLISH);
			}
		});

		mChinese.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				LogpieSystemSetting
						.getInstance(mActivity)
						.setSystemSetting(
								LanguageHelper.KEY_LANGUAGE,
								LanguageHelper.CHINESE);
				changeLanguage(LanguageHelper.CHINESE);
			}
		});

		mLogin.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mEmail.getText() != null
						&& mPassword.getText() != null)
				{
					// TODO add Login Service call
					ActivityOpenHelper
							.openActivityAndFinishPreviousActivity(
									mActivity,
									SquareActivity.class);

				}
			}
		});

		mRegister
				.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						// Create new fragment and transaction
						Fragment fragment = new RegisterFragment();
						FragmentTransaction transaction = getFragmentManager()
								.beginTransaction();

						// Replace whatever is in the fragment_container view
						// with this fragment
						transaction.replace(
								R.id.container,
								fragment);

						// Commit the transaction
						transaction.commit();
					}
				});
		return v;
	}
}
