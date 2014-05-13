package com.logpie.android.logic;

public class LogpieLocation
{
    private Double mLatitude;
    private Double mLongitude;
    private String mAddress;

    public LogpieLocation(Double lat, Double lon)
    {
        this(lat, lon, null);
    }

    public LogpieLocation(String address)
    {
        this(null, null, address);
    }

    public LogpieLocation(Double lat, Double lon, String address)
    {
        mLatitude = lat;
        mLongitude = lon;
        mAddress = address;
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

        return "";
    }
}
