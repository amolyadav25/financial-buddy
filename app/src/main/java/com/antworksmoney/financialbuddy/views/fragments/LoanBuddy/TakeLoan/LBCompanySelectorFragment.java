package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LBCompanySelectorFragment extends Fragment {

    public LBCompanySelectorFragment() {
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

    private SharedPreferences mSharedPreferences;

    private EditText companyEmailId;

    private RelativeLayout progressBar;

    private CoordinatorLayout snackBarView;

    private String companyType = "";

    public static LBCompanySelectorFragment newInstance() {
        return new LBCompanySelectorFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lb_company_selector, container, false);

        companyTypeNameSelector = rootView.findViewById(R.id.companyTypeNameSelector);

        companyTypeSelectorLoader = rootView.findViewById(R.id.companyTypeSelectorLoader);

        companyName = rootView.findViewById(R.id.companyName);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        dataRequestQueue = Volley.newRequestQueue(getContext());

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        companyLoaderProgressBar = rootView.findViewById(R.id.companyLoaderProgressBar);

        companyEmailId = rootView.findViewById(R.id.companyEmailId);

        progressBar = rootView.findViewById(R.id.progress_bar);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7AA")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "7A");
            editor.apply();

            Log.e(TAG, "Pref Mod to 7A");
        }


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCompanyTypeFromAPI();

        getCompaniesDataFromAPI();

        nextButtonForQuestions.setOnClickListener(v -> {
            if (getActivity() != null) {
                if (((String) companyTypeNameSelector.getSelectedItem()).trim().equalsIgnoreCase("Select Company Type")) {
                    ((HomeActivity) getActivity()).showSnackBar("Please select Company Type!!!");
                } else if (companyName.getText().toString().trim().equalsIgnoreCase("")) {
                    companyName.requestFocus();
                    ((HomeActivity) getActivity()).showSnackBar("Please enter Company Name !!!");
                }else if (companyEmailId.getText().toString().trim().equalsIgnoreCase("")) {
                    companyEmailId.requestFocus();
                    ((HomeActivity) getActivity()).showSnackBar("Please enter Company E-mail Id !!!");
                }
                else {
                    updateCompanyDetails();
                }

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

                        if (!companyType.trim().equalsIgnoreCase("")){
                            int selectedPosition = companyTypeList.size() - 1;
                            for (int i=0; i<companyTypeList.size(); i++){
                                if (companyType.trim().equalsIgnoreCase(companyTypeList.get(i))){
                                    selectedPosition = i;
                                    break;
                                }
                            }
                            companyTypeNameSelector.setSelection(selectedPosition);
                        }
                        else {
                            companyTypeNameSelector.setSelection(companyTypeList.size() - 1);
                        }

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

    private void updateCompanyDetails() {

        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("company_type", companyTypeNameSelector.getSelectedItem().toString().trim());
        params.put("company_name", companyName.getText().toString().trim());
        params.put("company_mail_Id", companyEmailId.getText().toString().trim());

        companyType = companyTypeNameSelector.getSelectedItem().toString().trim();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/companydetailsAdd",
                new JSONObject(params),
                response -> {

                    progressBar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Company details successfully !!", R.color.green);
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


    private void changeFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, LBSalaryProcessFragment.newInstance());
            transaction.addToBackStack(null).commit();
        }
    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                   changeFragment();
                }
            }
        });
        snackbar.show();
    }

}
