package com.logpie.android.ui.helper;

import android.content.Context;
import android.widget.Toast;

public class LogpieToastHelper
{
    public static void showLongMessage(final Context context, final String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showShortMessage(final Context context, final String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
