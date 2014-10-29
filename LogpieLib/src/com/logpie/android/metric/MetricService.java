package com.logpie.android.metric;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.logic.AuthManager.AuthType;
import com.logpie.android.util.BuildInfo;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;

public class MetricService extends IntentService
{
    /**
     * Should use this key to add to the intent extra
     */
    public static final String KEY_METRIC_BUNDLE = "com.logpie.android.metric.bundle.key";

    private static final String TAG = MetricService.class.getName();

    public MetricService()
    {
        super("MetricService");
    }

    public MetricService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle metricBundle = intent.getBundleExtra(KEY_METRIC_BUNDLE);
        JSONObject metricJSON = buildRocketJSON(metricBundle);
        sendMetrics(metricJSON);
    }

    /**
     * "{\"type\":\"INSERT\",\"requestID\":\"1DEASEWO-2232GDA2\"," +
     * "\"company\":\"logpie\",\"platform\":\"java\"," +
     * "\"application\":\"logpie\"," + "\"software_version\":\"1.01\"," +
     * "\"environment\":\"alpha\"," + "\"metrics\":[{" +
     * "\"component\":\"loginpage\"," + "\"action\":\"register\"," +
     * "\"timestamp\":\"9800284756345\"," + "\"time\":\"91\"}," +
     * "{\"component\":\"loginpage\"," + "\"action\":\"login\"," +
     * "\"timestamp\":\"9800284756389\"," + "\"time\":\"27\"}]," +
     * "\"mobile_device\":\"true\"," + "\"OS_type\":\"android\"," +
     * "\"OS_version\":\"4.1\"," + "\"device_manufacture\":\"Samsung\"," +
     * "\"device_version\":\"Galaxy S3\"}";
     */
    private JSONObject buildRocketJSON(Bundle metricBundle)
    {
        JSONObject metricJSON = new JSONObject();
        try
        {
            metricJSON.put("type", "INSERT");
            metricJSON.put("requestID", UUID.randomUUID().toString());
            metricJSON.put("company", "logpie");
            metricJSON.put("platform", BuildInfo.PLATFORM);
            metricJSON.put("application", BuildInfo.APPLICATION);
            metricJSON.put("software_version", BuildInfo.getLogpieVersion(this));
            metricJSON.put("environment", BuildInfo.ENVIRONMENT);
            metricJSON.put("mobile_device", "true");
            metricJSON.put("OS_type", BuildInfo.OS_TYPE);
            metricJSON.put("OS_version", BuildInfo.OS_VERSION);
            metricJSON.put("device_manufacture", BuildInfo.MANUFACTURER);
            // TODO: try to figure out how to get device version
            metricJSON.put("device_version", "default");

            JSONArray metricArray = new JSONArray();
            for (String key : metricBundle.keySet())
            {
                JSONObject singleMetric = new JSONObject();
                String[] attributes = metricBundle.getString(key).split("&");
                singleMetric.put("component", attributes[0]);
                singleMetric.put("action", attributes[1]);
                singleMetric.put("timestamp", attributes[2]);
                singleMetric.put("time", attributes[3]);
                metricArray.put(singleMetric);
            }
            metricJSON.put("metrics", metricArray);

        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONExcepetion happed when build RocketJSON", e);
        }

        LogpieLog.d(TAG, metricJSON.toString());
        return metricJSON;
    }

    private void sendMetrics(JSONObject metricJSON)
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.RocektService, AuthType.NoAuth,
                this.getApplicationContext());
        connection.setRequestData(metricJSON);
        connection.send(new LogpieCallback()
        {
            @Override
            public void onSuccess(Bundle bundle)
            {
                LogpieLog.d(TAG, "send metrics to RocketService successfully");
            }

            @Override
            public void onError(Bundle bundle)
            {
                LogpieLog.e(TAG, "fail to send metrics to RocketService");
            }
        });
    }

}
