package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests.LBChangePasswordSendOtpFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBAddressDetails;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBBankDetailsFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LBProfileHome extends Fragment implements View.OnClickListener {

    public LBProfileHome() {
        // Required empty public constructor
    }

    public static LBProfileHome newInstance() {
        return new LBProfileHome();
    }

    private TextView occupationalDetailsTextView, personalDetailsTextView, accountDetailsTextView, residentialDetailsTextView;

    private LinearLayout pageTypeSelectorLayout;

    private ImageView toolBarIcon;

    private TextView userName, lastLogin;

    private Button resetPasswordButton, logoutButton;

    private static RelativeLayout progress_bar;

    private static final String TAG = "LBProfileHome";

    private CoordinatorLayout snackBarView;

    private SharedPreferences mSharedPreferances;

    private JSONObject responseObject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbprofile_home, container, false);

        pageTypeSelectorLayout = rootView.findViewById(R.id.pageTypeSelectorLayout);

        occupationalDetailsTextView = rootView.findViewById(R.id.occupationalDetailsTextView);

        personalDetailsTextView = rootView.findViewById(R.id.personalDetailsTextView);

        accountDetailsTextView = rootView.findViewById(R.id.accountDetailsTextView);

        residentialDetailsTextView = rootView.findViewById(R.id.residentialDetailsTextView);

        toolBarIcon = rootView.findViewById(R.id.toolBarIcon);

        userName = rootView.findViewById(R.id.userName);

        lastLogin = rootView.findViewById(R.id.lastLoginText);

        logoutButton = rootView.findViewById(R.id.logoutButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        resetPasswordButton = rootView.findViewById(R.id.resetPasswordButton);

        mSharedPreferances = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        toolBarIcon.setOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        fetchLenderProfileInfo();

        occupationalDetailsTextView.setOnClickListener(this);

        personalDetailsTextView.setOnClickListener(this);

        accountDetailsTextView.setOnClickListener(this);

        residentialDetailsTextView.setOnClickListener(this);

        logoutButton.setOnClickListener(this);

        resetPasswordButton.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.personalDetailsTextView:
                changeUI(0);
                break;

            case R.id.occupationalDetailsTextView:
                changeUI(2);
                break;

            case R.id.residentialDetailsTextView:
                changeUI(1);
                break;

            case R.id.accountDetailsTextView:
                changeUI(3);
                break;

            case R.id.logoutButton:
                SharedPreferences.Editor editor = mSharedPreferances.edit();
                editor.putString("loginSave","");
                editor.putString("loginToken", "");
                editor.putString(AppConstant.LOAN_STATUS_TRACKER,"");
                editor.putString(AppConstant.LOAN_TYPE_OBJECT,"");
                editor.putString(AppConstant.PROPOSAL_ID,"");
                editor.putString(AppConstant.LOAN_AMOUNT,"");
                editor.putString(AppConstant.SELECTED_LOAN_TYPE,"");
                editor.putString(AppConstant.OCCUPATION_ID,"");
                editor.putString(AppConstant.LOAN_DETAILS,"");
                editor.putString(AppConstant.LOAN_NAME, "");
                editor.putString(AppConstant.LOAN_AGREEMENT_OBJECT,"");
                editor.apply();

                Intent intent = new Intent(getContext(), HomeActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;

            case R.id.resetPasswordButton:
                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBChangePasswordSendOtpFragment.newInstance(true));
                    transaction.addToBackStack(null).commit();
                }

        }

    }




    private void changeUI(int position){
        for (int i=0; i<pageTypeSelectorLayout.getChildCount(); i++){
            TextView textView = (TextView) pageTypeSelectorLayout.getChildAt(i);
            if (i==position){
                if (getContext() != null){
                    textView.setTextColor(getContext().getResources().getColor(R.color.white));
                    textView.setBackground(getContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                }
            }
            else {
                if (getContext() != null){
                    textView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                    textView.setBackground(getContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                }
            }
        }

        Fragment fragmentToReplace = null;
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.profileBaseParent);
        switch (position){
            case 0:
                if (!(currentFragment instanceof LBPersonalInfo)){
                    fragmentToReplace = LBPersonalInfo.newInstance(responseObject);
                }
                break;

            case 1:
                if (!(currentFragment instanceof LBAddressDetails)){
                    fragmentToReplace = LBAddressDetails.newInstance("ProfileHome");
                }
                break;

            case 2:
                if (!(currentFragment instanceof LBMyOccupationFragment)){
                    fragmentToReplace = LBMyOccupationFragment.newInstance();
                }
                break;

            case 3:
                if (!(currentFragment instanceof LBBankDetailsFragment)){
                    fragmentToReplace = LBBankDetailsFragment.newInstance(true);
                }
                break;
        }

        if (fragmentToReplace != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profileBaseParent, fragmentToReplace);
            transaction.commit();
        }
    }

    public static RelativeLayout getProgressBar(){
        return progress_bar;
    }

    private void fetchLenderProfileInfo(){

        progress_bar.setVisibility(View.VISIBLE);

        try {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    AppConstant.borrowerBaseUrl + "borrowerinfo/myPersonalDetails",
                    null,
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            userName.setText(response.getJSONObject("MyPersonalDetails").getString("name"));
                            lastLogin.setText("Last Login : " + new java.util.Date(System.currentTimeMillis()).toString());

                            responseObject = response;
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.profileBaseParent, LBPersonalInfo.newInstance(responseObject));
                            transaction.commit();

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
                    params.put("Authorization", mSharedPreferances.getString("loginToken",""));
                    params.put("Content-Type", "application/json");
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
