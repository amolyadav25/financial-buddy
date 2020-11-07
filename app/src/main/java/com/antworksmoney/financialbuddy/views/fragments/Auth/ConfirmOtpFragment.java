package com.antworksmoney.financialbuddy.views.fragments.Auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.helpers.messaging.SendOtpToUser;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfirmOtpFragment extends Fragment implements View.OnClickListener {

    private static JSONObject mDataObject;

    private Context mContext;

    private TextView oneclicktext;

    private TextView resendOtpButton, timeRemainingText;

    private PinEntryEditText pinEntryEditText;

    private Button otpConfirmButton;

    private CoordinatorLayout snackBarView;

    private ProgressBar progressHud;

    private CountDownTimer timer;

    private final int DELAY_BEFORE_EXIT = 200;

    private static final String TAG = "ConfirmOtpFragment";

    private SharedPreferences preferences;

    public static ConfirmOtpFragment newInstance(JSONObject dataObject) {
        mDataObject = dataObject;
        return new ConfirmOtpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_confirm_otp, container, false);

/*
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });

*/



        mContext = getContext();

        oneclicktext = rootView.findViewById(R.id.otpHeading);

        timeRemainingText = rootView.findViewById(R.id.timeRemainingText);

        pinEntryEditText = rootView.findViewById(R.id.txt_pin_entry);

        resendOtpButton = rootView.findViewById(R.id.resendOtpButton);

        otpConfirmButton = rootView.findViewById(R.id.otpConfirmButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progressHud = rootView.findViewById(R.id.progressHud);

        preferences = mContext.getSharedPreferences("PersonalDetails",Context.MODE_PRIVATE);

        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("profileUpdate", "0.5");
            editor.putString("user_name", mDataObject.getString("user_name"));
            editor.putString("user_phone", mDataObject.getString("user_phone"));
            editor.apply();
        }catch (Exception e){
            e.printStackTrace();
        }


        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString str1 = new SpannableString("An ");
        str1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.toolbar_background_color)), 0, str1.length(), 0);
        builder.append(str1);

        SpannableString str2 = new SpannableString("OTP ");
        str2.setSpan(new ForegroundColorSpan(Color.RED), 0, str2.length(), 0);
        builder.append(str2);

        SpannableString str3 = new SpannableString("has been sent to your mobile. \n please enter it below ");
        str3.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.toolbar_background_color)), 0, str3.length(), 0);
        builder.append(str3);

        oneclicktext.setText(builder, TextView.BufferType.SPANNABLE);

        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                SpannableStringBuilder builder = new SpannableStringBuilder();

                SpannableString timeRemainingText = new SpannableString("Time remaining ");
                timeRemainingText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.toolbar_background_color)), 0, timeRemainingText.length(), 0);
                builder.append(timeRemainingText);

                SpannableString remaining = new SpannableString((millisUntilFinished / 1000) % 60 + " secs");
                remaining.setSpan(new ForegroundColorSpan(Color.RED), 0, remaining.length(), 0);
                builder.append(remaining);

                ConfirmOtpFragment.this.timeRemainingText.setText(builder);

            }

            public void onFinish() {
                timeRemainingText.setText(getString(R.string.time_ended));
                timeRemainingText.setTextColor(Color.RED);
                resendOtpButton.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
            }
        };

        timer.start();

//        Log.e(TAG, String.valueOf(checkWriteExternalPermission()));

