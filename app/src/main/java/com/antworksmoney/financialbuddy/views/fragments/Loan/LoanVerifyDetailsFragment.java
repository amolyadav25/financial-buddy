package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BankInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.fragments.Home.HomeFragment;
import org.json.JSONObject;
import java.text.MessageFormat;
import static android.content.Context.MODE_PRIVATE;

public class LoanVerifyDetailsFragment extends Fragment implements View.OnClickListener {

    private static BankInfoEntity mBankInfoEntity;

    private static LoanInfoEntity mLoanInfoEntity;

    public static LoanVerifyDetailsFragment newInstance(BankInfoEntity bankInfoEntity, LoanInfoEntity loanInfoEntity) {

        mBankInfoEntity = bankInfoEntity;

        mLoanInfoEntity = loanInfoEntity;

        return new LoanVerifyDetailsFragment();
    }

    private TextView loanApplicantName;

    private Button changeDataButton, applyButton;

    private AlertDialog dialogSocialsites;

    private View viewSocialSites;

    private AlertDialog.Builder builderSocialSites;

    private Context mContext;

    private Button returnToHome;

    private ImageView closeButton;

    private TextView sendingNotificationText;

    private TextView sendingNotificationHeading;

    private RequestQueue dataRequest;

    private static final String TAG = "LoanVerifyDetailsFragmn";

    private AppCompatActivity mActivity;

