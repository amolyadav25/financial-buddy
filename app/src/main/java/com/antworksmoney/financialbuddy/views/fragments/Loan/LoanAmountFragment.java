package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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


    private EditText et_reg_amount,et_reg_pan,et_reg_postal_Code;

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nationality_and_salary, container, false);
        Log.e("Mytag","LoanAmountFragment");

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        et_reg_amount = rootView.findViewById(R.id.et_reg_amount);

        et_reg_pan = rootView.findViewById(R.id.et_reg_pan);

        et_reg_postal_Code = rootView.findViewById(R.id.et_reg_postal_Code);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_reg_amount.getText().toString().trim().equals("")) {
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter loan amount !!!");
                } else if(et_reg_pan.getText().toString().isEmpty()){
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter pan !!!");
                }else if(et_reg_postal_Code.getText().toString().isEmpty()){
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter postal code !!!");
                }else if(et_reg_postal_Code.getText().toString().length()!=6){
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter valid postal code !!!");
                }
                else if(et_reg_pan.getText().toString().length()!=10){
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showSnackBar("Please enter valid pan number !!!");
                }
                else changeFragment();
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
           mLoanInfoEntity.setPostalcode(et_reg_postal_Code.getText().toString());
           mLoanInfoEntity.setPan(et_reg_pan.getText().toString());
            Fragment fragment;
//            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Instant Loan")){
//               fragment = InstantLoanResultFragment.newInstance();
//            }
//            else {
               fragment = LoanOfferRequestFragment.newInstance(mLoanInfoEntity);
          //  }

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
