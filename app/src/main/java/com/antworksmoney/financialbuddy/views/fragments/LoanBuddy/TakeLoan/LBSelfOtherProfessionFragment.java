package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class LBSelfOtherProfessionFragment extends Fragment {

    public static LBSelfOtherProfessionFragment newInstance() {
        return new LBSelfOtherProfessionFragment();
    }

    private Spinner companyTypeSpinner;

    private EditText companyNameEditText, grossTurnOverLastYearEditText;

    private RelativeLayout progress_bar;

    private Toolbar mToolbar;

    private static final String TAG = "LBSelfEmployedBusinessF";

    private SharedPreferences mSharedPreferences;

    private RequestQueue mRequestQueue;

    private CoordinatorLayout snackBarView;

    private String companyTypeHint;

    private TextView loanHeading;

    private HashMap<String, String> companyType;

    private Button previousButtonForQuestions, nextButtonForQuestions;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbself_other_profession, container, false);

        companyNameEditText = rootView.findViewById(R.id.companyNameEditText);

        grossTurnOverLastYearEditText = rootView.findViewById(R.id.grossTurnOverLastYearEditText);

        companyTypeSpinner = rootView.findViewById(R.id.companyTypeSpinner);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mToolbar = rootView.findViewById(R.id.top_toolBar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        companyType = new HashMap<>();

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mRequestQueue = Volley.newRequestQueue(getContext());

        if (mSharedPreferences.getString(AppConstant.OCCUPATION_ID, "").trim().equalsIgnoreCase("4")){
            loanHeading.setText("Retired");
        }
        else if (mSharedPreferences.getString(AppConstant.OCCUPATION_ID, "").trim().equalsIgnoreCase("5")){
            loanHeading.setText("Student");
        }
        else {
            loanHeading.setText("Home Maker");
        }

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
                    showSnackBar("Select source Of Income !!", R.color.red);
                }
                else if (companyNameEditText.getText().toString().trim().equalsIgnoreCase("")){
                    companyNameEditText.setError("Enter Monthly Income !!");
                    companyNameEditText.requestFocus();
                }
                else {
                    saveOtherDetailsToServer();
                }
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("8")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "7D");
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

                            JSONObject companyObject = response.getJSONArray("occupation_field_list")
                                    .getJSONObject(0).getJSONArray("optional_value").getJSONObject(0);

                            ArrayList<String> comapnyNames = new ArrayList<>();

                            String[] companyKeys = getNames(companyObject);

                            for (int i=0; i<companyKeys.length; i++){
                                comapnyNames.add(companyObject.getString(companyKeys[i]));
                                companyType.put(companyObject.getString(companyKeys[i]), companyKeys[i]);
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

                            companyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (comapnyNames.get(i).trim().equalsIgnoreCase("Other")){
                                        grossTurnOverLastYearEditText.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            companyNameEditText.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(1).getString("place_holder_name"));

                            grossTurnOverLastYearEditText.setHint(response.getJSONArray("occupation_field_list")
                                    .getJSONObject(2).getString("place_holder_name"));

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
        params.put("source_of_income", companyType.get(companyTypeSpinner.getSelectedItem().toString()));
        params.put("net_monthly_income", companyNameEditText.getText().toString().trim());
        params.put("turnover_last_year", grossTurnOverLastYearEditText.getText().toString().trim());

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
                            showSnackBar(response.getString("error_msg"), R.color.red);
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
