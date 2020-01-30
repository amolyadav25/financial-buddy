package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LBSalaryProcessFragment extends Fragment {

    public LBSalaryProcessFragment() {
        // Required empty public constructor
    }

    public static LBSalaryProcessFragment newInstance() {
        return new LBSalaryProcessFragment();
    }


    private Spinner maritalStatusSelector;

    private ArrayAdapter<String> adapterMaritalStatus;

    private ArrayList<String> maritalStatusList;

    private Activity mActivity;

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private AutoCompleteTextView bankNameSelector;

    private static final String TAG = "SalaryProcessFragment";

    private EditText salaryEditText, emiEditText;

    private TextView loanHeading;

    private CoordinatorLayout snackBarView;

    private SharedPreferences mSharedPreferences;

    private ProgressBar salaryProcessProgressBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_salary_process, container, false);

        mActivity = getActivity();

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        emiEditText = rootView.findViewById(R.id.emiEditText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        bankNameSelector = rootView.findViewById(R.id.bankNameSelector);

        salaryProcessProgressBar = rootView.findViewById(R.id.salaryProcessProgressBar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        maritalStatusList = new ArrayList<>();
        maritalStatusList.add("Cheque");
        maritalStatusList.add("Account Transfer");
        maritalStatusList.add("Cash");
        maritalStatusList.add("Salary Process");

        loanHeading.setText("Consumer Loan");

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        salaryEditText = rootView.findViewById(R.id.salaryEditText);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        salaryProcessProgressBar.setVisibility(View.INVISIBLE);

        adapterMaritalStatus = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, maritalStatusList) {
            @Override
            public int getCount() {
                return maritalStatusList.size() - 1;
            }
        };
        adapterMaritalStatus.setDropDownViewResource(R.layout.checked_text_view);
        maritalStatusSelector.setAdapter(adapterMaritalStatus);

        maritalStatusSelector.setSelection(maritalStatusList.size() - 1);

        nextButtonForQuestions.setOnClickListener(v -> {

            if (((String) maritalStatusSelector.getSelectedItem()).trim().equalsIgnoreCase("Salary Process")) {
                ((HomeActivity) getActivity()).showSnackBar("Please select Salary Process");
            } else if (salaryEditText.getText().toString().trim().equalsIgnoreCase("")) {
                ((HomeActivity) getActivity()).showSnackBar("Please Enter your salary");
            } else if (emiEditText.getText().toString().trim().equalsIgnoreCase("")){
                ((HomeActivity) getActivity()).showSnackBar("Please Enter your EMI");
            } else {
                saveSalaryProcess();
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

        ArrayAdapter<String> dataListAdapter = new ArrayAdapter<>(mActivity, R.layout.autocomplete_text_layout);
        dataListAdapter.addAll(
                "HDFC Bank",
                "CICI Bank",
                "Punjab National Bank",
                "Capital First",
                "DHFL",
                "RBL Bank",
                "Yes Bank",
                "Axis Bank",
                "TATA Capital",
                "Indiabulls",
                "State Bank Of India",
                "Shriram Finance",
                "HDB",
                "others");
        bankNameSelector.setAdapter(dataListAdapter);

        maritalStatusSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (maritalStatusList.get(position).trim().equalsIgnoreCase("Account Transfer")) {
                    bankNameSelector.setVisibility(View.VISIBLE);
                } else {
                    bankNameSelector.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        journeyCompletedProgressBar.setProgress(86);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("8")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "7AA");
            editor.apply();

            Log.e(TAG, "Pref Mod to 7AA");
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (maritalStatusSelector.getSelectedItem().toString().trim().equalsIgnoreCase("Account Transfer")){
            bankNameSelector.setVisibility(View.VISIBLE);
        }
        else {
            bankNameSelector.setVisibility(View.GONE);
        }
    }

    private void saveSalaryProcess() {

        salaryProcessProgressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("salary_process", maritalStatusSelector.getSelectedItem().toString().trim());
        params.put("net_monthly_income", salaryEditText.getText().toString().trim());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/incomeDetails",
                new JSONObject(params),
                response -> {

                    salaryProcessProgressBar.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")){
                            showSnackBar("Income added successfully !!", R.color.green);
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
            salaryProcessProgressBar.setVisibility(View.INVISIBLE);
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
                    transaction.replace(R.id.homeParent, LBAddressDetails.newInstance("SalaryProcess"));
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }
}