//        if (checkWriteExternalPermission()) {
//            Intent smsReadPermission = new Intent(mContext, SmsReciever.class);
//            mContext.startService(smsReadPermission);
//        }
//
//        SmsReciever.bindListener(messageText -> {
//            pinEntryEditText.setText(messageText);
//            otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
//            progressHud.setVisibility(View.VISIBLE);
//
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                try {
//                    callUserRegistrationApi();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }, DELAY_BEFORE_EXIT);
//        });

        pinEntryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (getActivity() != null) {
                    hideKeyboard((AppCompatActivity) getActivity());
                }
                verifyUser();
                return true;
            }
            return false;
        });

        otpConfirmButton.setOnClickListener(this);
        resendOtpButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }


    private void invalidOtpMessage() {
        showSnackBar("Invalid OTP !!!");
        pinEntryEditText.setText("");
    }


    private void verifyUser() {
        new Handler().postDelayed(() -> {
            if (pinEntryEditText.getText().toString().trim().equalsIgnoreCase("")) {
                invalidOtpMessage();
            } else if (pinEntryEditText.getText().toString().trim().length() != 4) {
                invalidOtpMessage();
            } else if (timeRemainingText.getText().toString().trim().equalsIgnoreCase(mContext.getString(R.string.time_ended))) {
                invalidOtpMessage();
            } else {
                otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                progressHud.setVisibility(View.VISIBLE);
                try {
                    if (preferences.getInt("OTP",0) == Integer.parseInt(pinEntryEditText.getText().toString().trim())) {
                        timer.cancel();
                        callUserRegistrationApi();

                    } else {
                        progressHud.setVisibility(View.GONE);
                        otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                        showSnackBar("OTP doesn't match !!!");

                    }
                } catch (Exception e) {
                    progressHud.setVisibility(View.GONE);
                    otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                    showSnackBar("OTP doesn't match !!!");
                    e.printStackTrace();
                }
            }
        }, DELAY_BEFORE_EXIT);
    }

    private void callUserRegistrationApi() throws JSONException {

        SharedPreferences pref = mContext.getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        JSONObject innerObject = new JSONObject();
        innerObject.put("name", mDataObject.getString("user_name"));
        innerObject.put("email", "");
        innerObject.put("contact", mDataObject.getString("user_phone"));
        innerObject.put("login_method", "OTP");
        innerObject.put("userType","0");
        innerObject.put("token", FirebaseInstanceId.getInstance().getToken());


        JSONObject outerObject = new JSONObject();
        outerObject.put("userData", innerObject);

        Log.e(TAG, outerObject.toString());

        JsonObjectRequest userVerifyRequest = new JsonObjectRequest(Request.Method.POST,
                AppConstant.BaseUrl + "verify-user", outerObject, response -> {

            Log.e(TAG, response.toString());

            try {
                JSONObject userDetails = response.getJSONObject("respone");
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("email_value", userDetails.getString("email"));
                editor.putString("user_name", userDetails.getString("name"));
                editor.putString("user_phone", userDetails.getString("mobile"));

                try {
                    editor.putString("user_dob", userDetails.getString("dob"));
                    editor.putString("user_gender", userDetails.getString("gender"));
                    editor.putString("user_city", userDetails.getString("city"));
                    editor.putString("user_address", userDetails.getString("address"));
                    editor.putString("fbCode", userDetails.getString("user_id"));
                }catch (Exception e){
                    e.printStackTrace();
                }

                editor.putString("user_image_url", userDetails.getString("profile_image_url"));
                editor.apply();

                progressHud.setVisibility(View.GONE);
                otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                FragmentTransaction transaction = ((FragmentActivity) mContext)
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.baseParentForAuthentication, ProfileUpdateFragment.newInstance(R.id.baseParentForAuthentication));
                transaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar("Some error occured !! Please try again later.");
            }
        },
                error ->
                {
                    progressHud.setVisibility(View.GONE);
                    otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                    Log.e(TAG, error.toString());
                    showSnackBar("Some error occured !! Please try again later.");
                }
        );

        userVerifyRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(userVerifyRequest);

    }


    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }


//    private boolean checkWriteExternalPermission() {
//        String permission = Manifest.permission.RECEIVE_SMS;
//        int res = mContext.checkCallingOrSelfPermission(permission);
//        return (res == PackageManager.PERMISSION_GRANTED);
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.otpConfirmButton:
                if (getActivity() != null) {
                   // hideKeyboard(getActivity());
                }
                verifyUser();
                break;

            case R.id.resendOtpButton:

                if (timeRemainingText.getText().toString().trim().equalsIgnoreCase(mContext.getString(R.string.time_ended))) {
                    timer.cancel();
                    otpConfirmButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                    timeRemainingText.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                    resendOtpButton.setTextColor(mContext.getResources().getColor(R.color.backgroundColor));
                    pinEntryEditText.setText("");
                    progressHud.setVisibility(View.VISIBLE);

                    try {
                        new SendOtpToUser(
                                mDataObject.getString("user_phone"),
                                mDataObject.getString("user_name"),
                                progressHud, otpConfirmButton,
                                snackBarView, getContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    timer.start();
                }

                break;
        }
    }
}
