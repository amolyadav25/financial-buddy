package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MaritalStatusFragment extends Fragment {
    public MaritalStatusFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;


    public static MaritalStatusFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new MaritalStatusFragment();
    }


    private Spinner maritalStatusSelector;

    private ArrayAdapter<String> adapterMaritalStatus;

    private ArrayList<String> maritalStatusList;

    private AppCompatActivity mActivity;

    private ProgressBar progressBar, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static boolean moveToNextPage;

    private TextView loanHeading;

    private static final String TAG = "MaritalStatusFragment";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_marital_status, container, false);

        mActivity = (AppCompatActivity) getActivity();

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        progressBar = rootView.findViewById(R.id.progressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        moveToNextPage = false;

        maritalStatusList = new ArrayList<>();
        maritalStatusList.add("Single");
        maritalStatusList.add("Married");
        maritalStatusList.add("Divorced");
        maritalStatusList.add("Widow");
        maritalStatusList.add("Select Marital Status");

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

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
                    mLoanInfoEntity.setMaritalStatus(maritalStatusList.get(position));
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
                if (((String) maritalStatusSelector.getSelectedItem()).trim().equalsIgnoreCase("Select Marital Status")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select Marital Status !!!");
                } else {
                    changeFragment();
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

        journeyCompletedProgressBar.setProgress(30);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }


    private void changeFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, ContactDetailsFragment.newInstance(mLoanInfoEntity));
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        moveToNextPage = false;
    }
}
