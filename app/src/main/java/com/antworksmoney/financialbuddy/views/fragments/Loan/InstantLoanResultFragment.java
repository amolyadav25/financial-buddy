package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.LoanTypesAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;

import org.json.JSONObject;

import java.util.ArrayList;

public class InstantLoanResultFragment extends Fragment {
    public InstantLoanResultFragment() {
        // Required empty public constructor
    }

    public static InstantLoanResultFragment newInstance() {
        return new InstantLoanResultFragment();
    }

    private RecyclerView instantLoanList;

    private ProgressBar instantLoanListLoader;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_instant_loan_result, container, false);

        instantLoanList = rootView.findViewById(R.id.instantLoanList);

        instantLoanListLoader = rootView.findViewById(R.id.progressBarListLoader);

        instantLoanList.setHasFixedSize(true);
        instantLoanList.setLayoutManager(new GridLayoutManager(getContext(), 2));


        loadList();
        return rootView;
    }

    private void loadList(){

        instantLoanListLoader.setVisibility(View.VISIBLE);

        JsonArrayRequest dataRequest = new JsonArrayRequest(
                Request.Method.GET,
                AppConstant.BaseUrl + "Instanse_loan_url",
                null,
                response -> {

                    Log.e("TAG",response.toString());

                    instantLoanListLoader.setVisibility(View.GONE);

                    try {
                        ArrayList<LoanInfoEntity> dataList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            dataList.add(new LoanInfoEntity(
                                    ((JSONObject) response.get(i)).getString("name"),
                                    ((JSONObject) response.get(i)).getString("url").trim().replaceAll("\\s", "%20"),
                                    ((JSONObject) response.get(i)).getString("icon")));
                        }

                        LoanTypesAdapter loanTypesAdapter = new LoanTypesAdapter(getContext(), dataList, instantLoanList);
                        instantLoanList.setAdapter(loanTypesAdapter);

                        loanTypesAdapter.setOnClick(position -> {

                            for (int i = 0; i < dataList.size(); i++) {
                                ImageView layout = ((View) (instantLoanList.findViewHolderForAdapterPosition(i)).itemView).findViewById(R.id.servicePic);
                                if (i == position) {

                                    layout.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_check_circle_grey_50dp));

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (getActivity() != null){
                                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(dataList.get(position).getLoanId(), dataList.get(position).getLoanName()));
                                                transaction.addToBackStack(null).commit();
                                            }
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
                    instantLoanListLoader.setVisibility(View.GONE);
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(dataRequest);
    }

}
