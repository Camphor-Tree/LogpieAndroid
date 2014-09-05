package com.logpie.android.ui.operation;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;

import com.logpie.android.ui.SquareActivity.LogpieBaseTabListener;

/**
 * This class is used to describe the high-level algorithm about what
 * BaseSquareMode switch should do. The implementation should be in sub-class
 * 
 * @author yilei
 * 
 */
public abstract class BaseSquareMode
{
	protected String mTabIdentifier;
	protected ActionBar mActionBar;
	protected Class mFragmentClass;
	protected Tab mTab;
	protected ActionBarActivity mActivity;

	// High-level algorithm. This is template pattern.
	public void switchToCurrentMode()
	{
		popupWindowIfNecessary();
		changeTitleIcon();
		refreshListView();
	}

	protected abstract void popupWindowIfNecessary();

	protected abstract void changeTitleIcon();

	protected abstract void refreshListView();

	/**
	 * Return the resource text to set to the corresponding tab
	 */
	public abstract int getTabStringResource();

	protected ActionBar getActionBar()
	{
		return mActionBar;
	}

	public String getTabIdentifier()
	{
		return mTabIdentifier;
	}

	public Class getFragmentClass()
	{
		return mFragmentClass;
	}

	public void setupTab()
	{
		mTab = mActionBar.newTab();
		mTab.setTabListener(new LogpieBaseTabListener(
				mActivity, getTabIdentifier(),
				getFragmentClass()));
		mTab.setText(getTabStringResource());
		mActionBar.addTab(mTab);
	}

}
