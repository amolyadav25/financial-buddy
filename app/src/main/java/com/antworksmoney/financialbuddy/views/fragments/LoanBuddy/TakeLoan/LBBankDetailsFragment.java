package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile.LBProfileHome;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LBBankDetailsFragment extends Fragment {

    public LBBankDetailsFragment() {
        // Required empty public constructor
    }

    private static boolean mCalledFromProfilePage;

    public static LBBankDetailsFragment newInstance(boolean calledFromProfilePage) {
        mCalledFromProfilePage = calledFromProfilePage;
        return new LBBankDetailsFragment();
    }

    private Toolbar mToolbar;

    private Spinner bankNameSelector;

    private EditText et_user_account, et_user_confirm_account, et_user_ifsc;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBBankDetailsFragment";

    private SharedPreferences mSharedPreferences;

    private RequestQueue mRequestQueue;

    private ArrayList<String> arrayBankDetails;

    private Button submitBankDetails;

    private RelativeLayout snackBarView;

    private ImageView bannerImage;

    private TextView confirmAccountNumberHeading;

    private boolean moveNextBankName = true, moveNextAccountNumber = true, moveNextIFSCCode = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbbank_detials, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        bankNameSelector = rootView.findViewById(R.id.bankNameSelector);

        et_user_account = rootView.findViewById(R.id.et_user_account);

        et_user_confirm_account = rootView.findViewById(R.id.et_user_confirm_account);

        et_user_ifsc = rootView.findViewById(R.id.et_user_ifsc_code);

        submitBankDetails = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        bannerImage = rootView.findViewById(R.id.bannerImage);

        confirmAccountNumberHeading = rootView.findViewById(R.id.confirmAccountNumberHeading);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mRequestQueue = Volley.newRequestQueue(getContext());

        arrayBankDetails = new ArrayList<>();

        if (mCalledFromProfilePage){

            //called from profile page
            bannerImage.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
            submitBankDetails.setVisibility(View.GONE);
            progress_bar = LBProfileHome.getProgressBar();

            bankNameSelector.setEnabled(false);
            et_user_account.setFocusableInTouchMode(false);
            et_user_confirm_account.setVisibility(View.GONE);
            et_user_ifsc.setFocusableInTouchMode(false);
            confirmAccountNumberHeading.setVisibility(View.GONE);
        }
        else {
            submitBankDetails.setVisibility(View.VISIBLE);
            progress_bar = rootView.findViewById(R.id.progress_bar);

            if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("14")) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(AppConstant.LOAN_STATUS_TRACKER, "13");
                editor.apply();
            }
        }

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


