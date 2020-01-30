package com.antworksmoney.financialbuddy.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class RazorPayPaymentActivity extends Activity implements PaymentResultWithDataListener {

    private static final String TAG = "RazorPayPaymentActivity";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay_payment);

        mSharedPreferences = getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        Checkout.preload(RazorPayPaymentActivity.this);

        startPayment();
    }

    public void startPayment() {

        Checkout checkout = new Checkout();

        checkout.setImage(R.drawable.applogo);

        try {

            JSONObject options = new JSONObject();
            options.put("name", "Antworks Money");
            options.put("description", "Reference No. #" + getIntent().getStringExtra("orderId"));
            options.put("order_id", getIntent().getStringExtra("orderId"));
            options.put("currency", "INR");
            options.put("amount", getIntent().getStringExtra("amount"));
            options.put("prefill.email",mSharedPreferences.getString("email_value",""));
            options.put("prefill.contact", mSharedPreferences.getString("user_phone",""));

            checkout.setKeyID(getIntent().getStringExtra("API_Key"));
            checkout.open(RazorPayPaymentActivity.this, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in submitting payment details", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        Intent intent = new Intent();
        intent.putExtra("data",paymentData.getData().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Intent intent = new Intent();
        intent.putExtra("data",paymentData.getData().toString());
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
