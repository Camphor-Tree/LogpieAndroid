package com.logpie.android.ui.helper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.widget.EditText;

/**
 * This class is used to help build the diaglogs in Logpie.
 * 
 * @author yilei
 * 
 */
public class LogpieDialogHelper
{
    public interface LogpieEditTextDialogCallback
    {
        void onSelect(final String text);

        void onCancel();
    }

    /**
     * Build the edit text dialog. This is quite commonly used in the settings
     * page.
     * 
     * @param context
     * @param title
     * @param hint
     * @param callback
     */
    public static void openEditTextDialog(final Context context, final String title,
            final String hint, final LogpieEditTextDialogCallback callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        // Set up the input
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(hint);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                callback.onSelect(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                callback.onCancel();
            }
        });
        builder.show();
    }

    public interface LogpiePickerDialogCallback
    {
        void onSelect(final int n);

        void onCancel();
    }

    /**
     * Build the edit text dialog. This is quite commonly used in the settings
     * page.
     * 
     * @param context
     * @param title
     * @param hint
     * @param callback
     */
    public static void openPickerDialog(final Context context, final String title,
            final String[] types, final LogpiePickerDialogCallback callback)
    {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setItems(types, new OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                callback.onSelect(which);
            }

        });
        builder.show();
    }
}
