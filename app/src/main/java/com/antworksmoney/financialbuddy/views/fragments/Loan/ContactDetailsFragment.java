package com.antworksmoney.financialbuddy.views.fragments.Loan;


import android.app.Activity;
import android.content.SharedPreferences;
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

import static android.content.Context.MODE_PRIVATE;


public class ContactDetailsFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public static ContactDetailsFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new ContactDetailsFragment();
    }

    private EditText et_reg_fname,
            et_user_mail,
            et_user_phone_number,
            nationality;

    private SharedPreferences pref;

    private Activity mActivity;

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static final String TAG = "ContactDetailsFragment";

    private TextView loanHeading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        mActivity = getActivity();

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        nationality = rootView.findViewById(R.id.et_nationality);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {

            et_reg_fname.setText(mLoanInfoEntity.getName());

            et_user_mail.setText(mLoanInfoEntity.getEmail());

            et_user_phone_number.setText(mLoanInfoEntity.getPhoneNumber());
        }


        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoanInfoEntity.setName(et_reg_fname.getText().toString().trim());
                mLoanInfoEntity.setEmail(et_user_mail.getText().toString().trim());
                mLoanInfoEntity.setPhoneNumber(et_user_phone_number.getText().toString().trim());
                mLoanInfoEntity.setNationality(nationality.getText().toString().trim());

                if (mLoanInfoEntity.getName().equals("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Name !!!");
                } else if (mLoanInfoEntity.getEmail().trim().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Email !!!");
                } else if (mLoanInfoEntity.getPhoneNumber().trim().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Phone Number !!!");
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

        journeyCompletedProgressBar.setProgress(37);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }


    private void changeFragment() {
        if (getActivity() != null) {

            Fragment fragmentToReplace;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")) {
                fragmentToReplace = OtherDetailsFragment.newInstance(mLoanInfoEntity);
            }
            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Credit Card")){
                fragmentToReplace = SalaryProcessFragment.newInstance(mLoanInfoEntity);
            }
            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Instant Loan")){
                fragmentToReplace = LoanAmountFragment.newInstance(mLoanInfoEntity);
            }
            else {
                fragmentToReplace = QualificationSelectorFragment.newInstance(mLoanInfoEntity);
            }

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }
}