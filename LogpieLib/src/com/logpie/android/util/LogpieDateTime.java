package com.logpie.android.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a wrapper for java.util.Date class. It is designed to work for
 * LogpieActivity: start time, end time, create time.
 * 
 * @author yilei
 * 
 */
public class LogpieDateTime implements Parcelable
{
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private Date mDate;
    private DateFormat mDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    // Here we must use "HH" to enable 24-hour format. If "hh", then it will
    // just use 12-hour format.
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    /**
     * Build the default LogpieDateTime using current date and time.
     */
    public LogpieDateTime()
    {
        mDate = new Date();
        mYear = mDate.getYear() + 1900;
        mMonth = mDate.getMonth();
        mDay = mDate.getDate();
        mHour = mDate.getHours();
        mMinute = mDate.getMinutes();
    }

    public LogpieDateTime(Date date)
    {
        mDate = date;
        mYear = date.getYear() + 1900;
        mMonth = date.getMonth();
        mDay = date.getDate();
        mHour = date.getHours();
        mMinute = date.getMinutes();
    }

    public LogpieDateTime(int year, int month, int day, int hour, int minute)
    {
        // The year in OnDateSet is the actual year, like:2014. But the
        // constructor for the Date is actually the year from 1900. So need
        // to substract 1900 to get the number.
        this(new Date(year - 1900, month, day));
    }

    public synchronized void setTime(int hour, int minute)
    {
        mHour = hour;
        mMinute = minute;
        mDate.setHours(hour);
        mDate.setMinutes(minute);
    }

    public synchronized void setDate(int year, int month, int day)
    {
        mYear = year;
        mMonth = month;
        mDay = day;
        mDate.setYear(year - 1900);
        mDate.setMonth(month);
        mDate.setDate(day);
    }

    public synchronized void setLogpieDateTime(final LogpieDateTime dateTime)
    {
        mYear = dateTime.getYear();
        mMonth = dateTime.getMonth();
        mDay = dateTime.getDay();
        mHour = dateTime.getHour();
        mMinute = dateTime.getMinute();
        mDate.setYear(mYear - 1900);
        mDate.setMonth(mMonth);
        mDate.setDate(mDay);
        mDate.setHours(mHour);
        mDate.setMinutes(mMinute);
    }

    public boolean after(LogpieDateTime dateTime)
    {
        if (dateTime == null || mDate == null)
        {
            throw new IllegalArgumentException("dateTime used to cpmpare can not be null!");
        }
        return mDate.getTime() > dateTime.getDate().getTime();
    }

    public boolean before(LogpieDateTime dateTime)
    {
        if (dateTime == null || mDate == null)
        {
            throw new IllegalArgumentException("dateTime used to cpmpare can not be null!");
        }
        return mDate.getTime() < dateTime.getDate().getTime();
    }

    public int getHour()
    {
        return mHour;
    }

    public int getMinute()
    {
        return mMinute;
    }

    public Date getDate()
    {
        return mDate;
    }

    public int getYear()
    {
        return mYear;
    }

    public int getMonth()
    {
        return mMonth;
    }

    public int getDay()
    {
        return mDay;
    }

    public String getDateString()
    {
        return mDateFormat.format(mDate);
    }

    public String getTimeString()
    {
        return mTimeFormat.format(mDate);
    }

    public String getDateTimeString()
    {
        return mDateFormat.format(mDate) + " " + mTimeFormat.format(mDate);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<LogpieDateTime> CREATOR = new Parcelable.Creator<LogpieDateTime>()
    {
        public LogpieDateTime createFromParcel(Parcel in)
        {
            return new LogpieDateTime(in);
        }

        public LogpieDateTime[] newArray(int size)
        {
            return new LogpieDateTime[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mYear);
        dest.writeInt(mMonth);
        dest.writeInt(mDay);
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeSerializable(mDate);
    }

    private LogpieDateTime(Parcel in)
    {
        mYear = in.readInt();
        mMonth = in.readInt();
        mDay = in.readInt();
        mHour = in.readInt();
        mMinute = in.readInt();
        mDate = (Date) in.readSerializable();
    }
}
