package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

public class QuickBillPayFragment extends Fragment {
    public QuickBillPayFragment() {
        // Required empty public constructor
    }


    private Button nextButtonForBiller;

    private ProgressBar loader;

    private static final String TAG = "QuickBillPayFragment";

    private RequestQueue mRequestQueue;

    public static QuickBillPayFragment newInstance() {
        return new QuickBillPayFragment();
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_quick_bill_pay, container, false);

        nextButtonForBiller = rootView.findViewById(R.id.nextButtonForBiller);

        loader = rootView.findViewById(R.id.loader);

        mRequestQueue = Volley.newRequestQueue(getContext());


        nextButtonForBiller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billFetchRequest();
            }
        });


        return rootView;
    }

    private void billFetchRequest() {

        loader.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + AppConstant.UatUrl + "c_bill_quick_pay_request",
                null,
                response -> {

                    loader.setVisibility(View.GONE);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent,BillerPayDetails.newInstance(response, null));
                    transaction.addToBackStack(null).commit();
                },
                error -> {
                    loader.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                });

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);

    }

}
