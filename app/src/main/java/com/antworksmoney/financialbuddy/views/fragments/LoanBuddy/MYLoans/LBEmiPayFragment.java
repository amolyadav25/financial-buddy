package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.RazorPayPaymentActivity;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class LBEmiPayFragment extends Fragment {

    public LBEmiPayFragment() {
        // Required empty public constructor
    }


    public static LBEmiPayFragment newInstance(LoanInfoEntity entity) {
        mLoanInfoEntity = entity;
        return new LBEmiPayFragment();
    }

    private static LoanInfoEntity mLoanInfoEntity;

    private Toolbar mToolbar;

    private TextView emiPayButton, foreClosurePayButton;

    private TextView emiAmount, foreClosureAmount;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBEmiPayFragment";

    private SharedPreferences mSharedPreferences;

    private CoordinatorLayout snackBarView;

    HashMap<String, String> postParams = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbemi_pay, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        emiPayButton = rootView.findViewById(R.id.emiPayButton);

        foreClosurePayButton = rootView.findViewById(R.id.foreClosurePayAmount);

        emiAmount = rootView.findViewById(R.id.emiAmount);

        foreClosureAmount = rootView.findViewById(R.id.foreClosureAmount);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        foreClosurePayButton.setOnClickListener(view -> {
            if (foreClosureAmount.getText().toString().trim().replace("₹","").trim().equalsIgnoreCase("0")){
                showSnackBar("Invalid Amount !!", R.color.red);
            }
            else {
                generateRazorPayOrderId(foreClosureAmount.getText().toString().trim().replace("₹","").trim(), true);
            }
        });

        emiPayButton.setOnClickListener(view ->
                generateRazorPayOrderId(emiAmount.getText().toString().trim().replace("₹","").trim(), false));



        fetchAmounts();

        return rootView;
    }

    private void fetchAmounts(){

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Map<String, String> params = new HashMap<>();
            params.put("emi_id", mLoanInfoEntity.getLoanId());
            params.put("bid_registration_id",mLoanInfoEntity.getbIdRegistrationId());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/emiPayment",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            emiAmount.setText("₹" + response.getJSONObject("myloanDetails").getString("emi_amount_to_pay"));


                            foreClosureAmount.setText("₹" + response.getJSONObject("myloanDetails").getString("foreclosure_amount"));


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
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

    private void generateRazorPayOrderId(String amount, boolean foreClosure) {

        progress_bar.setVisibility(View.VISIBLE);

        try {


            if (foreClosure){
                postParams.put("emi_id", mLoanInfoEntity.getLoanId());
                postParams.put("bid_registration_id", mLoanInfoEntity.getbIdRegistrationId());
                postParams.put("foreclusore_amount", amount);
                postParams.put("is_foreclosure", "Y");

            }
            else {
                postParams.put("emi_id", mLoanInfoEntity.getLoanId());
                postParams.put("bid_registration_id", mLoanInfoEntity.getbIdRegistrationId());
                postParams.put("emi_amount_to_pay", amount);
                postParams.put("is_foreclosure", "N");
            }

            Log.e(TAG, new JSONObject(postParams).toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerrazarpay/createOrderidemiPayment",
                    new JSONObject(postParams),
                    response -> {

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("msg").trim().contains("found")) {
                                fetchAPIKey(amount,response.getString("order_id"));
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

        } catch (Exception e) {
            progress_bar.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    private void fetchAPIKey(String amount, String orderId ){

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "https://antworksp2p.com/p2papi/commonapi/getRazorpaykeyrepayment",
                null,
                response -> {

                    Log.e(TAG, response.toString());

                    try {

                        Intent intent = new Intent(getContext(), RazorPayPaymentActivity.class);
                        intent.putExtra("amount", amount);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("API_Key", response.getString("key"));
                        startActivityForResult(intent, AppConstant.ADD_MONEY_ONLINE);

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
                progress_bar.setVisibility(View.GONE);
                showSnackBar("Failed to pay EMI !!", R.color.red);
            }
        }
    }

    private void updateUserPayment(String paymentResultObject) {

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Log.e(TAG, new JSONObject(paymentResultObject).put("emiData", new JSONObject(postParams)).toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerrazarpay/paymentResponseEmi",
                    new JSONObject(paymentResultObject).put("emiData", new JSONObject(postParams)),
                    response -> {

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")) {
                                showSnackBar("EMI Paid successfully !!", R.color.green);
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

        } catch (Exception e) {
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


}
