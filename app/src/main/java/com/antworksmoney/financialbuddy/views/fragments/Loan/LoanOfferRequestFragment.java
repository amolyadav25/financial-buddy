package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BankInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.BankOfferDataListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoanOfferRequestFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public static LoanOfferRequestFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new LoanOfferRequestFragment();
    }

    private static final String TAG = "LoanOfferRequest";

    private ImageView toolBarIcon;

    private Activity mActivity;

    private Context mContext;

    private ProgressBar dataLoaderProgressBar;

    private RecyclerView dataList;

    private JSONObject outerObject, innerObject;

    private RequestQueue dataObjectRequest;

    private TextView offerHeading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_offer_request, container, false);

        toolBarIcon = rootView.findViewById(R.id.toolBarIcon);

        mActivity = getActivity();

        mContext = getContext();

        dataLoaderProgressBar = rootView.findViewById(R.id.dataLoader);

        offerHeading = rootView.findViewById(R.id.offerHeading);

        dataList = rootView.findViewById(R.id.dataList);
        dataList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataList.setHasFixedSize(true);

        outerObject = new JSONObject();

        innerObject = new JSONObject();

        toolBarIcon.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        dataObjectRequest = Volley.newRequestQueue(mContext);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

            String date = "";

            try {
                date = mLoanInfoEntity.getDateOfBirth().split("/")[2]+"-"+
                        mLoanInfoEntity.getDateOfBirth().split("/")[1]+"-"+
                        mLoanInfoEntity.getDateOfBirth().split("/")[0];
            } catch (Exception e){
                e.printStackTrace();
            }


            innerObject.put("loan_type", mLoanInfoEntity.getLoanType());
            innerObject.put("loan_name", mLoanInfoEntity.getLoanId());
            innerObject.put("state", mLoanInfoEntity.getState());
            innerObject.put("city", mLoanInfoEntity.getCity());
            innerObject.put("gender", mLoanInfoEntity.getGender());
            innerObject.put("dob", date);
            innerObject.put("maritalstatus", mLoanInfoEntity.getMaritalStatus());
            innerObject.put("nationality", mLoanInfoEntity.getNationality());
            innerObject.put("qualification", mLoanInfoEntity.getQualification());
            innerObject.put("occupation", mLoanInfoEntity.getOccupation());
            innerObject.put("company_type", mLoanInfoEntity.getCompanyType());
            innerObject.put("company_name", mLoanInfoEntity.getCompanyName());
            innerObject.put("mode_of_salary",mLoanInfoEntity.getSalaryProcess());
            innerObject.put("bankName",mLoanInfoEntity.getBankName());
            innerObject.put("emi",mLoanInfoEntity.getEMI());
            innerObject.put("income",mLoanInfoEntity.getSalary());
            innerObject.put("professionType",mLoanInfoEntity.getProfessionType());
            innerObject.put("netWorth",mLoanInfoEntity.getNetWorth());
            innerObject.put("totalExperience",mLoanInfoEntity.getTotalExperience());
            innerObject.put("turnover1",mLoanInfoEntity.getGrossTurnOver());
            innerObject.put("turnover2",mLoanInfoEntity.getGrossTurnOver2());
            innerObject.put("turnover3",mLoanInfoEntity.getGrossTurnOver3());
            innerObject.put("ownerShip",mLoanInfoEntity.getOfficeOwnerShip());
            innerObject.put("auditDone",mLoanInfoEntity.getAuditDone());
            innerObject.put("defaultedOnLoanCard",mLoanInfoEntity.getDefaultedOnLoan());
            innerObject.put("industryType",mLoanInfoEntity.getIndustryType());
            innerObject.put("officePhoneNumber",mLoanInfoEntity.getOfficePhoneNumber());
            innerObject.put("amount", mLoanInfoEntity.getLoanAmount());
            innerObject.put("property_type",mLoanInfoEntity.getPropertyType());
            innerObject.put("dateOfIncorporation",mLoanInfoEntity.getDateOfIncorporation());
            innerObject.put("CIN",mLoanInfoEntity.getCIN());
            innerObject.put("chequeBouncedInLastSixMonth",mLoanInfoEntity.getCheckBounced());
            innerObject.put("ratedByFinancialAgency",mLoanInfoEntity.getCompanyRatedByAgency());
            innerObject.put("cibil_score","");
            innerObject.put("property_value","");
            innerObject.put("location_of_property_pincode","");
            innerObject.put("builder_name","");
            outerObject.put("userData", innerObject);

            Log.e(TAG,outerObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        getOfferFromAPI();
    }

    private void getOfferFromAPI() {

//        Log.e(TAG,AppConstant.BaseUrl + "loanoffer");

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "loanoffer",
                outerObject,
                response -> {
                    Log.e(TAG, response.toString());
                    dataLoaderProgressBar.setVisibility(View.GONE);

                    ArrayList<BankInfoEntity> entityArrayList = new ArrayList<>();

                    try {
                        JSONArray dataArray = response.getJSONArray("UserData");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = (JSONObject) response.getJSONArray("UserData").get(i);
                            BankInfoEntity entity = new BankInfoEntity();
                                    entity.setLoanid(dataObject.getString("loanid"));
                                    entity.setBank_id(dataObject.getString("bank_id"));
                                    entity.setMin_age(dataObject.getString("min_age"));
                                    entity.setMax_age(dataObject.getString("max_age"));
                                    entity.setGender(dataObject.getString("gender"));
                                    entity.setEmployement_status(dataObject.getString("employement_status"));
                                    entity.setMin_salary(dataObject.getString("min_salary"));
                                    entity.setMin_average_income(dataObject.getString("min-average_income"));
                                    entity.setMin_loan_amount(dataObject.getString("loan_amount_calculate"));
                                    entity.setMax_loan_amount(dataObject.getString("max_loan_amount"));
                                    entity.setProcessing_fee_type(dataObject.getString("processing_fee_type"));
                                    entity.setFixed_processing_fee(dataObject.getString("fixed_processing_fee"));
                                    entity.setMin_processing_fee(dataObject.getString("full_emi_calulate"));
                                    entity.setMax_processing_fee(dataObject.getString("max_processing_fee"));
                                    entity.setInr_rate_fixed(dataObject.getString("inr_rate_fixed"));
                                    entity.setInt_fixed_amount(dataObject.getString("int_fixed_amount"));
                                    entity.setMin_int_rate(dataObject.getString("min_int_rate"));
                                    entity.setMax_int_rate(dataObject.getString("max_int_rate"));
                                    entity.setTenure_month_start(dataObject.getString("tenor_calculate"));
                                    entity.setTenure_month_end(dataObject.getString("tenure_month_end"));
                                    entity.setMinimum_cibil(dataObject.getString("minimum_cibil"));
                                    entity.setMin_residence_yrs(dataObject.getString("min_residence_yrs"));
                                    entity.setMin_employement_yrs(dataObject.getString("min_employement_yrs"));
                                    entity.setNationality(dataObject.getString("nationality"));
                                    entity.setCompany_id(dataObject.getString("company_id"));
                                    entity.setLoan_type_id(dataObject.getString("loan_type_id"));
                                    entity.setCredit_card_type(dataObject.getString("credit_card_type"));
                                    entity.setCredit_card_image(dataObject.getString("credit_card_image"));
                                    entity.setLoan_description(dataObject.getString("loan_description"));
                                    entity.setF1(dataObject.getString("f1"));
                                    entity.setF2(dataObject.getString("f2"));
                                    entity.setF3(dataObject.getString("f3"));
                                    entity.setF4(dataObject.getString("f4"));
                                    entity.setF5(dataObject.getString("f5"));
                                    entity.setStatus(dataObject.getString("status"));
                                    entity.setDate_added(dataObject.getString("date_added"));
                                    entity.setDate_modified(dataObject.getString("date_modified"));
                                    entity.setFor_crm("");
                                    entity.setName(dataObject.getString("name"));
                                    entity.setImage(dataObject.getString("image"));
                                    entity.setAbout(dataObject.getString("about"));
                                    entity.setAdded_on(dataObject.getString("added_on"));

                                    entityArrayList.add(entity);
                        }

                        BankOfferDataListAdapter adapter = new BankOfferDataListAdapter(mContext,entityArrayList,dataList);
                        dataList.setAdapter(adapter);

                        adapter.setOnClick(new BankOfferDataListAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                                FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent,LoanVerifyDetailsFragment.newInstance(entityArrayList.get(position),mLoanInfoEntity));
                                transaction.addToBackStack(null).commit();

                            }
                        });


                    } catch (Exception e) {
                        try {
                            offerHeading.setText(response.getJSONObject("data").getString("message"));
                            offerHeading.setTextSize(16);
                            offerHeading.setGravity(Gravity.CENTER);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                        e.printStackTrace();

                    }
                },
                error -> {
                    dataLoaderProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                });

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataObjectRequest.add(dataRequest);
    }
}
