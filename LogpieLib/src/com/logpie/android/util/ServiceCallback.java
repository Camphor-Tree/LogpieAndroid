package com.logpie.android.util;

import android.os.Bundle;

public abstract class ServiceCallback {

	public abstract void onSuccess(Bundle result);
	
	public abstract void onError(Bundle errorMessagge);
}
