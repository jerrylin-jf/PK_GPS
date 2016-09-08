package com.example.hui.pk_gps;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.gcssloop.widget.RockerView;

public class MainActivity extends AppCompatActivity {

    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    private LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askForPermission();
        mockPositionPermission();
        Log.i("MainActivity", "after mock");
        Button start = (Button)findViewById(R.id.start_btn);
        Button end = (Button)findViewById(R.id.end_btn);

        //Rocker
        RockerView rocker = (RockerView) findViewById(R.id.rocker);
        if (null != rocker){
            rocker.setListener(new RockerView.RockerListener() {
                @Override
                public void callback(int eventType, int currentAngle) {
                    switch (eventType) {
                        case RockerView.EVENT_ACTION:

                            Log.e("EVENT_ACTION-------->", "angle="+currentAngle);
                            break;
                        case RockerView.EVENT_CLOCK:

                            Log.e("EVENT_CLOCK", "angle="+currentAngle);
                            break;
                    }
                }
            });
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "on Start_Click");
                Intent intent = new Intent(MainActivity.this, FloatGPS.class);
                startService(intent);
                finish();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "on End_Click");
                Intent intent = new Intent(MainActivity.this, FloatGPS.class);
                stopService(intent);
            }
        });
    }

    public void askForPermission() {
        if(!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else { }


    }

    public void mockPositionPermission() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean hasAddTestProvider = false;
        boolean canMockPosition = (Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0) || Build.VERSION.SDK_INT > 22;
        if (canMockPosition && hasAddTestProvider == false) {
            try {
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE,
                        null, System.currentTimeMillis());

                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
    }
}
