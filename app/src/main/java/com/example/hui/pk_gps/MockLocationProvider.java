package com.example.hui.pk_gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Hui on 9/13/2016.
 */
public class MockLocationProvider {
    private String TAG = "MockLocationProvider";
    String providerName;
    Context ctx;
    Location mockLocation;

    public MockLocationProvider(String name, Context ctx) {
        this.providerName = name;
        this.ctx = ctx;

        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.addTestProvider(providerName, false, false, false, false, false,
                true, true, 0, 5);
        lm.setTestProviderEnabled(providerName, true);
    }

    public void pushLocation(double lat, double lon) {
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        if(mockLocation == null)
            mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(6);
        mockLocation.setSpeed(10);
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        try{
            lm.setTestProviderLocation(providerName, mockLocation);
        }catch(IllegalArgumentException e){
            Log.e(TAG ,"IllegalArgumentException. Msg: "+e.getMessage());
        }
    }

    public void shutdown() {
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.removeTestProvider(providerName);
    }
}
