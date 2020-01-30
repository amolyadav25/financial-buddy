package com.antworksmoney.financialbuddy.views.fragments.Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AdditionalInfoFragment extends Fragment implements View.OnClickListener {

    private static int mBaseFrameLayout;

    private static JSONObject dataObject;

    public static AdditionalInfoFragment newInstance(int baseFrameLayout, JSONObject outerObject) {
        dataObject = outerObject;
        mBaseFrameLayout = baseFrameLayout;
        return new AdditionalInfoFragment();
    }

    private String profession = "", education = "";

    private String arrayProfession[] = new String[]{
            "Salaried",
            "Self Employed Professional",
            "Self Employed Business",
            "Retired",
            "Student",
            "Home Maker",
            "Others",
            "Profession"
    };

    private String arrayEducation[] = new String[]{
            "Post-graduate",
            "Graduate",
            "Diploma",
            "Education"
    };

    private String arrayIncome[] = new String[]{
            "0 - 25000",
            "25000 - 50000",
            "50000 - 100000",
            "> 100000",
            "Income"};

    private Spinner professionSelector, educationSelector;

    private Button skipButton, proceedButton;

    private Activity mActivity;

    private RadioGroup equityRadioGroup,
            ownHouseRadioGroup,
            finacialConsultancyServiceRadioGroup,
            finacialConsultancyTimeRadioGroup;

    private Spinner netMonthlyIncome;

    private SharedPreferences pref;

    private ProgressBar companyLoaderProgressBar;

    private AutoCompleteTextView companyName;

    private RequestQueue dataRequestQueue;

    private static final String TAG = "AdditionalInfoFragment";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_additional_info, container, false);

        skipButton = rootView.findViewById(R.id.skipButton);

        proceedButton = rootView.findViewById(R.id.nextButton);

        professionSelector = rootView.findViewById(R.id.professionSelector);

        educationSelector = rootView.findViewById(R.id.educationSelector);

        equityRadioGroup = rootView.findViewById(R.id.equityRadioGroup);

        ownHouseRadioGroup = rootView.findViewById(R.id.ownHouseRadioGroup);

        finacialConsultancyServiceRadioGroup = rootView.findViewById(R.id.finacialConsultancyServiceRadioGroup);

        finacialConsultancyTimeRadioGroup = rootView.findViewById(R.id.finacialConsultancyTimeRadioGroup);

        netMonthlyIncome = rootView.findViewById(R.id.netMonthlyIncome);

        companyLoaderProgressBar = rootView.findViewById(R.id.companyLoaderProgressBar);

        companyName = rootView.findViewById(R.id.companyName);

        mActivity = getActivity();

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        dataRequestQueue = Volley.newRequestQueue(getContext());


        ArrayAdapter<String> adapter_profession = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, arrayProfession) {
            @Override
            public int getCount() {
                return arrayProfession.length -1;
            }
        };
        adapter_profession.setDropDownViewResource(R.layout.checked_text_view);
        professionSelector.setAdapter(adapter_profession);
        if (!pref.getString("Profession","").trim().equalsIgnoreCase("")){
            int position = arrayProfession.length-1;
            for (int i=0; i<arrayProfession.length; i++){
                if (pref.getString("Profession","").trim().equalsIgnoreCase(arrayProfession[i].trim())){
                    position = i;
                    break;
                }
            }
            professionSelector.setSelection(position);
        }
        else {
            professionSelector.setSelection(arrayProfession.length-1);
        }



        ArrayAdapter<String> adapter_education = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, arrayEducation) {
            @Override
            public int getCount() {
                return 3;
            }
        };
        adapter_education.setDropDownViewResource(R.layout.checked_text_view);
        educationSelector.setAdapter(adapter_education);
        if (!pref.getString("Education","").trim().equalsIgnoreCase("")){
            int position = 3;
            for (int i=0; i<arrayEducation.length; i++){
                if (pref.getString("Education","").trim().equalsIgnoreCase(arrayEducation[i].trim())){
                    position = i;
                    break;
                }
            }
            educationSelector.setSelection(position);
        }
        else {
            educationSelector.setSelection(3);
        }


        ArrayAdapter<String> adapter_income = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, arrayIncome) {
            @Override
            public int getCount() {
                return 4;
            }
        };
        adapter_income.setDropDownViewResource(R.layout.checked_text_view);
        netMonthlyIncome.setAdapter(adapter_income);
        if (!pref.getString("Income","").trim().equalsIgnoreCase("")){
            int position = 4;
            for (int i=0; i<arrayEducation.length; i++){
                if (pref.getString("Income","").trim().equalsIgnoreCase(arrayIncome[i].trim())){
                    position = i;
                    break;
                }
            }
            netMonthlyIncome.setSelection(position);
        }
        else {
            netMonthlyIncome.setSelection(4);
        }

        professionSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                profession = adapter_profession.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        educationSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                education = adapter_education.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (pref.getString("equity","").trim().equalsIgnoreCase(
                ((RadioButton) equityRadioGroup.getChildAt(1)).getText().toString().trim())){
            ((RadioButton) equityRadioGroup.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) equityRadioGroup.getChildAt(0)).setChecked(true);
        }


        if (pref.getString("ownHouse","").trim().equalsIgnoreCase(
                ((RadioButton) ownHouseRadioGroup.getChildAt(1)).getText().toString().trim())) {
            ((RadioButton) ownHouseRadioGroup.getChildAt(1)).setChecked(true);
        }else {
            ((RadioButton) ownHouseRadioGroup.getChildAt(0)).setChecked(true);
        }

        if (pref.getString("finacialConsultancyService","").trim().equalsIgnoreCase(
                ((RadioButton) finacialConsultancyServiceRadioGroup.getChildAt(1)).getText().toString().trim())) {
            ((RadioButton) finacialConsultancyServiceRadioGroup.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) finacialConsultancyServiceRadioGroup.getChildAt(0)).setChecked(true);
        }

        if (pref.getString("finacialConsultancyTime","").trim().equalsIgnoreCase(
                ((RadioButton) finacialConsultancyTimeRadioGroup.getChildAt(1)).getText().toString().trim())) {
            ((RadioButton) finacialConsultancyTimeRadioGroup.getChildAt(1)).setChecked(true);
        }else {
            ((RadioButton) finacialConsultancyTimeRadioGroup.getChildAt(0)).setChecked(true);
        }

        getCompaniesDataFromAPI();

        skipButton.setOnClickListener(this);

        proceedButton.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {

        FragmentTransaction transaction = (Objects.requireNonNull(getActivity()))
                .getSupportFragmentManager()
                .beginTransaction();

        Fragment fragmentToReplace = null;

        switch (view.getId()) {
            case R.id.skipButton:
                Intent moveToHomeScreen = new Intent(mActivity, HomeActivity.class);
                mActivity.startActivity(moveToHomeScreen);
                mActivity.finish();
                break;

            case R.id.nextButton:
                try {

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Profession", ((String) professionSelector.getSelectedItem()).trim());
                    editor.putString("Education",((String) educationSelector.getSelectedItem()).trim());
                    editor.putString("Income",((String) netMonthlyIncome.getSelectedItem()).trim());
                    editor.putString("equity", ((RadioButton) equityRadioGroup.findViewById(
                            equityRadioGroup.getCheckedRadioButtonId()))
                            .getText().toString().trim());
                    editor.putString("ownHouse",((RadioButton) ownHouseRadioGroup.findViewById(
                            ownHouseRadioGroup.getCheckedRadioButtonId()))
                            .getText().toString().trim());
                    editor.putString("finacialConsultancyService",((RadioButton) finacialConsultancyServiceRadioGroup.findViewById(
                            finacialConsultancyServiceRadioGroup.getCheckedRadioButtonId()))
                            .getText().toString().trim());
                    editor.putString("finacialConsultancyTime", ((RadioButton) finacialConsultancyTimeRadioGroup.findViewById(
                            finacialConsultancyTimeRadioGroup.getCheckedRadioButtonId()))
                            .getText().toString().trim());
                    editor.putString("companyName", companyName.getText().toString().trim());

                    editor.apply();

                    if (profession.trim().equalsIgnoreCase("Profession")){
                        dataObject.put("profession","");
                    }
                    else dataObject.put("profession", profession);

                    if (education.trim().equalsIgnoreCase("Education")){
                        dataObject.put("education","");
                    }
                    else dataObject.put("education", education);

                    dataObject.put("invest_in_market",
                            ((RadioButton) equityRadioGroup.findViewById(
                                    equityRadioGroup.getCheckedRadioButtonId()))
                                    .getText().toString().trim());

                    dataObject.put("companyName", companyName.getText().toString().trim());

                    dataObject.put("own_a_house",
                            ((RadioButton) ownHouseRadioGroup.findViewById(
                                    ownHouseRadioGroup.getCheckedRadioButtonId()))
                                    .getText().toString().trim());

                    dataObject.put("net_monthly_income", ((String) netMonthlyIncome.getSelectedItem()).trim());

                    dataObject.put("provide_financial_consultancy_service",
                            ((RadioButton) finacialConsultancyTimeRadioGroup.findViewById(
                                    finacialConsultancyTimeRadioGroup.getCheckedRadioButtonId()))
                                    .getText().toString().trim());

                    dataObject.put("interested_in_financial_consultancy",
                            ((RadioButton) finacialConsultancyServiceRadioGroup.findViewById(
                                    finacialConsultancyServiceRadioGroup.getCheckedRadioButtonId()))
                                    .getText().toString().trim());

                    Log.e("TAg", dataObject.toString());

                    fragmentToReplace = AddContactsFragment.newInstance(mBaseFrameLayout, dataObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }

        if (fragmentToReplace != null) {
            transaction.replace(mBaseFrameLayout, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }

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

}
