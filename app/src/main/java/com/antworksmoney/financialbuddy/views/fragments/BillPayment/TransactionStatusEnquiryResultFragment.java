package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;

import org.json.JSONObject;


public class TransactionStatusEnquiryResultFragment extends Fragment {
    public TransactionStatusEnquiryResultFragment() {
        // Required empty public constructor
    }

    private static JSONObject dataObject;

    public static TransactionStatusEnquiryResultFragment newInstance(JSONObject param1) {
        dataObject = param1;
        return new TransactionStatusEnquiryResultFragment();
    }


    private TextView agentId, billerName, transactionReferenceId, transactionStatus, amount, transactionDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_transaction_status_enquiry_result, container, false);

        agentId = rootView.findViewById(R.id.agentId);

        billerName = rootView.findViewById(R.id.billerName);

        transactionReferenceId = rootView.findViewById(R.id.transactionReferanceId);

        transactionStatus = rootView.findViewById(R.id.transactionStatus);

        amount = rootView.findViewById(R.id.amount);

        transactionDate = rootView.findViewById(R.id.transactionDate);

        try {

            agentId.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("agentId"));

            billerName.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("billerId"));

            amount.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("amount"));

            transactionReferenceId.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("txnReferenceId"));

            transactionStatus.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("txnStatus"));

            transactionDate.setText(dataObject.getJSONObject("response")
                    .getJSONObject("txnList")
                    .getString("txnDate"));

        }catch (Exception e){
            e.printStackTrace();
        }


        return rootView;
    }

}
