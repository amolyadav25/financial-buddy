package com.antworksmoney.financialbuddy.views.fragments.Loan;


import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

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

import java.text.MessageFormat;
import java.util.ArrayList;

public class QualificationSelectorFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public QualificationSelectorFragment() {
        // Required empty public constructor
    }


    public static QualificationSelectorFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new QualificationSelectorFragment();
    }

    private Spinner qualificationSelector;

    private static final String TAG = "QualificationSelectorFr";

    private ProgressBar qualificationSelectorLoader, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static boolean moveToNextPage;

    private ArrayAdapter<String> adapterQualification;

    private RequestQueue dataRequestQueue;

    private FragmentActivity mActivity;

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_qualification_selector, container, false);

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

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        moveToNextPage = false;

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getQualificationDataFromAPI();


        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoanInfoEntity.getQualification().equals("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select Qualification !!!");
                } else changeFragment();
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

        journeyCompletedProgressBar.setProgress(44);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


    }

    private void getQualificationDataFromAPI() {

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "qualification",
                null,
                response -> {
                    Log.i(TAG, response.toString());
                    qualificationSelectorLoader.setVisibility(View.INVISIBLE);

                    try {
                        JSONArray dataArray = response.getJSONArray("UserData");

                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            dataList.add((String) dataArray.get(i));
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

                        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")){
                            if (!mLoanInfoEntity.getQualification().trim().equalsIgnoreCase("")) {
                                int selectedItem = dataList.size() - 1;
                                for (int i = 0; i < dataList.size(); i++) {
                                    if (dataList.get(i).trim().equalsIgnoreCase(mLoanInfoEntity.getQualification().trim())) {
                                        selectedItem = i;
                                        break;
                                    }
                                }
                                qualificationSelector.setSelection(selectedItem);
                            } else qualificationSelector.setSelection(dataList.size() - 1);
                        }
                        else qualificationSelector.setSelection(dataList.size() - 1);

                        qualificationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (moveToNextPage){
                                    mLoanInfoEntity.setQualification(dataList.get(position));
                                    qualificationSelectorLoader.setVisibility(View.VISIBLE);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            moveToNextPage = false;
                                            changeFragment();
                                        }
                                    },1000);
                                }
                                else moveToNextPage = true;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    qualificationSelectorLoader.setVisibility(View.INVISIBLE);
                    Log.e(TAG, error.toString());
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);
    }

    private void changeFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, OccupationSelectorFragment.newInstance(mLoanInfoEntity));
            transaction.addToBackStack(null).commit();
        }
    }
}
