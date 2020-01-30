package com.antworksmoney.financialbuddy.views.fragments.Investment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class InvestmentDetailsFragment extends Fragment {

    private static InvestmentEntity mInvestmentEntity;

    public InvestmentDetailsFragment() {
        // Required empty public constructor
    }

    public static InvestmentDetailsFragment newInstance(InvestmentEntity entity) {
        mInvestmentEntity = entity;
        return new InvestmentDetailsFragment();
    }


    private Toolbar topToolBar;

    private TextView titleTextView;

//    private TextView agreeText;

    private Button buttonInterested;

    private Activity mActivity;

    private static final String TAG = "InvestmentDetailsFragme";

    private CoordinatorLayout snackBarView;

    private TextView description;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_investment_details, container, false);

        topToolBar = rootView.findViewById(R.id.toolbar);

        titleTextView = rootView.findViewById(R.id.title);

//        agreeText = rootView.findViewById(R.id.agreeText);

        buttonInterested = rootView.findViewById(R.id.buttonInterested);

//        bottomSheet = rootView.findViewById(R.id.bottom_sheet);
//
//        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);
//
//        et_user_mail = rootView.findViewById(R.id.et_user_mail);
//
//        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);
//
//        stateName = rootView.findViewById(R.id.stateName);
//
//        cityName = rootView.findViewById(R.id.cityName);
//
//        stateSelectorLayout = rootView.findViewById(R.id.stateSelectorLayout);
//
//        citySelectorLayout = rootView.findViewById(R.id.citySelectorLayout);

        description = rootView.findViewById(R.id.description);

//        citySelectorProgressbar = rootView.findViewById(R.id.citySelectorLoader);
//
//        stateSeletorProgressBar = rootView.findViewById(R.id.stateSelectorLoader);
//
//        closeButton = rootView.findViewById(R.id.cancelButton);
//
//        view = rootView.findViewById(R.id.bg);

        snackBarView = rootView.findViewById(R.id.snackBarView);

//        proceedButton = rootView.findViewById(R.id.ProceedButton);
//
//        horizontalScrollView = rootView.findViewById(R.id.horizontalScrollView);
//
//        interestRateComparisonHeading = rootView.findViewById(R.id.interestRateComparisonHeading);

//        myTable = rootView.findViewById(R.id.myTable);

//        mDataRequestQueue = Volley.newRequestQueue(getContext());

//        tableLayoutLoader = rootView.findViewById(R.id.tableLayoutLoader);
//
//        querryTextBox = rootView.findViewById(R.id.querryTextBox);

        mActivity = getActivity();

//        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        if (topToolBar != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            assert getActivity() != null;


            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            topToolBar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));


            if (getContext() != null){
                if (mInvestmentEntity.getInvestmentId().trim().equalsIgnoreCase("4")){
                    description.setText("Peer-to-peer investment, also abbreviated as P2P investment, is the practice of lending money to individuals or businesses through online services that match lenders with borrowers. Since peer-to-peer lending companies offering these services generally operate online, they can run with lower overhead and provide the service more cheaply than traditional financial institutions.\n\nAs a result, lenders can earn higher returns compared to savings and investment products offered by banks, while borrowers can borrow money at lower interest rates,  even after the P2P lending company has taken a fee for providing the match-making platform and credit checking the borrower. There is the risk of the borrower defaulting on the loans taken out from peer-lending websites.");
                }
                else  if (mInvestmentEntity.getInvestmentId().trim().equalsIgnoreCase("5")){
                    description.setText(getContext().getResources().getString(R.string.cordporate_fd));
                } else  if (mInvestmentEntity.getInvestmentId().trim().equalsIgnoreCase("6")){
                    description.setText(getContext().getResources().getString(R.string.corporate_bonds));
                }
            }

