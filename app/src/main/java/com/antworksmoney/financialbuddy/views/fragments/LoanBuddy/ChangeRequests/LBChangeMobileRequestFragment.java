package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests;

import android.content.Context;
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
import android.widget.RelativeLayout;

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

public class LBChangeMobileRequestFragment extends Fragment {

    public LBChangeMobileRequestFragment() {
        // Required empty public constructor
    }

    public static LBChangeMobileRequestFragment newInstance() {
        return new LBChangeMobileRequestFragment();
    }

    private Toolbar mToolbar;

    private Button ProceedButton;

    private RelativeLayout snackBarView;

    private EditText et_user_phone_number;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBChangeMobileFragment";

    private RelativeLayout progress_bar;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbchnage_mobile, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);



        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        ProceedButton.setOnClickListener(view -> {
            if (et_user_phone_number.getText().toString().trim().length()<10){
                et_user_phone_number.setError("Invalid Number !!");
                et_user_phone_number.requestFocus();
            }
            else {
                changeUserPhoneNumber();
            }
        });


        return rootView;
    }




    private void changeUserPhoneNumber(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("mobile",et_user_phone_number.getText().toString().trim());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerrequest/requestChangemobile",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){

                                showSnackBar("OTP sent successfully !!", R.color.green);
                            }
                            else {
                                showSnackBar("Failed to send OTP !!", R.color.red);
                            }

                        }catch (Exception e){
                            showSnackBar("Failed to send OTP !!", R.color.red);
                            e.printStackTrace();
                        }

                    }, error -> {
                showSnackBar("Failed to send OTP !!", R.color.red);
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                    params.put("Content-Type", "application/json");
                    Log.e(TAG,  params.toString());
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

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    try {

                        JSONObject inputJsonObject = new JSONObject();
                        inputJsonObject.put("updatedNumber", et_user_phone_number.getText().toString().trim());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeParent, LBChangeMobileVerifyOTPFragment.newInstance(inputJsonObject));
                        transaction.commit();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        });
        snackbar.show();
    }



}
