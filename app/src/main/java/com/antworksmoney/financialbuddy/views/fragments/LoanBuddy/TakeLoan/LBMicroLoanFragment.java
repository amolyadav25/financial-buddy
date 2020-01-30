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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LBMicroLoanFragment extends Fragment {


    public LBMicroLoanFragment() {
        // Required empty public constructor
    }

    public static LBMicroLoanFragment newInstance() {
        return new LBMicroLoanFragment();
    }

    private Toolbar mToolbar;


    private Spinner modeOfPurchaseSpinner;

    private Button submitButton;

    private CoordinatorLayout snackBarView;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBConsumerLoanFragment";

    private SharedPreferences mSharedPreferences;

    private JSONObject consumerLoanObject;

    private SeekBar amountSeekBar;

    private TextView amountTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_micro_loan, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        modeOfPurchaseSpinner = rootView.findViewById(R.id.modeOfPurchaseSpinner);

        submitButton = rootView.findViewById(R.id.submitButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        amountSeekBar = rootView.findViewById(R.id.amountSeekBar);

        amountTextView = rootView.findViewById(R.id.amountTextView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        amountTextView.setText(String.valueOf(500*amountSeekBar.getProgress()));

        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                amountTextView.setText(String.valueOf(500*i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        try {
            consumerLoanObject = new JSONObject(mSharedPreferences.getString(AppConstant.LOAN_TYPE_OBJECT,""))
                    .getJSONArray("loan_type").getJSONObject(0);

            int tenureStartValue = Integer.parseInt(consumerLoanObject.getJSONObject("cash_loan")
                                          .getString("minimum_tenor"));

            int tenureEndValue = Integer.parseInt(consumerLoanObject.getJSONObject("cash_loan")
                    .getString("maximum_tenor"));

            ArrayList<String> tenureValues = new ArrayList<>();
            for (int i=tenureStartValue; i<=tenureEndValue; i++){
                tenureValues.add(String.valueOf(i));
            }
            tenureValues.add("Select Tenure");

            ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(
                    getActivity(), R.layout.checked_text_view, tenureValues) {
                @Override
                public int getCount() {
                    return tenureValues.size()-1;
                }
            };
            adapter_city.setDropDownViewResource(R.layout.checked_text_view);
            modeOfPurchaseSpinner.setAdapter(adapter_city);
            modeOfPurchaseSpinner.setSelection(tenureValues.size()-1);

        }catch (Exception e){
            e.printStackTrace();
        }


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("2")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "1A");
            editor.apply();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        submitButton.setOnClickListener(view1 -> {
          try {
              if (Integer.parseInt(amountTextView.getText().toString().trim())<1000){
                  showSnackBar("Amount Can't be less then 1000",R.color.red);
              }
              else if (modeOfPurchaseSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Select Tenure")){
                  showSnackBar("Select Tenure !!", R.color.red);
              }
              else {
                  addLoanProposal(consumerLoanObject.getString("p2p_product_id"));
              }
          }catch (Exception e){
              e.printStackTrace();
          }
        });

    }


    private void addLoanProposal(String productId){

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("p2p_product_id", productId);
        params.put("loan_amount", amountTextView.getText().toString().trim());
        params.put("tenor_months", modeOfPurchaseSpinner.getSelectedItem().toString().trim());
        params.put("loan_description","Micro Loan");

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/addProposal",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")){

                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(AppConstant.SELECTED_LOAN_TYPE,"0");
                            editor.putString(AppConstant.PROPOSAL_ID, response.getString("proposal_id"));
                            editor.putString(AppConstant.LOAN_AMOUNT, amountTextView.getText().toString().trim());
                            editor.apply();

                            showSnackBar("Proposal added successfully !!", R.color.green);
                        }
                        else {
                            showSnackBar("Failed to add !!", R.color.red);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        showSnackBar("Failed to add !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", mSharedPreferences.getString("loginToken",""));
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
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBGenderSelectorFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }



}
