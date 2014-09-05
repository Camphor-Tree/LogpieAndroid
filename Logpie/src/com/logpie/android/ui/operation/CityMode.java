package com.logpie.android.ui.operation;

import android.support.v7.app.ActionBarActivity;

import com.logpie.android.ui.ActivityListFragment;
import com.logpie.android.ui.helper.LanguageHelper;

public class CityMode extends BaseSquareMode
{
	private static final String sTabIdentifier = "City";

	public CityMode(ActionBarActivity activity)
	{
		mActivity = activity;
		mActionBar = activity
				.getSupportActionBar();
		mFragmentClass = ActivityListFragment.class;
		mTabIdentifier = sTabIdentifier;
	}

	@Override
	protected void changeTitleIcon()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshListView()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void popupWindowIfNecessary()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int getTabStringResource()
	{
		return LanguageHelper
				.getId(LanguageHelper.KEY_CITY,
						mActivity
								.getApplicationContext());
	}

}
