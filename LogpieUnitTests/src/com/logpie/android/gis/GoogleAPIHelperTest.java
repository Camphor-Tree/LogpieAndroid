package com.logpie.android.gis;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public class GoogleAPIHelperTest extends AndroidTestCase
{
    public void testGetCityFromLatLonFromGoogleAPI()
    {
        // test ±±¾©
        Double lat1 = 39.983424;
        Double lon1 = 116.322987;
        String city = GoogleAPIHelper.getCityFromLatLon(lat1, lon1);
        Assert.assertEquals(city, "Beijing");

        // test Î÷ÑÅÍ¼ should return null
        Double lat2 = 47.6168725;
        Double lon2 = -122.3453397;
        String city2 = GoogleAPIHelper.getCityFromLatLon(lat2, lon2);
        Assert.assertEquals(city2, "Seattle");
    }
}
