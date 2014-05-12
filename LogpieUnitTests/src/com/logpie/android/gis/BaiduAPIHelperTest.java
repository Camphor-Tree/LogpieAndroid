package com.logpie.android.gis;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public class BaiduAPIHelperTest extends AndroidTestCase
{
    public void testGetCityFromLatLon()
    {
        // test 北京
        Double lat1 = 39.983424;
        Double lon1 = 116.322987;
        String city = BaiduAPIHelper.getCityFromLatLon(lat1, lon1);
        Assert.assertEquals(city, "北京市");

        // test 西雅图 should return null
        Double lat2 = 47.6168725;
        Double lon2 = -122.3453397;
        String city2 = BaiduAPIHelper.getCityFromLatLon(lat2, lon2);
        Assert.assertNull(city2);
    }
}
