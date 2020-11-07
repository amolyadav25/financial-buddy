package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class SalaryProcessFragment extends Fragment {

    public SalaryProcessFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;

    public static SalaryProcessFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new SalaryProcessFragment();
    }


    private Spinner maritalStatusSelector;

    private ArrayAdapter<String> adapterMaritalStatus;

    private ArrayList<String> maritalStatusList;

    private FragmentActivity mActivity;

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private AutoCompleteTextView bankNameSelector;

    private static final String TAG = "SalaryProcessFragment";

    private EditText salaryEditText, emiEditText;

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_salary_process, container, false);
        mActivity = getActivity();

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        emiEditText = rootView.findViewById(R.id.emiEditText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        bankNameSelector = rootView.findViewById(R.id.bankNameSelector);

        maritalStatusList = new ArrayList<>();
        maritalStatusList.add("Cheque");
        maritalStatusList.add("Account Transfer");
        maritalStatusList.add("Cash");
        maritalStatusList.add("Salary Process");

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        salaryEditText = rootView.findViewById(R.id.salaryEditText);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        adapterMaritalStatus = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, maritalStatusList) {
            @Override
            public int getCount() {
                return maritalStatusList.size() - 1;
            }
        };
        adapterMaritalStatus.setDropDownViewResource(R.layout.checked_text_view);
        maritalStatusSelector.setAdapter(adapterMaritalStatus);

        if (mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Professional")
                || mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Business")) {

            maritalStatusSelector.setVisibility(View.GONE);
            salaryEditText.setHint("Net Monthly Income");
        }
        else if ( mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")){
            maritalStatusSelector.setVisibility(View.GONE);
            salaryEditText.setVisibility(View.GONE);
        }


        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {
            if (!mLoanInfoEntity.getMaritalStatus().trim().equalsIgnoreCase("")) {
                int selectedItem = maritalStatusList.size() - 1;
                for (int i = 0; i < maritalStatusList.size(); i++) {
                    if (maritalStatusList.get(i).trim().equalsIgnoreCase(mLoanInfoEntity.getMaritalStatus().trim())) {
                        selectedItem = i;
                        break;
                    }
                }
                maritalStatusSelector.setSelection(selectedItem);
            } else maritalStatusSelector.setSelection(maritalStatusList.size() - 1);
        } else maritalStatusSelector.setSelection(maritalStatusList.size() - 1);


        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (((String) maritalStatusSelector.getSelectedItem()).trim().equalsIgnoreCase("Salary Process")) {
//                    ((HomeActivity) getActivity()).showSnackBar("Please select Salary Process");
//                } else if (salaryEditText.getText().toString().trim().equalsIgnoreCase("")) {
//                    ((HomeActivity) getActivity()).showSnackBar("Please Enter your salary");
//                } else if (emiEditText.getText().toString().trim().equalsIgnoreCase("")){
//                    ((HomeActivity) getActivity()).showSnackBar("Please Enter your EMI");
//                } else {
                if (getActivity() != null) {
                    mLoanInfoEntity.setSalaryProcess(((String) maritalStatusSelector.getSelectedItem()).trim());
                    mLoanInfoEntity.setSalary(salaryEditText.getText().toString().trim());
                    mLoanInfoEntity.setEMI(emiEditText.getText().toString());
                    mLoanInfoEntity.setBankName(bankNameSelector.getText().toString().trim());

                    changeFragment();
//                    }
                }
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

        ArrayAdapter<String> dataListAdapter = new ArrayAdapter<>(mActivity, R.layout.autocomplete_text_layout);
        dataListAdapter.addAll(
                "HDFC Bank",
                "CICI Bank",
                "Punjab National Bank",
                "Capital First",
                "DHFL",
                "RBL Bank",
                "Yes Bank",
                "Axis Bank",
                "TATA Capital",
                "Indiabulls",
                "State Bank Of India",
                "Shriram Finance",
                "HDB",
                "others");
        bankNameSelector.setAdapter(dataListAdapter);

        maritalStatusSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (maritalStatusList.get(position).trim().equalsIgnoreCase("Account Transfer")) {
                    bankNameSelector.setVisibility(View.VISIBLE);
                } else {
                    bankNameSelector.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        journeyCompletedProgressBar.setProgress(86);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }

    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragmentToReplace;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Credit Card")){
                fragmentToReplace = ThankYouFragment.newInstance(mLoanInfoEntity);
            }
            else {
                fragmentToReplace = LoanAmountFragment.newInstance(mLoanInfoEntity);
            }

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }
}