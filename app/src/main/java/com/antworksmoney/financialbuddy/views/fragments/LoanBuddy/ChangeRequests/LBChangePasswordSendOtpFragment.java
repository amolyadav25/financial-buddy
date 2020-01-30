package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LBChangePasswordSendOtpFragment extends Fragment {

    public LBChangePasswordSendOtpFragment() {
        // Required empty public constructor
    }

    public static boolean calledFromProfilePage;


    public static LBChangePasswordSendOtpFragment newInstance(boolean b) {
        calledFromProfilePage = b;
        return  new LBChangePasswordSendOtpFragment();
    }

    private Toolbar mToolbar;

    private Button ProceedButton;

    private RelativeLayout snackBarView;

    private EditText et_user_phone_number;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBChangeMobileFragment";

    private RelativeLayout progress_bar;

    private TextView toolBarHeadingText;

    private RequestQueue mRequestQueue;

    private String MToken = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbchnage_mobile, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        toolBarHeadingText = rootView.findViewById(R.id.toolBarHeadingText);

        mRequestQueue = Volley.newRequestQueue(getContext());

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        toolBarHeadingText.setText("Enter Mobile Number");

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        ProceedButton.setOnClickListener(view -> {
            if (et_user_phone_number.getText().toString().trim().length()<10){
                et_user_phone_number.setError("Invalid Number !!");
                et_user_phone_number.requestFocus();
            }
            else {
                chnageUserPassword();
            }
        });


        if (!calledFromProfilePage){
            fetchTokenWithoutLogin();
        }

        return rootView;
    }

    private void fetchTokenWithoutLogin() {

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "getauth/token",
                null,
                response -> {

                    Log.i(TAG, response.toString());

                    progress_bar.setVisibility(View.GONE);

                    try {

                        MToken = response.getString("token");

                    } catch (Exception e) {

                        progress_bar.setVisibility(View.GONE);

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

        mRequestQueue.add(dataRequest);

    }



    private void chnageUserPassword(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("mobile",et_user_phone_number.getText().toString().trim());

            String url = "";
            if (calledFromProfilePage){
                url = AppConstant.borrowerBaseUrl + "borrowerrequest/sendOtpPassword";
            }
            else {
                url = AppConstant.borrowerBaseUrl + "borrowerrequest/sendotpForgotpassword";
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){

                                showSnackBar("OTP sent successfully !!", R.color.green);
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
                    if (calledFromProfilePage){
                        params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                    }
                    else {
                        params.put("Authorization", MToken);
                    }
                    params.put("Content-Type", "application/json");
                    Log.e(TAG,  params.toString());
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(request);

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
                    try {

                        JSONObject inputJsonObject = new JSONObject();
                        inputJsonObject.put("updatedNumber", et_user_phone_number.getText().toString().trim());

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeParent, LBChangePasswordVerifyOTPFragment.newInstance(inputJsonObject, calledFromProfilePage, MToken));
                        transaction.commit();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        });
        snackbar.show();
    }


}
