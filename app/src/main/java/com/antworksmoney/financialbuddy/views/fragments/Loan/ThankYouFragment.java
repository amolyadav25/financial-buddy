package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

public class ThankYouFragment extends Fragment {
    public ThankYouFragment() {
        // Required empty public constructor
    }

    public static ThankYouFragment newInstance(LoanInfoEntity mLoanInfoEntity) {
        return new ThankYouFragment();
    }

    private ProgressBar loader;

    private LinearLayout thankYouLayout;

    private Button returnToHomeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_thank_you, container, false);

        returnToHomeButton = rootView.findViewById(R.id.returnToHomeButton);

        loader = rootView.findViewById(R.id.loader);

        thankYouLayout = rootView.findViewById(R.id.thankYouLayout);

        new Handler().postDelayed(() -> {
            loader.setVisibility(View.GONE);
            thankYouLayout.setVisibility(View.VISIBLE);
        }, 3000);

        returnToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            getContext().startActivity(intent);
            getActivity().finish();
        });


        return rootView;
    }

}
