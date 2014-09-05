package com.logpie.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.R;
import com.logpie.android.ui.base.LogpieBaseFragment;

/**
 * Show the title part of the Square Page.
 * 
 * @author yilei
 * 
 */
public class ActivityListFragment extends
		LogpieBaseFragment
{

	@Override
	public void handleOnCreate(
			Bundle savedInstanceState)
	{

	}

	@Override
	public View handleOnCreateView(
			LayoutInflater inflater,
			ViewGroup parent,
			Bundle savedInstanceState)
	{
		View v = inflater
				.inflate(
						R.layout.fragment_activity_viewer,
						parent, false);
		return v;
	}
}
