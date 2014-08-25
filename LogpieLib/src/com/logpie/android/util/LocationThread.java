package com.logpie.android.util;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;

public class LocationThread extends Thread {    
    	LocationManager manager;
    	LocationListener listener;
        boolean isGPS;

        public LocationThread(LocationListener listener, LocationManager manager, boolean isGPS) {
            this.listener = listener;
            this.manager = manager;
            this.isGPS = isGPS;
        }

        public void run() {
            Looper.prepare();
            LogpieLog.d("LocationThread", "Start to get update");
            if(isGPS)
            {
            	manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    listener);
            }else
            {
            	manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                        listener);
            }
            Looper.loop();
        }
}
