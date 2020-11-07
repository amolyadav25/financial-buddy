package com.antworksmoney.financialbuddy.helpers.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONObject;

public class UpdateProductStatus {

    private SharedPreferences preferences;

    private static final String TAG = "UpdateProductStatus";

    public UpdateProductStatus(Context context, String product_type, String prduct_id) {
        preferences = context.getSharedPreferences("PersonalDetails",Context.MODE_PRIVATE);
        try {

            Log.e(TAG, new JSONObject()
                    .put("mobile",preferences.getString("user_phone",""))
                    .put("product_type",product_type)
                    .put("product_id",prduct_id).toString());

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl+"save_response_tutorial",
                    new JSONObject()
                            .put("mobile",preferences.getString("user_phone",""))
                            .put("product_type",product_type)
                            .put("product_id",prduct_id),
                    response -> {
                        Log.e(TAG,response.toString());
                    },
                    error -> {
                        Log.e(TAG,error.toString());
                    });

            dataRequest.setShouldCache(false);

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue dataObjectRequest = Volley.newRequestQueue(context);
            dataObjectRequest.add(dataRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
