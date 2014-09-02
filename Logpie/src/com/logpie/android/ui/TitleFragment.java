package com.logpie.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.R;
import com.logpie.android.model.Title;

/**
 * Show the title part of the Square Page.
 * 
 * @author yilei
 * 
 */
public class TitleFragment extends Fragment
{
    private Title mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mTitle = new Title();
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_title, parent, false);
        return v;
    }
}
