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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LBLoanConfirmationFragment extends Fragment {

    public LBLoanConfirmationFragment() {
        // Required empty public constructor
    }

    private  JSONObject mRJsonObject;

    public static LBLoanConfirmationFragment newInstance() {
        return new LBLoanConfirmationFragment();
    }

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBLoanCnfrmFragment";

    private CoordinatorLayout mSnackBarView;

    private TextView loanAmountTextView,
            tenureTextView,
            interestRateTextView,
            emiAmountTextView,
            emiDateTextView,
            repaymentAmountTextView,
            processingFeeTextView,
            loanStatusTextView;

    private Button confirmLoanButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_consumer_loan, container, false);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mSnackBarView = rootView.findViewById(R.id.snackBarView);

        loanAmountTextView = rootView.findViewById(R.id.loanAmountTextView);

        tenureTextView = rootView.findViewById(R.id.tenureTextView);

        interestRateTextView = rootView.findViewById(R.id.interestRateTextView);

        emiAmountTextView = rootView.findViewById(R.id.emiAmountTextView);

        emiDateTextView = rootView.findViewById(R.id.emiDateTextView);

        repaymentAmountTextView = rootView.findViewById(R.id.repaymentAmountTextView);

        processingFeeTextView = rootView.findViewById(R.id.processingFeeTextView);

        loanStatusTextView = rootView.findViewById(R.id.loanStatusTextView);

        confirmLoanButton = rootView.findViewById(R.id.confirmLoanButton);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        confirmLoanButton.setOnClickListener(view -> confirmLoan());

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        try {

            if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("17")) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(AppConstant.LOAN_STATUS_TRACKER, "16");
                editor.apply();
            }

            mRJsonObject = new JSONObject(mSharedPreferences.getString(AppConstant.LOAN_DETAILS, ""));

            loanAmountTextView.setText(mRJsonObject.getString("loan_amount"));

            tenureTextView.setText(mRJsonObject.getString("tenor"));

            interestRateTextView.setText(mRJsonObject.getString("interest_rate"));

            emiAmountTextView.setText(mRJsonObject.getString("emi"));

            emiDateTextView.setText(mRJsonObject.getString("emi_date"));

            repaymentAmountTextView.setText(mRJsonObject.getString("total_repayment_amount"));

            processingFeeTextView.setText(mRJsonObject.getString("processing_fee"));

            loanStatusTextView.setText(mRJsonObject.getString("loan_status"));

        }catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }


    private void confirmLoan(){

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Map<String, String> params = new HashMap<>();
            params.put("proposal_id", mSharedPreferences.getString("proposalId",""));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/loanConfirmation",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("msg").trim().equalsIgnoreCase("Loan Confirmed successfully")){
                               showSnackBar("Loan confirmed successfully !!", R.color.green);
                            }
                            else {
                                showSnackBar("Failed to confirm loan !!", R.color.red);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            showSnackBar("Failed to confirm loan !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to confirm loan !!", R.color.red);
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
        final Snackbar snackbar = Snackbar.make(mSnackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LoanAgreementViewFragment.newInstance( true));
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }



}
