package com.antworksmoney.financialbuddy.views.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication.LBHomeFragment;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity implements InstallReferrerStateListener {

    private int DELAY_BEFORE_EXIT = 1000;
    private static final String TAG = "SplashActivity";
    InstallReferrerClient referrerClient;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Db_Helper mDataBaseObject;
    HomeActivity homeActivity;
    String dialogMessage,dialogTitle;
   // String url = "https://play.google.com/store/apps/details?id=com.antworksmoney.financialbuddy&referrer=utm_source%3Dgothird%26utm_medium%3Dcpc%26anid%3Dadmob";


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("click_action",getIntent().getStringExtra("click_action"));
        editor.commit();
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(this);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    callNextActivity();
                }
            }, DELAY_BEFORE_EXIT);

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
        mDataBaseObject = new Db_Helper(getApplicationContext());
        Intent nextActivityIntent;
        SharedPreferences pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        editor = pref.edit();
        Log.e(TAG, pref.getString("first_run", "0"));


        if (pref.getString("first_run", "0").equalsIgnoreCase("1")) {
            if(getIntent().getStringExtra("click_action")!=null)
            {
                mDataBaseObject.insertNotificationData("0",
                        pref.getString("notificationmessage",null),
                        getIntent().getStringExtra("click_action"),
                        String.valueOf(Calendar.getInstance().getTime()),
                        pref.getString("notificationtitle",null));
                nextActivityIntent = new Intent(SplashActivity.this, HomeActivity.class);
                nextActivityIntent.putExtra("click_action",getIntent().getStringExtra("click_action"));
                startActivity(nextActivityIntent);
                finish();
            }else {
                nextActivityIntent = new Intent(SplashActivity.this, HomeActivity.class);
            }
        } else if (pref.getString("first_run", "0").equalsIgnoreCase("0.5")) {
            nextActivityIntent = new Intent(SplashActivity.this, GetNumber.class);
        } else {
            nextActivityIntent = new Intent(SplashActivity.this, IntroActivity.class);
            //handleNotification();
        }
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        nextActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(nextActivityIntent);
        finish();
    }

   /* @Override
    public void onInstallReferrerSetupFinished(int responseCode) {

        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                try {
                    Log.v(TAG, "InstallReferrer conneceted");

                    ReferrerDetails response = referrerClient.getInstallReferrer();
                    // Refer Code , Save this to sharedpreference
                    String referrerUrl = response.getInstallReferrer();


                    Log.e("referralcode" , referrerUrl);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("referralcode", referrerUrl);
                    editor.apply();

                    // Link Click time (Save this to sharedpreference)
                    long referrerClickTime = response.getReferrerClickTimestampSeconds();
                    // App install time (Save this to sharedpreference)
                    long appInstallTime = response.getInstallBeginTimestampSeconds();
                    boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                    referrerClient.endConnection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                Log.e(TAG, "InstallReferrer not supported");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                Log.e(TAG, "Unable to connect to the service");
                break;
            default:
                Log.e(TAG, "responseCode not found.");
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        referrerClient.startConnection(this);
    }

*/


    @Override
    public void onInstallReferrerSetupFinished(int responseCode) {
        switch (responseCode) {
            case InstallReferrerClient.InstallReferrerResponse.OK:
                try {
                    Log.v(TAG, "InstallReferrer conneceted");
                    ReferrerDetails response = referrerClient.getInstallReferrer();

                    // SharedPreferences.getInstance().saveString("referrer", response.getInstallReferrer());
                    String referrerUrl = response.getInstallReferrer();

                    editor.putString("referralcode", referrerUrl);


                    Log.e("referrerUrl", referrerUrl);
                    editor.apply();

                    referrerClient.endConnection();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                Log.e(TAG, "InstallReferrer not supported");
                break;
            case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                Log.e(TAG, "Unable to connect to the service");
                break;
            default:
                Log.e(TAG, "responseCode not found.");
        }
    }

    @Override
    public void onInstallReferrerServiceDisconnected() {
        referrerClient.startConnection(this);
    }




   private void handleNotification(String message) {

        Log.d("notificationData", message);
        String idChannel = "roshni messages";
        Intent mainIntent;

        mainIntent = new Intent(SplashActivity.this, LBHomeFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(SplashActivity.this, 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) SplashActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(SplashActivity.this, idChannel);
        builder.setContentTitle(SplashActivity.this.getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(message);
//KPgiri@2020
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, SplashActivity.this.getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(SplashActivity.this.getString(R.string.default_notification_channel_id));
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(SplashActivity.this.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(SplashActivity.this, R.color.colorAccent))
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }
    }
}
