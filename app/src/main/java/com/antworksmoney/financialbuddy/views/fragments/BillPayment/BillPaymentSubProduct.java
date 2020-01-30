package com.antworksmoney.financialbuddy.views.fragments.BillPayment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BillerEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BillPaymentSubProduct extends Fragment implements View.OnClickListener {
    public BillPaymentSubProduct() {
        // Required empty public constructor
    }

    private static BillerEntity mBillerEntity;


    public static BillPaymentSubProduct newInstance(BillerEntity billerEntity) {
        mBillerEntity = billerEntity;
        return new BillPaymentSubProduct();
    }

    private Spinner billerSubCategoryProduct;

    private ArrayAdapter<String> adapterOccupation;

    private Button nextButtonForBiller;

    private ProgressBar loader;

    private RequestQueue queue;

    private Context mContext;


    private EditText mobileNumberEditText;

    private static final String TAG = "BillPaymentSubProduct";

    private TextView dataTypeTextView;

    private EditText dataTypeEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bill_payment_sub_product, container, false);

        mContext = getContext();

        billerSubCategoryProduct = rootView.findViewById(R.id.billerSubCategoryProduct);

        nextButtonForBiller = rootView.findViewById(R.id.nextButtonForBiller);

        loader = rootView.findViewById(R.id.loader);

        mobileNumberEditText = rootView.findViewById(R.id.mobileNumberEditText);

        dataTypeTextView = rootView.findViewById(R.id.dataTypeTextview);

        dataTypeEditText = rootView.findViewById(R.id.dataTypeEditText);


        queue = Volley.newRequestQueue(mContext);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchBillerInfo();

        nextButtonForBiller.setOnClickListener(this);
    }


    private void fetchBillerInfo() {

        loader.setVisibility(View.VISIBLE);

        try {
            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + AppConstant.BBPS_API + "getBillers",
                    new JSONObject().put("billerCategory", mBillerEntity.getBillerCategory()),
                    response -> {

                        loader.setVisibility(View.GONE);

                        try {
                            JSONArray dataArray = response.getJSONArray("billers");

                            ArrayList<BillerEntity> billInfoEntityList = new ArrayList<>();

                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject dataObject = dataArray.getJSONObject(i);

                                BillerEntity leadInfoEntity = new BillerEntity();

                                leadInfoEntity.setBillerId(dataObject.getString("billerId"));
                                leadInfoEntity.setBillerName(dataObject.getString("billerName"));
                                leadInfoEntity.setBillerCategory(dataObject.getString("billerCategory"));
                                leadInfoEntity.setBillerAdhoc(dataObject.getString("billerAdhoc"));
                                leadInfoEntity.setBillerCoverage(dataObject.getString("billerCoverage"));
                                leadInfoEntity.setBillerFetchRequiremet(dataObject.getString("billerFetchRequiremet"));
                                leadInfoEntity.setBillerPaymentExactness(dataObject.getString("billerPaymentExactness"));

                                billInfoEntityList.add(leadInfoEntity);

                            }

                            ArrayList<String> billers = new ArrayList<>();
                            for (int i = 0; i < billInfoEntityList.size(); i++) {
                                billers.add(billInfoEntityList.get(i).getBillerName());
                            }
                            billers.add("Select your biller");

                            adapterOccupation = new ArrayAdapter<String>(mContext, R.layout.checked_text_view, billers) {
                                @Override
                                public int getCount() {
                                    return billers.size() - 1;
                                }
                            };
                            adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);

                            billerSubCategoryProduct.setAdapter(adapterOccupation);

                            billerSubCategoryProduct.setSelection(billers.size() - 1);

                            billerSubCategoryProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if (position != billInfoEntityList.size()) {

                                        BillerEntity entity = billInfoEntityList.get(position);

                                        mBillerEntity.setBillerName(entity.getBillerName());
                                        mBillerEntity.setBillerId(entity.getBillerId());

                                        fetchBillInputParams(entity);
                                    }

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                            loader.setVisibility(View.GONE);
                        }


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

            queue.add(dataRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextButtonForBiller) {
            if (mobileNumberEditText.getText().toString().trim().length() < 10) {
                mobileNumberEditText.setError("Invalid phone number !!");
            }
            if (dataTypeEditText.getText().toString().trim().equalsIgnoreCase("")) {
                dataTypeEditText.setError("Invalid " + dataTypeTextView.getText().toString() + " !!!");
            } else {
                fetchBillInfo();
            }

        }
    }

    private void fetchBillInputParams(BillerEntity entity) {

        loader.setVisibility(View.VISIBLE);

        try {

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + AppConstant.BBPS_API + "getBillerinputfields",
                    new JSONObject().put("billerId", entity.getBillerId()),
                    response -> {
                        Log.e(TAG, response.toString());

                        loader.setVisibility(View.GONE);

                        try {
                            if (response.getString("msg").trim().equalsIgnoreCase("Found")) {

                                for (int i = 0; i < response.getJSONArray("billers").length(); i++) {


                                    dataTypeEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(
                                            response.getJSONArray("billers").getJSONObject(i).getString("maxLength")))});


                                    if (response.getJSONArray("billers").getJSONObject(i)
                                            .getString("dataType").trim().equalsIgnoreCase("NUMERIC")) {
                                        dataTypeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }

                                    dataTypeTextView.setText(response.getJSONArray("billers").getJSONObject(i)
                                            .getString("paramName"));

                                    dataTypeTextView.setVisibility(View.VISIBLE);

                                    dataTypeEditText.setVisibility(View.VISIBLE);

                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(mContext, "Failed to get biller info", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        Log.e(TAG, error.toString());
                    });

            dataRequest.setShouldCache(false);

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(dataRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchBillInfo() {
        loader.setVisibility(View.VISIBLE);

        try {

            Log.e(TAG, new JSONObject().put("mobile", mobileNumberEditText.getText().toString().trim())
                    .put("billerId", mBillerEntity.getBillerId())
                    .put("inputparameter", new JSONObject().put(
                            dataTypeTextView.getText().toString().trim(),
                            dataTypeEditText.getText().toString().trim()))
                    .put("biller_category",mBillerEntity.getBillerCategory()).toString());

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + AppConstant.BBPS_API + "billFetch",
                    new JSONObject().put("mobile", mobileNumberEditText.getText().toString().trim())
                            .put("billerId", mBillerEntity.getBillerId())
                            .put("inputparameter", new JSONObject().put(
                                    dataTypeTextView.getText().toString().trim(),
                                    dataTypeEditText.getText().toString().trim()))
                            .put("biller_category",mBillerEntity.getBillerCategory()),
                    response -> {

                        Log.e(TAG, response.toString());

                        loader.setVisibility(View.GONE);

                        try {

                            if (response.getString("msg").trim().equalsIgnoreCase("Found")) {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, BillFetchFtagment.newInstance(response, mBillerEntity));
                                    transaction.addToBackStack(null).commit();
                                }
                            } else {
                                Toast.makeText(mContext, "Failed to get details !!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            Toast.makeText(mContext, "Failed to get details !!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(mContext, "Failed to get details !!", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        Log.e(TAG, error.toString());
                    });

            dataRequest.setShouldCache(false);

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(dataRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
