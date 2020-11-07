package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class LBDateOfBirthFragment extends Fragment {

    public LBDateOfBirthFragment() {
        // Required empty public constructor
    }


    public static LBDateOfBirthFragment newInstance() {
        return new LBDateOfBirthFragment();
    }

    private TextView dateOfBirthSelector;

    private AppCompatActivity mActivity;

    private ProgressBar progressBar, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static final String TAG = "DateOfBirthFragment";

    private LinearLayout dateOfBirthSelectorLayout;

    private ImageView dateOfBirthImage;

    private TextView loanHeading;

    private CoordinatorLayout snackBarView;

    private String selectedDateOfBirth = "";

    private SharedPreferences mSharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_of_birth, container, false);

        mActivity = (AppCompatActivity) getActivity();

        dateOfBirthSelector = rootView.findViewById(R.id.dateOfBirthSelector);

        progressBar = rootView.findViewById(R.id.progressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        dateOfBirthSelectorLayout = rootView.findViewById(R.id.dateOfBirthSelectorLayout);

        dateOfBirthImage = rootView.findViewById(R.id.dateOfBirthImage);

        loanHeading  = rootView.findViewById(R.id.loanHeading);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        progressBar.setVisibility(View.INVISIBLE);

        dateOfBirthSelectorLayout.setOnClickListener(view -> DateDialog());

        nextButtonForQuestions.setOnClickListener(v -> {
            if (dateOfBirthSelector.getText().toString().equalsIgnoreCase("Date of Birth")) {
                ((HomeActivity) mActivity).showSnackBar("Please select Date of Birth !!!");
            } else {
                saveDateOfBirth();
            }
        });

        previousButtonForQuestions.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        loanHeading.setText(mSharedPreferences.getString(AppConstant.LOAN_NAME, ""));

        dateOfBirthImage.setOnClickListener(v -> DateDialog());

        journeyCompletedProgressBar.setProgress(23);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("4")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "3");
            editor.apply();
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!selectedDateOfBirth.trim().equalsIgnoreCase("")){
            dateOfBirthSelector.setText(selectedDateOfBirth);
        }
    }

    public void DateDialog() {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mMonth, mDay;

        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(mActivity);
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(2000, mMonth, mDay);
        datePickerDialogBuilder.maxDate(2000, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback((view, year, monthOfYear, dayOfMonth) -> {
            if (String.valueOf(year).trim().contains(",")) {
                year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
            }
            dateOfBirthSelector.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            selectedDateOfBirth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

        });

        datePickerDialogBuilder.build().show();
    }

    private void saveDateOfBirth() {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("dob", dateOfBirthSelector.getText().toString().trim());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/dobUpdate",
                new JSONObject(params),
                response -> {

                    progressBar.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")){
                            showSnackBar("Date Of Birth added successfully !!", R.color.green);
                        }
                        else {
                            showSnackBar("Failed to add !!", R.color.red);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        try {
                            showSnackBar("Failed to add !!", R.color.red);
                        }catch (Exception ignored){}

                    }

                }, error -> {
            Log.e(TAG, error.toString());
            progressBar.setVisibility(View.INVISIBLE);
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

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBMaritalStatusFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }
}
