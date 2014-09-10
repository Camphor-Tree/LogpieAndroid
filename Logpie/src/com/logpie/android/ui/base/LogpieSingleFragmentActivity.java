package com.logpie.android.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.logpie.android.R;

public abstract class LogpieSingleFragmentActivity extends FragmentActivity
{
    /**
     * Subclass should implement this abstract function to create it own
     * fragment
     * 
     * @return
     */
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null)
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.container, fragment).commit();
        }
    }

}
