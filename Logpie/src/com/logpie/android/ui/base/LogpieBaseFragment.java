package com.logpie.android.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logpie.android.util.LogpieLog;

/**
 * This is logpie base fragment template. Fragment should extends this class to
 * ensure it will handle the onCreate and OnCreateView
 * 
 * @author yilei
 * 
 */
public abstract class LogpieBaseFragment extends Fragment
{
    private static final String TAG = LogpieBaseFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // call subclass' implementation
        handleOnCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup parent, Bundle savedInstanceState)
    {
        if (inflator == null || parent == null)
        {
            LogpieLog.e(TAG, "Inflator or ViewGroup is null! It is impossible.");
            return null;
        }
        // Safely call subclass' implementation
        return handleOnCreateView(inflator, parent, savedInstanceState);
    }

    /**
     * Sub-class must handle onCreate()
     * 
     * @param savedInstanceState
     */
    public abstract void handleOnCreate(Bundle savedInstanceState);

    /**
     * Sub-class must handle onCreateView()
     * 
     * @param inflator
     * @param parent
     * @param savedInstanceState
     * @return
     */
    public abstract View handleOnCreateView(LayoutInflater inflator, ViewGroup parent,
            Bundle savedInstanceState);

}
