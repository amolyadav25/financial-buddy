package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
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
import com.antworksmoney.financialbuddy.views.activities.LBStateCityFetch;
import com.antworksmoney.financialbuddy.views.activities.RegionalDataFetch;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile.LBProfileHome;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LBAddressDetails extends Fragment implements View.OnClickListener {

    public LBAddressDetails() {
        // Required empty public constructor
    }

    private static String mClassName;

    public static LBAddressDetails newInstance(String className) {
        mClassName = className;
        return new LBAddressDetails();
    }

    private RelativeLayout stateSelectorLayout, citySelectorLayout;

    private TextView stateTextView, cityTextView;

    private RelativeLayout snackBarView;

    private RelativeLayout progress_bar;

    private Button ProceedButton;

    private EditText et_reg_address, et_user_pincode;

    private static final String TAG = "LBAddressDetails";

    private SharedPreferences preferences;

    private Toolbar mToolbar;

    private ImageView bannerImage;

    private String stateCode = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbaddress_details, container, false);

        stateSelectorLayout = rootView.findViewById(R.id.stateSelectorLayout);

        citySelectorLayout = rootView.findViewById(R.id.citySelectorLayout);

        stateTextView = rootView.findViewById(R.id.stateTextView);

        cityTextView = rootView.findViewById(R.id.cityTextView);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        et_reg_address = rootView.findViewById(R.id.et_reg_address);

        et_user_pincode = rootView.findViewById(R.id.et_user_pincode);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        bannerImage = rootView.findViewById(R.id.bannerImage);

        preferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        Log.e(TAG, mClassName);

        if (mClassName.trim().equalsIgnoreCase("ProfileHome")){

            //this is when called from profile page
            bannerImage.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
            ProceedButton.setVisibility(View.GONE);
            progress_bar = LBProfileHome.getProgressBar();


            et_reg_address.setFocusableInTouchMode(false);
            stateSelectorLayout.setEnabled(false);
            citySelectorLayout.setEnabled(false);
            et_user_pincode.setFocusableInTouchMode(false);
        }
        else {

            if (!mClassName.trim().equalsIgnoreCase("ChangeRequest")){
                if (!preferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("9")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(AppConstant.LOAN_STATUS_TRACKER, "8");
                    editor.apply();
                }
            }

            ProceedButton.setVisibility(View.VISIBLE);
            progress_bar = rootView.findViewById(R.id.progress_bar);
        }

        getAddressDetails();

        citySelectorLayout.setOnClickListener(this);

        stateSelectorLayout.setOnClickListener(this);

        ProceedButton.setOnClickListener(this);

        return rootView;
    }

    private void getAddressDetails(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.borrowerBaseUrl + "borrowerinfo/myResidentalDetails",
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        et_reg_address.setText(jsonObject.getJSONObject("MyResidentalDetails").getString("r_address"));

                        cityTextView.setText(jsonObject.getJSONObject("MyResidentalDetails").getString("r_city"));

                        stateTextView.setText((jsonObject.getJSONObject("MyResidentalDetails")
                                .getString("r_state").trim().equalsIgnoreCase("null")) ?"": jsonObject.getJSONObject("MyResidentalDetails").getString("r_state"));

                        et_user_pincode.setText(jsonObject.getJSONObject("MyResidentalDetails").getString("r_pincode"));


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
                params.put("Authorization", preferences.getString("loginToken",""));
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.citySelectorLayout:
                if (!stateTextView.getText().toString().trim().equalsIgnoreCase("")){
                    Intent cityIntent = new Intent(getActivity(), LBStateCityFetch.class);
                    cityIntent.putExtra("stateName", stateTextView.getText().toString().trim());
                    cityIntent.putExtra("theme", R.style.MyAppbarTheme);
                    startActivityForResult(cityIntent, AppConstant.SELECT_REGIONAL_DATA);
                }
                else {
                    showSnackBar("Select state first !!",R.color.red);
                }
                break;

            case R.id.stateSelectorLayout:
                Intent stateIntent = new Intent(getActivity(), LBStateCityFetch.class);
                stateIntent.putExtra("stateName", "");
                stateIntent.putExtra("theme", R.style.MyAppbarTheme);
                startActivityForResult(stateIntent, AppConstant.SELECT_REGIONAL_DATA);
                break;

            case R.id.ProceedButton:
                if (et_reg_address.getText().toString().trim().equalsIgnoreCase("")){
                    et_reg_address.setError("Invalid Address !!");
                    et_reg_address.requestFocus();
                }
                else if (stateTextView.getText().toString().trim().equalsIgnoreCase("")){
                    stateTextView.setError("Invalid State");
                }
                else if (cityTextView.getText().toString().trim().equalsIgnoreCase("")){
                    cityTextView.setError("Invalid City");
                }
                else if (et_user_pincode.getText().toString().trim().equalsIgnoreCase("")){
                    et_user_pincode.setError("Invalid Pin-code");
                    et_user_pincode.requestFocus();
                }
                else {
                    if (mClassName.trim().equalsIgnoreCase("SalaryProcess")){
                        updateAddressDetails();
                    }
                    else {
                        requestDetailsUpdation();
                    }

                }
                break;


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.SELECT_REGIONAL_DATA) {
            if (resultCode == Activity.RESULT_OK) {
                if (((String) data.getSerializableExtra("name")).trim().contains("cityName")) {
                    cityTextView.setText(((String) data.getSerializableExtra("name")).trim().split("=")[1]);
                } else if (((String) data.getSerializableExtra("name")).trim().contains("stateName")) {
                    stateTextView.setText(((String) data.getSerializableExtra("name")).trim().split("=")[1]);
                    cityTextView.setText("");
                    et_user_pincode.setText("");
                    stateCode = ((String) data.getSerializableExtra("stateCode")).trim();
                }
            }
        }

    }

    private void updateAddressDetails(){

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("r_address", et_reg_address.getText().toString().trim());
        params.put("r_state", stateCode);
        params.put("r_city", cityTextView.getText().toString().trim());
        params.put("residence_type", "permanent");
        params.put("r_pincode",et_user_pincode.getText().toString().trim());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/addAddress",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {

                        showSnackBar("Address saved successfully !!", R.color.green);

                    }catch (Exception e){
                        e.printStackTrace();
                        showSnackBar("Failed to save !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to Login !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", preferences.getString("loginToken",""));
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

    private void requestDetailsUpdation(){

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("address1", et_reg_address.getText().toString().trim());
        params.put("state_code", stateCode);
        params.put("city", cityTextView.getText().toString().trim());
        params.put("pincode",et_user_pincode.getText().toString().trim());

        Log.e(TAG, params.toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerrequest/requestChangeaddress",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {

                        showSnackBar("Updated successfully !!", R.color.green);

                    }catch (Exception e){
                        e.printStackTrace();
                        showSnackBar("Failed to save !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to Login !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", preferences.getString("loginToken",""));
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


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {

                if (getActivity() != null && backgroundColor == R.color.green
                        && mClassName.trim().equalsIgnoreCase("SalaryProcess")){

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBPaymentRegistrationFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
                else {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        snackbar.show();
    }
}
