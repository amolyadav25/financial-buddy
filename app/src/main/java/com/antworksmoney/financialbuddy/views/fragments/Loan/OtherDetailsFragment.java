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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.MessageFormat;
import java.util.Calendar;


public class OtherDetailsFragment extends Fragment {
    public OtherDetailsFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;

    public static OtherDetailsFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new OtherDetailsFragment();
    }

    private Spinner professionTypeSpinner,
            officeOwnerShip,
            auditDone,
            defaultedOnLoanCard,
            chequeBounceInLastSixMonth,
            ratedByRatingAgency,
            industryTypeSpinner;

    private EditText netWorthEditText,
            totalExperienceEditText,
            grossTurnOverLastYearEditText;

    private Button nextButtonForQuestions,
            previousButtonForQuestions;

    private Toolbar top_toolBar;

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private String[] arrayProfessionType = {
            "Doctor",
            "Teacher",
            "CA",
            "CS",
            "Architect",
            "Lawyer",
            "Other",
            "Select Profession Type"
    };

    private String[] arrayIndustryType = {
            "Manufacturing",
            "Trading",
            "Service",
            "KPO",
            "BPO",
            "Software",
            "Other",
            "Select Industry Type"
    };

    private String[] officeOwnerShipList = {
            "Owned",
            "Rented",
            "Select office ownership"
    };


    private String[] auditDoneArray = {
            "Yes",
            "No",
            "Audit done"
    };

    private String[] loanDefaulterArray = {
            "Yes",
            "No",
            "Ever defaulted on Loan/Card"
    };

    private String[] ratedByFinancialAgencyArray = {
            "Yes",
            "No",
            "Whether Company is rated by Rating Agency"
    };

    private String[] chequeBounceInLastSixMonthArray = {
            "Yes",
            "No",
            "Any Cheque Bounces in last 6 Months"
    };

    private ArrayAdapter<String> adapterProfessionType,
            adapterOwnerShip,
            auditDoneAdapter,
            loanDefaulterAdapter,
            ratedByFinancialAgencyAdapter,
            chequeBounceInLastSixMonthAdapter,
            adapterIndustryType;

    private TextView loanHeading;

    private EditText grossTurnOverBeforeLastYearEditText,
            officePhoneNumberEditText,
            cinNumber,
            grossTurnOver2EditText,
            grossTurnOver3EditText;

    private LinearLayout dateOfIncorporationLayout;

    private TextView dateOfIncorporation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_other_details, container, false);

        professionTypeSpinner = rootView.findViewById(R.id.industryTypeSpinner);

        officeOwnerShip = rootView.findViewById(R.id.officeOwnerShip);

        netWorthEditText = rootView.findViewById(R.id.netWorthEditText);

        totalExperienceEditText = rootView.findViewById(R.id.totalExperienceEditText);

        grossTurnOverLastYearEditText = rootView.findViewById(R.id.grossTurnOverLastYearEditText);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        defaultedOnLoanCard = rootView.findViewById(R.id.defaultedOnLoanCard);

        auditDone = rootView.findViewById(R.id.auditDone);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        officePhoneNumberEditText = rootView.findViewById(R.id.officePhoneNumberEditText);

        grossTurnOverBeforeLastYearEditText = rootView.findViewById(R.id.grossTurnOverBeforeLastYearEditText);

        chequeBounceInLastSixMonth = rootView.findViewById(R.id. chequeBounceInLastSixMonth);

        ratedByRatingAgency = rootView.findViewById(R.id.companyRatedByRatingAgency);

        cinNumber = rootView.findViewById(R.id.cinNumber);

        grossTurnOver2EditText = rootView.findViewById(R.id.grossTurnOver2EditText);

        grossTurnOver3EditText = rootView.findViewById(R.id.grossTurnOver3EditText);

        dateOfIncorporation = rootView.findViewById(R.id.dateOfIncorporation);

        dateOfIncorporationLayout = rootView.findViewById(R.id.dateOfIncorporationLayout);

        industryTypeSpinner = rootView.findViewById(R.id.professionTypeSpinner);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        adapterProfessionType = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, arrayProfessionType) {
            @Override
            public int getCount() {
                return arrayProfessionType.length - 1;
            }
        };
        adapterProfessionType.setDropDownViewResource(R.layout.checked_text_view);
        professionTypeSpinner.setAdapter(adapterProfessionType);
        professionTypeSpinner.setSelection(arrayProfessionType.length-1);


        adapterIndustryType = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, arrayIndustryType) {
            @Override
            public int getCount() {
                return arrayIndustryType.length - 1;
            }
        };
        adapterIndustryType.setDropDownViewResource(R.layout.checked_text_view);
        industryTypeSpinner.setAdapter(adapterIndustryType);
        industryTypeSpinner.setSelection(arrayIndustryType.length-1);


        adapterOwnerShip = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, officeOwnerShipList) {
            @Override
            public int getCount() {
                return officeOwnerShipList.length - 1;
            }
        };
        adapterOwnerShip.setDropDownViewResource(R.layout.checked_text_view);
        officeOwnerShip.setAdapter(adapterOwnerShip);
        officeOwnerShip.setSelection(officeOwnerShipList.length -1);

        auditDoneAdapter = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, auditDoneArray) {
            @Override
            public int getCount() {
                return auditDoneArray.length - 1;
            }
        };
        auditDoneAdapter.setDropDownViewResource(R.layout.checked_text_view);
        auditDone.setAdapter(auditDoneAdapter);
        auditDone.setSelection(auditDoneArray.length -1);

        loanDefaulterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, loanDefaulterArray) {
            @Override
            public int getCount() {
                return loanDefaulterArray.length - 1;
            }
        };
        loanDefaulterAdapter.setDropDownViewResource(R.layout.checked_text_view);
        defaultedOnLoanCard.setAdapter(loanDefaulterAdapter);
        defaultedOnLoanCard.setSelection(loanDefaulterArray.length -1);


        chequeBounceInLastSixMonthAdapter = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, chequeBounceInLastSixMonthArray) {
            @Override
            public int getCount() {
                return chequeBounceInLastSixMonthArray.length - 1;
            }
        };
        chequeBounceInLastSixMonthAdapter.setDropDownViewResource(R.layout.checked_text_view);
        chequeBounceInLastSixMonth.setAdapter(chequeBounceInLastSixMonthAdapter);
        chequeBounceInLastSixMonth.setSelection(chequeBounceInLastSixMonthArray.length -1);


        ratedByFinancialAgencyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.checked_text_view, ratedByFinancialAgencyArray) {
            @Override
            public int getCount() {
                return ratedByFinancialAgencyArray.length - 1;
            }
        };
        ratedByFinancialAgencyAdapter.setDropDownViewResource(R.layout.checked_text_view);
        ratedByRatingAgency.setAdapter(ratedByFinancialAgencyAdapter);
        ratedByRatingAgency.setSelection(ratedByFinancialAgencyArray.length -1);



        if (mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Professional")){
            officePhoneNumberEditText.setVisibility(View.GONE);
            cinNumber.setVisibility(View.GONE);
            grossTurnOver2EditText.setVisibility(View.GONE);
            grossTurnOver3EditText.setVisibility(View.GONE);
            chequeBounceInLastSixMonth.setVisibility(View.GONE);
            ratedByRatingAgency.setVisibility(View.GONE);
            dateOfIncorporationLayout.setVisibility(View.GONE);
            industryTypeSpinner.setVisibility(View.GONE);
        }
        else if (mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Business")){
            cinNumber.setVisibility(View.GONE);
            grossTurnOver2EditText.setVisibility(View.GONE);
            grossTurnOver3EditText.setVisibility(View.GONE);
            chequeBounceInLastSixMonth.setVisibility(View.GONE);
            ratedByRatingAgency.setVisibility(View.GONE);
            dateOfIncorporationLayout.setVisibility(View.GONE);
            professionTypeSpinner.setVisibility(View.GONE);
        }
        else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")){
            totalExperienceEditText.setVisibility(View.GONE);
            grossTurnOverBeforeLastYearEditText.setVisibility(View.GONE);
            officeOwnerShip.setVisibility(View.GONE);
            defaultedOnLoanCard.setVisibility(View.GONE);
            professionTypeSpinner.setVisibility(View.GONE);

        }

        dateOfIncorporationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
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

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                        mLoanInfoEntity.setGrossTurnOver(grossTurnOverLastYearEditText.getText().toString().trim());
                        mLoanInfoEntity.setPropertyType(((String) professionTypeSpinner.getSelectedItem()).trim());
                        mLoanInfoEntity.setNetWorth(netWorthEditText.getText().toString().trim());
                        mLoanInfoEntity.setTotalExperience(totalExperienceEditText.getText().toString().trim());
                        mLoanInfoEntity.setOfficeOwnerShip(((String) officeOwnerShip.getSelectedItem()).trim());
                        mLoanInfoEntity.setGrossTurnOverBeforeLastYear(grossTurnOverBeforeLastYearEditText.getText().toString().trim());
                        mLoanInfoEntity.setDefaultedOnLoan(defaultedOnLoanCard.getSelectedItem().toString().trim());
                        mLoanInfoEntity.setAuditDone(auditDone.getSelectedItem().toString().trim());
                        mLoanInfoEntity.setOfficePhoneNumber(officePhoneNumberEditText.getText().toString().trim());
                        mLoanInfoEntity.setCIN(cinNumber.getText().toString().trim());
                        mLoanInfoEntity.setDateOfIncorporation(dateOfIncorporation.getText().toString().trim());
                        mLoanInfoEntity.setGrossTurnOver2(grossTurnOver2EditText.getText().toString().trim());
                        mLoanInfoEntity.setGrossTurnOver3(grossTurnOver3EditText.getText().toString().trim());
                        mLoanInfoEntity.setCheckBounced(chequeBounceInLastSixMonth.getSelectedItem().toString().trim());
                        mLoanInfoEntity.setCompanyRatedByAgency(ratedByRatingAgency.getSelectedItem().toString().trim());
                        mLoanInfoEntity.setIndustryType(industryTypeSpinner.getSelectedItem().toString().trim());

                        changeFragment();

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

        journeyCompletedProgressBar.setProgress(76);
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
        datePickerDialogBuilder.context(getActivity());
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(mYear, mMonth, mDay);
        datePickerDialogBuilder.maxDate(mYear, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (String.valueOf(year).trim().contains(",")) {
                    year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
                }
                dateOfIncorporation.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        });

        datePickerDialogBuilder.build().show();
    }


    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragmentToReplace;

            if (mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Professional")
                    || mLoanInfoEntity.getOccupation().trim().equalsIgnoreCase("Self Employed Business")
                    || mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")){
                fragmentToReplace = SalaryProcessFragment.newInstance(mLoanInfoEntity);
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
