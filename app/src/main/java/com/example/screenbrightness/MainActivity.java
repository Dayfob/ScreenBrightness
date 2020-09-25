package com.example.screenbrightness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.system.*;
import android.content.ContentResolver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLight;
    private boolean isSensorPresent;
    private ContentResolver mContentResolver;
    private Window mWindow;
    TextView textView;
//    private final SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }
        initScreenBrightness();
    }



    public void initScreenBrightness() {
        mContentResolver = getContentResolver();
        mWindow = getWindow();
    }


    public void changeScreenBrightness(float brightness) {
        Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, (int) (brightness * 255));
        WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
        mLayoutParams.screenBrightness = brightness;
        mWindow.setAttributes(mLayoutParams);
    }











    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float light = sensorEvent.values[0];
        if (light > 0 && light < 1000) {
            changeScreenBrightness(light / 100);
            textView.append(String.valueOf(light));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }













    @Override
    protected void onResume() {
        super.onResume();
        if (isSensorPresent) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSensorManager = null;
        mLight = null;
        mContentResolver = null;
        mWindow = null;
    }
}