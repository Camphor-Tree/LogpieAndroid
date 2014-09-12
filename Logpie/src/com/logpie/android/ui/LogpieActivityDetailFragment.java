package com.logpie.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.R;
import com.logpie.android.ui.base.LogpieBaseFragment;

public class LogpieActivityDetailFragment extends LogpieBaseFragment
{
    private Activity mActivity;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mActivity = getActivity();
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_activity_detail, parent, false);

        return view;
    }

}
