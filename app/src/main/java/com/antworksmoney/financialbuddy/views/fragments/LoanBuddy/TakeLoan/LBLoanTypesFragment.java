package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LBLoanTypesFragment extends Fragment {

    public LBLoanTypesFragment() {
        // Required empty public constructor
    }

    public static LBLoanTypesFragment newInstance() {
        return new LBLoanTypesFragment();
    }

    private RelativeLayout consumerLoanLayout, cashLoanLayout;

    private Toolbar mToolbar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBLoanTypesFragment";

    private RelativeLayout progress_bar;

    private String pageType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lblend_money_loan_types, container, false);

        cashLoanLayout = rootView.findViewById(R.id.cashLoanLayout);

        consumerLoanLayout = rootView.findViewById(R.id.consumerLoanLayout);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        consumerLoanLayout.setOnClickListener(view -> {
            pageType = "Consumer";
            getProductTypes();
        });

        cashLoanLayout.setOnClickListener(view -> {
            pageType = "Cash";
            getProductTypes();
        });

        return rootView;
    }

    private void getProductTypes(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getLoantype",
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response);

                    try {

                        JSONObject responseObject = new JSONObject(response);

                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString(AppConstant.LOAN_TYPE_OBJECT, responseObject.toString());
                        editor.putString(AppConstant.LOAN_NAME, pageType + " Loan");
                        editor.apply();

                       if (pageType.trim().equalsIgnoreCase("consumer")){
                           if (getActivity() != null){
                               FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                               transaction.replace(R.id.homeParent, LBConsumerLoanFragment.newInstance());
                               transaction.addToBackStack(null).commit();
                           }
                       }
                       else {
                           if (getActivity() != null){
                               FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                               transaction.replace(R.id.homeParent, LBMicroLoanFragment.newInstance());
                               transaction.addToBackStack(null).commit();
                           }
                       }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e("ERROR", "error => " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(dataRequest);
    }


}
