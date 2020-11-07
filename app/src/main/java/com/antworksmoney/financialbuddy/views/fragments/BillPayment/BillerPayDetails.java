package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BillerEntity;

import org.json.JSONObject;

import java.util.Calendar;

public class BillerPayDetails extends Fragment {
    public BillerPayDetails() {
        // Required empty public constructor
    }

    private static JSONObject dataObject;

    private static BillerEntity mBillerEntity;

    public static BillerPayDetails newInstance(JSONObject response, BillerEntity billerEntity) {
        mBillerEntity = billerEntity;
        dataObject = response;
        return new BillerPayDetails();
    }

    private TextView billerId;
    private TextView billerName;
    private TextView customerName;
    private TextView customerNumber;
    private TextView billDate;
    private TextView billPeriod;
    private TextView billNumber;
    private TextView dueDate;
    private TextView billAmount;
    private TextView customerConvinienceFees;
    private TextView totalAmount;
    private TextView transactionDateAndTime;
    private TextView initiatingChannel;
    private TextView paymentMode;
    private TextView transactionStatus;

    private static final String TAG = "BillerPayDetails";


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_biller_pay_details, container, false);

        TextView transactionId = rootView.findViewById(R.id.transactionId);

        billerId = rootView.findViewById(R.id.billerId);

        billerName = rootView.findViewById(R.id.billerName);

        customerName = rootView.findViewById(R.id.customerName);

        customerNumber = rootView.findViewById(R.id.customerNumber);

        billDate = rootView.findViewById(R.id.billDate);

        billPeriod = rootView.findViewById(R.id.billPeriod);

        billNumber = rootView.findViewById(R.id.billNumber);

        dueDate = rootView.findViewById(R.id.dueDate);

        billAmount = rootView.findViewById(R.id.billAmount);

        customerConvinienceFees = rootView.findViewById(R.id.customerConvinienceFees);

        totalAmount = rootView.findViewById(R.id.totalAmount);

        transactionDateAndTime = rootView.findViewById(R.id.transactionDateAndTime);

        initiatingChannel = rootView.findViewById(R.id.initiatingChannel);

        paymentMode = rootView.findViewById(R.id.paymentMode);

        transactionStatus = rootView.findViewById(R.id.transactionStatus);

        try {
            transactionId.setText(dataObject.getJSONObject("response").getString("txnRefId"));

           if (mBillerEntity != null){
               billerId.setText(mBillerEntity.getBillerId());

               billerName.setText(mBillerEntity.getBillerName());
           }
           else {
               billerId.setText("0XCTVTXN00123X");

               billerName.setText("DISH TV");
           }

            billDate.setText(dataObject.getJSONObject("response").getString("RespBillDate"));

            customerNumber.setText(dataObject.getJSONObject("response").getString("RespBillNumber"));

            billPeriod.setText(dataObject.getJSONObject("response").getString("RespBillPeriod"));

            billNumber.setText(dataObject.getJSONObject("response").getString("RespBillNumber"));

            dueDate.setText(dataObject.getJSONObject("response").getString("RespDueDate"));

            billAmount.setText(dataObject.getJSONObject("response").getString("RespAmount"));

            customerConvinienceFees.setText(dataObject.getJSONObject("response").getString("CustConvFee"));

            transactionDateAndTime.setText(Calendar.getInstance().getTime().toString());

            initiatingChannel.setText(dataObject.getJSONObject("response").getString("txnRespType"));

            paymentMode.setText("WALLET");

            transactionStatus.setText(dataObject.getJSONObject("response").getString("responseReason"));

            totalAmount.setText(dataObject.getJSONObject("response").getString("RespAmount"));

            customerName.setText(dataObject.getJSONObject("response").getString("RespCustomerName"));

        }catch (Exception e){
            e.printStackTrace();
        }






        return rootView;
    }

}
