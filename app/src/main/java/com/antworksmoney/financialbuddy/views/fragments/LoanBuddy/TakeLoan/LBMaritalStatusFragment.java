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
import android.widget.Button;
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

public class LBMaritalStatusFragment extends Fragment {
    public LBMaritalStatusFragment() {
        // Required empty public constructor
    }


    public static LBMaritalStatusFragment newInstance() {
        return new LBMaritalStatusFragment();
    }


    private Spinner maritalStatusSelector;

    private ArrayAdapter<String> adapterMaritalStatus;

    private ArrayList<String> maritalStatusList;

    private Activity mActivity;

    private ProgressBar progressBar, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private TextView loanHeading;

    private CoordinatorLayout snackBarView;

    private static final String TAG = "MaritalStatusFragment";

    private SharedPreferences mSharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_marital_status, container, false);

        mActivity = getActivity();

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        progressBar = rootView.findViewById(R.id.progressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        maritalStatusList = new ArrayList<>();
        maritalStatusList.add("Single");
        maritalStatusList.add("Married");
        maritalStatusList.add("Select Marital Status");

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading.setText(mSharedPreferences.getString(AppConstant.LOAN_NAME, ""));

        adapterMaritalStatus = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, maritalStatusList) {
            @Override
            public int getCount() {
                return maritalStatusList.size() - 1;
            }
        };
        adapterMaritalStatus.setDropDownViewResource(R.layout.checked_text_view);
        maritalStatusSelector.setAdapter(adapterMaritalStatus);
        maritalStatusSelector.setSelection(maritalStatusList.size()-1);
        progressBar.setVisibility(View.INVISIBLE);

        nextButtonForQuestions.setOnClickListener(v -> {
            if (((String) maritalStatusSelector.getSelectedItem()).trim().equalsIgnoreCase("Select Marital Status")) {
                ((HomeActivity) mActivity).showSnackBar("Please select Marital Status !!!");
            } else {
                updateMaritalStatus();
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

        journeyCompletedProgressBar.setProgress(30);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("5")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "4");
            editor.apply();
        }


        return rootView;
    }


    private void updateMaritalStatus() {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();

        String marital_status_id;
        if (maritalStatusSelector.getSelectedItem().toString().trim().equalsIgnoreCase("Single")){
            marital_status_id = "1";
        }
        else {
            marital_status_id = "2";
        }
        params.put("marital_status", marital_status_id);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/maritalStatusUpdate",
                new JSONObject(params),
                response -> {

                    progressBar.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Marital Status added successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to add !!", R.color.red);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            showSnackBar("Failed to add !!", R.color.red);
                        } catch (Exception ignored) {
                        }

                    }

                }, error -> {
            Log.e(TAG, error.toString());
            progressBar.setVisibility(View.INVISIBLE);
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

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBQualificationSelectorFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }
}
