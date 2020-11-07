package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;

import org.json.JSONObject;

public class RaiseIssueResultFragment extends Fragment {
    public RaiseIssueResultFragment() {
        // Required empty public constructor
    }

    private static String pageTitle;

    private static JSONObject responseObject;

    public static RaiseIssueResultFragment newInstance(JSONObject param1, String param2) {

        responseObject = param1;

        pageTitle = param2;

        return new RaiseIssueResultFragment();
    }

    private TextView headingText;

    private TextView complaintReason, complaintAssigned, complaintId, responseCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_raise_issue_result, container, false);

        headingText = rootView.findViewById(R.id.headingText);

        complaintAssigned = rootView.findViewById(R.id.complaintAssigned);

        complaintId = rootView.findViewById(R.id.complaintId);

        complaintReason = rootView.findViewById(R.id.responseReason);

        responseCode = rootView.findViewById(R.id.responseCode);

        try {

            complaintAssigned.setText(responseObject.getJSONObject("response")
                    .getString("complaintAssigned"));

            complaintReason.setText(responseObject.getJSONObject("response")
                    .getString("responseReason"));

            complaintId.setText(responseObject.getJSONObject("response")
                    .getString("complaintId"));

            responseCode.setText(responseObject.getJSONObject("response")
                    .getString("responseCode"));

        }catch (Exception e){
            e.printStackTrace();
        }

        headingText.setText(pageTitle);


        return rootView;
    }
}
