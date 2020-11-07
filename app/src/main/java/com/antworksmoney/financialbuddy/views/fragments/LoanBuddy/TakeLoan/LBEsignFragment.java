package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

public class LBEsignFragment extends Fragment {

    public LBEsignFragment() {
        // Required empty public constructor
    }


    public static LBEsignFragment newInstance(JSONObject inputObject) {
        mRJsonObject = inputObject;
        return new LBEsignFragment();
    }


    private Toolbar mToolbar;

    private Button acceptLoanAgreementButton;

    private CoordinatorLayout snackBarView;

    private static JSONObject mRJsonObject;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbesign, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        acceptLoanAgreementButton = rootView.findViewById(R.id.acceptLoanAgreementButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        acceptLoanAgreementButton.setOnClickListener(view ->
                showSnackBar("E-sign Uploaded successfully !!", R.color.green));


        return rootView;
    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBNachFormFragment.newInstance(mRJsonObject));
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}
