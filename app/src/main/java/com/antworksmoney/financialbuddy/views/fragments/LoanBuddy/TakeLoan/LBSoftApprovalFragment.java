package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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


public class LBSoftApprovalFragment extends Fragment {

    public LBSoftApprovalFragment() {
        // Required empty public constructor
    }


    public static LBSoftApprovalFragment newInstance() {
        return new LBSoftApprovalFragment();
    }

    private CoordinatorLayout snackBarView;

    private LinearLayout successLayout, faliureLayout;

    private Button continueButton;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBSoftApprovalFragment";

    private TextView loanAmountTextView;

    private Button continueAnywaysButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbsoft_approval, container, false);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        successLayout = rootView.findViewById(R.id.successLayout);

        faliureLayout = rootView.findViewById(R.id.faliureLayout);

        continueButton = rootView.findViewById(R.id.continueButton);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        loanAmountTextView = rootView.findViewById(R.id.loanAmountTextView);

        continueAnywaysButton = rootView.findViewById(R.id.continueAnywaysButton);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        continueButton.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent,LBKYCUploadFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        continueAnywaysButton.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent,LBKYCUploadFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("11")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "10");
            editor.apply();
        }

        fetchResult();

        return rootView;
    }

    private void fetchResult(){

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("proposal_id", mSharedPreferences.getString("proposalId",""));
        params.put("approve_loan_amount", mSharedPreferences.getString("loanAmount",""));

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerloan/softApprovelloanaccept",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {

                        if (response.getString("status").trim().equalsIgnoreCase("1")){
                            loanAmountTextView.setText("₹"+ mSharedPreferences.getString("loanAmount",""));
                            successLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            faliureLayout.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        faliureLayout.setVisibility(View.VISIBLE);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            faliureLayout.setVisibility(View.VISIBLE);
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
    }


}
