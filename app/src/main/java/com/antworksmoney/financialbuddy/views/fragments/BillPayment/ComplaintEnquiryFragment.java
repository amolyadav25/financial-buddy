package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import java.util.ArrayList;

public class ComplaintEnquiryFragment extends Fragment {

    public ComplaintEnquiryFragment() {
        // Required empty public constructor
    }

    public static ComplaintEnquiryFragment newInstance() {
        return new ComplaintEnquiryFragment();
    }

    private Spinner complaintTypeSpinner;

    private Button proceedButton;

    private RequestQueue mRequestQueue;

    private ProgressBar loader;

    private static final String TAG = "ComplaintEnquiryFragmen";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complaint_enquiry, container, false);

        complaintTypeSpinner = rootView.findViewById(R.id.complaintTypeSpinner);

        proceedButton = rootView.findViewById(R.id.nextButtonForBiller);

        mRequestQueue = Volley.newRequestQueue(getContext());

        loader = rootView.findViewById(R.id.loader);

        ArrayList<String> billers = new ArrayList<>();
        billers.add("Transaction");
        billers.add("Service");
        billers.add("Select Transaction Type");
        ArrayAdapter<String> adapterOccupation = new ArrayAdapter<String>(getContext(), R.layout.checked_text_view, billers) {
            @Override
            public int getCount() {
                return billers.size() - 1;
            }
        };
        adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);
        complaintTypeSpinner.setAdapter(adapterOccupation);
        complaintTypeSpinner.setSelection(billers.size() - 1);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchResponse();
            }
        });


        return rootView;
    }

    private void fetchResponse(){
        loader.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + AppConstant.UatUrl + "complaint_tracking",
                null,
                response -> {

                    loader.setVisibility(View.GONE);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent,ComplaintEnquiryResultFragment.newInstance(
                            response, ((String)complaintTypeSpinner.getSelectedItem()).trim()));
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
