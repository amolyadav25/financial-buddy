package com.antworksmoney.financialbuddy.helpers.messaging;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstanceIDSer";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PersonalDetails",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirebaseToken",refreshedToken);
        editor.apply();

        Log.e(TAG, "Refreshed token: " + refreshedToken);
    }
}
