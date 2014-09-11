package com.logpie.android.logic;

import android.os.Parcel;
import android.os.Parcelable;

import com.logpie.android.gis.GisAPIHelper;
import com.logpie.android.util.LogpieLog;

/**
 * Make LogpieLocation parcelable, so that it can be pass in the intent.
 * 
 * @author yilei
 * 
 */
public class LogpieLocation implements Parcelable
{
    private static String TAG = LogpieLocation.class.getName();
    private Double mLatitude;
    private Double mLongitude;
    private String mAddress;
    private String mCity;

    public LogpieLocation(Double lat, Double lon)
    {
        this(lat, lon, null, null);
    }

    public LogpieLocation(String address)
    {
        this(null, null, address, null);
    }

    public LogpieLocation(Double lat, Double lon, String address)
    {
        this(lat, lon, address, null);
    }

    public LogpieLocation(Double lat, Double lon, String address, String city)
    {
        mLatitude = lat;
        mLongitude = lon;
        mAddress = address;
        mCity = city;
    }

    public void setCity(String city)
    {
        mCity = city;
    }

    public String getCity()
    {
        return mCity;
    }

    /**
     * @return the latitude
     */
    public double getLatitude()
    {
        return mLatitude;
    }

    /**
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(double latitude)
    {
        mLatitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude()
    {
        return mLongitude;
    }

    /**
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(double longitude)
    {
        mLongitude = longitude;
    }

    /**
     * @return the address
     */
    public String getAddress()
    {
        return mAddress;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(String address)
    {
        mAddress = address;
    }

    public String getCurrentCity()
    {
        if (mLatitude != null && mLongitude != null)
        {
            LogpieLog.d(TAG, "Output latitude & longitude: " + mLatitude + ", " + "mLongitude");
            return GisAPIHelper.getCityFromLatLon(mLatitude, mLongitude);
        }
        else
        {
            return null;
        }
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Parcelable.Creator<LogpieLocation> CREATOR = new Parcelable.Creator<LogpieLocation>()
    {
        public LogpieLocation createFromParcel(Parcel in)
        {
            return new LogpieLocation(in);
        }

        public LogpieLocation[] newArray(int size)
        {
            return new LogpieLocation[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeDouble(mLatitude.doubleValue());
        dest.writeDouble(mLongitude.doubleValue());
        dest.writeString(mAddress);
        dest.writeString(mCity);
    }

    private LogpieLocation(Parcel in)
    {
        mLatitude = Double.valueOf(in.readDouble());
        mLongitude = Double.valueOf(in.readDouble());
        mAddress = in.readString();
        mCity = in.readString();
    }
}
