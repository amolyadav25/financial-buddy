package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.antworksmoney.financialbuddy.R;

import java.security.MessageDigest;

public class SplashActivity extends AppCompatActivity {

    private int DELAY_BEFORE_EXIT = 1000;

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               callNextActivity();
            }
        },DELAY_BEFORE_EXIT);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public void callNextActivity() {
        Intent nextActivityIntent;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        Log.e(TAG,pref.getString("first_run", "0"));

        if (pref.getString("first_run", "0").equalsIgnoreCase("1")) {
            nextActivityIntent = new Intent(SplashActivity.this,HomeActivity.class);
        }
        else if (pref.getString("first_run","0").equalsIgnoreCase("0.5")){
            nextActivityIntent = new Intent(SplashActivity.this,UserAuthActivity.class);
        }
        else {
            nextActivityIntent =  new Intent(SplashActivity.this,IntroActivity.class);
        }

        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(nextActivityIntent);
        finish();
    }
}
