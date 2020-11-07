package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
//import com.google.gson.JsonObject;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONObject;

public class PayOptionFragment extends Fragment {

    private static String mRequestId;

    public PayOptionFragment() {
        // Required empty public constructor
    }

    public static PayOptionFragment newInstance(String requestId) {
        mRequestId = requestId;
        return new PayOptionFragment();
    }

    private Button proceedToPayButton,
            internetBankingButton,
            debitCardButton,
            creditCardButton;

    private TextView bill_amount;

    private ProgressBar loader;

    private static final String TAG = "PayOptionFragment";

    private TextView responseTxt;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pay_option, container, false);

        proceedToPayButton = rootView.findViewById(R.id.proceedToPayButton);

        bill_amount = rootView.findViewById(R.id.bill_amount);

        internetBankingButton = rootView.findViewById(R.id.internetBankingButton);

        debitCardButton = rootView.findViewById(R.id.debitCardButton);

        creditCardButton = rootView.findViewById(R.id.creditCardButton);

        loader = rootView.findViewById(R.id.loader);

        responseTxt = rootView.findViewById(R.id.response);

        bill_amount.setText("Request Id : " + mRequestId + "\n" +
                "Bill Amount : Rs. 2008");

        proceedToPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResponseFromWallet();
            }
        });

        creditCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, CardInfoFragment.newInstance(
                            mRequestId,
                            creditCardButton.getText().toString().trim()));
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        debitCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, CardInfoFragment.newInstance(
                            mRequestId,
                            debitCardButton.getText().toString().trim()));
                    transaction.addToBackStack(null).commit();
                }
            }
        });

        internetBankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, CardInfoFragment.newInstance(
                            mRequestId,
                            internetBankingButton.getText().toString().trim()));
                    transaction.addToBackStack(null).commit();
                }
            }
        });


        return rootView;
    }


    private void loadResponseFromWallet() {
        loader.setVisibility(View.VISIBLE);

        try {
            JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + AppConstant.UatUrl + "w_bill_pay_request",
                    new JSONObject().put("user_bill_request_id", mRequestId),
                    response -> {
                        loader.setVisibility(View.GONE);
                        Log.e(TAG,response.toString());
                        try {
                            responseTxt.setText("Response : \n"+ response.toString());
                            JSONObject dataObject = response.getJSONObject("response");
                            StringBuilder data = new StringBuilder();

                            for (int i=0; i< dataObject.getJSONObject("inputParams").getJSONArray("input").length(); i++){
                                JSONObject dataJ = dataObject.getJSONObject("inputParams").getJSONArray("input").getJSONObject(i);
                                    data.append("Param Name : "+  dataJ.getString("paramName") + "\t"+ "Param Value : "+  dataJ.getString("paramValue") + "\n");
                            }
                            responseTxt.setText("Response code : "+ dataObject.getString("responseCode")+"\n"+
                                    "Response Reason : "+ dataObject.getString("responseReason")+ "\n"+
                                    "Transaction Id : " + dataObject.getString("txnRefId")+"\n"+
                                    "Approval Ref number : "+ dataObject.getString("approvalRefNumber")+"\n"+
                                    "Transaction Response Type : "+ dataObject.getString("txnRespType")+"\n"+
                                    data.toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    },
                    error -> {
                        Log.e(TAG,error.toString());
                        loader.setVisibility(View.GONE);
                    });
            dataObjectRequest.setShouldCache(false);

            dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(dataObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
