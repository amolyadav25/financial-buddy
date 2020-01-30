package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.antworksmoney.financialbuddy.R;

public class ComplaintHomeFragment extends Fragment {
    public ComplaintHomeFragment() {
        // Required empty public constructor
    }

    public static ComplaintHomeFragment newInstance() {
        return new ComplaintHomeFragment();
    }

    private RelativeLayout raiseAnIssue, complaintTracking, serviceTracking, trackAnIssue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complaint_home, container, false);

        raiseAnIssue = rootView.findViewById(R.id.raiseAnIssue);

        complaintTracking = rootView.findViewById(R.id.complaintTracking);

        serviceTracking = rootView.findViewById(R.id.serviceTracking);

        trackAnIssue = rootView.findViewById(R.id.trackAnIssue);

        raiseAnIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent,RaiseAnIssueFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }

            }
        });

        complaintTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent,ComplaintEnquiryFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        trackAnIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent,TransactionStatusEnquiryFragment.newInstance());
                transaction.addToBackStack(null).commit();
            }
        });

        serviceTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }

}
