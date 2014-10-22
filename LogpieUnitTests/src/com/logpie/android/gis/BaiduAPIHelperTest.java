package com.logpie.android.gis;

import junit.framework.Assert;
import android.test.AndroidTestCase;

import com.logpie.android.logic.LogpieLocation;

public class BaiduAPIHelperTest extends AndroidTestCase
{
    public void testGetCityFromLatLon()
    {
        // test ����
        Double lat1 = 39.983424;
        Double lon1 = 116.322987;
        String city = BaiduAPIHelper.getCityFromLatLon(lat1, lon1);
        Assert.assertEquals(city, "������");

        // test ����ͼ should return null
        Double lat2 = 47.6168725;
        Double lon2 = -122.3453397;
        String city2 = BaiduAPIHelper.getCityFromLatLon(lat2, lon2);
        Assert.assertNull(city2);
    }

    public void testGetLatLonFromAddressAndCity()
    {
        String address = "������ѧ";
        String city = "����";
        LogpieLocation location = BaiduAPIHelper.getLatLonFromAddressAndCity(getContext(), address,
                city);
        assertNotNull(location);
        assertEquals(location.getLongitude(), 120.62989618293);
        assertEquals(location.getLatitude(), 31.302686885837);
        assertEquals(location.getCity(), city);
        assertEquals(location.getAddress(), address);
    }
}
