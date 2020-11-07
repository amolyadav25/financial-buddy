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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import java.util.ArrayList;

public class RaiseAnIssueFragment extends Fragment {
    public RaiseAnIssueFragment() {
        // Required empty public constructor
    }

    public static RaiseAnIssueFragment newInstance() {
        return new RaiseAnIssueFragment();
    }

    private RadioButton transactionIssue, serviceIssue;

    private TextView participationHeading, transactionReferenceHeading, agentIdReferenceHeading;

    private EditText agentIdReferenceText, transactionReferenceText;

    private Spinner participationSpinner, complaintTypeSpinner;

    private Button proceedButton;

    private static final String TAG = "RaiseAnIssueFragment";

    private RequestQueue mRequestQueue;

    private ProgressBar loader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_complaint, container, false);

        transactionIssue = rootView.findViewById(R.id.transactionIssue);

        serviceIssue = rootView.findViewById(R.id.serviceIssue);

        participationHeading = rootView.findViewById(R.id.participationHeading);

        transactionReferenceHeading = rootView.findViewById(R.id.transactionReferenceHeading);

        agentIdReferenceHeading = rootView.findViewById(R.id.agentIdReferenceHeading);

        agentIdReferenceText = rootView.findViewById(R.id.agentIdReferenceText);

        transactionReferenceText = rootView.findViewById(R.id.transactionReferanceText);

        participationSpinner = rootView.findViewById(R.id.participationTypeSpinner);

        complaintTypeSpinner = rootView.findViewById(R.id.complaintTypeSpinner);

        proceedButton = rootView.findViewById(R.id.nextButtonForBiller);

        mRequestQueue = Volley.newRequestQueue(getContext());

        loader = rootView.findViewById(R.id.loader);


        transactionIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    agentIdReferenceHeading.setVisibility(View.GONE);
                    agentIdReferenceText.setVisibility(View.GONE);

                    participationHeading.setVisibility(View.GONE);
                    participationSpinner.setVisibility(View.GONE);

                    transactionReferenceHeading.setVisibility(View.VISIBLE);
                    transactionReferenceText.setVisibility(View.VISIBLE);

                    ArrayList<String> billers = new ArrayList<>();
                    billers.add("Transaction successful, Account not credited");
                    billers.add("Select Transaction Issue");
                    ArrayAdapter<String> adapterOccupation = new ArrayAdapter<String>(getContext(), R.layout.checked_text_view, billers) {
                        @Override
                        public int getCount() {
                            return billers.size() - 1;
                        }
                    };
                    adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);
                    complaintTypeSpinner.setAdapter(adapterOccupation);
                    complaintTypeSpinner.setSelection(billers.size() - 1);
                }

            }
        });

        serviceIssue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    agentIdReferenceHeading.setVisibility(View.VISIBLE);
                    agentIdReferenceText.setVisibility(View.VISIBLE);

                    participationHeading.setVisibility(View.VISIBLE);
                    participationSpinner.setVisibility(View.VISIBLE);

                    transactionReferenceHeading.setVisibility(View.GONE);
                    transactionReferenceText.setVisibility(View.GONE);

                    ArrayList<String> billers = new ArrayList<>();
                    billers.add("Agent not willing to print receipt");
                    billers.add("Select Service Issue");
                    ArrayAdapter<String> adapterOccupation = new ArrayAdapter<String>(getContext(), R.layout.checked_text_view, billers) {
                        @Override
                        public int getCount() {
                            return billers.size() - 1;
                        }
                    };
                    adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);
                    complaintTypeSpinner.setAdapter(adapterOccupation);
                    complaintTypeSpinner.setSelection(billers.size() - 1);
                }
            }
        });


        ArrayList<String> billers = new ArrayList<>();
        billers.add("AGENT");
        billers.add("USER");
        billers.add("Select Participation Type");
        ArrayAdapter<String> adapterOccupation = new ArrayAdapter<String>(getContext(), R.layout.checked_text_view, billers) {
            @Override
            public int getCount() {
                return billers.size() - 1;
            }
        };
        adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);
        participationSpinner.setAdapter(adapterOccupation);
        participationSpinner.setSelection(billers.size() - 1);

        serviceIssue.setChecked(true);

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

        String Url;

        if (serviceIssue.isChecked()) Url = "complaint_registration_services";
        else Url = "complaint_registration_transaction";


        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + AppConstant.UatUrl + Url,
                null,
                response -> {

                    loader.setVisibility(View.GONE);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (transactionIssue.isChecked()){
                        transaction.replace(R.id.homeParent,RaiseIssueResultFragment.newInstance(response, "Raise Issue Result"));
                    }
                    else{
                        transaction.replace(R.id.homeParent,RaiseIssueResultFragment.newInstance(response, "Raise Issue Result"));
                    }

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