    private SharedPreferences pref;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_apply_response, container, false);

        loanApplicantName = rootView.findViewById(R.id.loanApplicantName);

        changeDataButton = rootView.findViewById(R.id.changeDataButton);

        mActivity = (AppCompatActivity) getActivity();

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        applyButton = rootView.findViewById(R.id.applyButton);

        mContext = getContext();

        loanApplicantName.setText(MessageFormat.format("-> Applicant Name : {0}\n" +
                        "-> Applicant Mail Id : {1}\n" +
                        "-> Applicant Gender : {2}\n" +
                        "-> Applicant Date of Birth : {3}\n" +
                        "-> Applicant Marital Status : {4}\n" +
                        "-> Applicant Nationality : Indian\n" +
                        "-> Applied Loan Amount : {5}\n" +
                        "-> Applicant Occupation : {6}\n" +
                        "-> Applicant Qualification : {7}\n" +
                        "-> City : {8}\n-> State : {9}\n" +
                        "-> Loan type : {10}\n-> Loan Name : {11}\n" +
                        "-> Applicant Company Type : {12}\n" +
                        "-> Applicant Company Name : {13}\n" +
                        "-> Loan Details : Loan of Rs. {14} from {15} at an interest rate of {16} % per year only.",
                mLoanInfoEntity.getName(),
                mLoanInfoEntity.getEmail(),
                mLoanInfoEntity.getGender(),
                mLoanInfoEntity.getDateOfBirth(),
                mLoanInfoEntity.getMaritalStatus(),
                mLoanInfoEntity.getLoanAmount(),
                mLoanInfoEntity.getOccupation(),
                mLoanInfoEntity.getQualification(),
                mLoanInfoEntity.getCity(),
                mLoanInfoEntity.getState(),
                mLoanInfoEntity.getLoanType(),
                mLoanInfoEntity.getLoanName(),
                mLoanInfoEntity.getCompanyType(),
                mLoanInfoEntity.getCompanyName(),
                mBankInfoEntity.getMin_loan_amount(),
                mBankInfoEntity.getName().trim(),
                mBankInfoEntity.getMin_int_rate()));

        dataRequest = Volley.newRequestQueue(mContext);

        viewSocialSites = LayoutInflater.from(mContext).inflate(R.layout.layout_sending_notification, null);

        builderSocialSites = new AlertDialog.Builder(mContext);

        builderSocialSites.setView(viewSocialSites);

        dialogSocialsites = builderSocialSites.create();
        if (dialogSocialsites.getWindow() != null) {
            dialogSocialsites.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogSocialsites.setCancelable(false);

        closeButton = viewSocialSites.findViewById(R.id.closeButton);

        returnToHome = viewSocialSites.findViewById(R.id.returnToHome);
        returnToHome.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));

        sendingNotificationText = viewSocialSites.findViewById(R.id.sendingNotificationText);

        sendingNotificationHeading = viewSocialSites.findViewById(R.id.sendingNotificationHeading);

        returnToHome.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(this);

        returnToHome.setOnClickListener(this);

        applyButton.setOnClickListener(this);

        changeDataButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.applyButton:

                sendingNotificationHeading.setText("Submitting request");

                sendingNotificationText.setText("Please wait !! We are sending request to the server.");

                dialogSocialsites.show();

                applyForLoanNow();

                break;

            case R.id.changeDataButton:
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LoanTypesFragment.newInstance(mLoanInfoEntity));
                    transaction.addToBackStack(null).commit();
                }

                break;

            case R.id.closeButton:
                dialogSocialsites.dismiss();
                break;

            case R.id.returnToHome:

                dialogSocialsites.dismiss();

                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, HomeFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
                break;


        }

    }

    private void applyForLoanNow() {

        JSONObject dataObject = new JSONObject();

        JSONObject innerObject = new JSONObject();

        try {

            String dateOfBirth = "";
            if (mLoanInfoEntity.getDateOfBirth().trim().contains("/")) {
                dateOfBirth = mLoanInfoEntity.getDateOfBirth().trim().replace("/", "-");
            } else dateOfBirth = mLoanInfoEntity.getDateOfBirth().trim();

            innerObject.put("fname", mLoanInfoEntity.getName());
            innerObject.put("mobile", mLoanInfoEntity.getPhoneNumber());
            innerObject.put("selfcontact", pref.getString("user_phone", ""));
            innerObject.put("state", mLoanInfoEntity.getState());
            innerObject.put("city", mLoanInfoEntity.getCity());
            innerObject.put("loan_name", mLoanInfoEntity.getLoanId());
            innerObject.put("loan_amount", mLoanInfoEntity.getLoanAmount());
            innerObject.put("occupation", mLoanInfoEntity.getOccupation());
            innerObject.put("company_type", mLoanInfoEntity.getCompanyType());
            innerObject.put("company_name", mLoanInfoEntity.getCompanyName());
            innerObject.put("salary_process", mLoanInfoEntity.getSalaryProcess());
            innerObject.put("bank_name", mBankInfoEntity.getName().trim());
            innerObject.put("property_type", mLoanInfoEntity.getPropertyType());
            innerObject.put("qualification", mLoanInfoEntity.getQualification());
            innerObject.put("maritalstatus", mLoanInfoEntity.getMaritalStatus());
            innerObject.put("gender", mLoanInfoEntity.getGender());
            innerObject.put("applyfor", mLoanInfoEntity.getLoanApplyFor());
            innerObject.put("dob", dateOfBirth);
            innerObject.put("income", mLoanInfoEntity.getSalary());
            innerObject.put("profession_type", mLoanInfoEntity.getProfessionType());
            innerObject.put("experiance", mLoanInfoEntity.getTotalExperience());
            innerObject.put("net_worth", mLoanInfoEntity.getNetWorth());
            innerObject.put("turnover1", mLoanInfoEntity.getGrossTurnOver());
            innerObject.put("turnover2", mLoanInfoEntity.getGrossTurnOver2());
            innerObject.put("audit_done", mLoanInfoEntity.getAuditDone());
            innerObject.put("office_ownership", mLoanInfoEntity.getOfficeOwnerShip());
            innerObject.put("cc_defaulter", mLoanInfoEntity.getDefaultedOnLoan());
            innerObject.put("persuing", "");
            innerObject.put("educational_institute_name", mLoanInfoEntity.getQualification());
            innerObject.put("lat", mLoanInfoEntity.getLatitude());
            innerObject.put("lng", mLoanInfoEntity.getLongitude());

            dataObject.put("userData", innerObject);

            Log.e(TAG, dataObject.toString());
            Log.e(TAG,AppConstant.BaseUrl + "apply_for");

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "apply_for",
                dataObject,
                response -> {
                    Log.e(TAG, response.toString());
                    returnToHome.setVisibility(View.VISIBLE);

                    try {
                        if (response.getString("message").trim().equalsIgnoreCase("Loan Apply successfully")) {
                            sendingNotificationHeading.setText("Request Submitted");

                            sendingNotificationText.setText("Your Application is shared with "+ mBankInfoEntity.getName() + " successfully. You shall get a call from bank very soon.\n\nIf you have any query/concern, please call us at 888 275 0000 or write to support@antworksmoney.com");
                        } else {

                            sendingNotificationHeading.setText("Failed to Request");

                            sendingNotificationHeading.setText("Please try after sometime.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    Log.e("Error", error.toString());

                    sendingNotificationHeading.setText("Failed to Request");

                    sendingNotificationHeading.setText("Please try after sometime.");

                    returnToHome.setVisibility(View.VISIBLE);
                });

        dataObjectRequest.setShouldCache(false);

        dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequest.add(dataObjectRequest);
    }
}
