package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import com.google.gson.JsonObject;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.fragments.Auth.ConfirmOtpFragment;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;
import com.antworksmoney.financialbuddy.views.fragments.Auth.SignUpFragment;

import org.json.JSONObject;

public class UserAuthActivity extends AppCompatActivity {

    Fragment signUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("first_run","0.5");
        editor.apply();

        signUpFragment = new SignUpFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (pref.getString("profileUpdate", "0").equalsIgnoreCase("0.5")) {

            JSONObject innerObject = new JSONObject();
            try {
                innerObject.put("user_name", pref.getString("user_name",""));
                innerObject.put("user_phone", pref.getString("user_phone",""));
                innerObject.put("firebase_token", pref.getString("FirebaseToken", ""));
            }catch (Exception e){
                e.printStackTrace();
            }

            transaction.replace(R.id.baseParentForAuthentication, ConfirmOtpFragment.newInstance(innerObject));

        }else if (pref.getString("profileUpdate", "0").equalsIgnoreCase("1")) {
            transaction.replace(R.id.baseParentForAuthentication, ProfileUpdateFragment.newInstance(R.id.baseParentForAuthentication));
        }
        else {
            transaction.replace(R.id.baseParentForAuthentication, SignUpFragment.newInstance());
        }
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            signUpFragment.onActivityResult(requestCode, resultCode, data);

    }
}
