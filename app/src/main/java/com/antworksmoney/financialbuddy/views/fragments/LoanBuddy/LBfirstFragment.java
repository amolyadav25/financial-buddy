package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication.LBHomeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LBfirstFragment extends Fragment implements View.OnClickListener {


    private Button applyForOthers;

    public LBfirstFragment() {
        // Required empty public constructor
    }



    public static LBfirstFragment newInstance() {
        return new LBfirstFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=  inflater.inflate(R.layout.fragment_lbfirst, container, false);


        applyForOthers= rootview.findViewById(R.id.applyForOthers);



        applyForOthers.setOnClickListener(this);

        return rootview;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.applyForOthers:

                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBHomeFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }

                break;

        }
    }

}
