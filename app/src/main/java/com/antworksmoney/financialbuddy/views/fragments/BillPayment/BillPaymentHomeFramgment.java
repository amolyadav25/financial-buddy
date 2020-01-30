package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BillerEntity;
import com.antworksmoney.financialbuddy.helpers.Utility.AvenuesParams;
import com.antworksmoney.financialbuddy.helpers.Utility.ServiceUtility;
import com.antworksmoney.financialbuddy.helpers.adapters.BillProductsAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.WebViewActivity;
import com.antworksmoney.financialbuddy.views.fragments.Wallet.MyWalletFragment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class BillPaymentHomeFramgment extends Fragment {
    public BillPaymentHomeFramgment() {
        // Required empty public constructor
    }

    public static BillPaymentHomeFramgment newInstance() {
        return new BillPaymentHomeFramgment();
    }

    private RecyclerView productsWeOfferlist;

    private Context mContext;

    private ProgressBar productsLoader;

    private Toolbar toolbar;

    private TextView menu_leads, menu_wallet, menu_profile;

    private static final String TAG = "BillPaymentHomeFramgmen";

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bill_payment_home_framgment, container, false);

        productsLoader = rootView.findViewById(R.id.productsLoader);

        mContext = getContext();

        toolbar = rootView.findViewById(R.id.top_toolBar);

        menu_leads = rootView.findViewById(R.id.menu_leads);

        menu_wallet = rootView.findViewById(R.id.menu_wallet);

        menu_profile = rootView.findViewById(R.id.menu_profile);


        productsWeOfferlist = rootView.findViewById(R.id.productsWeOfferlist);
        productsWeOfferlist.setLayoutManager(new GridLayoutManager(getContext(),2));
        productsWeOfferlist.setHasFixedSize(true);

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        }




        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadProductsList();

        menu_leads.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, QuickBillPayFragment.newInstance());
            transaction.addToBackStack(null).commit();
        });

        menu_wallet.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, MyWalletFragment.newInstance());
            transaction.addToBackStack(null).commit();
        });

        menu_profile.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, ComplaintHomeFragment.newInstance());
            transaction.addToBackStack(null).commit();
        });
    }

    private void loadProductsList(){

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.BaseUrl + AppConstant.BBPS_API + "billercategories",
                null,
                response -> {
                    productsLoader.setVisibility(View.GONE);

                    try {
                        JSONArray dataArray = response.getJSONArray("categories");

                        ArrayList<BillerEntity> billProductInfoEntity = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {

                            BillerEntity leadInfoEntity = new BillerEntity();
                            JSONObject jsonObject = ((JSONObject) dataArray.get(i));

//                            leadInfoEntity.setBillerId(jsonObject.getString("billerId"));
//                            leadInfoEntity.setBillerName(jsonObject.getString("billerName"));
                            leadInfoEntity.setBillerCategory(jsonObject.getString("billerCategory"));
//                            leadInfoEntity.setBillerAdhoc(jsonObject.getString("billerAdhoc"));
//                            leadInfoEntity.setBillerCoverage(jsonObject.getString("billerCoverage"));
//                            leadInfoEntity.setBillerFetchRequiremet(jsonObject.getString("billerFetchRequiremet"));
//                            leadInfoEntity.setBillerPaymentExactness(jsonObject.getString("billerPaymentExactness"));
//                            leadInfoEntity.setBillerSupportBillValidation(jsonObject.getString("billerSupportBillValidation"));
//                            leadInfoEntity.setBillerAmountOptions(jsonObject.getString("billerAmountOptions"));
//                            leadInfoEntity.setBillerPaymentModes(jsonObject.getString("billerPaymentModes"));
//                            leadInfoEntity.setBillerDescription(jsonObject.getString("billerDescription"));
//                            leadInfoEntity.setRechargeAmountInValidationRequest(jsonObject.getString("rechargeAmountInValidationRequest"));
                            leadInfoEntity.setDrawable(R.drawable.bill_payment_nav_icon);

                            billProductInfoEntity.add(leadInfoEntity);
                        }

                        BillProductsAdapter adapter = new BillProductsAdapter(mContext, billProductInfoEntity, productsWeOfferlist);
                        productsWeOfferlist.setAdapter(adapter);

                        adapter.setOnClick(position -> {
                            if (getActivity() != null) {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent,BillPaymentSubProduct.newInstance(billProductInfoEntity.get(position)));
                                transaction.addToBackStack(null).commit();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    productsLoader.setVisibility(View.GONE);
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
