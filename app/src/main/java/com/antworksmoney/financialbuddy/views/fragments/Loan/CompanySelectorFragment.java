package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CompanySelectorFragment extends Fragment {

    public CompanySelectorFragment() {
        // Required empty public constructor
    }

    private Spinner companyTypeNameSelector;

    private ProgressBar companyTypeSelectorLoader, journeyCompletedProgressBar, companyLoaderProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;
    private AutoCompleteTextView companyName;

    private static final String TAG = "CompanySelectorFragment";

    private ArrayAdapter<String> adapterCompanyType;

    private RequestQueue dataRequestQueue;

    private static LoanInfoEntity mLoanInfoEntity;

    private TextView loanHeading;


    public static CompanySelectorFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new CompanySelectorFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_company_selector, container, false);

        companyTypeNameSelector = rootView.findViewById(R.id.companyTypeNameSelector);

        companyTypeSelectorLoader = rootView.findViewById(R.id.companyTypeSelectorLoader);

        companyName = rootView.findViewById(R.id.companyName);

        dataRequestQueue = Volley.newRequestQueue(getContext());

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        companyLoaderProgressBar = rootView.findViewById(R.id.companyLoaderProgressBar);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCompanyTypeFromAPI();

        getCompaniesDataFromAPI();

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (((String) companyTypeNameSelector.getSelectedItem()).trim().equalsIgnoreCase("Select Company Type")) {
                        ((HomeActivity) getActivity()).showSnackBar("Please select Company Type!!!");
                    } else if (companyName.getText().toString().trim().equalsIgnoreCase("")) {
                        ((HomeActivity) getActivity()).showSnackBar("Please enter Company Name !!!");
                    } else {
                        changeFragment();
                    }

                }
            }
        });

        previousButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        journeyCompletedProgressBar.setProgress(58);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


    }

    private void getCompanyTypeFromAPI() {

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST, AppConstant.BaseUrl + "company_type", null,
                response -> {
                    Log.i(TAG, response.toString());
                    companyTypeSelectorLoader.setVisibility(View.GONE);

                    try {
                        JSONArray dataArray = response.getJSONArray("UserData");

                        ArrayList<String> companyTypeList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            companyTypeList.add(((String) dataArray.get(i)));
                        }

                        companyTypeList.add("Select Company Type");

                        adapterCompanyType = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, companyTypeList) {
                            @Override
                            public int getCount() {
                                return companyTypeList.size() - 1;
                            }
                        };
                        adapterCompanyType.setDropDownViewResource(R.layout.checked_text_view);

                        companyTypeNameSelector.setAdapter(adapterCompanyType);

                        companyTypeNameSelector.setSelection(companyTypeList.size() - 1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    companyTypeSelectorLoader.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);

    }

    private void getCompaniesDataFromAPI() {
        JsonObjectRequest dataRequest = new JsonObjectRequest(Request.Method.POST,
                AppConstant.BaseUrl + "company_list",
                null,
                response -> {
                    Log.e(TAG, response.toString());
                    companyLoaderProgressBar.setVisibility(View.GONE);

                    ArrayAdapter<String> dataListAdapter = new ArrayAdapter<>(getContext(), R.layout.autocomplete_text_layout);

                    try {
                        for (int i = 0; i < response.getJSONArray("UserData").length(); i++) {
                            dataListAdapter.add(((JSONObject) response.getJSONArray("UserData").get(i)).getString("company_name"));
                        }
                        companyName.setAdapter(dataListAdapter);

                    } catch (Exception e) {
                        companyLoaderProgressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> {
                    companyLoaderProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());

                }
        );

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);
    }


    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragmentToReplace;
            if (mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Salaried")) {
                fragmentToReplace = SalaryProcessFragment.newInstance(mLoanInfoEntity);
            } else {
                fragmentToReplace = LoanAmountFragment.newInstance(mLoanInfoEntity);
            }
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mLoanInfoEntity.setCompanyType((String) companyTypeNameSelector.getSelectedItem());

        mLoanInfoEntity.setCompanyName(companyName.getText().toString().trim());

    }
}
