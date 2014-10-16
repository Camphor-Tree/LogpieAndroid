package com.logpie.android.logic;

import android.content.Context;
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
    private Context mContext;
    private Double mLatitude;
    private Double mLongitude;
    private String mAddress;
    private String mCity;
    private String mCityId;

    public LogpieLocation(Context context)
    {
        mContext = context;
    }

    public LogpieLocation(Context context, String city)
    {
        mContext = context;
        setCity(city);
    }

    public LogpieLocation(Context context, Double lat, Double lon, String address, String city)
    {
        mContext = context;
        mLatitude = lat;
        mLongitude = lon;
        mAddress = address;
        setCity(city);
    }

    public void setCity(String city)
    {
        mCity = city;
        mCityId = getCityId(city);
    }

    public String getCity()
    {
        if (mCity == null)
        {
            if (mAddress == null)
            {
                LogpieLog.e(TAG, "Cannot get the address from Logpie Location.");
                return null;
            }
            /*
             * LogpieLocation loc =
             * BaiduAPIHelper.getLatLonFromAddressAndCity(mAddress, null); mCity
             * = BaiduAPIHelper.getCityFromLatLon(loc.getLatitude(),
             * loc.getLongitude());
             */
        }
        return mCity;
    }

    /**
     * @return the latitude
     */
    public Double getLatitude()
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
    public Double getLongitude()
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

    public String getCityId()
    {
        return mCityId;
    }

    public void setCityId(String cityId)
    {
        mCityId = cityId;
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

    public String getCityById(String cityId)
    {
        String city = CityManager.getInstance(mContext).getCityById(cityId);
        if (city == null || city.equals(""))
        {
            LogpieLog.e(TAG, "Cannot get the city name by using id " + cityId);
        }
        return city;
    }

    public String getCityId(String city)
    {
        String cityId = CityManager.getInstance(mContext).getCityId(city);
        if (cityId == null || cityId.equals(""))
        {
            LogpieLog.e(TAG, "Cannot get the city id by using the name " + city);
        }
        return cityId;
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
        dest.writeString(mCityId);
    }

    private LogpieLocation(Parcel in)
    {
        mLatitude = Double.valueOf(in.readDouble());
        mLongitude = Double.valueOf(in.readDouble());
        mAddress = in.readString();
        mCity = in.readString();
        mCityId = in.readString();
    }
}
