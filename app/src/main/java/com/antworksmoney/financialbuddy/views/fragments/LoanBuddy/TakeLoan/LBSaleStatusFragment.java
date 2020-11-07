package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class LBSaleStatusFragment extends Fragment {

    public LBSaleStatusFragment() {
        // Required empty public constructor
    }

    private Toolbar mToolbar;

    public static LBSaleStatusFragment newInstance() {
        return new LBSaleStatusFragment();
    }

    private SharedPreferences mSharedPreferences;

    private Button loanAgreementButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbsale_status, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        loanAgreementButton = rootView.findViewById(R.id.loanAgreementButton);

        mToolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        loanAgreementButton.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LoanAgreementViewFragment.newInstance(false));
                transaction.addToBackStack(null).commit();
            }
        });

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstant.LOAN_STATUS_TRACKER, "");
        editor.apply();

        return rootView;
    }
}
