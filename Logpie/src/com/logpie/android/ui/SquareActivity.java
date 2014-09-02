package com.logpie.android.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.logpie.android.R;

public class SquareActivity extends FragmentActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_square_header_container, new TitleFragment()).commit();
        }
    }

}
