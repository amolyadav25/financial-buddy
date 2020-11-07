package com.antworksmoney.financialbuddy.views.fragments.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AdditionalInfoFragment extends Fragment implements View.OnClickListener {

    private static int mBaseFrameLayout;

    private static JSONObject dataObject;
    private static JSONObject mDataObject;


    public static AdditionalInfoFragment newInstance(int baseFrameLayout, JSONObject outerObject) {
        dataObject = outerObject;
        mBaseFrameLayout = baseFrameLayout;
        return new AdditionalInfoFragment();
    }

    private String profession = "", education = "";

    private String[] arrayProfession = new String[]{
            "Salaried",
            "Self Employed Professional",
            "Self Employed Business",
            "Retired",
            "Student",
            "Home Maker",
            "Others",
            "Profession"
    };

    private String[] arrayEducation = new String[]{
            "Post-graduate",
            "Graduate",
            "Diploma",
            "Education"
    };

    private String[] arrayIncome = new String[]{
            "0 - 25000",
            "25000 - 50000",
            "50000 - 100000",
            "> 100000",
            "Income"};

    private Spinner professionSelector, educationSelector;

    private Button skipButton, proceedButton;

    private FragmentActivity mActivity;

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
    private ProgressBar progressAdHud;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_additional_info, container, false);

        skipButton = rootView.findViewById(R.id.skipButton);

        proceedButton = rootView.findViewById(R.id.nextButton);
        progressAdHud = rootView.findViewById(R.id.progressAdHud);
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
                    progressAdHud.setVisibility(View.VISIBLE);
                    Log.e("MytagAmol"," dfgfg");
                    Log.e("Mytag","dataObjectAmol"+dataObject);
                    sendDataToServer();
//                    fragmentToReplace = AddContactsFragment.newInstance(mBaseFrameLayout,dataObject);
//                    if (fragmentToReplace != null) {
//                        transaction.replace(mBaseFrameLayout, fragmentToReplace);
//                        transaction.addToBackStack(null).commit();
//                    }

//                    JsonObjectRequest updateProfileDataReques = new JsonObjectRequest(
//                            Request.Method.POST,
//                            AppConstant.BaseUrl + "update-profile",
//                            dataObject, response -> {
//                        try {
//                            progressAdHud.setVisibility(View.GONE);
//                            // submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//
//                            Log.e("MytagAmol", response.toString());
//
//                            Intent intent = new Intent(mActivity, HomeActivity.class);
//                            getActivity().startActivity(intent);
//
//                            Log.e("TAg","Asdfsfds");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("TAg","Asdfsfds"+e.getMessage());
//                        }
//                    },
//                            error -> {
//                                progressAdHud.setVisibility(View.GONE);
//                                //submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//                                Log.e("error", error.toString());
//                                Toast.makeText(mActivity, "Failed to send data to server.....", Toast.LENGTH_SHORT).show();
//                                //  showSnackBar("Failed to send data to server.....");
//                            });

//                    updateProfileDataReques.setRetryPolicy(new DefaultRetryPolicy(
//                            AppConstant.MY_SOCKET_TIMEOUT_MS,
//                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                    updateProfileDataReques.setShouldCache(false);
//
//                    RequestQueue queue = Volley.newRequestQueue(getActivity());
//                    queue.add(updateProfileDataReques);
                   // fragmentToReplace = AddContactsFragment.newInstance(mBaseFrameLayout, dataObject);
                    //sendDataToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

//        if (fragmentToReplace != null) {
//            transaction.replace(mBaseFrameLayout, fragmentToReplace);
//            transaction.addToBackStack(null).commit();
//        }

    }
