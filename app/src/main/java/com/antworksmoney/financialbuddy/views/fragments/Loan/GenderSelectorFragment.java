package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class GenderSelectorFragment extends Fragment {
    public GenderSelectorFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;


    public static GenderSelectorFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new GenderSelectorFragment();
    }


    private ImageView genderMale, genderFemale;

    private AppCompatActivity mActivity;

    private Context mContext;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private TextView journeyCompletedProgressText;

    private ProgressBar genderSelectorProgressBar, journeyCompletedProgressBar;

    private Toolbar top_toolBar;

    private static final String TAG = "GenderSelectorFragment";

    private TextView loanHeading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gender_selector, container, false);

        mActivity = (AppCompatActivity) getActivity();

        mContext = getContext();

        genderFemale = rootView.findViewById(R.id.genderFemale);

        genderMale = rootView.findViewById(R.id.genderMale);

        genderSelectorProgressBar = rootView.findViewById(R.id.genderSelectorProgressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {

            if (mLoanInfoEntity.getGender().trim().equalsIgnoreCase("Male"))
            {
                genderMale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                genderFemale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
            }
            else if (mLoanInfoEntity.getGender().trim().equalsIgnoreCase("Female"))
            {
                genderFemale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                genderMale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
            }
        }

        genderSelectorProgressBar.setVisibility(View.INVISIBLE);


        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoanInfoEntity.setGender("Male");
                genderMale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                genderFemale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
                genderSelectorProgressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeFragment();
                    }
                }, 1000);
            }
        });

        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoanInfoEntity.setGender("Female");
                genderFemale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                genderMale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
                genderSelectorProgressBar.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeFragment();
                    }
                }, 1000);
            }
        });

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoanInfoEntity.getGender().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select gender !!!");
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

        journeyCompletedProgressBar.setProgress(16);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }

    private void changeFragment() {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, DateOfBirthFragment.newInstance(mLoanInfoEntity));
            transaction.addToBackStack(null).commit();
        }
    }
}
