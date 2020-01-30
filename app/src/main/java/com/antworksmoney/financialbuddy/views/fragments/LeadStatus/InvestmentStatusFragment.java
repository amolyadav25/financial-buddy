package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LeadInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.LeadStatusAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class InvestmentStatusFragment extends Fragment {

    public InvestmentStatusFragment() {
        // Required empty public constructor
    }

    public static InvestmentStatusFragment newInstance(String param1, String param2) {
        return new InvestmentStatusFragment();
    }

    private RecyclerView myLeadsList;

    private static final String TAG = "LeadStatusFragment";

    private SharedPreferences pref;

    private ProgressBar dataLoaderProgressbar;

    private EditText filterQuerryEditText;

    private TextView changeDateText;

    private RelativeLayout changeDateLayout;

    private CoordinatorLayout snackBarView;

    private Spinner spinner;

    private ArrayAdapter<String> adapterProducts;

    private ArrayList<String> productsList;

    private Activity mActivity;

    private Context mContext;

    private String productName = "";


    public static String startDate = "", endDate = "";





    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_investment_status, container, false);

        mActivity = getActivity();

        mContext = getContext();

        myLeadsList = rootView.findViewById(R.id.myLeadsList);

        dataLoaderProgressbar = rootView.findViewById(R.id.dataLoaderProgressbar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        filterQuerryEditText = rootView.findViewById(R.id.filterQuerryEditText);

        changeDateLayout = rootView.findViewById(R.id.changeDateLayout);

        changeDateText = rootView.findViewById(R.id.changeDateText);

        spinner = rootView.findViewById(R.id.spinner);

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);


        return rootView;
    }

        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myLeadsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myLeadsList.setHasFixedSize(true);

        productsList = new ArrayList<>();
        productsList.add("Housing Loan");
        productsList.add("Loan Against Property");
        productsList.add("Personal Loan");
        productsList.add("Business Loan");
        productsList.add("Home Loan");
        productsList.add("Loan Against Property");
        productsList.add("Equipment Finance Loan");
        productsList.add("Products");

        adapterProducts = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view_small, productsList) {
                @Override
                public int getCount() {
                    return productsList.size() - 1;
                }
        };
        adapterProducts.setDropDownViewResource(R.layout.checked_text_view);
        spinner.setAdapter(adapterProducts);
        spinner.setSelection(productsList.size() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                productName = productsList.get(position);
                loadLeadStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        changeDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    // Create and show the dialog.
                    TabbedDialog dialogFragment = new TabbedDialog();
                    dialogFragment.setDissmissListener(myDialogFragment -> {
                        productName = "Products";
                        changeDateText.setText(MessageFormat.format("{0} - {1}", startDate, endDate));
                        loadLeadStatus();
                    });
                    dialogFragment.show(ft, "dialog");

                }


            }
        });

    }

    private void loadLeadStatus() {

        dataLoaderProgressbar.setVisibility(View.VISIBLE);
        myLeadsList.setVisibility(View.INVISIBLE);

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("userData", new JSONObject().put("contact", pref.getString("user_phone", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, dataObject.toString());

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "fc_lead_status",
                dataObject,
                response -> {
                    dataLoaderProgressbar.setVisibility(View.GONE);
                    myLeadsList.setVisibility(View.VISIBLE);


                    Log.e(TAG, response.toString());

                    try {
                        JSONArray dataArray = response.getJSONObject("respone").getJSONArray("UserData");

                        ArrayList<LeadInfoEntity> leadInfoEntityArrayList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            LeadInfoEntity leadInfoEntity = new LeadInfoEntity();
                            JSONObject jsonObject = ((JSONObject) dataArray.get(i));

                            if (productName.trim().equalsIgnoreCase("Products")){

                                if (!startDate.trim().equalsIgnoreCase("")){
                                    Date rangeStartDate = new Date(new SimpleDateFormat("yyyy-MM-dd", Locale.UK)
                                            .parse("20"+startDate.split("/")[2] +"-"+ startDate.split("/")[1] +"-"+ startDate.split("/")[0])
                                            .getTime());

                                    Date rangeEndDate = new Date(new SimpleDateFormat("yyyy-MM-dd", Locale.UK)
                                            .parse("20"+endDate.split("/")[2] +"-"+ endDate.split("/")[1] +"-"+ endDate.split("/")[0])
                                            .getTime());

                                    Date createdDate = new Date(new SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(jsonObject.getString("created_date"))
                                            .getTime());

                                    if (rangeStartDate.compareTo(createdDate) * createdDate.compareTo(rangeEndDate) >= 0){
                                        leadInfoEntity.setCreated_date(jsonObject.getString("created_date") + "\nEstimated Earning : "+ jsonObject.getString("estimated_earning"));
                                        leadInfoEntity.setBorowwerName(jsonObject.getString("Borrower Name"));
                                        leadInfoEntity.setLeadStatus(jsonObject.getString("Lead Status"));
                                        leadInfoEntity.setLoanAmount(jsonObject.getString("loan_amount"));
                                        leadInfoEntity.setLoanType(jsonObject.getString("Loan Type"));
                                        leadInfoEntity.setSubmittedToBanks(jsonObject.getString("call_status"));
                                        leadInfoEntity.setEmail(jsonObject.getString("email"));
                                        leadInfoEntity.setCity(jsonObject.getString("city"));
                                        leadInfoEntity.setAddress(jsonObject.getString("address1"));
                                        leadInfoEntity.setPin(jsonObject.getString("pin"));
                                        leadInfoEntity.setOccupation(jsonObject.getString("occupation"));
                                        leadInfoEntity.setCompanyType(jsonObject.getString("company_type"));
                                        leadInfoEntity.setCompanyName(jsonObject.getString("company_name"));
                                        leadInfoEntity.setProfessionType(jsonObject.getString("profession_type"));
                                        leadInfoEntity.setProperty_details(jsonObject.getString("property_details"));
                                        leadInfoEntity.setProperty_value(jsonObject.getString("property_value"));
                                        leadInfoEntity.setIncome(jsonObject.getString("income"));

                                        leadInfoEntityArrayList.add(leadInfoEntity);
                                    }


                                }
                                else {
                                    leadInfoEntity.setCreated_date(jsonObject.getString("created_date") + "\nEstimated Earning : "+ jsonObject.getString("estimated_earning"));
                                    leadInfoEntity.setBorowwerName(jsonObject.getString("Borrower Name"));
                                    leadInfoEntity.setLeadStatus(jsonObject.getString("Lead Status"));
                                    leadInfoEntity.setLoanAmount(jsonObject.getString("loan_amount"));
                                    leadInfoEntity.setLoanType(jsonObject.getString("Loan Type"));
                                    leadInfoEntity.setSubmittedToBanks(jsonObject.getString("call_status"));
                                    leadInfoEntity.setEmail(jsonObject.getString("email"));
                                    leadInfoEntity.setCity(jsonObject.getString("city"));
                                    leadInfoEntity.setAddress(jsonObject.getString("address1"));
                                    leadInfoEntity.setPin(jsonObject.getString("pin"));
                                    leadInfoEntity.setOccupation(jsonObject.getString("occupation"));
                                    leadInfoEntity.setCompanyType(jsonObject.getString("company_type"));
                                    leadInfoEntity.setCompanyName(jsonObject.getString("company_name"));
                                    leadInfoEntity.setProfessionType(jsonObject.getString("profession_type"));
                                    leadInfoEntity.setProperty_details(jsonObject.getString("property_details"));
                                    leadInfoEntity.setProperty_value(jsonObject.getString("property_value"));
                                    leadInfoEntity.setIncome(jsonObject.getString("income"));

                                    leadInfoEntityArrayList.add(leadInfoEntity);
                                }


                            }
                            else {

                                if (productName.trim().contains(jsonObject.getString("Loan Type"))){
                                    leadInfoEntity.setCreated_date(jsonObject.getString("created_date"));
                                    leadInfoEntity.setBorowwerName(jsonObject.getString("Borrower Name"));
                                    leadInfoEntity.setLeadStatus(jsonObject.getString("Lead Status"));
                                    leadInfoEntity.setLoanAmount(jsonObject.getString("loan_amount"));
                                    leadInfoEntity.setLoanType(jsonObject.getString("Loan Type"));
                                    leadInfoEntity.setSubmittedToBanks(jsonObject.getString("call_status"));
                                    leadInfoEntity.setEmail(jsonObject.getString("email"));
                                    leadInfoEntity.setCity(jsonObject.getString("city"));
                                    leadInfoEntity.setAddress(jsonObject.getString("address1"));
                                    leadInfoEntity.setPin(jsonObject.getString("pin"));
                                    leadInfoEntity.setOccupation(jsonObject.getString("occupation"));
                                    leadInfoEntity.setCompanyType(jsonObject.getString("company_type"));
                                    leadInfoEntity.setCompanyName(jsonObject.getString("company_name"));
                                    leadInfoEntity.setProfessionType(jsonObject.getString("profession_type"));
                                    leadInfoEntity.setProperty_details(jsonObject.getString("property_details"));
                                    leadInfoEntity.setProperty_value(jsonObject.getString("property_value"));
                                    leadInfoEntity.setIncome(jsonObject.getString("income"));

                                    leadInfoEntityArrayList.add(leadInfoEntity);
                                }

                            }


                        }

                        LeadStatusAdapter adapter = new LeadStatusAdapter(mContext, leadInfoEntityArrayList, myLeadsList);
                        myLeadsList.setAdapter(adapter);

                        filterQuerryEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (adapter != null) {
                                    adapter.getFilter().filter(String.valueOf(s));

                                }

                            }
                        });

                        adapter.setOnClick(new LeadStatusAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                                FragmentTransaction transaction = ((FragmentActivity) mActivity).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent, LeadDetailsFragment.newInstance(leadInfoEntityArrayList.get(position)));
                                transaction.addToBackStack(null).commit();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    dataLoaderProgressbar.setVisibility(View.GONE);
                });

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(dataRequest);


    }


}
