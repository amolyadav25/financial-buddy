package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.LoanTypesAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class LoanTypesFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public LoanTypesFragment() {
        // Required empty public constructor
    }

    public static LoanTypesFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new LoanTypesFragment();
    }

    private RecyclerView loanTypeList, loanSubTypeList;

    private ProgressBar loanTypeLoader, loanSubTypeLoader;

    private static final String TAG = "LoanTypesFragment";

    private RequestQueue dataRequestQueue;

    private Toolbar top_toolBar;

    private Context mContext;

    private TextView loanHeading;

    private TextView selectLoanTypeHeading, loanSubCategoryHeading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_types, container, false);

        mContext = getContext();
Log.e("Mytag","LoanType");
        loanTypeList = rootView.findViewById(R.id.loanTypeList);
        loanTypeList.setLayoutManager(new GridLayoutManager(mContext, 2));
        loanTypeList.setHasFixedSize(true);

        loanSubTypeList = rootView.findViewById(R.id.loanSubTypeList);
        loanSubTypeList.setLayoutManager(new GridLayoutManager(mContext, 2));
        loanSubTypeList.setHasFixedSize(true);

        loanTypeLoader = rootView.findViewById(R.id.loanTypeLoader);

        loanSubTypeLoader = rootView.findViewById(R.id.loanSubTypeLoader);

        selectLoanTypeHeading = rootView.findViewById(R.id.selectLoanTypeHeading);

        loanSubCategoryHeading = rootView.findViewById(R.id.loanSubCategoryHeading);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        dataRequestQueue = Volley.newRequestQueue(mContext);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        Log.e(TAG, mLoanInfoEntity.toString());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loanSubCategoryHeading.setVisibility(View.INVISIBLE);

        loanSubTypeList.setVisibility(View.INVISIBLE);

        loanSubTypeLoader.setVisibility(View.INVISIBLE);

        selectLoanTypeHeading.setVisibility(View.GONE);

        loanTypeList.setVisibility(View.GONE);

        loanTypeLoader.setVisibility(View.GONE);

        top_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START);
                }
            }
        });

        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {
            mLoanInfoEntity.setLoanType("Individual Loan");
            getLoanNameDataFromAPI("1");
        } else {
            getMajorLoanCategories();
        }
    }

    private void getMajorLoanCategories() {

        selectLoanTypeHeading.setVisibility(View.VISIBLE);

        loanTypeList.setVisibility(View.VISIBLE);

        loanTypeLoader.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "loan_type",
                null,
                response -> {
                    loanTypeLoader.setVisibility(View.GONE);

                    selectLoanTypeHeading.setText("What is Applicant's Profile ? ");

                    try {
                        JSONArray dataArray = response.getJSONArray("UserData");

                        Log.e(TAG, response.toString());

                        ArrayList<LoanInfoEntity> dataList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {
                            dataList.add(new LoanInfoEntity(
                                    ((JSONObject) dataArray.get(i)).getString("loan_name"),
                                    ((JSONObject) dataArray.get(i)).getString("id"),
                                    ((JSONObject) dataArray.get(i)).getString("icon_url")));
                        }

                        LoanTypesAdapter loanTypesAdapter = new LoanTypesAdapter(mContext, dataList, loanTypeList);
                        loanTypeList.setAdapter(loanTypesAdapter);

                        loanTypesAdapter.setOnClick(position -> {
                            Log.e("Mytag","loanTypesAdapter");
                            for (int i = 0; i < dataList.size(); i++) {
                                ImageView layout = ((View) (loanTypeList.findViewHolderForAdapterPosition(i)).itemView).findViewById(R.id.servicePic);
                                if (i == position) {

                                    layout.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_grey_50dp));

                                    mLoanInfoEntity.setLoanType(dataList.get(position).getLoanName());

                                    loanHeading.setText(dataList.get(position).getLoanName());

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            selectLoanTypeHeading.setVisibility(View.GONE);

                                            loanTypeList.setVisibility(View.GONE);

                                            loanTypeLoader.setVisibility(View.GONE);

                                            getLoanNameDataFromAPI(dataList.get(position).getLoanId());
                                        }
                                    }, 500);

                                }
                            }


                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    loanTypeLoader.setVisibility(View.GONE);
                    Log.w(TAG, error.toString());
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);
    }


    private void getLoanNameDataFromAPI(String id) {

        loanSubCategoryHeading.setVisibility(View.VISIBLE);

        loanSubTypeList.setVisibility(View.VISIBLE);

        loanSubTypeLoader.setVisibility(View.VISIBLE);

        JSONObject dataObject = null;
        try {
            dataObject = new JSONObject().put("userData", new JSONObject().put("loan_type", id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.w(TAG, AppConstant.BaseUrl + "loan_name");
        Log.w(TAG, dataObject.toString());

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST, AppConstant.BaseUrl + "loan_name",
                dataObject, response -> {
            loanSubTypeLoader.setVisibility(View.GONE);

            loanSubCategoryHeading.setText("Looking For ");

            try {

                JSONArray dataArray = response.getJSONArray("UserData");


                ArrayList<LoanInfoEntity> loanNameList = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {

                    loanNameList.add(new LoanInfoEntity(
                            ((JSONObject) dataArray.get(i)).getString("loan_type"),
                            ((JSONObject) dataArray.get(i)).getString("id"),
                            ((JSONObject) dataArray.get(i)).getString("icon_url")));
                }

                LoanTypesAdapter loanTypesAdapter = new LoanTypesAdapter(mContext, loanNameList, loanTypeList);
                loanSubTypeList.setAdapter(loanTypesAdapter);

                loanTypesAdapter.setOnClick(position -> {
                    Log.e("Mytag","loanTypesAdapter2");
                    for (int i = 0; i < loanNameList.size(); i++) {
                        ImageView layout = ((View) (loanSubTypeList.findViewHolderForAdapterPosition(i)).itemView).findViewById(R.id.servicePic);

                        if (i == position) {

                            loanHeading.setText(loanNameList.get(position).getLoanName());

                            mLoanInfoEntity.setLoanId(loanNameList.get(position).getLoanId());

                            mLoanInfoEntity.setLoanName(loanNameList.get(position).getLoanName());

                            loanSubTypeLoader.setVisibility(View.VISIBLE);
                            Log.e("Mytag","loanNameList.get(position).getLoanName()"+loanNameList.get(position).getLoanName());
                            Log.e("Mytag","loanNameList.get(position).getLoanId()"+loanNameList.get(position).getLoanId());
                            layout.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_grey_50dp));
                        }
                    }
                    new Handler().postDelayed(() -> {

                        if (loanNameList.get(position).getLoanName().trim().equalsIgnoreCase("Credit Counselling")){
                            if (getActivity() != null){
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent, CreditCounsellingFragment.newInstance());
                                transaction.addToBackStack(null).commit();
                            }
                        }
                        else {
                            if (getActivity() != null) {
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                                transaction.addToBackStack(null).commit();

                            }
                        }
                    }, 1000);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        },
                error -> {
                    loanSubTypeLoader.setVisibility(View.GONE);
                    Log.w(TAG, error.toString());
                });

        dataRequest.setShouldCache(false);

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);
    }

}