//            if (mInvestmentEntity.getId() == null){
//                description.setText("Peer-to-peer investment, also abbreviated as P2P investment, is the practice of lending money to individuals or businesses through online services that match lenders with borrowers. Since peer-to-peer lending companies offering these services generally operate online, they can run with lower overhead and provide the service more cheaply than traditional financial institutions.\n\nAs a result, lenders can earn higher returns compared to savings and investment products offered by banks, while borrowers can borrow money at lower interest rates,  even after the P2P lending company has taken a fee for providing the match-making platform and credit checking the borrower. There is the risk of the borrower defaulting on the loans taken out from peer-lending websites.");
//            }

        }

        titleTextView.setText(mInvestmentEntity.getInvestmentType());

        topToolBar.setTitle(mInvestmentEntity.getInvestmentType());

//        interestRateComparisonHeading.setText(MessageFormat.format("{0} INTEREST RATE COMPARISON",
//                mInvestmentEntity.getInvestmentType()));

//        agreeText.setText(MessageFormat.format("Simply click on I am interested and register for Antworks ''{0}'' services.",
//                mInvestmentEntity.getInvestmentType()));


//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheetBehavior.setPeekHeight(0);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
//                    view.setVisibility(View.GONE);
//                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            displayLocationSettingsRequest(getContext());
//                        }
//                    }, 100);
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                view.setVisibility(View.VISIBLE);
//                view.setAlpha(slideOffset);
//            }
//        });
//
//        if (mInvestmentEntity.getApplyFor().trim().equalsIgnoreCase("Self")) {
//            et_reg_fname.setText(pref.getString("user_name", ""));
//
//            et_user_mail.setText(pref.getString("email_value", ""));
//
//            et_user_phone_number.setText(pref.getString("user_phone", ""));
//
//        }

        buttonInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changeVisibilityOfbottomSheet();
                Log.e(TAG, mInvestmentEntity.getInvestmentId().trim());

                FragmentTransaction transaction = ((FragmentActivity)mActivity).getSupportFragmentManager().beginTransaction();
                if (mInvestmentEntity.getInvestmentId().trim().equalsIgnoreCase("4")){
                    transaction.replace(R.id.homeParent,InvestmentApplyNowFragment.newInstance(mInvestmentEntity));
                }
                else {
                    transaction.replace(R.id.homeParent,InvestmentBankDetailsFragment.newInstance(mInvestmentEntity));
                }
                transaction.addToBackStack(null).commit();
            }
        });
//
//        closeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                changeVisibilityOfbottomSheet();
//            }
//        });
//
//        stateSelectorLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mActivity, RegionalDataFetch.class);
//                intent.putExtra("stateName", "");
//                startActivityForResult(intent, SELECT_REGIONAL_DATA);
//            }
//        });
//
//        citySelectorLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mActivity, RegionalDataFetch.class);
//                intent.putExtra("stateName", stateName.getText().toString().trim());
//                startActivityForResult(intent, SELECT_REGIONAL_DATA);
//            }
//        });
//
//        proceedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (et_reg_fname == null || et_reg_fname.getText().toString().length() < 1) {
//                    showSnackBar("Please enter a valid User name");
//                    et_reg_fname.requestFocus();
//                } else if (et_user_phone_number == null || et_user_phone_number.getText().toString().length() < 10) {
//                    showSnackBar("Please enter a valid phone number");
//                    et_user_phone_number.requestFocus();
//                } else if (et_user_mail == null || (!et_user_mail.getText().toString().contains("@"))) {
//                    showSnackBar("Please enter a E-mail id");
//                    et_user_mail.requestFocus();
//                } else {
//                    mInvestmentEntity.setPersonName(et_reg_fname.getText().toString().trim());
//                    mInvestmentEntity.setPersonPhoneNumber(et_user_phone_number.getText().toString().trim());
//                    mInvestmentEntity.setPersonMailId(et_user_mail.getText().toString().trim());
//                    mInvestmentEntity.setPersonCity(cityName.getText().toString().trim());
//                    mInvestmentEntity.setPersonState(stateName.getText().toString().trim());
//                    mInvestmentEntity.setQuerryString(querryTextBox.getText().toString().trim());
//                }
//            }
//        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000);
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        showTableLayout();

    }


//    private void showSnackBar(String message) {
//        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
//    }

//    private void changeVisibilityOfbottomSheet() {
//        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//    }




}
