package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.LoanListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class LBMyLoansFragment extends Fragment {

    public LBMyLoansFragment() {
        // Required empty public constructor
    }


    public static LBMyLoansFragment newInstance() {
        return new LBMyLoansFragment();
    }


    private Toolbar mToolbar;

    private RelativeLayout progress_bar;

    private RecyclerView myLoanList;

    private CoordinatorLayout snackBarView;

    private static final String TAG = "LBMyLoansFragment";

    private SharedPreferences mSharedPreferences;

    private TextView onGoingLoansTextView, closedLoansTextView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbmy_loans, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        onGoingLoansTextView = rootView.findViewById(R.id.onGoingLoansTextView);

        closedLoansTextView = rootView.findViewById(R.id.closedLoansTextView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        myLoanList = rootView.findViewById(R.id.myLoanList);
        myLoanList.setLayoutManager(new LinearLayoutManager(getContext()));
        myLoanList.setHasFixedSize(true);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        closedLoansTextView.setOnClickListener(view -> {
            closedLoansTextView.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            onGoingLoansTextView.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
            loadMyLoanList("Closed");
        });


        onGoingLoansTextView.setOnClickListener(view -> {
            onGoingLoansTextView.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            closedLoansTextView.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
            loadMyLoanList("open");

        });


        loadMyLoanList("open");

        return rootView;
    }

    private void loadMyLoanList(String loanStatus){

        progress_bar.setVisibility(View.VISIBLE);

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    AppConstant.borrowerBaseUrl + "borrowerloan/myLoanlist",
                    null,
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            ArrayList<LoanInfoEntity> loanInfoEntityArrayList = new ArrayList<>();

                            for (int i=0; i<response.getJSONArray("myloan").length(); i++){
                                if (!loanStatus.trim().equalsIgnoreCase("")){
                                    JSONObject dataObject = response.getJSONArray("myloan").getJSONObject(i);
                                    if (dataObject.getString("loan_status").trim().equalsIgnoreCase(loanStatus)){
                                        LoanInfoEntity entity = new LoanInfoEntity();
                                        entity.setbIdRegistrationId(dataObject.getString("bid_registration_id"));
                                        entity.setAccount(dataObject.getString("loan_no"));
                                        entity.setLoanName(dataObject.getString("product_type"));
                                        entity.setEMI(dataObject.getString("emi_amount"));
                                        entity.setLoanId(dataObject.getString("emi_id"));
                                        entity.setLoanAmount(dataObject.getString("emi_principal"));
                                        entity.setEmi_balance(dataObject.getString("balance_amount"));
                                        entity.setEmi_date(dataObject.getString("emi_date"));
                                        entity.setState(dataObject.getString("loan_status"));

                                        loanInfoEntityArrayList.add(entity);
                                    }
                                }
                                else {
                                    JSONObject dataObject = response.getJSONArray("myloan").getJSONObject(i);
                                    LoanInfoEntity entity = new LoanInfoEntity();
                                    entity.setbIdRegistrationId(dataObject.getString("bid_registration_id"));
                                    entity.setAccount(dataObject.getString("loan_no"));
                                    entity.setLoanName(dataObject.getString("product_type"));
                                    entity.setEMI(dataObject.getString("emi_amount"));
                                    entity.setLoanId(dataObject.getString("emi_id"));
                                    entity.setLoanAmount(dataObject.getString("emi_principal"));
                                    entity.setEmi_balance(dataObject.getString("balance_amount"));
                                    entity.setEmi_date(dataObject.getString("emi_date"));
                                    entity.setState(dataObject.getString("loan_status"));

                                    loanInfoEntityArrayList.add(entity);
                                }
                            }

                            LoanListAdapter adapter = new LoanListAdapter(getContext(), loanInfoEntityArrayList, myLoanList);
                            myLoanList.setAdapter(adapter);
                            adapter.setOnClick((position, view) -> {
                                Fragment fragmentToReplace = null;
                                switch (view.getId()){
                                    case R.id.viewDetailsButton:
                                        fragmentToReplace = LBLoanDetailsFragment.newInstance(loanInfoEntityArrayList.get(position));
                                        break;

                                    case R.id.payNowButton:
                                        fragmentToReplace = LBEmiPayFragment.newInstance(loanInfoEntityArrayList.get(position));
                                        break;
                                }

                                if (fragmentToReplace != null && getActivity() != null){
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, fragmentToReplace);
                                    transaction.addToBackStack(null).commit();
                                }
                            });


                        }catch (Exception e){
                            e.printStackTrace();
                            showSnackBar("Failed to get details !!", R.color.red);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to get details !!", R.color.red);
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

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getContext()).add(request);

        }catch (Exception e){
            progress_bar.setVisibility(View.GONE);
            showSnackBar("Failed to get details !!", R.color.red);
            e.printStackTrace();
        }

    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.show();
    }


}
