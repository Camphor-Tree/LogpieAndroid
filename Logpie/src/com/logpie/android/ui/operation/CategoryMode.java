package com.logpie.android.ui.operation;

import android.support.v7.app.ActionBarActivity;

import com.logpie.android.ui.ActivityViewerFragment;
import com.logpie.android.ui.helper.LanguageHelper;

public class CategoryMode extends BaseSquareMode
{
    private static final String sTabIdentifier = "Catory";

    public CategoryMode(ActionBarActivity activity)
    {
        mActivity = activity;
        mActionBar = activity.getSupportActionBar();
        mFragmentClass = ActivityViewerFragment.class;
        mTabIdtentifier = sTabIdentifier;
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
        return LanguageHelper.getCategoryModeTabString();
    }
}
