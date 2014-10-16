package com.logpie.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.util.LogpieLog;

public class PhotoPickerDialog extends DialogFragment
{
    private static final String TAG = PhotoPickerDialog.class.getName();

    public static final String SERVICE_TAKE_PHOTO = "take_photo";
    public static final String SERVICE_OPEN_GALLERY = "open_gallery";

    private String[] items = new String[] {
            LanguageHelper.getString(LanguageHelper.KEY_TAKE_PHOTO, getActivity()),
            LanguageHelper.getString(LanguageHelper.KEY_FROM_GALLERY, getActivity()) };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(LanguageHelper.getId(LanguageHelper.KEY_PHOTO_PICKER_TITLE, getActivity()));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }

        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        return builder.create();
    }

    private void sendResult(int resultCode, int position)
    {
        if (getTargetFragment() == null)
        {
            LogpieLog.d(TAG, "target fragment is null.");
            return;
        }

        Intent i = new Intent();
        if (position == 0)
        {

        }
        else if (position == 1)
        {

        }
        else
        {
            LogpieLog.e(TAG, "Failed to get the item.");
            return;
        }
    }
}
