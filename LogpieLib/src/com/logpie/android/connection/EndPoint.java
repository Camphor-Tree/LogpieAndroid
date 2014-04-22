package com.logpie.android.connection;

import java.net.MalformedURLException;
import java.net.URL;

import android.text.TextUtils;

import com.logpie.android.util.LogpieLog;

public class EndPoint {
	private static String TAG = EndPoint.class.getName();
	public enum ServiceURL
	{
		RocektService("RocketService","http://10.0.0.6:8080/RocketService/servlet","1.0.0","test",true,false);
		
	    String mServiceName;
		String mUrl;
		String mVersion;
		String mEnvironment;
		boolean mDoOutput;
		boolean mDoInput;
		
        ServiceURL(String name, String url, String version, String environment, boolean doOutput, boolean doInput)
        {
    	    mServiceName = name;
    	    mUrl = url;
    	    mVersion = version;
    	    mEnvironment = environment;
    	    mDoOutput = doOutput;
    	    mDoInput = doInput;
        }
        
        public URL getURL()
        {
        	try {
				return new URL(mUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				LogpieLog.e(TAG, e.getMessage());
			}
			return null;
        }
        
        public boolean needDoOutput()
        {
        	return mDoOutput;
        }

        public boolean needDoInput()
        {
        	return mDoInput;
        }
        
        public String getServiceName()
        {
        	return mServiceName;
        }
	}
	
    public static ServiceURL getServiceUrlByName(String name)
    {
    	for(ServiceURL serviceUrl : ServiceURL.values())
    	{
    		if(TextUtils.equals(name, serviceUrl.getServiceName()))
    			return serviceUrl;
    	}
    	return null;
    }
}
