package com.logpie.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.logpie.android.datastorage.KeyValueStorage;
import com.logpie.android.datastorage.SQLStorage;

public class MainActivity extends FragmentActivity
{
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onStart()
    {
        super.onStart();

        Context context = getBaseContext();
        SQLStorage sqlStorage = SQLStorage.getInstance(context);
        sqlStorage.initialize();
        KeyValueStorage kvStorage = KeyValueStorage.getInstance(context);
        kvStorage.initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);

        if (fragment == null)
        {
            fragment = new LoginFragment();
            fm.beginTransaction().add(R.id.container, fragment).commit();
        }
    }

}
