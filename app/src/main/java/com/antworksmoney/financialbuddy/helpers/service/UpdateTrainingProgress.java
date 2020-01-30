package com.antworksmoney.financialbuddy.helpers.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class UpdateTrainingProgress extends Service {

    private static final String TAG = "UpdateTrainingProgress";

    public static CountDownTimer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences preferences = getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        timer = new CountDownTimer(43200000 - preferences.getInt("progressData",0), 1000) {
            public void onTick(long millisUntilFinished) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("progressData",43200000 - Integer.parseInt(String.valueOf(millisUntilFinished)));
                editor.apply();

                Log.e(TAG,String.valueOf(millisUntilFinished));
            }

            public void onFinish() {
                Log.e(TAG,"onFinish()");
            }
        };
        timer.start();

        return START_STICKY;
    }
}
