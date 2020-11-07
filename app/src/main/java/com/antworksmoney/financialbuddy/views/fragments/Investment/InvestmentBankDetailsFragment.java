package com.antworksmoney.financialbuddy.views.fragments.Investment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.InvestmentServiceListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;


public class InvestmentBankDetailsFragment extends Fragment {

    private static InvestmentEntity mInvestmentEntity;

    public static InvestmentBankDetailsFragment newInstance(InvestmentEntity investmentEntity) {
        mInvestmentEntity = investmentEntity;
        return new InvestmentBankDetailsFragment();
    }

    private RelativeLayout recommendedInvestmentTypes;

    private ProgressBar bankLoadProgressBar;

    private Toolbar topToolBar;

    private RecyclerView investmentTypeList;

    private CoordinatorLayout snackBarView;

    private ImageView imageView;

    private LinearLayout bankInterestRateCard;

    private ProgressBar tableLayoutLoader;

    private TableLayout tableLayout;

    private static final String TAG = "InvestmentBankDetailsFr";

    private TextView bankName;

    private RequestQueue mRequestQueue;

    private Context mContext;

    private int TEXTPADDING = 20;

    private TextView agreeText;

    private Button buttonInterested;

    private TextView description;

    private TextView bankDetails;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_investment_bank_details, container, false);

        mContext = getContext();

        bankLoadProgressBar = rootView.findViewById(R.id.bankLoadProgressBar);

        recommendedInvestmentTypes = rootView.findViewById(R.id.recommendedInvestmentTypes);

        topToolBar = rootView.findViewById(R.id.toolbar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        imageView = rootView.findViewById(R.id.image);

        tableLayoutLoader = rootView.findViewById(R.id.tableLayoutLoader);

        tableLayout = rootView.findViewById(R.id.myTable);

        bankInterestRateCard = rootView.findViewById(R.id.bankInterestRateCard);

        bankName = rootView.findViewById(R.id.bankName);

        buttonInterested = rootView.findViewById(R.id.buttonInterested);

        mRequestQueue = Volley.newRequestQueue(mContext);

        agreeText = rootView.findViewById(R.id.agreeText);

        description = rootView.findViewById(R.id.description);

        bankDetails = rootView.findViewById(R.id.bankDetails);

        investmentTypeList = rootView.findViewById(R.id.investmentTypeList);
        investmentTypeList.setLayoutManager(new GridLayoutManager(mContext, 2));
        investmentTypeList.setHasFixedSize(true);

        if (topToolBar != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            if (getActivity() != null) {

                ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

                topToolBar.setNavigationOnClickListener(v ->
                        ((HomeActivity) getActivity())
                                .getmDrawerLayout()
                                .openDrawer(GravityCompat.START));

                topToolBar.setTitle("Investment");

            }

        }

        recommendedInvestmentTypes.setVisibility(View.GONE);

        bankInterestRateCard.setVisibility(View.GONE);

        Glide.with(getContext())
                .load("https://www.antworksmoney.com//assets/img/antworks-corporate-fixed-deposites-inner.jpg")
                .into(imageView);

        getBanksData();

        return rootView;
    }

    private void getBanksData() {

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("userData", new JSONObject().put("investor_product_type", mInvestmentEntity.getInvestmentId()));
        } catch (Exception e) {
            e.printStackTrace();
        }



        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "investment_product_list",
                dataObject, response -> {
            bankLoadProgressBar.setVisibility(View.GONE);
            recommendedInvestmentTypes.setVisibility(View.VISIBLE);

            Log.i(TAG, response.toString());

            ArrayList<InvestmentEntity> investmentEntityArrayList = new ArrayList<>();
            try {
                for (int i = 0; i < response.getJSONArray("UserData").length(); i++) {
                    InvestmentEntity entity = new InvestmentEntity();

                    entity.setInvestmentId(((JSONObject) response.getJSONArray("UserData").get(i))
                            .getString("investment_list_id"));

                    entity.setInvestmentType(((JSONObject) response.getJSONArray("UserData").get(i))
                            .getString("company_name") + " ( " + ((JSONObject) response.getJSONArray("UserData").get(i)).getString("industry") + " )");

                    entity.setCompanyDetails(((JSONObject) response.getJSONArray("UserData").get(i))
                            .getString("about_corporat_fund"));

                    entity.setInvestmentImage(((JSONObject) response.getJSONArray("UserData").get(i))
                            .getString("image"));

                    investmentEntityArrayList.add(entity);
                }

                InvestmentServiceListAdapter adapter = new InvestmentServiceListAdapter(getContext(), investmentEntityArrayList, "BankList");
                investmentTypeList.setAdapter(adapter);

                adapter.setOnClick(position -> {
                    tableLayout.removeAllViews();

                    agreeText.setText(MessageFormat.format(
                            "Simply click on I am interested and register for Antworks ''{0}'' services.",
                            investmentEntityArrayList.get(position).getInvestmentType()));

                    recommendedInvestmentTypes.setVisibility(View.GONE);
                    bankLoadProgressBar.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);


                    bankDetails.setText(investmentEntityArrayList.get(position).getCompanyDetails());
                    mInvestmentEntity.setRecommendedInvestmentId(investmentEntityArrayList.get(position).getInvestmentId());
                    bankName.setText(investmentEntityArrayList.get(position).getInvestmentType());
                    showTableLayout(investmentEntityArrayList.get(position).getInvestmentId());

                    tableLayoutLoader.setVisibility(View.VISIBLE);
                    bankInterestRateCard.setVisibility(View.VISIBLE);

                });

                buttonInterested.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() != null){
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.homeParent, InvestmentApplyNowFragment.newInstance(mInvestmentEntity));
                            transaction.addToBackStack(null).commit();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                showSnackBar("Falied to load Investment data !!!");
            }
        },
                error -> {
                    bankLoadProgressBar.setVisibility(View.GONE);
                    showSnackBar("Falied to load Investment data !!!");
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequest.setShouldCache(false);

        mRequestQueue.add(dataRequest);

    }


    public void showTableLayout(String investmentId) {

        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject innerObject = new JSONObject();
            innerObject.put("investment_list_id", mInvestmentEntity.getRecommendedInvestmentId());
            innerObject.put("investor_product_type", mInvestmentEntity.getInvestmentId());

            jsonObject.put("userData", innerObject);

            Log.e(TAG,jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "investment_detail",
                jsonObject, response -> {
            tableLayoutLoader.setVisibility(View.GONE);
            try {

                Log.i(TAG,response.toString());

                int row = response.getJSONObject("respone").getJSONArray("UserData").length();

                TableRow headingRow = new TableRow(mContext);
                headingRow.setBackground(mContext.getResources().getDrawable(R.drawable.addcontactbuttonbackground));

                String[] headingRowData = {
                        "Name of Company",
                        "Industry",
                        "Product Name",
                        "Rating",
                        "Interest Rates on Cumulative Interest rate option*"
                };

                for (String aHeadingRowData : headingRowData) {
                    TextView txtGeneric = new TextView(mContext);
                    txtGeneric.setTextSize(14);
                    txtGeneric.setGravity(Gravity.CENTER);
                    txtGeneric.setText(aHeadingRowData);
                    txtGeneric.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    txtGeneric.setPadding(TEXTPADDING, TEXTPADDING + 20, TEXTPADDING, TEXTPADDING + 20);

                    headingRow.addView(txtGeneric);
                }

                tableLayout.addView(headingRow);

                TableRow headingRowNew = new TableRow(mContext);
                headingRowNew.setBackground(mContext.getResources().getDrawable(R.drawable.addcontactbuttonbackground));

                String[] headingRowNewData = {
                        "","","","","\t1yr \t\t 2yr \t\t 3yr \t\t 4yr \t\t 5yr \t\t 6yr \t\t 7yr \t\t Senior Citizen"
                };

                for (String aHeadingRowData : headingRowNewData) {
                    TextView txtGeneric = new TextView(mContext);
                    txtGeneric.setTextSize(12);
                    txtGeneric.setGravity(Gravity.CENTER);
                    txtGeneric.setText(aHeadingRowData);
                    txtGeneric.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    txtGeneric.setPadding(TEXTPADDING, TEXTPADDING, TEXTPADDING, TEXTPADDING);

                    headingRowNew.addView(txtGeneric);
                }

                tableLayout.addView(headingRowNew);




                for (int i = 0; i < row; i++) {

                    JSONObject dataObject = (JSONObject) response.getJSONObject("respone").getJSONArray("UserData").get(i);

                    ArrayList<String> data = new ArrayList<>();

                    mInvestmentEntity.setFinanceCompanyId(dataObject.getString("investment_detail_id"));
                    mInvestmentEntity.setInt_rate1(dataObject.getString("int_rate1"));
                    mInvestmentEntity.setInt_rate2(dataObject.getString("int_rate2"));
                    mInvestmentEntity.setInt_rate3(dataObject.getString("int_rate3"));
                    mInvestmentEntity.setInt_rate4(dataObject.getString("int_rate4"));
                    mInvestmentEntity.setInt_rate5(dataObject.getString("int_rate5"));
                    mInvestmentEntity.setInt_rate6(dataObject.getString("int_rate6"));
                    mInvestmentEntity.setInt_rate7(dataObject.getString("int_rate7"));
                    mInvestmentEntity.set_sr_citizen(dataObject.getString("sr_citizens"));
                    bankDetails.setText(dataObject.getString("about"));

                    data.add(dataObject.getString("company_name"));
                    data.add(dataObject.getString("industry"));
                    data.add(mInvestmentEntity.getInvestmentType());
                    data.add(dataObject.getString("rating"));
                    data.add(dataObject.getString("int_rate1") + "\t\t\t" +
                            dataObject.getString("int_rate2") +"\t\t\t\t" +
                            dataObject.getString("int_rate3") +"\t\t\t\t" +
                            dataObject.getString("int_rate4") +"\t\t\t\t" +
                            dataObject.getString("int_rate5") +"\t\t\t\t" +
                            dataObject.getString("int_rate6") +"\t\t\t\t" +
                            dataObject.getString("int_rate7") +"\t\t\t\t" +
                            dataObject.getString("sr_citizens"));

                    TableRow dataRow = new TableRow(mContext);

                    for (int j = 0; j < data.size(); j++) {
                        TextView txtGeneric = new TextView(mContext);
                        txtGeneric.setTextSize(14);
                        txtGeneric.setText(data.get(j));
                        txtGeneric.setBackground(mContext.getResources().getDrawable(R.drawable.textlines));
                        txtGeneric.setPadding(TEXTPADDING, TEXTPADDING + 10, TEXTPADDING, TEXTPADDING + 10);

                        dataRow.addView(txtGeneric);

                    }
                    tableLayout.addView(dataRow);

                }

                tableLayout.setStretchAllColumns(true);
                tableLayout.bringToFront();

                Log.i(TAG,mInvestmentEntity.toString());

            } catch (Exception e) {
                e.printStackTrace();
                bankInterestRateCard.setVisibility(View.GONE);
                showSnackBar("No Data found for this company !!");
            }
        },
                error -> {
                    bankInterestRateCard.setVisibility(View.GONE);
                    Log.i(TAG, error.toString());
                    showSnackBar("Failed to load data !!");
                });

        dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataObjectRequest.setShouldCache(false);

        mRequestQueue.add(dataObjectRequest);
    }

    private void showSnackBar(String message) {
        final Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();

            }
        });
        snackBar.show();
    }


}
