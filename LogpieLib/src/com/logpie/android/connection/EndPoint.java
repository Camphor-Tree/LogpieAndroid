package com.logpie.android.connection;

import java.net.MalformedURLException;
import java.net.URL;

import android.text.TextUtils;

import com.logpie.android.util.LogpieLog;

public class EndPoint
{
    private static String TAG = EndPoint.class.getName();

    public enum ServiceURL
    {
        RocektService("RocketService", "https://10.0.0.6:8443/RocketService/servlet", "1.0.0",
                "test", true, false),

        AuthenticationService("RocketService",
                "https://10.0.0.6:8443/AuthenticationService/server", "1.0.0", "test", true, true);

        String mServiceName;
        String mUrl;
        String mVersion;
        String mEnvironment;
        boolean mDoOutput;
        boolean mDoInput;

        ServiceURL(String name, String url, String version, String environment, boolean doOutput,
                boolean doInput)
        {
            mServiceName = name;
            mUrl = url;
            mVersion = version;
            mEnvironment = environment;
            mDoOutput = doOutput;
            mDoInput = doInput;
        }

        public String getVersion()
        {
            return mVersion;
        }

        public String getEnvironment()
        {
            return mEnvironment;
        }

        public boolean isDoOutput()
        {
            return mDoOutput;
        }

        public boolean isDoInput()
        {
            return mDoInput;
        }

        public URL getURL()
        {
            try
            {
                return new URL(mUrl);
            } catch (MalformedURLException e)
            {
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

        public boolean equals(ServiceURL serviceURL)
        {
            if (serviceURL.getServiceName().equals(mServiceName)
                    && serviceURL.getEnvironment().equals(mEnvironment)
                    && serviceURL.getURL().equals(this.getURL())
                    && serviceURL.getVersion().equals(mVersion))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    // Basically the method will only return the first service endpoint satisfy
    // the condition
    // Not recommend to use that
    public static ServiceURL getServiceUrlByName(String name)
    {
        for (ServiceURL serviceUrl : ServiceURL.values())
        {
            if (TextUtils.equals(name, serviceUrl.getServiceName()))
                return serviceUrl;
        }
        return null;
    }

    public static ServiceURL getServiceUrlByCondition(String name, String environment,
            String version)
    {
        for (ServiceURL serviceUrl : ServiceURL.values())
        {
            if (TextUtils.equals(name, serviceUrl.getServiceName())
                    && TextUtils.equals(environment, serviceUrl.getEnvironment())
                    && TextUtils.equals(version, serviceUrl.getVersion()))
                return serviceUrl;
        }
        return null;
    }
}
