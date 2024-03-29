package com.logpie.android.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.test.AndroidTestCase;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.logic.AuthManager.AuthType;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;

public class GenericConnectionTests extends AndroidTestCase
{
    private final static String TAG = GenericConnectionTests.class.getName();

    private String testRocketData = "{\"type\":\"INSERT\",\"requestID\":\"1DEASEWO-2232GDA2\","
            + "\"company\":\"logpie\",\"platform\":\"java\"," + "\"application\":\"logpie\","
            + "\"software_version\":\"1.01\"," + "\"environment\":\"alpha\"," + "\"metrics\":[{"
            + "\"component\":\"loginpage\"," + "\"action\":\"register\","
            + "\"timestamp\":\"9800284756345\"," + "\"time\":\"91\"},"
            + "{\"component\":\"loginpage\"," + "\"action\":\"login\","
            + "\"timestamp\":\"9800284756389\"," + "\"time\":\"27\"}],"
            + "\"mobile_device\":\"true\"," + "\"OS_type\":\"android\","
            + "\"OS_version\":\"4.1\"," + "\"device_manufacture\":\"Samsung\","
            + "\"device_version\":\"Galaxy S3\"}";

    // This is just test single thread
    public void testSingleGenericConnection()
    {
        testRocketService();
    }

    // TODO: there's a bug. it is always success, because the main thread just
    // get through
    // and do not wait for other threads' result.
    // we should refactor this method to let the current wait until all the
    // sub-threads finished
    public void testMultipleGenericConnection()
    {
        final CountDownLatch latch = new CountDownLatch(20);
        final AtomicInteger countSuccess = new AtomicInteger(0);
        Thread[] threads = new Thread[20];
        for (int i = 0; i < 20; i++)
        {
            threads[i] = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    testRocketService(latch, countSuccess);
                }
            });
        }
        for (int i = 0; i < 20; i++)
            threads[i].start();
        try
        {
            latch.await();
            if (countSuccess.get() != 20)
            {
                fail("In Multipe Thread Connection Test, only pass+" + countSuccess.get());
            }
            else
            {
                LogpieLog.i(TAG, "Totally 20 threads works successfully");
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    // using RocketService as test target service
    public void testRocketService()
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.RocektService, AuthType.NoAuth, this.getContext());
        try
        {
            connection.setRequestData(new JSONObject(testRocketData));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle bundle)
            {
                Assert.assertNotNull(bundle);
            }

            @Override
            public void onError(Bundle bundle)
            {
                fail();
            }
        });
    }

    // using RocketService as test target service
    // test MultiThread
    private void testRocketService(final CountDownLatch latch, final AtomicInteger countSuccess)
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.RocektService, AuthType.NoAuth, this.getContext());
        try
        {
            connection.setRequestData(new JSONObject(testRocketData));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle bundle)
            {
                countSuccess.addAndGet(1);
                latch.countDown();
            }

            @Override
            public void onError(Bundle bundle)
            {
                latch.countDown();
            }
        });
    }

    // using AuthenticationService as test target service
    public void testAuthenticationServiceRegister()
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.AuthenticationService, AuthType.NoAuth, this.getContext());
        try
        {
            JSONObject testAuthRegData = new JSONObject();
            testAuthRegData.put("auth_type", "REGISTER");
            // testAuthRegData.put("register_email",
            // UUID.randomUUID().toString().subSequence(0, 10)+"@gmail.com");
            testAuthRegData.put("register_email", "testlogpie@aa.com");
            testAuthRegData.put("register_password", "123456");
            testAuthRegData.put("register_nickname", "testUserName");
            testAuthRegData.put("register_city", "Seattle");
            connection.setRequestData(testAuthRegData);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle bundle)
            {
                LogpieLog.d(TAG, bundle.toString());
            }

            @Override
            public void onError(Bundle bundle)
            {
                fail();
            }
        });
    }

    // using AuthenticationService as test target service
    public void testAuthenticationServiceLogin()
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.AuthenticationService, AuthType.NoAuth, this.getContext());
        try
        {
            JSONObject testAuthLoginData = new JSONObject();
            testAuthLoginData.put("auth_type", "AUTHENTICATE");
            testAuthLoginData.put("login_email", "testlogpie@aa.com");
            testAuthLoginData.put("login_password", "123456");

            connection.setRequestData(testAuthLoginData);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle bundle)
            {
                LogpieLog.d(TAG, bundle.toString());
            }

            @Override
            public void onError(Bundle bundle)
            {
                LogpieLog.d(TAG, bundle.toString());
                fail();
            }
        });
    }
}
