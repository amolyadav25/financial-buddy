package com.antworksmoney.financialbuddy.helpers.messaging;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.fragments.Auth.ConfirmOtpFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

public class SendOtpToUser {
    private String phoneNumber, userName;

    private ProgressBar sendOtpProgressBar;

    private Button sendOtpButton;

    private CoordinatorLayout snackBarView;

    private JSONObject innerObject, outerObject;

    private Context mContext;

    private SharedPreferences preferences;

    private static final String TAG = "SendOtpToUser";

    public SendOtpToUser(String phoneNumber, String mUserName,
                         ProgressBar sendOtpProgressBar,
                         Button sendOtpButton,
                         CoordinatorLayout snackBarView,
                         Context context) {

        this.phoneNumber = phoneNumber;
        this.sendOtpProgressBar = sendOtpProgressBar;
        this.sendOtpButton = sendOtpButton;
        this.snackBarView = snackBarView;
        this.userName = mUserName;
        this.mContext = context;

        innerObject = new JSONObject();
        outerObject = new JSONObject();

        preferences = mContext.getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        callSendOtpApi(outerObject);
    }

    private void callSendOtpApi(JSONObject outerObject) {

        try {
            innerObject.put("user_name", userName);
            innerObject.put("user_phone", phoneNumber);
            innerObject.put("firebase_token", FirebaseInstanceId.getInstance().getToken());
            outerObject.put("userData", innerObject);


            Log.e(TAG,AppConstant.BaseUrl + "send-otp");

            Log.e(TAG, outerObject.toString());

            JsonObjectRequest sendOtpRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "send-otp",
                    outerObject,
                    response -> {

                        controlUi();

                        Log.e(TAG, response.toString());

                        try {
                            if (response.getJSONObject("respone").getString("state").trim().equalsIgnoreCase("success")) {

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("OTP",Integer.parseInt(response.getJSONObject("respone").getString("sent_otp")));
                                editor.apply();

                                Fragment currentFragment = ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.baseParentForAuthentication);
                                if (!(currentFragment instanceof ConfirmOtpFragment)) {
                                    FragmentTransaction transaction = ((FragmentActivity) mContext)
                                            .getSupportFragmentManager()
                                            .beginTransaction();

                                    transaction.replace(R.id.baseParentForAuthentication, ConfirmOtpFragment.newInstance(innerObject));
                                    transaction.commit();

                                }


                            } else {
                                showSnackBar("Failed to send OTP !!!");
                            }
                        } catch (Exception e) {
                            showSnackBar("Failed to send OTP !!!");
                            Log.e(TAG, e.toString());
                        }
                    },
                    error -> {
                        showSnackBar("Failed to send OTP !!!");
                        controlUi();
                        Log.e(TAG, error.toString());
                    });

            sendOtpRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sendOtpRequest.setShouldCache(false);

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(sendOtpRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

    private void controlUi() {
        sendOtpProgressBar.setVisibility(View.INVISIBLE);
        sendOtpButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
    }


}
