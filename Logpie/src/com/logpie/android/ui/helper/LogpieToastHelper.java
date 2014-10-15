package com.logpie.android.ui.helper;

import android.content.Context;
import android.widget.Toast;

import com.logpie.android.util.ThreadHelper;

/**
 * Used to show Android toast message. All the methods will run on MainThread.
 * 
 * @author yilei
 * 
 */
public class LogpieToastHelper
{
    public static void showLongMessage(final Context context, final String message)
    {
        ThreadHelper.runOnMainThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void showShortMessage(final Context context, final String message)
    {
        ThreadHelper.runOnMainThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
