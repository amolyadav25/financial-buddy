package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile.LBProfileHome;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.MYLoans.LBMyLoansFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBAddressDetails;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBCoBorrowerFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBLoanTypesFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBWaitingForApprovalFragment;


public class LBChangeRequestTypesFragment extends Fragment {

    public LBChangeRequestTypesFragment() {
        // Required empty public constructor
    }


    public static LBChangeRequestTypesFragment newInstance() {
        return new LBChangeRequestTypesFragment();
    }

    private Toolbar mToolbar;

    private RelativeLayout changeAddressLayout,
            changeMobileNumberLayout,
            coApplicantLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbchange_request, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        changeAddressLayout = rootView.findViewById(R.id.changeAddressLayout);

        changeMobileNumberLayout = rootView.findViewById(R.id.changeMobileNumberLayout);

        coApplicantLayout = rootView.findViewById(R.id.coApplicantLayout);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        changeAddressLayout.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBAddressDetails.newInstance("ChangeRequest"));
                transaction.addToBackStack(null).commit();
            }
        });

        changeMobileNumberLayout.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBChangeMobileRequestFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        coApplicantLayout.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBCoBorrowerFragment.newInstance(false));
                transaction.addToBackStack(null).commit();
            }
        });


        return rootView;
    }

}
