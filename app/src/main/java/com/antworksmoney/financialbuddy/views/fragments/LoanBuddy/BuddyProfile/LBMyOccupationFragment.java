package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LBMyOccupationFragment extends Fragment {


    public LBMyOccupationFragment() {
        // Required empty public constructor
    }


    public static LBMyOccupationFragment newInstance() {
        return new LBMyOccupationFragment();
    }

    private TextView et_reg_occupation_type,
            et_reg_company_type,
            et_reg_company_name,
            et_reg_total_experice,
            et_reg_current_emi,
            et_reg_salary_process,
            et_reg_net_monthly_income,
            et_reg_turnoer_lat_year;

    private RelativeLayout progress_bar;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBMyOccupationFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbmy_occupation, container, false);

        progress_bar = LBProfileHome.getProgressBar();

        et_reg_occupation_type = rootView.findViewById(R.id.et_reg_occupation_type);

        et_reg_company_type = rootView.findViewById(R.id.et_reg_company_type);

        et_reg_company_name = rootView.findViewById(R.id.et_reg_company_name);

        et_reg_total_experice = rootView.findViewById(R.id.et_reg_total_experice);

        et_reg_current_emi = rootView.findViewById(R.id.et_reg_current_emi);

        et_reg_salary_process = rootView.findViewById(R.id.et_reg_salary_process);

        et_reg_net_monthly_income = rootView.findViewById(R.id.et_reg_net_monthly_income);

        et_reg_turnoer_lat_year = rootView.findViewById(R.id.et_reg_turnoer_lat_year);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        fetchOccupationalInfo();


        return rootView;
    }


    private void fetchOccupationalInfo(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.borrowerBaseUrl + "borrowerinfo/myOccupationDetails",
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response);

                    try {

                        JSONObject jsonObject = new JSONObject(response).getJSONObject("MyOccupationDetails");

                        et_reg_occupation_type.setText(jsonObject.getString("occuption_type").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("occuption_type"));

                        et_reg_company_type.setText(jsonObject.getString("company_type").trim().equalsIgnoreCase("")?"NA":jsonObject.getString("company_type"));

                        et_reg_company_name.setText(jsonObject.getString("company_name").trim().equalsIgnoreCase("")?"NA":jsonObject.getString("company_name"));

                        et_reg_total_experice.setText(jsonObject.getString("total_experience").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("total_experience"));

                        et_reg_current_emi.setText(jsonObject.getString("current_emis").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("current_emis"));

                        et_reg_salary_process.setText(jsonObject.getString("salary_process").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("salary_process"));

                        et_reg_net_monthly_income.setText(jsonObject.getString("net_monthly_income").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("net_monthly_income"));

                        et_reg_turnoer_lat_year.setText(jsonObject.getString("turnover_last_year").trim().equalsIgnoreCase("null")?"NA":jsonObject.getString("turnover_last_year"));

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

}
