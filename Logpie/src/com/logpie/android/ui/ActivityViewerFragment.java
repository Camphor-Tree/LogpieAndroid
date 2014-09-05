package com.logpie.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.R;
import com.logpie.android.model.Title;
import com.logpie.android.ui.base.LogpieBaseFragment;

/**
 * Show the title part of the Square Page.
 * 
 * @author yilei
 * 
 */
public class ActivityViewerFragment extends LogpieBaseFragment
{
    private Title mTitle;

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mTitle = new Title();
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_activity_viewer, parent, false);
        return v;
    }
}