package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
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
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class LBCoBorrowerFragment extends Fragment implements View.OnClickListener {

    public LBCoBorrowerFragment() {
        // Required empty public constructor
    }

    private static boolean mToMoveToNextPage;

    public static LBCoBorrowerFragment newInstance(boolean toMoveToNextPage) {
        mToMoveToNextPage = toMoveToNextPage;
        return new LBCoBorrowerFragment();
    }

    private Toolbar mToolbar;

    private EditText et_reg_fname,
            et_user_phone_number,
            et_user_pan_card,
            et_reg_relation;

    private TextView et_user_dob;

    private RelativeLayout progress_bar;

    private Button ProceedButton, skipButton;

    private SharedPreferences mSharedPreferences;

    private RequestQueue mRequestQueue;

    private static final String TAG = "LBSignUpFragment";

    private CoordinatorLayout snackBarView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_buddy_co_applicant, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_dob = rootView.findViewById(R.id.et_user_dob);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        et_reg_relation = rootView.findViewById(R.id.et_reg_relation);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        et_user_pan_card = rootView.findViewById(R.id.et_user_pan_card);

        skipButton = rootView.findViewById(R.id.skipButton);

        mRequestQueue = Volley.newRequestQueue(getActivity());

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        if (!mToMoveToNextPage){
            skipButton.setVisibility(View.GONE);
        }
        else {
            if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("13")) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(AppConstant.LOAN_STATUS_TRACKER, "12");
                editor.apply();
            }
        }

        skipButton.setOnClickListener(this);

        ProceedButton.setOnClickListener(this);

        et_user_dob.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ProceedButton) {
            if (et_reg_fname.getText().toString().trim().equalsIgnoreCase("")) {
                et_reg_fname.setError("Invalid Name !!");
                et_reg_fname.requestFocus();
            } else if (et_user_dob.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_dob.setError("Invalid Date Of Birth!!");
                et_user_dob.requestFocus();
            } else if (et_user_phone_number.getText().toString().trim().length() < 10) {
                et_user_phone_number.setError("Invalid Phone Number !!");
                et_user_phone_number.requestFocus();
            } else if (et_user_pan_card.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_pan_card.setError("Invalid PAN !!");
                et_user_pan_card.requestFocus();
            } else if (et_reg_relation.getText().toString().trim().equalsIgnoreCase("")) {
                et_reg_relation.setError("Invalid Relation !!");
                et_reg_relation.requestFocus();
            } else if (et_user_pan_card.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_pan_card.setError("Passwords don't match !!");
                et_user_pan_card.requestFocus();
            } else {
                saveCoApplicantDetails();
            }
        } else if (view.getId() == R.id.et_user_dob) {
            DateDialog();
        }
        else if (view.getId() == R.id.skipButton){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, LBBankDetailsFragment.newInstance(false));
            transaction.addToBackStack(null).commit();
        }

    }

    public void DateDialog() {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mMonth, mDay;

        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(getActivity());
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(2000, mMonth, mDay);
        datePickerDialogBuilder.maxDate(2000, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback((view, year, monthOfYear, dayOfMonth) -> {
            if (String.valueOf(year).trim().contains(",")) {
                year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
            }
            et_user_dob.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

        });

        datePickerDialogBuilder.build().show();
    }


    private void saveCoApplicantDetails() {

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("full_name", et_reg_fname.getText().toString().trim());
        params.put("dob", et_user_dob.getText().toString().trim());
        params.put("mobile", et_user_phone_number.getText().toString().trim());
        params.put("relation", et_reg_relation.getText().toString().trim());
        params.put("pan", et_user_pan_card.getText().toString().trim());

        Log.e(TAG, new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/coApplicant",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Added Successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to Update !!", R.color.red);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar("Failed to Update !!", R.color.red);


                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to Update !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                Log.e(TAG, params.toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);

    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    if (mToMoveToNextPage){
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.homeParent, LBBankDetailsFragment.newInstance(false));
                        transaction.addToBackStack(null).commit();
                    }
                    else {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }

                }
            }
        });
        snackbar.show();
    }


}
