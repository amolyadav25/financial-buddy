package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.RazorPayPaymentActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class LBPaymentRegistrationFragment extends Fragment {

    public LBPaymentRegistrationFragment() {
        // Required empty public constructor
    }


    public static LBPaymentRegistrationFragment newInstance() {
        return new LBPaymentRegistrationFragment();
    }

    private Button payNowButton;

    private TextView amountTextView;

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBPaymentRegistrationFr";

    private int amount = 0;

    private CoordinatorLayout snackBarView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbpayment_registration, container, false);

        payNowButton = rootView.findViewById(R.id.payNowButton);

        amountTextView = rootView.findViewById(R.id.amountTextView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        payNowButton.setOnClickListener(view -> {
            if (amount == 0){
                showSnackBar("Invalid loan amount !!", R.color.red);
            }
            else {
                fetchAPIKey();
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("10")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "9");
            editor.apply();
        }

        checkRegistrationFeesPaidOrNot();

        return rootView;
    }

    private void checkRegistrationFeesPaidOrNot(){

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.borrowerBaseUrl + "borrowerinfo/paymentRegistration",
                null,
                response -> {

                    Log.e(TAG, response.toString());

                    try {

                     if (response.getString("status").trim().equalsIgnoreCase("0")){
                         fetchRegistrationAmount();
                     }
                     else {
                         progress_bar.setVisibility(View.GONE);
                         showSnackBar("Registration fees already paid !!", R.color.green);
                     }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progress_bar.setVisibility(View.GONE);
                        showSnackBar("Failed to get API key. Please try again later !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to registration details !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
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
    }


    private void fetchRegistrationAmount(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getborrowerRegistrationfee",
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("status").trim().equalsIgnoreCase("1")){
                            amountTextView.setText("₹"+ jsonObject.getString("lender_registration_fee"));
                            amount = Integer.parseInt(jsonObject.getString("lender_registration_fee"));
                        }
                        else {
                            amount = 0;
                            amountTextView.setText("₹0");
                        }



                    } catch (Exception e) {
                        amount = 0;
                        amountTextView.setText("₹0");
                        e.printStackTrace();
                    }

                },
                error -> {
                    amount = 0;
                    progress_bar.setVisibility(View.GONE);
                    amountTextView.setText("₹0");
                    Log.e("ERROR", "error => " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                Log.e(TAG, params.toString());
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(dataRequest);


    }

    private void fetchAPIKey(){

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://antworksp2p.com/p2papi/commonapi/getRazorpaykey",
                null,
                response -> {

                    Log.e(TAG, response.toString());

                    try {

                        generateRazorPayOrderId(response.getString("key"));

                    } catch (Exception e) {
                        e.printStackTrace();
                        progress_bar.setVisibility(View.GONE);
                        showSnackBar("Failed to get API key. Please try again later !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to get API key. Please try again later !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
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
    }

    private void generateRazorPayOrderId(String API_Key) {

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.borrowerBaseUrl+"borrowerrazarpay/createOrderborrowerRes",
                null,
                response -> {

                    Log.e(TAG, response.toString());

                    try {

                        if (response.getString("msg").trim().contains("found")) {
                            Intent intent = new Intent(getContext(), RazorPayPaymentActivity.class);
                            intent.putExtra("amount", String.valueOf(amount));
                            intent.putExtra("orderId", response.getString("order_id"));
                            intent.putExtra("API_Key", API_Key);
                            startActivityForResult(intent, AppConstant.ADD_MONEY_ONLINE);

                        } else {
                            progress_bar.setVisibility(View.GONE);
                            showSnackBar("Failed to generate orderId. Please try again later !!", R.color.red);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        progress_bar.setVisibility(View.GONE);
                        showSnackBar("Failed to generate orderId. Please try again later !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to generate orderId. Please try again later !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progress_bar.setVisibility(View.GONE);

        if (requestCode == AppConstant.ADD_MONEY_ONLINE){
            if (resultCode == RESULT_OK){
                Log.e(TAG, data.getStringExtra("data"));
                updateUserPayment(data.getStringExtra("data"));
            }
            else {
                Log.e(TAG, data.getStringExtra("data"));
                progress_bar.setVisibility(View.GONE);
                showSnackBar("Failed to add money online !!", R.color.red);
            }
        }
    }

    private void updateUserPayment(String paymentResultObject){

        progress_bar.setVisibility(View.VISIBLE);

        try {

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl+"borrowerrazarpay/responseBorrowerres",
                    new JSONObject(paymentResultObject),
                    response -> {

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){
                                showSnackBar("Payment Added successfully !!", R.color.green);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progress_bar.setVisibility(View.GONE);
                            showSnackBar("Failed to update payment. Please try again later !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to update payment. Please try again later !!", R.color.red);
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
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
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBSoftApprovalFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }



}






