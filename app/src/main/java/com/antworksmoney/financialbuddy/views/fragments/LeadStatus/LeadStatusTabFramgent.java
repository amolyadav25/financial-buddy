package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.InvestmentStatusAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class LeadStatusTabFramgent extends Fragment {
    public LeadStatusTabFramgent() {
        // Required empty public constructor
    }

    public static LeadStatusTabFramgent newInstance() {
        return  new LeadStatusTabFramgent();
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

    public static String startDate = "", endDate = "";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_lead_status_tab_framgent, container, false);

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

        adapterProducts = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, productsList) {
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
                loadLeadStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        loadLeadStatus();

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
                    dialogFragment.setDissmissListener(new TabbedDialog.OnDismissListener() {
                        @Override
                        public void onDismiss(TabbedDialog myDialogFragment) {
                            Log.e(TAG,myDialogFragment.okButton.getText().toString().trim());
                            changeDateText.setText(startDate + " - " + endDate);
                            loadLeadStatus();
                        }
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
                AppConstant.BaseUrl + "investment_queries_list",
                dataObject,
                response -> {
                    dataLoaderProgressbar.setVisibility(View.GONE);
                    myLeadsList.setVisibility(View.VISIBLE);


                    Log.e(TAG, response.toString());

                    try {
                        JSONArray dataArray = response.getJSONObject("respone").getJSONArray("UserData");

                        ArrayList<InvestmentEntity> leadInfoEntityArrayList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            InvestmentEntity leadInfoEntity = new InvestmentEntity();
                            JSONObject jsonObject = ((JSONObject) dataArray.get(i));
                            leadInfoEntity.setId(jsonObject.getString("id"));
                            leadInfoEntity.setInvestment_product(jsonObject.getString("investment_product"));
                            leadInfoEntity.setCompany_name(jsonObject.getString("company_name"));
                            leadInfoEntity.setFirst_name(jsonObject.getString("first_name"));
                            leadInfoEntity.setLast_name(jsonObject.getString("last_name"));
                            leadInfoEntity.setMobile(jsonObject.getString("mobile"));
                            leadInfoEntity.setEmail(jsonObject.getString("email"));
                            leadInfoEntity.setCity(jsonObject.getString("city"));
                            leadInfoEntity.setState(jsonObject.getString("state"));
                            leadInfoEntity.setInvestment_queries_id(jsonObject.getString("investment_queries_id"));
                            leadInfoEntity.setInvestment_detail_id(jsonObject.getString("investment_detail_id"));
                            leadInfoEntity.setInvestment_product_id(jsonObject.getString("investment_product_id"));
                            leadInfoEntity.setInv_product_list_id(jsonObject.getString("inv_product_list_id"));
                            leadInfoEntity.setInt_rate1(jsonObject.getString("int_rate1"));
                            leadInfoEntity.setInt_rate2(jsonObject.getString("int_rate2"));
                            leadInfoEntity.setInt_rate3(jsonObject.getString("int_rate3"));
                            leadInfoEntity.setInt_rate4(jsonObject.getString("int_rate4"));
                            leadInfoEntity.setInt_rate5(jsonObject.getString("int_rate5"));
                            leadInfoEntity.setInt_rate6(jsonObject.getString("int_rate6"));
                            leadInfoEntity.setInt_rate7(jsonObject.getString("int_rate7"));
                            leadInfoEntity.set_sr_citizen(jsonObject.getString("sr_citizens"));
                            leadInfoEntity.setStatus(jsonObject.getString("status"));
                            leadInfoEntity.setLead_status(jsonObject.getString("lead_status"));
                            leadInfoEntity.setDate_added(jsonObject.getString("date_added") + "\nEstimated Earning : "+ jsonObject.getString("estimated_earning"));
                            leadInfoEntityArrayList.add(leadInfoEntity);
                        }

                        InvestmentStatusAdapter adapter = new InvestmentStatusAdapter(mContext, leadInfoEntityArrayList, myLeadsList);
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

                        adapter.setOnClick(new InvestmentStatusAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {

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
