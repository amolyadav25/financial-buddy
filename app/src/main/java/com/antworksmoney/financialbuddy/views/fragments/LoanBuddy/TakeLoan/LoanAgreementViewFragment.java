package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
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
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication.LBOTPVerificationFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoanAgreementViewFragment extends Fragment {

    public LoanAgreementViewFragment() {
        // Required empty public constructor
    }


    public static LoanAgreementViewFragment newInstance(boolean toMoveToNextPage) {
        mToMoveToNextPage = toMoveToNextPage;
        return  new LoanAgreementViewFragment();
    }

    private Toolbar mToolbar;

    private CoordinatorLayout snackBarView;

    private TextView loanAgreementText;

    private RelativeLayout progress_bar;

    private JSONObject jsonObject;

    private static boolean mToMoveToNextPage;

    private Button loanAgreementAcceptButton;

    private static final String TAG = "LoanAgreementViewFragme";

    private SharedPreferences mSharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_loan_agreement_view, container, false);

        mToolbar  =  rootView.findViewById(R.id.mToolbar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        loanAgreementText = rootView.findViewById(R.id.agreementText);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        loanAgreementAcceptButton = rootView.findViewById(R.id.loanAgreementAcceptButton);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        loanAgreementAcceptButton.setOnClickListener(view -> acceptLoanAgreement());

        if (!mToMoveToNextPage){

            try {
                jsonObject = new JSONObject(mSharedPreferences.getString(AppConstant.LOAN_AGREEMENT_OBJECT,""));
            }catch (Exception e){
                e.printStackTrace();
            }
            loanAgreementAcceptButton.setVisibility(View.GONE);
        }
        else {

            try {
                jsonObject = new JSONObject(mSharedPreferences.getString(AppConstant.LOAN_DETAILS,""));
            }catch (Exception e){
                e.printStackTrace();
            }

            if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("18")) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(AppConstant.LOAN_STATUS_TRACKER, "17");
                editor.apply();
            }
        }

        showLoanAgreement();

        return rootView;
    }

    private void showLoanAgreement(){

        progress_bar.setVisibility(View.VISIBLE);

        try {


            Map<String, String> params = new HashMap<>();
            params.put("bid_registration_id", jsonObject.getString("bid_registration_id"));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/viewLoanaggrement",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        try {

                            loanAgreementText.setText(Html.fromHtml(
                                    response.getJSONObject("msg").getString("agreement")));



                        }catch (Exception e){
                            e.printStackTrace();
                            showSnackBar("Failed to get Loan Agreement !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to get Loan Agreement !!", R.color.red);
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
            showSnackBar("Failed to get details !!", R.color.red);
            e.printStackTrace();
        }
    }


    private void acceptLoanAgreement(){

        progress_bar.setVisibility(View.VISIBLE);

        try {

            jsonObject = new JSONObject(mSharedPreferences.getString(AppConstant.LOAN_DETAILS,""));

            Map<String, String> params = new HashMap<>();
            params.put("bid_registration_id", jsonObject.getString("bid_registration_id"));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/sendOtpsignature",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){
                                showSnackBar("Agreement successful !!", R.color.green);
                            }
                            else {
                                showSnackBar("Failed to Agree !!", R.color.red);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            showSnackBar("Failed to Agree !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to Agree !!", R.color.red);
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
            showSnackBar("Failed to get details !!", R.color.red);
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
                    transaction.replace(R.id.homeParent, LBSignOTPVerificationFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }


}
