package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
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
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.util.Calendar;

public class TransactionStatusEnquiryFragment extends Fragment {
    public TransactionStatusEnquiryFragment() {
        // Required empty public constructor
    }

    public static TransactionStatusEnquiryFragment newInstance() {
        return new TransactionStatusEnquiryFragment();
    }

    private TextView fromDateTextView, toDateTextView;

    private Button proceedButton;

    private ProgressBar loader;

    private static final String TAG = "TransactionStatusEnquir";

    private RequestQueue mRequestQueue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_transaction_status_enquiry, container, false);

        fromDateTextView = rootView.findViewById(R.id.fromDateTextView);

        toDateTextView = rootView.findViewById(R.id.toDateTextView);

        proceedButton = rootView.findViewById(R.id.nextButtonForBiller);

        loader = rootView.findViewById(R.id.loader);

        mRequestQueue = Volley.newRequestQueue(getActivity());


        fromDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(fromDateTextView);
            }
        });

        toDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog(toDateTextView);
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchResponse();
            }
        });




        return rootView;
    }


    public void DateDialog(TextView textView) {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mYear, mMonth, mDay;

        mYear = dateCalender.get(Calendar.YEAR);
        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(getActivity());
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(mYear, mMonth, mDay);
        datePickerDialogBuilder.callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (String.valueOf(year).trim().contains(",")) {
                    year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
                }
                textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        });

        datePickerDialogBuilder.build().show();
    }


    private void fetchResponse(){
        loader.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + AppConstant.UatUrl + "transaction_status",
                null,
                response -> {

                    loader.setVisibility(View.GONE);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent,TransactionStatusEnquiryResultFragment.newInstance(response));
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
