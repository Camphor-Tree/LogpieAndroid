/*
 * Copyright (c) 2014 logpie.com
 * All rights reserved.
*/
package com.logpie.android.util;

import java.util.concurrent.atomic.AtomicBoolean;

import android.util.Log;

public final class LogpieLog {
	private static AtomicBoolean sDebug = new AtomicBoolean(true);
	
	public static void openLog()
	{
		sDebug.set(true);
	}
	
	public static void closeLog()
	{
		sDebug.set(false);
	}
	
	
	public static void i(String TAG, String info)
	{
		if(sDebug.get())
		{
			Log.i(TAG,info);
		}
	}
	
	public static void d(String TAG, String info)
	{
		if(sDebug.get())
		{
			Log.d(TAG,info);
		}
	}
	
	public static void e(String TAG, String info)
	{
		if(sDebug.get())
		{
			Log.e(TAG,info);
		}
	}
	
}

