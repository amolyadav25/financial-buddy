package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
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
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LoanAgreementViewFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LBLoanDetailsFragment extends Fragment {

    public LBLoanDetailsFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity loanInfoEntity;

    public static LBLoanDetailsFragment newInstance(LoanInfoEntity entity) {
        loanInfoEntity = entity;
        return new LBLoanDetailsFragment();
    }

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBLoanCnfrmFragment";

    private CoordinatorLayout mSnackBarView;

    private TextView loanAccountNumberTextView,
            tenureTextView,
            disbursedAmountTextview,
            loanAmountTextView, loanIdTextView;

    private Button loanStateMentButton, loanAgreementButton, confirmLoanButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_loan_details, container, false);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mSnackBarView = rootView.findViewById(R.id.snackBarView);

        tenureTextView = rootView.findViewById(R.id.tenureTextView);

        loanAccountNumberTextView = rootView.findViewById(R.id.loanAccountNumberTextView);

        disbursedAmountTextview = rootView.findViewById(R.id.disbursedAmountTextview);

        loanAmountTextView = rootView.findViewById(R.id.loanAmountTextView);

        loanIdTextView = rootView.findViewById(R.id.loanIdTextView);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        loanStateMentButton = rootView.findViewById(R.id.loanStateMentButton);

        loanAgreementButton = rootView.findViewById(R.id.loanAgreementButton);

        confirmLoanButton = rootView.findViewById(R.id.confirmLoanButton);


        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        loanIdTextView.setText("View Details (" + loanInfoEntity.getAccount() + ")");

        loanStateMentButton.setOnClickListener(view -> {
            try {
                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, MyLoanStateMentFragment.newInstance(
                            new JSONObject().put("loan_no", loanInfoEntity.getAccount())));
                    transaction.addToBackStack(null).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        loanAgreementButton.setOnClickListener(view -> {
            try {
                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LoanAgreementViewFragment.newInstance(false));
                    transaction.addToBackStack(null).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        });

        try {
            Log.e(TAG, loanInfoEntity.getState().trim());
            if (loanInfoEntity.getState().trim().equalsIgnoreCase("open")){
                confirmLoanButton.setBackground(getActivity().getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        getLoanDetails();

        return rootView;
    }


    private void getLoanDetails(){

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Map<String, String> params = new HashMap<>();
            params.put("bid_registration_id", loanInfoEntity.getbIdRegistrationId());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/myLoanDetails",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            loanAccountNumberTextView.setText(response.getJSONObject("myloanDetails").getString("loan_no"));

                            loanAmountTextView.setText(response.getJSONObject("myloanDetails").getString("bid_loan_amount"));

                            tenureTextView.setText(response.getJSONObject("myloanDetails").getString("accepted_tenor"));

                            disbursedAmountTextview.setText(response.getJSONObject("myloanDetails").getString("disburse_amount"));

                           SharedPreferences.Editor editor = mSharedPreferences.edit();
                           editor.putString(AppConstant.LOAN_AGREEMENT_OBJECT,  new JSONObject().put(
                                   "bid_registration_id", loanInfoEntity.getbIdRegistrationId()).toString());
                           editor.apply();

                        }catch (Exception e){
                            e.printStackTrace();
                            showSnackBar("Failed to get loan details !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to get loan details !!", R.color.red);
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
            showSnackBar("Failed to get details !!", R.color.red);
            e.printStackTrace();
        }
    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(mSnackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, null);
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }



}
