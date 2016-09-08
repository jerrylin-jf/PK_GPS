package com.example.hui.pk_gps;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.location.LocationManager;
import android.os.IBinder;
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
/**
 * Created by Hui on 9/7/2016.
 */
public class FloatGPS extends Service {
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;




    Button btn_UP;
    Button btn_LEFT;
    Button btn_DOWN;
    Button btn_RIGHT;

    @Override
    public void onCreate() {
        Log.i("FloatGPS", "oncreat");
        super.onCreate();
        createFloatView();
       // onLocationSetting(mockLocation);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;

        wmParams.x = 700;
        wmParams.y = 1000;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        btn_UP = (Button)mFloatLayout.findViewById(R.id.up_btn);
        btn_LEFT = (Button)mFloatLayout.findViewById(R.id.left_btn);
        btn_DOWN = (Button)mFloatLayout.findViewById(R.id.down_btn);
        btn_RIGHT = (Button)mFloatLayout.findViewById(R.id.right_btn);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED));


        btn_UP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FloatGPS", "click up_btn");
            }
        });

        btn_LEFT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FloatGPS", "click left_btn");

            }
        });

        btn_DOWN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FloatGPS", "click down_btn");
            }
        });

        btn_RIGHT.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FloatGPS", "click right_btn");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

}
