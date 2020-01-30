package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
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

public class LBOTPVerificationFragment extends Fragment {

    public LBOTPVerificationFragment() {
        // Required empty public constructor
    }

    public static LBOTPVerificationFragment newInstance() {
        return new LBOTPVerificationFragment();
    }

    private TextView resendOtpText;

    private PinEntryEditText pinEntryEditText;

    private Button otpConfirmButton;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBOTPVerificationFragme";

    private String tokenValue = "";

    private SharedPreferences mSharedPreferences;

    private CoordinatorLayout snackBarView;

    private Toolbar mToolbar;

    private CountDownTimer timer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbotpverification, container, false);

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


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        otpConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pinEntryEditText.getText().toString().trim().length()<4){
                    pinEntryEditText.setError("Invalid OTP");
                    pinEntryEditText.requestFocus();
                }
                else {
                    verifyOTP();
                }
            }
        });

        fetchTokenWithoutLogin();
    }

    private void fetchTokenWithoutLogin() {

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "getauth/token",
                null,
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.i(TAG, response.toString());

                    try {

                        tokenValue = response.getString("token");


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                },
                error -> {

                    Log.e(TAG, error.toString());

                    progress_bar.setVisibility(View.GONE);

                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(dataRequest);

    }


    private void customTextView(TextView view) {

        SpannableStringBuilder spanTxt = new SpannableStringBuilder("Did not receive the code?");

        spanTxt.append(" Resend Verification Code ");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                sendOtpToUser();
            }
        }, spanTxt.length() - " Resend Verification Code ".length(), spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanTxt.setSpan(new ForegroundColorSpan(Color.RED), 25, spanTxt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        view.setMovementMethod(LinkMovementMethod.getInstance());

        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void sendOtpToUser(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("email", mSharedPreferences.getString("LBMail",""));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.commonAPIUrl + "commonapi/resendEmailverification",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            showSnackBar("OTP sent successfully !!", R.color.colorPrimaryDark);

                        }catch (Exception e){
                            showSnackBar("Failed to resend OTP !!", R.color.red);
                            e.printStackTrace();
                        }

                    }, error -> {
                showSnackBar("Failed to resend OTP !!", R.color.red);
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", tokenValue);
                    params.put("Content-Type", "application/json");
                    Log.e(TAG, params.toString());
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

    private void verifyOTP(){

        progress_bar.setVisibility(View.VISIBLE);

        try {

        Map<String, String> params = new HashMap<>();
        params.put("email", mSharedPreferences.getString("LBMail", ""));
        params.put("email_verification_code",pinEntryEditText.getText().toString().trim());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.commonAPIUrl + "commonapi/verifyEmailcode",
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
            showSnackBar("Failed to verify OTP !!", R.color.red);
            Log.e(TAG, error.toString());
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", tokenValue);
                params.put("Content-Type", "application/json");
                Log.e(TAG, params.toString());
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

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("loginSave", "0");
                    editor.apply();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, EmailVerifiedFragment.newInstance());
                    transaction.addToBackStack(null).commit();
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
