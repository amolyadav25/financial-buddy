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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LBSelfEmployedProfessionalFragment extends Fragment {

    public static LBSelfEmployedProfessionalFragment newInstance() {
        return new LBSelfEmployedProfessionalFragment();
    }

    private Spinner companyTypeSpinner, totalExperienceSpinner, defaultedOnLoanCard;

    private EditText companyNameEditText, grossTurnOver2EditText, net_monthly_income, grossTurnOverLastYearEditText;

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private static final String TAG = "LBSelfEmployedBusinessF";

    private SharedPreferences mSharedPreferences;

    private RequestQueue mRequestQueue;

    private CoordinatorLayout snackBarView;

    private String companyTypeHint, experienceHint, defaultedHint;

    private HashMap<String, String> companyType, experience, defaulted;

    private Button previousButtonForQuestions, nextButtonForQuestions;

    private TextView loanHeading;

    private ImageView professionalImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbself_employed_business, container, false);

        companyNameEditText = rootView.findViewById(R.id.companyNameEditText);

        grossTurnOver2EditText = rootView.findViewById(R.id.grossTurnOver2EditText);

        net_monthly_income = rootView.findViewById(R.id.net_monthly_income);

        defaultedOnLoanCard = rootView.findViewById(R.id.defaultedOnLoanCard);

        grossTurnOverLastYearEditText = rootView.findViewById(R.id.grossTurnOverLastYearEditText);

        companyTypeSpinner = rootView.findViewById(R.id.companyTypeSpinner);

        totalExperienceSpinner = rootView.findViewById(R.id.totalExperienceSpinner);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mToolbar = rootView.findViewById(R.id.top_toolBar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        companyType = new HashMap<>();

        defaulted = new HashMap<>();

        experience = new HashMap<>();

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        loanHeading.setText("Self Employed Professional");

        professionalImage = rootView.findViewById(R.id.professionalImage);
        professionalImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.self_employed_prof_banr));

        mRequestQueue = Volley.newRequestQueue(getContext());

        mToolbar.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        previousButtonForQuestions.setOnClickListener(view -> {
            if (getActivity() != null){
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (companyTypeSpinner.getSelectedItem().toString().equalsIgnoreCase(companyTypeHint)){
                    showSnackBar("Select Industry Type !!", R.color.red);
                }
                else if (companyNameEditText.getText().toString().trim().equalsIgnoreCase("")){
                    companyNameEditText.setError("Enter company name !!");
                    companyNameEditText.requestFocus();
                }
                else if (totalExperienceSpinner.getSelectedItem().toString().equalsIgnoreCase(experienceHint)){
                    showSnackBar("Select Experince !!", R.color.red);
                }
                else if (grossTurnOverLastYearEditText.getText().toString().trim().equalsIgnoreCase("")){
                    grossTurnOverLastYearEditText.setError("Enter lat year turn over");
                    grossTurnOverLastYearEditText.requestFocus();
                }
                else if (grossTurnOver2EditText.getText().toString().trim().equalsIgnoreCase("")){
                    grossTurnOver2EditText.setError("Enter last two year turnover");
                    grossTurnOver2EditText.requestFocus();
                }
                else if (net_monthly_income.getText().toString().trim().equalsIgnoreCase("")){
                    net_monthly_income.setError("Enter Net worth !!");
                    net_monthly_income.requestFocus();
                }
                else if (defaultedOnLoanCard.getSelectedItem().toString().equalsIgnoreCase(defaultedHint)){
                    showSnackBar("Select defaulted on Loan card or Not !!", R.color.red);
                }
                else {
                    saveOtherDetailsToServer();
                }
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("8")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "7C");
            editor.apply();
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchOccupationalFields();
    }

    private void fetchOccupationalFields() {

        progress_bar.setVisibility(View.VISIBLE);

        try {

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerres/getOccuptionfields",
                    new JSONObject().put("occupation",mSharedPreferences.getString(AppConstant.OCCUPATION_ID, "")),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        try {

                            ArrayList<String> comapnyNames = new ArrayList<>();

                            if (!response.getJSONArray("occupation_field_list")
                                    .getJSONObject(0).getJSONArray("optional_value").isNull(0)){

                                JSONObject companyObject = response.getJSONArray("occupation_field_list")
                                        .getJSONObject(0)
                                        .getJSONArray("optional_value")
                                        .getJSONObject(0);


                                String[] companyKeys = getNames(companyObject);

                                for (int i=0; i<companyKeys.length; i++){
                                    comapnyNames.add(companyObject.getString(companyKeys[i]));
                                    companyType.put(companyObject.getString(companyKeys[i]), companyKeys[i]);
                                }

                            }
                            else {
                                comapnyNames.add("TEST DATA");
                                companyType.put("TEST DATA", "TEST");
                            }

                            comapnyNames.add(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(0).getString("place_holder_name"));

                            companyTypeHint = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(0).getString("place_holder_name");

                            ArrayAdapter<String> adapterComapny = new ArrayAdapter<String>(getContext(),
                                    R.layout.checked_text_view, comapnyNames) {
                                @Override
                                public int getCount() {
                                    return comapnyNames.size() - 1;
                                }
                            };

                            adapterComapny.setDropDownViewResource(R.layout.checked_text_view);

                            companyTypeSpinner.setAdapter(adapterComapny);

                            companyTypeSpinner.setSelection(comapnyNames.size()-1);

                            companyNameEditText.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(1).getString("place_holder_name"));

                            JSONObject experienceObject = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(2).getJSONArray("optional_value").getJSONObject(0);

                            ArrayList<String> experienceNames = new ArrayList<>();

                            String[] experinceKeys = getNames(experienceObject);

                            for (int i=0; i<experinceKeys.length; i++){
                                experienceNames.add(experienceObject.getString(experinceKeys[i]));
                                experience.put(experienceObject.getString(experinceKeys[i]),experinceKeys[i]);
                            }
                            experienceNames.add(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(2).getString("place_holder_name"));

                            experienceHint = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(2).getString("place_holder_name");

                            ArrayAdapter<String> adapterExperience = new ArrayAdapter<String>(getContext(),
                                    R.layout.checked_text_view, experienceNames) {
                                @Override
                                public int getCount() {
                                    return experienceNames.size() - 1;
                                }
                            };

                            adapterExperience.setDropDownViewResource(R.layout.checked_text_view);

                            totalExperienceSpinner.setAdapter(adapterExperience);

                            totalExperienceSpinner.setSelection(experienceNames.size()-1);

                            grossTurnOverLastYearEditText.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(3).getString("place_holder_name"));

                            grossTurnOver2EditText.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(4).getString("place_holder_name"));

                            net_monthly_income.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(5).getString("place_holder_name"));

                            JSONObject defaultedOnLoanCardObject = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(6).getJSONArray("optional_value").getJSONObject(0);

                            ArrayList<String> defaultedLoanNames = new ArrayList<>();

                            String[] defaultedLoanKeys = getNames(defaultedOnLoanCardObject);

                            for (int i=0; i<defaultedLoanKeys.length; i++){
                                defaultedLoanNames.add(defaultedOnLoanCardObject.getString(defaultedLoanKeys[i]));
                                defaulted.put(defaultedOnLoanCardObject.getString(defaultedLoanKeys[i]),defaultedLoanKeys[i]);
                            }
                            defaultedLoanNames.add(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(6).getString("place_holder_name"));

                            defaultedHint = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(6).getString("place_holder_name");

                            ArrayAdapter<String> adapterLoanDefalted = new ArrayAdapter<String>(getContext(),
                                    R.layout.checked_text_view, defaultedLoanNames) {
                                @Override
                                public int getCount() {
                                    return defaultedLoanNames.size() - 1;
                                }
                            };

                            adapterLoanDefalted.setDropDownViewResource(R.layout.checked_text_view);

                            defaultedOnLoanCard.setAdapter(adapterLoanDefalted);

                            defaultedOnLoanCard.setSelection(defaultedLoanNames.size()-1);

                        } catch (Exception e) {
                            showSnackBar("Failed to load Data !!", R.color.red);
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        Log.e(TAG, error.toString());
                        showSnackBar("Failed to load Data !!", R.color.red);
                        progress_bar.setVisibility(View.GONE);

                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                    params.put("Content-Type", "application/json");
                    Log.e(TAG, params.toString());
                    return params;
                }
            };

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(dataRequest);

        }catch (Exception e){
            progress_bar.setVisibility(View.GONE);
            showSnackBar("Failed to load Data !!", R.color.red);
            e.printStackTrace();
        }
    }

    public static String[] getNames(JSONObject jo) {
        int length = jo.length();
        if (length == 0) {
            return null;
        }
        Iterator<String> iterator = jo.keys();
        String[] names = new String[length];
        int i = 0;
        while (iterator.hasNext()) {
            names[i] = iterator.next();
            i += 1;
        }
        return names;
    }

    private void saveOtherDetailsToServer(){
        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("occuption_id", mSharedPreferences.getString(AppConstant.OCCUPATION_ID, ""));
        params.put("company_type", companyType.get(companyTypeSpinner.getSelectedItem().toString()));
        params.put("company_name", companyNameEditText.getText().toString().trim());
        params.put("total_experience", experience.get(totalExperienceSpinner.getSelectedItem().toString()));
        params.put("turnover_last_year", grossTurnOverLastYearEditText.getText().toString().trim());
        params.put("turnover_last2_year", grossTurnOver2EditText.getText().toString().trim());
        params.put("net_monthly_income", net_monthly_income.getText().toString().trim());
        params.put("ever_defaulted", defaulted.get(defaultedOnLoanCard.getSelectedItem().toString()));

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/occupationAdd",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar(response.getString("msg"), R.color.green);
                        } else {
                            showSnackBar(response.getString("error_msg"), R.color.red);
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
            progress_bar.setVisibility(View.INVISIBLE);
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


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
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
