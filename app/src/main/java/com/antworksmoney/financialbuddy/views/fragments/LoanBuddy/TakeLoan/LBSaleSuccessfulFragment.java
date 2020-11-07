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
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class LBSaleSuccessfulFragment extends Fragment {

    public LBSaleSuccessfulFragment() {
        // Required empty public constructor
    }

    private Toolbar mToolbar;

    public static LBSaleSuccessfulFragment newInstance() {
        return new LBSaleSuccessfulFragment();
    }

    private TextView loanStatusTextView;

    private SharedPreferences mSharedPreferences;

    private Button loanAgreementButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbsale_successful, container, false);

        loanStatusTextView = rootView.findViewById(R.id.loanStatusTextView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        loanAgreementButton = rootView.findViewById(R.id.loanAgreementButton);

        loanStatusTextView.setText("Your Loan of Rs. " +
                mSharedPreferences.getString(AppConstant.LOAN_AMOUNT, "2000") +
                " has been disbursed to your account.");

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