//    private void sendDataToServer() {
//
//        progressAdHud.setVisibility(View.VISIBLE);
//
//        try {
//
//            JSONObject contactObject;
//
//            JSONArray contactArray = new JSONArray();
//
////            for (int i = 0; i < completeSOSList.size(); i++) {
////                ProfileInfo info = completeSOSList.get(i);
////                contactObject = new JSONObject();
////                contactObject.put("name", info.getName());
////                contactObject.put("number", info.getPhoneNumber());
////                contactArray.put(contactObject);
////            }
////
////            mDataObject.put("my_network_contacts", contactArray);
//
//            JSONObject outerObject = new JSONObject();
//            outerObject.put("userData", mDataObject);
//
//            Log.e(TAG,outerObject.toString());
//
//            JsonObjectRequest updateProfileDataReques = new JsonObjectRequest(
//                    Request.Method.POST,
//                    AppConstant.BaseUrl + "update-profile",
//                    outerObject, response -> {
//                try {
//                    progressHUD.setVisibility(View.GONE);
//                    submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//
//                    Log.e(TAG, response.toString());
//
//                    JSONObject data = response.getJSONObject("respone").getJSONObject("UserData");
//
//                    SharedPreferences.Editor editor = pref.edit();
//
//                    editor.putString("email_value", data.getString("mail"));
//                    editor.putString("user_name", data.getString("name"));
//                    editor.putString("user_phone", data.getString("contact"));
//                    editor.putString("date_of_birth", data.getString("date_of_birth"));
//                    editor.putString("gender", data.getString("gender"));
//                    editor.putString("marital_status", data.getString("marital_status"));
//                    editor.putString("profession", data.getString("profession"));
//                    editor.putString("education", data.getString("education"));
//                    editor.putString("invest_in_market", data.getString("invest_in_market"));
//                    editor.putString("own_a_house", data.getString("own_a_house"));
//                    editor.putString("user_dob",data.getString("date_of_birth"));
//                    editor.putString("net_monthly_income", data.getString("net_monthly_income"));
//                    editor.putString("provide_financial_consultancy_service", data.getString("provide_financial_consultancy_service"));
//                    editor.putString("interested_in_financial_consultancy", data.getString("interested_in_financial_consultancy"));
//                    editor.apply();
//
//                    databaseObject.deleteContactsFromTable();
//
//                    for (int i = 0; i < data.getJSONArray("my_network_contacts").length(); i++) {
//                        databaseObject.insertContactNumber(
//                                data.getJSONArray("my_network_contacts")
//                                        .getJSONObject(i)
//                                        .getString("number"),
//
//                                data.getJSONArray("my_network_contacts")
//                                        .getJSONObject(i)
//                                        .getString("name"));
//
//                    }
//
//                    new Handler().postDelayed(() -> {
//                        Intent intent = new Intent(mActivity, HomeActivity.class);
//                        mContext.startActivity(intent);
//                    }, 100);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            },
//                    error -> {
//                        progressHUD.setVisibility(View.GONE);
//                        submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//                        Log.e("error", error.toString());
//                        showSnackBar("Failed to send data to server.....");
//                    });
//
//            updateProfileDataReques.setRetryPolicy(new DefaultRetryPolicy(
//                    AppConstant.MY_SOCKET_TIMEOUT_MS,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            updateProfileDataReques.setShouldCache(false);
//
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//            queue.add(updateProfileDataReques);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }
private void sendDataToServer() {

  //  progressHUD.setVisibility(View.VISIBLE);

    try {

        JSONObject contactObject;

        JSONArray contactArray = new JSONArray();

//        for (int i = 0; i < completeSOSList.size(); i++) {
//            ProfileInfo info = completeSOSList.get(i);
//            contactObject = new JSONObject();
//            contactObject.put("name", info.getName());
//            contactObject.put("number", info.getPhoneNumber());
//            contactArray.put(contactObject);
//        }

        dataObject.put("my_network_contacts", contactArray);

        JSONObject outerObject = new JSONObject();
        outerObject.put("userData", dataObject);

        Log.e(TAG,outerObject.toString());

        JsonObjectRequest updateProfileDataReques = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "update-profile",
                outerObject, response -> {
            try {
               // progressHUD.setVisibility(View.GONE);
             //   submit_button.setBackground(getActivity().getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                Log.e(TAG, response.toString());

                JSONObject data = response.getJSONObject("respone").getJSONObject("UserData");

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("email_value", data.getString("mail"));
                editor.putString("user_name", data.getString("name"));
                editor.putString("user_phone", data.getString("contact"));
                editor.putString("date_of_birth", data.getString("date_of_birth"));
                editor.putString("gender", data.getString("gender"));
                editor.putString("marital_status", data.getString("marital_status"));
                editor.putString("profession", data.getString("profession"));
                editor.putString("education", data.getString("education"));
                editor.putString("invest_in_market", data.getString("invest_in_market"));
                editor.putString("own_a_house", data.getString("own_a_house"));
                editor.putString("user_dob",data.getString("date_of_birth"));
                editor.putString("net_monthly_income", data.getString("net_monthly_income"));
                editor.putString("provide_financial_consultancy_service", data.getString("provide_financial_consultancy_service"));
                editor.putString("interested_in_financial_consultancy", data.getString("interested_in_financial_consultancy"));
                editor.apply();

              //  databaseObject.deleteContactsFromTable();

//                for (int i = 0; i < data.getJSONArray("my_network_contacts").length(); i++) {
//                    databaseObject.insertContactNumber(
//                            data.getJSONArray("my_network_contacts")
//                                    .getJSONObject(i)
//                                    .getString("number"),
//
//                            data.getJSONArray("my_network_contacts")
//                                    .getJSONObject(i)
//                                    .getString("name"));
//
//                }

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(mActivity, HomeActivity.class);
                    getActivity().startActivity(intent);
                }, 100);


            } catch (Exception e) {
                e.printStackTrace();
            }


        },
                error -> {
                   // progressHUD.setVisibility(View.GONE);
                   // submit_button.setBackground(getActivity().getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                    Log.e("error", error.toString());
                   // showSnackBar("Failed to send data to server.....");
                });

        updateProfileDataReques.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        updateProfileDataReques.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(updateProfileDataReques);


    } catch (JSONException e) {
        e.printStackTrace();
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
