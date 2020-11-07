package com.antworksmoney.financialbuddy.views.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.fragments.Auth.ConfirmOtpFragment;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;
import com.antworksmoney.financialbuddy.views.fragments.Auth.SignUpFragment;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class UserAuthActivity extends AppCompatActivity {

    Fragment signUpFragment;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("first_run", "0.5");
        editor.apply();

        signUpFragment = new SignUpFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (pref.getString("profileUpdate", "0").equalsIgnoreCase("0.5")) {

            JSONObject innerObject = new JSONObject();
            try {
                innerObject.put("user_name", pref.getString("user_name", ""));
                innerObject.put("user_phone", pref.getString("user_phone", ""));
                innerObject.put("firebase_token", pref.getString("FirebaseToken", ""));
                innerObject.put("referralcode", pref.getString("referralcode", ""));
                Log.e("referralcode", pref.getString("referralcode", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.replace(R.id.baseParentForAuthentication, ConfirmOtpFragment.newInstance(innerObject));

        } else if (pref.getString("profileUpdate", "0").equalsIgnoreCase("1")) {
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                boolean isNotificationServiceRunning = isNotificationServiceRunning();
                Log.e("Mytag","Problem2");
                if(!isNotificationServiceRunning){
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                }
                ActivityCompat.requestPermissions(this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE,READ_CONTACTS,WRITE_CONTACTS}, PERMISSION_REQUEST_CODE);
//                Intent intent = new Intent(UserAuthActivity.this,CanNotUseAppActivity.class);
//                startActivity(intent);
//                finish();
            } else {
                transaction.replace(R.id.baseParentForAuthentication, ProfileUpdateFragment.newInstance(R.id.baseParentForAuthentication));
            }
        } else {
            transaction.replace(R.id.baseParentForAuthentication, SignUpFragment.newInstance());
        }
        transaction.commit();
    }
    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners =
                Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        signUpFragment.onActivityResult(requestCode, resultCode, data);
    }
}
