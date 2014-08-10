package com.logpie.android.metric;

import java.util.HashMap;

import android.text.TextUtils;

import com.logpie.android.util.LogpieLog;

/**
 * This class is used to measure the counter information
 * @author yilei
 *
 */
public class LogpieCounter
{
    private static String TAG = LogpieCounter.class.getName();
    private static LogpieCounter sLogpieCounter;
    private static HashMap<String, Integer> sCounterMap;
    
    public enum LogpicBasicCounterEntry
    {
        Login("Login",1),
        Register("Register",1),
        RefreshSquare("RefreshSquare",5);
        
        private String mCounterName;
        //the buffer size indicates the max times we cache before sending the counter to server
        private Integer mBufferSize;
        LogpicBasicCounterEntry(String name, Integer size)
        {
            mCounterName = name;
            mBufferSize = size;
        }
        
        public String getName()
        {
            return this.mCounterName;
        }
        
        public Integer getBufferSize()
        {
            return this.mBufferSize;
        }
        
        public static int getBufferSize(String name)
        {
            for(LogpicBasicCounterEntry entry : LogpicBasicCounterEntry.values())
            {
                if(TextUtils.equals(name, entry.getName()))
                {
                    return entry.getBufferSize().intValue();
                }
            }
            return 1;
        }

    }
    
    
    public synchronized LogpieCounter getInstance()
    {
        if(sLogpieCounter==null)
        {
            sLogpieCounter = new LogpieCounter();
            initialize();
        }
        return sLogpieCounter;
    }
    
    private void initialize()
    {
        this.getInstance();
        sCounterMap = new HashMap<String,Integer>();
        for( LogpicBasicCounterEntry entry:  LogpicBasicCounterEntry.values())
        {
            sCounterMap.put(entry.name(), 0);
        }
    }
    
    //If you add the counter add runtime, then the default the buffer size is one
    public synchronized static void  addCounter(String newCounterName)
    {
        if(!sCounterMap.containsKey(newCounterName))
        {
            sCounterMap.put(newCounterName, 0);
        }
    }
    
    public synchronized static void counterIncrease( LogpicBasicCounterEntry entry)
    {
        String entryName = null;
        if(entry !=null)
        {
            entryName = entry.name();
        }
        counterIncrease(entryName);
    }
    
    public synchronized static void counterIncrease(String entry)
    {
        if(entry==null)
        {
            LogpieLog.e(TAG, "entry cannot be null");
            return;
        }
        int count = sCounterMap.get(entry);
        count++;
        int bufferSize = LogpicBasicCounterEntry.getBufferSize(entry);
        if(count>=bufferSize)
        {
            sendCounterToServer(entry);
            count=0;
        }
        sCounterMap.put(entry, count);
    }
    
    public static boolean checkCounterExistence(String counterName)
    {
        if(sCounterMap.containsKey(counterName))
        {
            return true;
        }
        return false;
    }
    
    private static void sendCounterToServer(String entry)
    {
        //TODO wait for server side's change.
    }
}
