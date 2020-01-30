package com.antworksmoney.financialbuddy.helpers.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Objects;

public class SmsReciever extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String abcd;
    private static final String TAG = "SmsReciever";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) Objects.requireNonNull(data).get("pdus");
        for (Object pdu : Objects.requireNonNull(pdus)) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            b = sender.endsWith("ANTWOX");
            String messageBody = smsMessage.getMessageBody();
            abcd = messageBody.replaceAll("[^0-9]", "");
            if (b && (mListener != null)) {
                mListener.messageReceived(abcd);
            } else {
                Log.e(TAG, "Some other Message");
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}