package com.appsrise.face;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FaceActivity extends Activity  {

    private TextView watchTime;
    private TextView watchBattery;
    final private String TAG = "AppsriseFace";

    private BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Log.d(TAG, "onReceive mTimeInfoReceiver");
            watchTime.setText(new SimpleDateFormat("kk:mm a").format(Calendar.getInstance().getTime()));
        }
    };

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            Log.d(TAG, "onReceive mBatInfoReceiver");
            watchBattery.setText(String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "%"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                watchTime = (TextView) stub.findViewById(R.id.watchTime);
                watchBattery = (TextView) stub.findViewById(R.id.watchBattery);

                //broadcast receiver for time changes
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_TIME_TICK);
                intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
                intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

                mTimeInfoReceiver.onReceive(FaceActivity.this, registerReceiver(null, intentFilter));
                registerReceiver(mTimeInfoReceiver, intentFilter);

                //broadcast receiver for battery changes
                intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

                registerReceiver(mBatInfoReceiver, intentFilter);
            }
        });

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (watchTime!=null) {
            watchTime.setTextColor(Color.GRAY);
            watchBattery.setTextColor(Color.GRAY);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        if (watchTime!=null) {
            watchTime.setTextColor(Color.parseColor("#29ABE2"));
            watchBattery.setTextColor(Color.parseColor("#29ABE2"));
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
        unregisterReceiver(mBatInfoReceiver);
    }
}
