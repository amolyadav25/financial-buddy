package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ArrayAdapter;
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
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LBQualificationSelectorFragment extends Fragment {

    public LBQualificationSelectorFragment() {
        // Required empty public constructor
    }


    public static LBQualificationSelectorFragment newInstance() {
        return new LBQualificationSelectorFragment();
    }

    private Spinner qualificationSelector;

    private static final String TAG = "QualificationSelectorFr";

    private ProgressBar qualificationSelectorLoader, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private ArrayAdapter<String> adapterQualification;

    private RequestQueue dataRequestQueue;

    private Activity mActivity;

    private TextView loanHeading;

    private CoordinatorLayout snackBarView;

    private SharedPreferences mSharedPreferences;

    private HashMap<String, String> dataIdMap, dataNameMap;

    private String qualificationId = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_qualification_selector, container, false);

        mActivity = getActivity();

        qualificationSelector = rootView.findViewById(R.id.qualificationSelector);

        qualificationSelectorLoader = rootView.findViewById(R.id.qualificationSelectorLoader);

        dataRequestQueue = Volley.newRequestQueue(getContext());

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        dataIdMap = new HashMap<>();

        dataNameMap = new HashMap<>();

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        loanHeading.setText(mSharedPreferences.getString(AppConstant.LOAN_NAME, ""));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getQualificationDataFromAPI();


        nextButtonForQuestions.setOnClickListener(v -> {
            if (qualificationSelector.getSelectedItem().toString().trim().equals("Select Qualification")) {
                ((HomeActivity) mActivity).showSnackBar("Please select Qualification !!!");
            } else updateQualification();
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

        journeyCompletedProgressBar.setProgress(44);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("6")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "5");
            editor.apply();
        }


    }

    private void getQualificationDataFromAPI() {

        qualificationSelectorLoader.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getQualification",
                null,
                response -> {

                    qualificationSelectorLoader.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        JSONArray dataArray = response.getJSONArray("qualification_list");

                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {

                            dataList.add(dataArray.getJSONObject(i).getString("qualification"));

                            dataIdMap.put(dataArray.getJSONObject(i).getString("qualification").trim(),
                                    dataArray.getJSONObject(i).getString("id").trim());

                            dataNameMap.put(dataArray.getJSONObject(i).getString("id").trim(),
                                    dataArray.getJSONObject(i).getString("qualification").trim());

                        }
                        dataList.add("Select Qualification");

                        adapterQualification = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, dataList) {
                            @Override
                            public int getCount() {
                                return dataList.size() - 1;
                            }
                        };

                        adapterQualification.setDropDownViewResource(R.layout.checked_text_view);

                        qualificationSelector.setAdapter(adapterQualification);

                        if (!qualificationId.trim().equalsIgnoreCase("")){
                            String selectedQualification = dataNameMap.get(qualificationId.trim());
                            int selectedPosition = dataList.size() - 1;
                            for (int i=0; i<dataList.size(); i++){
                                if (selectedQualification.trim().equalsIgnoreCase(dataList.get(i))){
                                    selectedPosition = i;
                                    break;
                                }
                            }
                            qualificationSelector.setSelection(selectedPosition);
                        }
                        else {
                            qualificationSelector.setSelection(dataList.size() - 1);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Log.e(TAG, error.toString());
            qualificationSelectorLoader.setVisibility(View.INVISIBLE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(request);
    }

    private void updateQualification() {

        qualificationSelectorLoader.setVisibility(View.VISIBLE);

        qualificationId = dataIdMap.get(qualificationSelector.getSelectedItem().toString().trim());

        Map<String, String> params = new HashMap<>();
        params.put("highest_qualification", qualificationId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/updateQualification",
                new JSONObject(params),
                response -> {

                    qualificationSelectorLoader.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Qualification added successfully !!", R.color.green);
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
            qualificationSelectorLoader.setVisibility(View.INVISIBLE);
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

        dataRequestQueue.add(request);

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
                    transaction.replace(R.id.homeParent, LBOccupationSelectorFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }
}
