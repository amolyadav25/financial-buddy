package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile.LBProfileHome;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests.LBChangeRequestTypesFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Documents.LBDocumentsUploadFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans.LBMyLoansFragment;

import org.json.JSONObject;

import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;


public class LBDashBoardFragment extends Fragment {

    private LinearLayout otherLayout_loanbuddy;

    public LBDashBoardFragment() {
        // Required empty public constructor
    }


    public static LBDashBoardFragment newInstance() {
        return new LBDashBoardFragment();
    }

    private Toolbar mToolbar;

    private RelativeLayout lendMoneyLayout,
            requestLayout,
            lbProfileLayout,
            uploadDocument,
            myLoanSLayout;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout progressBar;

    private CoordinatorLayout snackBarView;

    private static final String TAG = "LBDashBoardFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbdash_board, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        lendMoneyLayout = rootView.findViewById(R.id.lendMoneyLayout);

        requestLayout = rootView.findViewById(R.id.requestLayout);

        lbProfileLayout = rootView.findViewById(R.id.lbProfileLayout);

        uploadDocument = rootView.findViewById(R.id.uploadDocument);

        progressBar = rootView.findViewById(R.id.progress_bar);

        myLoanSLayout = rootView.findViewById(R.id.myLoanSLayout);

        snackBarView = rootView.findViewById(R.id.snackBarView);


      //  otherLayout_loanbuddy = rootView.findViewById(R.id.otherLayout_loanbuddy);






        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        lendMoneyLayout.setOnClickListener(view -> {
            if (getActivity() != null) {

                Fragment fragmentToReplace;

//                SharedPreferences.Editor editor = mSharedPreferences.edit();
//                editor.putString(AppConstant.LOAN_STATUS_TRACKER, "0");
//                editor.apply();

                if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("1A")) {
                    fragmentToReplace = LBMicroLoanFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("1B")) {
                    fragmentToReplace = LBConsumerLoanFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("2")) {
                    fragmentToReplace = LBGenderSelectorFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("3")) {
                    fragmentToReplace = LBDateOfBirthFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("4")) {
                   fragmentToReplace = LBMaritalStatusFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("5")) {
                   fragmentToReplace = LBQualificationSelectorFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("6")) {
                   fragmentToReplace = LBOccupationSelectorFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7A")) {
                   fragmentToReplace = LBCompanySelectorFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7AA")) {
                   fragmentToReplace = LBSalaryProcessFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7B")) {
                   fragmentToReplace = LBSelfEmployedBusinessFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7C")) {
                   fragmentToReplace = LBSelfEmployedProfessionalFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7D")) {
                   fragmentToReplace = LBSelfOtherProfessionFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("8")) {
                   fragmentToReplace = LBAddressDetails.newInstance("SalaryProcess");
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("9")) {
                   fragmentToReplace = LBPaymentRegistrationFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("10")) {
                   fragmentToReplace = LBSoftApprovalFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("11")) {
                   fragmentToReplace = LBKYCUploadFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("12")) {
                   fragmentToReplace = LBCoBorrowerFragment.newInstance(true);
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("13")) {
                   fragmentToReplace = LBBankDetailsFragment.newInstance(false);
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("14")) {
                   fragmentToReplace = LBBankStatementUploadFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("15")) {
                   fragmentToReplace = LBWaitingForApprovalFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("16")) {
                   fragmentToReplace = LBLoanConfirmationFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("17")) {
                   fragmentToReplace = LoanAgreementViewFragment.newInstance(true);
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("18")) {
                   fragmentToReplace = LBSignOTPVerificationFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("19")) {
                   fragmentToReplace = LBEstimatedTimeFragment.newInstance();
                } else if (mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("20")) {
                   fragmentToReplace = LBAccountFragment.newInstance();
                } else {
                    fragmentToReplace = null;
                    checkIfLoanExistsOrNot();
                }

                if (fragmentToReplace != null && getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, fragmentToReplace);
                    transaction.addToBackStack(null).commit();
                }


            }
        });


        lbProfileLayout.setOnClickListener(view -> {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBProfileHome.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        myLoanSLayout.setOnClickListener(view -> {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBMyLoansFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        requestLayout.setOnClickListener(view -> {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBChangeRequestTypesFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        uploadDocument.setOnClickListener(view -> {
            if (getActivity() != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBDocumentsUploadFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });


        return rootView;
    }

    private void checkIfLoanExistsOrNot() {
        progressBar.setVisibility(View.VISIBLE);

        try {

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    AppConstant.borrowerBaseUrl + "borrowerloan/checkBorrowerCurrentproposal",
                    null,
                    response -> {

                        progressBar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            if (response.getString("status").trim().equalsIgnoreCase("0")) {
                                showSnackBar(response.getString("msg"), R.color.red);
                            } else {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent, LBLoanTypesFragment.newInstance());
                                transaction.addToBackStack(null).commit();

                            }

                        } catch (Exception e) {
                            showSnackBar("Failed to get status !!", R.color.red);
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to get status !!", R.color.red);
                progressBar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
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

        } catch (Exception e) {
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
