package com.antworksmoney.financialbuddy.views.fragments.Loan;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.MessageFormat;
import java.util.Calendar;

public class DateOfBirthFragment extends Fragment {

    public DateOfBirthFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;


    public static DateOfBirthFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new DateOfBirthFragment();
    }

    private TextView dateOfBirthSelector;

    private Activity mActivity;

    private ProgressBar progressBar, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static final String TAG = "DateOfBirthFragment";

    private LinearLayout dateOfBirthSelectorLayout;

    private ImageView dateOfBirthImage;

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_of_birth, container, false);

        mActivity = getActivity();

        dateOfBirthSelector = rootView.findViewById(R.id.dateOfBirthSelector);

        progressBar = rootView.findViewById(R.id.progressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        dateOfBirthSelectorLayout = rootView.findViewById(R.id.dateOfBirthSelectorLayout);

        dateOfBirthImage = rootView.findViewById(R.id.dateOfBirthImage);

        loanHeading  = rootView.findViewById(R.id.loanHeading);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        Log.e(TAG, mLoanInfoEntity.toString());


        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {
            if (mLoanInfoEntity.getDateOfBirth() != null) {
                dateOfBirthSelector.setText(mLoanInfoEntity.getDateOfBirth());
            }
        }

        progressBar.setVisibility(View.INVISIBLE);

        dateOfBirthSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoanInfoEntity.getDateOfBirth().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select Date of Birth !!!");
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

        dateOfBirthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        journeyCompletedProgressBar.setProgress(23);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }

    public void DateDialog() {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mYear, mMonth, mDay;

        mYear = dateCalender.get(Calendar.YEAR);
        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(mActivity);
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(2000, mMonth, mDay);
        datePickerDialogBuilder.maxDate(2000, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (String.valueOf(year).trim().contains(",")) {
                    year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
                }
                dateOfBirthSelector.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                progressBar.setVisibility(View.VISIBLE);
                mLoanInfoEntity.setDateOfBirth(dateOfBirthSelector.getText().toString().trim());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeFragment();
                    }
                }, 1000);
            }
        });

        datePickerDialogBuilder.build().show();
    }

    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragmentToReplace ;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Credit Card")){
                fragmentToReplace = ContactDetailsFragment.newInstance(mLoanInfoEntity);
            }
            else {
              fragmentToReplace = MaritalStatusFragment.newInstance(mLoanInfoEntity);
            }


            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }
}
