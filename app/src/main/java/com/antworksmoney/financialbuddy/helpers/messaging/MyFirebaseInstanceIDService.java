package com.antworksmoney.financialbuddy.helpers.messaging;

import android.content.SharedPreferences;
import android.util.Log;

import com.antworksmoney.financialbuddy.helpers.AllApiInterface;
import com.antworksmoney.financialbuddy.updateTokenPOJO.updateTokenPOJO;
import com.antworksmoney.financialbuddy.updateTokenPOJO.uploadTokenRequest;
import com.antworksmoney.financialbuddy.views.fragments.Loan.PaySenseURL.ApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInstanceIDSer";
    private SharedPreferences pref;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PersonalDetails",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirebaseToken",refreshedToken);
        editor.apply();

        Log.e(TAG, "Refreshed token: " + refreshedToken);

        AllApiInterface apiService =
                ApiClient.getClient().create(AllApiInterface.class);
//        Log.e("Mytag","phone"+mLoanInfoEntity.getPhoneNumber());
//        Log.e("Mytag","email"+mLoanInfoEntity.getEmail()
//        );

        pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        if(pref.getString("user_phone",null)!=null) {
            uploadTokenRequest uploadTokenRequest = new uploadTokenRequest();
            uploadTokenRequest.setMobile(pref.getString("user_phone", null));

            Log.e("Mytag","NotificationTitle"+refreshedToken);
            uploadTokenRequest.setToken(refreshedToken);
            Call<updateTokenPOJO> call2 = apiService.uploadtoken(uploadTokenRequest);
            call2.enqueue(new Callback<updateTokenPOJO>() {
                @Override
                public void onResponse(Call<updateTokenPOJO> call, Response<updateTokenPOJO> response) {

                    if (response.code() == 400) {
                        Log.d(TAG, "onResponse - Status : " + response.code());
                        Gson gson = new Gson();
                        // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                        try {
                            if (response.errorBody() != null)
                                Log.e("Mytag", "response" + response.errorBody().string());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("Mytag", "ResponseFirebase" + response.body().getMsg());
//                    mLoanInfoEntity.setX_sessionid(response.body().getSessionToken());
                    }

                }

                @Override
                public void onFailure(Call<updateTokenPOJO> call2, Throwable t) {
                    // Log error here since request failed
                    Log.e("Mytag", t.toString());
                }
            });
        }
    }
}
