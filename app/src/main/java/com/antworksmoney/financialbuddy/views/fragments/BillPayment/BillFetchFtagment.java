package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BillerEntity;
import com.antworksmoney.financialbuddy.helpers.Utility.AvenuesParams;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.WebViewActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Random;


public class BillFetchFtagment extends Fragment {

    public BillFetchFtagment() {
        // Required empty public constructor
    }

    private static JSONObject dataObject;

    private static BillerEntity mBillerEntity;

    public static BillFetchFtagment newInstance(JSONObject response, BillerEntity entity) {
        dataObject = response;
        mBillerEntity = entity;
        return new BillFetchFtagment();
    }

    private static final String TAG = "BillFetchFtagment";

    private TextView billerName,
            customerName,
            billDate,
            billPeriod,
            billNumber,
            dueDate,
            customerconvienceFess,
            totalAmount;

    private EditText mobileNumber;

    private Button proceedButton;

    private ProgressBar payProgressBar;

    private String requestCode = "";


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bill_fetch_ftagment, container, false);

        billerName = rootView.findViewById(R.id.billerName);

        customerName = rootView.findViewById(R.id.customerName);

        billDate = rootView.findViewById(R.id.billDate);

        billPeriod = rootView.findViewById(R.id.billPeriod);

        billNumber = rootView.findViewById(R.id.billNumber);

        dueDate = rootView.findViewById(R.id.dueDate);

        customerconvienceFess = rootView.findViewById(R.id.customerconvienceFess);

        mobileNumber = rootView.findViewById(R.id.mobileNumber);

        totalAmount = rootView.findViewById(R.id.totalAmount);

        payProgressBar = rootView.findViewById(R.id.payProgressBar);

        proceedButton = rootView.findViewById(R.id.proceedButton);


        try {

            billerName.setText(mBillerEntity.getBillerName());

            customerName.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("customerName"));

            requestCode = dataObject.getString("order_id");

            billDate.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("billDate"));

            billPeriod.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("billPeriod"));

            billNumber.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("billNumber"));

            dueDate.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("dueDate"));

            totalAmount.setText(dataObject.getJSONObject("bill_result")
                    .getJSONObject("billerResponse")
                    .getString("billAmount"));
        }catch (Exception e){
            e.printStackTrace();
        }

        proceedButton.setOnClickListener(v -> startCCAvenuePaymentFlow());

        return rootView;
    }

    public void startCCAvenuePaymentFlow(){
        if(!(totalAmount.getText().toString().trim()).equals("")){

            if (!totalAmount.getText().toString().trim().equalsIgnoreCase("0.1")){
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(AvenuesParams.ACCESS_CODE, "AVZL69EB16AS60LZSA");
                intent.putExtra(AvenuesParams.MERCHANT_ID, "123884");
                intent.putExtra(AvenuesParams.ORDER_ID, requestCode);
                intent.putExtra(AvenuesParams.CURRENCY, "INR");
                intent.putExtra(AvenuesParams.AMOUNT, totalAmount.getText().toString().trim());  //totalAmount.getText().toString().trim()
                intent.putExtra(AvenuesParams.REDIRECT_URL, "https://www.antworksmoney.com/financial_buddy/Bbps_api/ccavResponseHandler");
                intent.putExtra(AvenuesParams.CANCEL_URL, "https://www.antworksmoney.com/financial_buddy/Bbps_api/ccavResponseHandler");
                intent.putExtra(AvenuesParams.RSA_KEY_URL, "https://www.antworksmoney.com/financial_buddy/Bbps_api/payCCavenue");
                startActivity(intent);
            }
            else {
                Toast.makeText(getContext(), "No valid bills found !!", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getContext(), "All Parameters are needed !!", Toast.LENGTH_LONG).show();
        }

    }

//    private void loadResponseFromWallet() {
//        payProgressBar.setVisibility(View.VISIBLE);
//
//        try {
//            JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
//                    Request.Method.POST,
//                    AppConstant.BaseUrl + AppConstant.UatUrl + "w_bill_pay_request",
//                    new JSONObject().put("user_bill_request_id", dataObject.getString("requestId")),
//                    response -> {
//                        payProgressBar.setVisibility(View.GONE);
//
//                       if (getActivity() != null){
//                           FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                           transaction.replace(R.id.homeParent, BillerPayDetails.newInstance(response, mBillerEntity));
//                           transaction.addToBackStack(null).commit();
//                       }
//                    },
//                    error -> {
//                        Log.e(TAG,error.toString());
//                        payProgressBar.setVisibility(View.GONE);
//                    });
//            dataObjectRequest.setShouldCache(false);
//
//            dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    AppConstant.MY_SOCKET_TIMEOUT_MS,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//            RequestQueue queue = Volley.newRequestQueue(getContext());
//            queue.add(dataObjectRequest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