//        et_user_confirm_account.setText("3903582719");
//        et_user_account.setText("3903582719");
//        et_user_ifsc.setText("CBIN0280402");


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchBankNames();

        submitBankDetails.setOnClickListener(view1 -> {
            if (bankNameSelector.getSelectedItem().toString().trim().equalsIgnoreCase("Select Bank")){
                showSnackBar("Select Bank first !!", R.color.red);
            }
            else if (et_user_account.getText().toString().trim().equalsIgnoreCase("")){
                et_user_account.setError("Invalid Account Number !!");
                et_user_account.requestFocus();
            }
            else if (!et_user_account.getText().toString().trim().equalsIgnoreCase(et_user_confirm_account.getText().toString().trim())){
                et_user_confirm_account.setError("Account details doesn't match !!");
                et_user_confirm_account.requestFocus();
            }
            else if (et_user_ifsc.getText().toString().trim().equalsIgnoreCase("")){
                et_user_ifsc.setError("Invalid IFSC code !!");
                et_user_ifsc.requestFocus();
            }
            else {
                Log.e(TAG, String.valueOf(moveNextIFSCCode));
                Log.e(TAG, String.valueOf(moveNextAccountNumber));
                Log.e(TAG, String.valueOf(moveNextBankName));

                if (moveNextIFSCCode && moveNextAccountNumber && moveNextBankName){
                    showSnackBar("Validated Successfully !!", R.color.green);
                }
                else {
                    Log.e(TAG, "Details not complete");
                    updateBankDetailsToServer();
                }
            }
        });
    }

    private void fetchBankNames(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getBanklist",
                response -> {

                    try {

                        Log.e(TAG, response);

                        JSONObject jsonObject = new JSONObject(response);

                        for (int i = 0; i < jsonObject.getJSONArray("bank_list").length(); i++) {

                            arrayBankDetails.add(jsonObject.getJSONArray("bank_list")
                                    .getJSONObject(i)
                                    .getString("name"));
                        }

                        arrayBankDetails.add("Select Bank");

                        ArrayAdapter<String> adapter_education = new ArrayAdapter<String>(
                                getActivity(), R.layout.checked_text_view, arrayBankDetails) {
                            @Override
                            public int getCount() {
                                return arrayBankDetails.size() - 1;
                            }
                        };

                        adapter_education.setDropDownViewResource(R.layout.checked_text_view);
                        bankNameSelector.setAdapter(adapter_education);
                        bankNameSelector.setSelection(arrayBankDetails.size() - 1);

                        fetchUserAccountInfo();

                    } catch (Exception e) {

                        progress_bar.setVisibility(View.GONE);

                        e.printStackTrace();
                    }

                },
                error -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e("ERROR", "error => " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                Log.e(TAG, params.toString());
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);
    }

    private void updateBankDetailsToServer(){

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("bank_name", bankNameSelector.getSelectedItem().toString().trim());
        params.put("account_no", et_user_account.getText().toString().trim());
        params.put("caccount_no",et_user_confirm_account.getText().toString().trim());
        params.put("ifsc_code",et_user_ifsc.getText().toString().trim());

        Log.e(TAG, new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/addBank",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {

                        if (response.getString("status").trim().equalsIgnoreCase("1")){

                            showSnackBar("Bank updated successfully !!", R.color.green);

                        }
                        else {
                            showSnackBar("Failed to update bank details !!", R.color.red);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        showSnackBar("Failed to update bank details !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to update company bank !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                params.put("Content-Type", "application/json");
                Log.e(TAG, params.toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);
    }

    private void fetchUserAccountInfo(){

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.borrowerBaseUrl + "borrowerinfo/myAccountDetails",
                response -> {

                    Log.e(TAG, response);

                    progress_bar.setVisibility(View.GONE);

                    try {

                        JSONObject dataObject = new JSONObject(response);

                        if (!dataObject.getJSONObject("MyAccountDetails").getString("bank_name").trim().equalsIgnoreCase("")){

                            int selectedIndex = arrayBankDetails.size() -1 ;

                            for (int i=0; i<arrayBankDetails.size(); i++){
                                if (arrayBankDetails.get(i).trim().equalsIgnoreCase(
                                        dataObject.getJSONObject("MyAccountDetails").getString("bank_name").trim())){
                                    selectedIndex = i;
                                    moveNextBankName = true;
                                    break;
                                }
                                else {
                                    moveNextBankName = false;
                                }
                            }

                            bankNameSelector.setSelection(selectedIndex);

                        }
                        else {
                            moveNextBankName = false;
                        }

                        if (!dataObject.getJSONObject("MyAccountDetails").getString("account_number").trim().equalsIgnoreCase("")){
                            et_user_account.setText(dataObject.getJSONObject("MyAccountDetails").getString("account_number"));
                            et_user_confirm_account.setText(dataObject.getJSONObject("MyAccountDetails").getString("account_number"));
                        }
                        else {
                            moveNextAccountNumber = false;
                        }

                        if (!dataObject.getJSONObject("MyAccountDetails").getString("ifsc_code").trim().equalsIgnoreCase("")){
                            et_user_ifsc.setText(dataObject.getJSONObject("MyAccountDetails").getString("ifsc_code"));
                        }
                        else {
                            moveNextIFSCCode = false;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e("ERROR", "error => " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);
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
                    transaction.replace(R.id.homeParent, LBBankStatementUploadFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}
