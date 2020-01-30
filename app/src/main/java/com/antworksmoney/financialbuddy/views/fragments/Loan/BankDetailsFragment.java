package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BankInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class BankDetailsFragment extends Fragment {

    private static BankInfoEntity mBankInfoEntity;

    private static LoanInfoEntity mLoanInfoEntity;

    public static BankDetailsFragment newInstance(BankInfoEntity bankInfoEntity, LoanInfoEntity loanInfoEntity) {
        mBankInfoEntity = bankInfoEntity;
        mLoanInfoEntity = loanInfoEntity;
        return new BankDetailsFragment();
    }

    private ImageView bankImage;

    private TextView bankName, bankDetails, bankOfferDetails;

    private Button ProceedButton;

    private Context mContext;

    private ImageView toolBarIcon;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bank_details, container, false);

        mContext = getContext();

        bankImage = rootView.findViewById(R.id.bankImage);

        bankName = rootView.findViewById(R.id.bankName);

        bankDetails = rootView.findViewById(R.id.bankDetails);

        bankOfferDetails = rootView.findViewById(R.id.bankOfferDetails);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        toolBarIcon = rootView.findViewById(R.id.toolBarIcon);

        toolBarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START);
                }
            }
        });

//        Glide.with(mContext).load(mBankInfoEntity.getImage()).error(R.drawable.bank).into(bankImage);

        bankName.setText("Bank Name : "+mBankInfoEntity.getName().trim());

        bankDetails.setText(Html.fromHtml(mBankInfoEntity.getLoan_description().trim()));

        bankOfferDetails.setText("Maximum Interest Rate : "+Html.fromHtml(mBankInfoEntity.getMax_int_rate().trim())+" %"+"\n"+
                                 "Minimum Interest Rate : "+Html.fromHtml(mBankInfoEntity.getMin_int_rate().trim())+" %"+"\n"+
                                 "Minimum Tenure Time : "+Html.fromHtml(mBankInfoEntity.getTenure_month_start().trim())+" months"+"\n"+
                                 "Maximum Tenure Time : "+Html.fromHtml(mBankInfoEntity.getTenure_month_end().trim())+" months"+"\n");

        ProceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LoanVerifyDetailsFragment.newInstance(mBankInfoEntity, mLoanInfoEntity));
                    transaction.addToBackStack(null).commit();
                }
            }
        });





        return rootView;
    }
}
