package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.text.MessageFormat;
import java.util.Objects;


public class LoanAmountFragment extends Fragment {

    public LoanAmountFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;

    public static LoanAmountFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new LoanAmountFragment();
    }

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;


    private EditText et_reg_amount;

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nationality_and_salary, container, false);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        et_reg_amount = rootView.findViewById(R.id.et_reg_amount);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reg_amount.getText().toString().trim().equals("")) {
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter loan amount !!!");
                } else changeFragment();
            }
        });

        previousButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        journeyCompletedProgressBar.setProgress(97);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }

    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragment;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Instant Loan")){
               fragment = InstantLoanResultFragment.newInstance();
            }
            else {
               fragment = LoanOfferRequestFragment.newInstance(mLoanInfoEntity);
            }

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragment);
            transaction.addToBackStack(null).commit();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mLoanInfoEntity.setLoanAmount(et_reg_amount.getText().toString().trim());
    }
}
