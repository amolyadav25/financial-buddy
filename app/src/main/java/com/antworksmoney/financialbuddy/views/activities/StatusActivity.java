package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;


public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Intent mainIntent = getIntent();
        TextView tv4 = (TextView) findViewById(R.id.textView1);
        tv4.setText(mainIntent.getStringExtra("transStatus"));
    }



}
