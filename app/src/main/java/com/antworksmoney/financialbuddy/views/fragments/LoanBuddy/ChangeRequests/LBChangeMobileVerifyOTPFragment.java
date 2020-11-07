package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LBChangeMobileVerifyOTPFragment extends Fragment {

    public LBChangeMobileVerifyOTPFragment() {
        // Required empty public constructor
    }

    public static LBChangeMobileVerifyOTPFragment newInstance(JSONObject mResponseObject) {
        inputObject = mResponseObject;
        return new LBChangeMobileVerifyOTPFragment();
    }

    private TextView resendOtpText;

    private PinEntryEditText pinEntryEditText;

    private Button otpConfirmButton;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBOTPVerificationFragme";

    private static JSONObject inputObject;

    private SharedPreferences mSharedPreferences;

    private CoordinatorLayout snackBarView;

    private Toolbar mToolbar;

    private TextView toolBarHeadingText;

    private CountDownTimer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbotpverification, container, false);



        toolBarHeadingText = rootView.findViewById(R.id.toolBarHeadingText);


        resendOtpText = rootView.findViewById(R.id.resendOtpText);

        pinEntryEditText = rootView.findViewById(R.id.txt_pin_entry);

        otpConfirmButton = rootView.findViewById(R.id.otpConfirmButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {

                SpannableStringBuilder builder = new SpannableStringBuilder();

                SpannableString timeRemainingText = new SpannableString("Time remaining ");
                timeRemainingText.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.toolbar_background_color)), 0, timeRemainingText.length(), 0);
                builder.append(timeRemainingText);

                SpannableString remaining = new SpannableString((millisUntilFinished / 1000) % 60 + " secs");
                remaining.setSpan(new ForegroundColorSpan(Color.RED), 0, remaining.length(), 0);
                builder.append(remaining);

                resendOtpText.setText(builder);

            }

            public void onFinish() {
                customTextView(resendOtpText);
            }
        };

        timer.start();

        toolBarHeadingText.setText("Change Mobile No");


        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        otpConfirmButton.setOnClickListener(view -> {
            if (pinEntryEditText.getText().toString().trim().length()<4){
                pinEntryEditText.setError("Invalid OTP");
                pinEntryEditText.requestFocus();
            }
            else {
                changeUserPhoneNumber();
            }
        });


        return rootView;
    }

    private void customTextView(TextView view) {

        SpannableStringBuilder spanTxt = new SpannableStringBuilder("Did not receive the code?");

        spanTxt.append(" Resend OTP ");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                    sendOTPToUser();
            }
        }, spanTxt.length() - " Resend OTP ".length(), spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanTxt.setSpan(new ForegroundColorSpan(Color.RED), 25, spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        view.setMovementMethod(LinkMovementMethod.getInstance());

        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }





    private void changeUserPhoneNumber(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("mobile",inputObject.getString("updatedNumber"));
            params.put("otp", pinEntryEditText.getText().toString().trim());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerrequest/verifyChangemobile",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){

                                showSnackBar("OTP verified successfully !!", R.color.green);
                            }
                            else {
                                showSnackBar("Failed to verify OTP !!", R.color.red);
                            }

                        }catch (Exception e){
                            showSnackBar("Failed to verify OTP !!", R.color.red);
                            e.printStackTrace();
                        }

                    }, error -> {
                showSnackBar("Failed to send OTP !!", R.color.red);
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getContext()).add(request);

        }catch (Exception e){
            progress_bar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void sendOTPToUser(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("mobile",inputObject.getString("updatedNumber"));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerrequest/requestChangemobile",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){

                                showSnackBar("OTP sent successfully !!", R.color.backgroundColor);
                            }
                            else {
                                showSnackBar("Failed to send OTP !!", R.color.red);
                            }

                        }catch (Exception e){
                            showSnackBar("Failed to send OTP !!", R.color.red);
                            e.printStackTrace();
                        }

                    }, error -> {
                showSnackBar("Failed to send OTP !!", R.color.red);
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getContext()).add(request);

        }catch (Exception e){
            progress_bar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        snackbar.show();
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




}
