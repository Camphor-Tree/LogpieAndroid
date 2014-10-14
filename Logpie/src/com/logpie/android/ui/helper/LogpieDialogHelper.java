package com.logpie.android.ui.helper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.logpie.android.util.LogpieDateTime;

/**
 * This class is used to help build the diaglogs in Logpie.
 * 
 * @author yilei
 * 
 */
public class LogpieDialogHelper
{
    // This is used to distinguish the all these dialog fragment (Like city
    // picker dialog, category picker dialog)
    public static final int REQUEST_CODE_CITY_DIALOG = 0;
    public static final int REQUEST_CODE_CATEGORY_DIALOG = 1;
    public static final String KEY_CITY_PICKER_DIALOG = "city_picker_dialog";
    public static final String KEY_CATEGORY_PICKER_DIALOG = "category_picker_dialog";

    private static final int YEAR_1900 = 1900;

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
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                callback.onSelect(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
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

    public interface LogpieDatePickerDialogCallback
    {
        void onSelectDate(int year, int monthOfYear, int dayOfMonth);

        void onCancel();
    }

    private static class LogpieDatePickerDialogCallbackAdapter implements OnDateSetListener
    {
        private LogpieDatePickerDialogCallback mCallback;

        LogpieDatePickerDialogCallbackAdapter(LogpieDatePickerDialogCallback callback)
        {
            mCallback = callback;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            if (mCallback != null)
            {
                mCallback.onSelectDate(year, monthOfYear, dayOfMonth);
            }
        }
    }

    public static void openDatePickerDialog(final Context context,
            final LogpieDateTime initialDate, final LogpieDatePickerDialogCallback callback)
    {
        int year = initialDate.getYear();
        int month = initialDate.getMonth();
        int day = initialDate.getDay();
        OnDateSetListener listener = new LogpieDatePickerDialogCallbackAdapter(callback);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, 0, listener, year, month,
                day);
        setTitleAndShow(context, LanguageHelper.KEY_DATE_PICKER_DIALOG_TITLE_STRING,
                datePickerDialog);
    }

    public interface LogpieTimePickerDialogCallback
    {
        void onSelectTime(int hour, int minute);

        void onCancel();
    }

    private static class LogpieTimePickerDialogCallbackAdapter implements OnTimeSetListener
    {
        private LogpieTimePickerDialogCallback mCallback;

        LogpieTimePickerDialogCallbackAdapter(LogpieTimePickerDialogCallback callback)
        {
            mCallback = callback;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            if (mCallback != null)
            {
                mCallback.onSelectTime(hourOfDay, minute);
            }

        }
    }

    public static void openTimePickerDialog(final Context context,
            final LogpieDateTime initialDate, final LogpieTimePickerDialogCallback callback)
    {
        int hour = initialDate.getHour();
        int minute = initialDate.getMinute();

        OnTimeSetListener listener = new LogpieTimePickerDialogCallbackAdapter(callback);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, listener, hour, minute,
                true);
        setTitleAndShow(context, LanguageHelper.KEY_TIME_PICKER_DIALOG_TITLE_STRING,
                timePickerDialog);
    }

    private static void setTitleAndShow(final Context context, final String languageKey,
            Dialog timePickerDialog)
    {
        // Set the dialog title
        timePickerDialog.setTitle(LanguageHelper.getString(languageKey, context));
        timePickerDialog.show();
    }
}
