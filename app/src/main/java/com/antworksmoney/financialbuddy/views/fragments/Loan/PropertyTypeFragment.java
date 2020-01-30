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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class PropertyTypeFragment extends Fragment {

    public PropertyTypeFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;

    public static PropertyTypeFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new PropertyTypeFragment();
    }

    private Spinner maritalStatusSelector;

    private ArrayAdapter<String> adapterMaritalStatus;

    private ArrayList<String> maritalStatusList;

    private Activity mActivity;

    private ProgressBar progressBar, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static boolean moveToNextPage;

    private TextView loanHeading;

    private static final String TAG = "PropertyTypeFragment";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_property_type, container, false);
        mActivity = getActivity();

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        progressBar = rootView.findViewById(R.id.progressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        moveToNextPage = false;

        maritalStatusList = new ArrayList<>();
        maritalStatusList.add("Residential");
        maritalStatusList.add("Commercial");
        maritalStatusList.add("Gram Panchayat");
        maritalStatusList.add("GPA");
        maritalStatusList.add("Agricultural");
        maritalStatusList.add("Select Property Type");

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        adapterMaritalStatus = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, maritalStatusList) {
            @Override
            public int getCount() {
                return maritalStatusList.size() - 1;
            }
        };
        adapterMaritalStatus.setDropDownViewResource(R.layout.checked_text_view);
        maritalStatusSelector.setAdapter(adapterMaritalStatus);

        progressBar.setVisibility(View.INVISIBLE);

        Log.e(TAG, mLoanInfoEntity.toString());

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

        maritalStatusSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (moveToNextPage) {
                    mLoanInfoEntity.setPropertyType(maritalStatusList.get(position));
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            moveToNextPage = false;
                            changeFragment();
                        }
                    }, 1000);
                } else moveToNextPage = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoanInfoEntity.getPropertyType().equals("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select Property Type !!!");
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

        journeyCompletedProgressBar.setProgress(13);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }


    private void changeFragment() {
        if (getActivity() != null) {
            Fragment fragmentToReplace;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")) {
                fragmentToReplace = ContactDetailsFragment.newInstance(mLoanInfoEntity);
            } else {
                fragmentToReplace = GenderSelectorFragment.newInstance(mLoanInfoEntity);
            }
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        moveToNextPage = false;
    }
}
