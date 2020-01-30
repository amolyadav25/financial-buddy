package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class ResponseActivity extends AppCompatActivity {

    private static final String TAG = "ResponseActivity";

    private TextView respnseData;

    private GifImageView responseImage;

    private ProgressBar checkStatusLoader;

    private Button checkStatusButton;

    private JSONObject dataObject;

    private LinearLayout statusLayout;

    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        respnseData = findViewById(R.id.respnseData);

        responseImage = findViewById(R.id.responseImage);

        checkStatusButton = findViewById(R.id.checkStatusButton);

        checkStatusLoader = findViewById(R.id.updateStatusLoader);

        statusLayout = findViewById(R.id.statusLayout);

        statusTextView = findViewById(R.id.statusTextView);

        Intent intent = getIntent();
        if (intent.hasExtra("Data")){
            Log.e(TAG, intent.getStringExtra("Data"));

            try {

                 dataObject = new JSONObject(intent.getStringExtra("Data"));

                respnseData.setText("Order Id - "+dataObject.getString("orderId")+"\n\n"+
                        "Status Code - "+ dataObject.getString("statusCode")+"\n\n"+
                        "Status Message - " + dataObject.getString("statusCode")+"\n\n"+
                        "Amount - Rs. " + dataObject.getString("amount")+"\n\n"+
                        "Time - " + dataObject.getString("dateTime")+"\n\n"+
                        "Account Number - " + dataObject.getString("accountNumber")+"\n\n"+
                        "Note : If amount is not received in your PayTm wallet, you can always check its status in the My Wallet section");

                if (dataObject.getString("statusCode").trim().equalsIgnoreCase("ACCEPTED")){

                    responseImage.setImageResource(R.drawable.check);
                }
                else {
                    responseImage.setImageResource(R.drawable.ic_close);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        checkStatus();

        checkStatusButton.setVisibility(View.GONE);
        checkStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStatus();
            }
        });
    }

    private void checkStatus(){

        checkStatusLoader.setVisibility(View.VISIBLE);

        try {

            Log.e(TAG, new JSONObject().put("txnId",dataObject.getString("orderId")).toString());

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl+"check_txn_status",
                    new JSONObject().put("txnId",dataObject.getString("orderId")),
                    response -> {
                        checkStatusLoader.setVisibility(View.GONE);
                        statusLayout.setVisibility(View.VISIBLE);

                        Log.e(TAG,response.toString());

                        try {

                            statusTextView.setText("Transaction GUID : "+response.getJSONArray("txnList").getJSONObject(0).getString("txnGuid")+"\n"+
                                    "Amount : Rs. "+ response.getJSONArray("txnList").getJSONObject(0).getString("txnAmount")+"\n"+
                                    "status : "+response.getJSONArray("txnList").getJSONObject(0).getString("status")+"\n"+
                                    "Message : "+ response.getJSONArray("txnList").getJSONObject(0).getString("message")+"\n"+
                                    "SSO ID : "+ response.getJSONArray("txnList").getJSONObject(0).getString("ssoId")+"\n"+
                                    "Transaction Type : "+ response.getJSONArray("txnList").getJSONObject(0).getString("txnType")+"\n"+
                                    "Order Id : " + response.getJSONArray("txnList").getJSONObject(0).getString("merchantOrderId")+"\n"+
                                    "Merchant Id : "+ response.getJSONArray("txnList").getJSONObject(0).getString("mId"));

//                            statusTextView.setText("Type : "+response.getString("type")+"\n\n"+
//                                    "Request Id : "+ response.getString("requestGuid")+"\n\n"+
//                                    "Order Id : "+ response.getString("orderId")+ "\n\n"+
//                                    "Status : " + response.getString("status") + "\n\n" +
//                                    "Status Code : " + response.getString("statusCode")+ "\n\n"+
//                                    "Status Message : " + response.getString("statusMessage")+ "\n\n"+
//                                    "Response : " + response.getString("response") +"\n\n" +
//                                    "Meta data : " + response.getString("metadata"));

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    },
                    error -> {
                        checkStatusLoader.setVisibility(View.GONE);
                    });

            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mRequestQueue.add(dataRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ResponseActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
