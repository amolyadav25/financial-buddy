package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LBNewPasswordFragment extends Fragment {

    public LBNewPasswordFragment() {
        // Required empty public constructor
    }


    public static LBNewPasswordFragment newInstance(JSONObject inputObject, boolean mCalledFromProfilePage, String token) {
        calledFromProfilePage = mCalledFromProfilePage;
        mToken = token;
        mInputObject = inputObject;
        return  new LBNewPasswordFragment();
    }

    private Toolbar mToolbar;

    private Button ProceedButton;

    private RelativeLayout snackBarView;

    private EditText et_user_password, et_user_new_password;

    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LBChangeMobileFragment";

    private RelativeLayout progress_bar;

    private TextView toolBarHeadingText;

    private RequestQueue mRequestQueue;

    private static JSONObject mInputObject;

    private static boolean calledFromProfilePage;

    private static String mToken;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbchnage_password, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        toolBarHeadingText = rootView.findViewById(R.id.toolBarHeadingText);

        mRequestQueue = Volley.newRequestQueue(getContext());

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        toolBarHeadingText.setText("Change Password");

        et_user_password = rootView.findViewById(R.id.et_user_password);

        et_user_new_password = rootView.findViewById(R.id.et_user_new_password);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        ProceedButton.setOnClickListener(view -> {
            if (et_user_password.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_password.setError("Invalid Password !!");
                et_user_password.requestFocus();
            }
            else if (!et_user_new_password.getText().toString().trim().equalsIgnoreCase(et_user_password.getText().toString().trim())) {
                et_user_new_password.setError("Passwords don't match !!");
                et_user_new_password.requestFocus();
            }
            else {
                chnageUserPassword();
            }
        });

        return rootView;
    }


    private void chnageUserPassword(){

        try {

            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("mobile",mInputObject.getString("updatedNumber"));
            params.put("otp",mInputObject.getString("otp"));
            params.put("password", AppConstant.getPasswordHash(et_user_password.getText().toString().trim()));
            params.put("cpassword", AppConstant.getPasswordHash(et_user_new_password.getText().toString().trim()));


            String url = "";
            if (calledFromProfilePage){
                url = AppConstant.borrowerBaseUrl + "borrowerrequest/changepassword";
            }
            else {
                url = AppConstant.borrowerBaseUrl + "borrowerrequest/changePasswordforgot";
            }


            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("1")){

                                showSnackBar("Password updated successfully !!", R.color.green);
                            }
                            else {
                                showSnackBar("Failed to Update Password !!", R.color.red);
                            }

                        }catch (Exception e){
                            showSnackBar("Failed to Update Password !!", R.color.red);
                            e.printStackTrace();
                        }

                    }, error -> {
                showSnackBar("Failed to Update Password !!", R.color.red);
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    if (calledFromProfilePage){
                        params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                    }
                    else {
                        params.put("Authorization", mToken);
                    }                    params.put("Content-Type", "application/json");
                    Log.e(TAG,  params.toString());
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(request);

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

                        Intent intent = new Intent(getContext(), HomeActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }
        });
        snackbar.show();
    }


}
