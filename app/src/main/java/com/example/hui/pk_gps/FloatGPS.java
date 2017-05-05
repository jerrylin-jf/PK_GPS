package com.example.hui.pk_gps;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.MutableFloat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.location.Location;

import com.gcssloop.widget.RockerView;

import java.util.List;

/**
 * Created by Hui on 9/7/2016.
 */
public class FloatGPS extends Service {
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;

    private LocationManager mockLocationManager;
    private String provider;
    private MockLocationProvider mockGPS;

    double x_Latitude;
    double y_Longitude;
    String providerStr = LocationManager.GPS_PROVIDER;
    Location mockLocation = new Location(providerStr);


    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 700;
        wmParams.y = 500;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED));

        mockLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        System.out.println("do InitLocation");
        List<String> providerList = mockLocationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            Toast.makeText(this, "provider is GPS", Toast.LENGTH_SHORT).show();
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            Toast.makeText(this, "provider is NETWORK", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("do permission checked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        final Location location = mockLocationManager.getLastKnownLocation(provider);
        System.out.println("provider: " + provider);
        System.out.println("location: " + location);

        if(location != null) {
            showLocation(location);
        }

        x_Latitude = location.getLatitude();
        y_Longitude = location.getLongitude();

        //Rocker
        RockerView rocker = (RockerView) mFloatLayout.findViewById(R.id.rocker);

        if (null != rocker){
            rocker.setListener(new RockerView.RockerListener() {

                @Override
                public void callback(int eventType, int currentAngle,  float currentDistance) {
                    switch (eventType) {
                        case RockerView.EVENT_ACTION:

                            if (currentAngle <= 30 || currentAngle >= 330) {
                                y_Longitude += 0.00005;
                                location.setLatitude(x_Latitude);
                                showLocation(location);
                                Log.e("EVENT_ACTION-------->", "+ right");
                                pushMockLocation(x_Latitude, y_Longitude);
                            }
                            if (currentAngle >= 60 && currentAngle <= 120) {
                                x_Latitude += 0.00005;
                                location.setLongitude(y_Longitude);
                                showLocation(location);
                                Log.e("EVENT_ACTION-------->", "+ up");
                                pushMockLocation(x_Latitude, y_Longitude);
                            }
                            if (currentAngle >= 150 && currentAngle <= 200) {
                                y_Longitude -= 0.00005;
                                location.setLatitude(x_Latitude);
                                showLocation(location);
                                Log.e("EVENT_ACTION-------=>", "+ left");
                                pushMockLocation(x_Latitude, y_Longitude);
                            }

                            if (currentAngle >= 240 && currentAngle <= 300) {
                                x_Latitude -= 0.00005;
                                location.setLongitude(y_Longitude);
                                showLocation(location);
                                Log.e("EVENT_ACTION-------->", "+ down");
                                pushMockLocation(x_Latitude, y_Longitude);
                            }
                            break;
                        case RockerView.EVENT_CLOCK:
                            //Log.e("EVENT_CLOCK", "angle="+currentAngle);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    private void showLocation(final Location location) {
        Log.i("FloatGPS", "in showLocation function");
        Log.i("FloatGPS", "latitude is " + location.getLatitude());
        Log.i("FloatGPS", "longitude is " + location.getLongitude());
    }

    private void pushMockLocation (double x, double y) {
        mockGPS = new MockLocationProvider("gps", FloatGPS.this);
        if (ActivityCompat.checkSelfPermission(FloatGPS.this
                , Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FloatGPS.this
                , Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling\
            Log.i("FloatGPS", "requestLocationUpdates NOT ALLOW");
            return;
        }

        mockGPS.pushLocation(x, y);
    }

}
