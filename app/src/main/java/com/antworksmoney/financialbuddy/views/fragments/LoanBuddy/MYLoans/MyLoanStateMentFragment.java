package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.LoanSummaryAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyLoanStateMentFragment extends Fragment {

    public MyLoanStateMentFragment() {
        // Required empty public constructor
    }

    private static JSONObject loanInfoEntity;

    public static MyLoanStateMentFragment newInstance(JSONObject entity) {
        loanInfoEntity = entity;
        return new MyLoanStateMentFragment();
    }

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBLoanCnfrmFragment";

    private CoordinatorLayout mSnackBarView;

    private TextView accountNameTextView,
            productTextView,
            agreementNumberTextView,
            agreementDateTextView,
            amountFinancedTextView,
            tenureTextView,
            interestRateTextview;

    private RecyclerView myEMISList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_loan_state_ment, container, false);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mSnackBarView = rootView.findViewById(R.id.snackBarView);

        tenureTextView = rootView.findViewById(R.id.tenureTextView);

        accountNameTextView = rootView.findViewById(R.id.accountNameTextView);

        productTextView = rootView.findViewById(R.id.productTextView);

        agreementNumberTextView = rootView.findViewById(R.id.agreementNumberTextView);

        agreementDateTextView = rootView.findViewById(R.id.agreementDateTextView);

        amountFinancedTextView = rootView.findViewById(R.id.amountFinancedTextView);

        tenureTextView = rootView.findViewById(R.id.tenureTextView);

        interestRateTextview = rootView.findViewById(R.id.interestRateTextview);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        myEMISList = rootView.findViewById(R.id.myEMISList);


        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });




        getMyLoanStatement();

        return rootView;
    }


    private void getMyLoanStatement() {

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Map<String, String> params = new HashMap<>();
            params.put("loan_no", loanInfoEntity.getString("loan_no"));

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/myloanStatement",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            accountNameTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("name"));

                            productTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("prodtuc_type"));

                            agreementNumberTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("loan_no"));

                            agreementDateTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("borrower_signature_date"));

                            amountFinancedTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("bid_loan_amount"));

                            tenureTextView.setText(response.getJSONObject("MyloanStatement")
                                    .getString("accepted_tenor"));

                            interestRateTextview.setText(response.getJSONObject("MyloanStatement")
                                    .getString("interest_rate"));

                            loadList(response.getJSONArray("emi_list"));

                        } catch (Exception e) {
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
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                    params.put("Content-Type", "application/json");
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
            showSnackBar("Failed to get details !!", R.color.red);
            e.printStackTrace();
        }
    }

    private void loadList(JSONArray jsonArray){

        myEMISList.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            ArrayList<LoanInfoEntity> loanInfoEntityArrayList = new ArrayList<>();
            for (int i=0; i<jsonArray.length(); i++){
                LoanInfoEntity entity = new LoanInfoEntity();
                entity.setEmi_date(jsonArray.getJSONObject(i).getString("emi_date"));
                entity.setEmi_balance(jsonArray.getJSONObject(i).getString("emi_balance"));
                entity.setEMI(jsonArray.getJSONObject(i).getString("emi_amount"));
                entity.setEmi_interest(jsonArray.getJSONObject(i).getString("emi_interest"));
                entity.setEmi_principal(jsonArray.getJSONObject(i).getString("emi_principal"));
                loanInfoEntityArrayList.add(entity);
            }

            LoanSummaryAdapter adapter = new LoanSummaryAdapter(getContext(),loanInfoEntityArrayList,myEMISList);
            myEMISList.setAdapter(adapter);

        }catch (Exception e){
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
                if (getActivity() != null && backgroundColor == R.color.green) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, null);
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }


}
