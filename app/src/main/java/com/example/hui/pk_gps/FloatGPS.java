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

import com.gcssloop.widget.RockerView;
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

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED));

        //Rocker
        RockerView rocker = (RockerView) mFloatLayout.findViewById(R.id.rocker);
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



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

}
