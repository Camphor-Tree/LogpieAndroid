package com.logpie.android.util.test;

import junit.framework.Assert;

import com.logpie.android.connection.EndPoint;
import com.logpie.android.connection.EndPoint.ServiceURL;

import android.test.AndroidTestCase;

public class EndPointTests extends AndroidTestCase {
	
	public void testGetServiceUrlByName()
	{
		ServiceURL serviceURL = EndPoint.getServiceUrlByName("RocketService");
		Assert.assertTrue(serviceURL.equals(ServiceURL.RocektService));
		ServiceURL serviceURL2 = EndPoint.getServiceUrlByName("BadService");
		Assert.assertNull(serviceURL2);
	}
	
	public void testGetServiceUrlByCondition()
	{
		ServiceURL serviceURL = EndPoint.getServiceUrlByCondition("RocketService","test","1.0.0");
		Assert.assertTrue(serviceURL.equals(ServiceURL.RocektService));
		ServiceURL serviceURL2 = EndPoint.getServiceUrlByCondition("RocketService","test","10.0.0");
		Assert.assertNull(serviceURL2);
	}

}
