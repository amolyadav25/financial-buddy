package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONObject;

import java.text.MessageFormat;


public class CardInfoFragment extends Fragment {

    private static String requestId;

    private static String mPaymentMode;

    public CardInfoFragment() {
        // Required empty public constructor
    }


    public static CardInfoFragment newInstance(String mRequestId, String paymentMode) {
        mPaymentMode = paymentMode;
        requestId = mRequestId;
        return  new CardInfoFragment();
    }

    private Button payNowButton;

    private TextView paymentSummaryInfo;

    private TextView responseText;

    private ProgressBar loader;

    private static final String TAG = "CardInfoFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_bank_info, container, false);

        payNowButton = rootView.findViewById(R.id.payNowButton);

        paymentSummaryInfo = rootView.findViewById(R.id.paymentSummaryInfo);

        responseText = rootView.findViewById(R.id.response);

        loader = rootView.findViewById(R.id.loader);

        Log.e(TAG,mPaymentMode);

        paymentSummaryInfo.setText(MessageFormat.format("Request Id : {0}\nAmount : 1948\n2008 - 60(Wallet Baliance) = 1948", requestId));

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "";
              if (mPaymentMode.trim().contains("Credit Card")){
                  url = "c_bill_pay_request";
              }
              else if (mPaymentMode.trim().contains("Debit Card")){
                  url = "d_bill_pay_request";
              }
              else if (mPaymentMode.trim().contains("Internet Banking")){
                  url = "ib_bill_quick_pay_request";
              }

              fetchResponseFromServer(url);
            }
        });


        return rootView;
    }

    private void fetchResponseFromServer(String url) {

        loader.setVisibility(View.VISIBLE);

        try {

            Log.e(TAG,AppConstant.BaseUrl + AppConstant.UatUrl + url);
            Log.e(TAG,new JSONObject().put("user_bill_request_id", requestId).toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + AppConstant.UatUrl + url,
                    new JSONObject().put("user_bill_request_id", requestId),
                    response -> {
                            loader.setVisibility(View.GONE);
                            Log.e(TAG,response.toString());
                            try {

                                responseText.setText("Response : \n"+ response.toString());

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                    },
                    error -> {
                        loader.setVisibility(View.GONE);
                        Log.e(TAG,error.toString());
                    });

            jsonObjectRequest.setShouldCache(false);

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(jsonObjectRequest);


        }catch (Exception e){
           e.printStackTrace();
        }



    }

}
