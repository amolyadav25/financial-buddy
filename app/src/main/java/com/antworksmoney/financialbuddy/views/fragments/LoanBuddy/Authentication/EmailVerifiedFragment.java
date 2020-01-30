package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class EmailVerifiedFragment extends Fragment {

    public EmailVerifiedFragment() {
        // Required empty public constructor
    }


    public static EmailVerifiedFragment newInstance() {
        return new EmailVerifiedFragment();
    }

    private Toolbar mToolbar;

    private Button loginNowButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_email_verified, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        loginNowButton = rootView.findViewById(R.id.loginNowButton);

        mToolbar.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        loginNowButton.setOnClickListener(view -> {
            if (getActivity() != null){
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LBLoginFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        return rootView;
    }


}
