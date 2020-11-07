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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.InvestmentServiceListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;


public class InvestmentApplyForFragment extends Fragment {


    private ImageView inventment_banner;

    public InvestmentApplyForFragment() {
        // Required empty public constructor
    }

    private Toolbar toolbar;

    private Context mContext;

    private String applyFor = "";

//    private CardView recommendedIVnvestmentTypes;

    private ScrollView scrollBar;

    private CoordinatorLayout snackBarView;

    private Button self, referAFriend;

    private ProgressBar investmentTypeLoader;

    private RecyclerView investmentTypeList;

    private LinearLayout applyForList,invest_desc_layout;

    private TextView investmentContent;

    private int[] images = {

            R.drawable.corporate_bond,
            R.drawable.corporate_fd,
            R.drawable.ptp_investment
    };

    private static final String TAG = "InvestmentApplyForFragm";

    private String defaultContent = "At Financial Buddy, we are committed to make your investment grow manifold. Now, acceleratate the returns on your savings with a state-of-the-art investment platform to purchase investment products like Corporate bonds, Corporate Fixed Deposits and Peer to Peer (P2P) lending platform of Antworks.\n\nBenefits of applying through Financial buddy\n☑ Get investment products that suits your risk profile\n☑ Best offers and reward\n☑ Choice of more than 20 Companies\n\nPlan you Investment today ";

    private String updateContent = "Recommended Investment For you : ";


    public static InvestmentApplyForFragment newInstance() {
        return new InvestmentApplyForFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_investment_home, container, false);

        mContext = getContext();

        toolbar = rootView.findViewById(R.id.toolBar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        applyForList = rootView.findViewById(R.id.applyForList);

        invest_desc_layout =rootView.findViewById(R.id.otherLayout);


     //   inventment_banner = rootView.findViewById(R.id.invest_banner_image);

        scrollBar = rootView.findViewById(R.id.scrollBar);

        referAFriend = rootView.findViewById(R.id.applyForOthers);

        investmentTypeLoader = rootView.findViewById(R.id.investmentTypeLoader);

        self = rootView.findViewById(R.id.applyForSelf);

        investmentContent = rootView.findViewById(R.id.investmentContent);

        investmentTypeList = rootView.findViewById(R.id.investmentTypeList);
        investmentTypeList.setLayoutManager(new GridLayoutManager(mContext, 2));
        investmentTypeList.setHasFixedSize(true);


        if (toolbar != null && getActivity() != null) {

            toolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));
        }

        investmentContent.setText(defaultContent);
        invest_desc_layout.setVisibility(View.VISIBLE);



        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                investmentTypeLoader.setVisibility(View.VISIBLE);
                getInvestmentProduct("Self");
            }
        });

        referAFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                investmentTypeLoader.setVisibility(View.VISIBLE);
                getInvestmentProduct("Others");
            }
        });

        return rootView;
    }

    private void getInvestmentProduct(String others) {

        applyFor = others;

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.BaseUrl + "investment_product",
                null,
                response -> {
                    investmentTypeLoader.setVisibility(View.GONE);
                    scrollBar.fullScroll(View.FOCUS_DOWN);
                    applyForList.setVisibility(View.GONE);
                    invest_desc_layout.setVisibility(View.GONE);
                    investmentContent.setText(updateContent);

                    Log.e(TAG, response.toString());

                    ArrayList<InvestmentEntity> investmentEntityArrayList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.getJSONArray("UserData").length(); i++) {
                            InvestmentEntity entity = new InvestmentEntity();
                            entity.setInvestmentId(((JSONObject) response.getJSONArray("UserData").get(i)).getString("id"));
                            entity.setInvestmentType(((JSONObject) response.getJSONArray("UserData").get(i)).getString("investment_product"));
                            entity.setInvestmentImage(response.getJSONArray("UserData").getJSONObject(i).getString("icon_url"));
                            investmentEntityArrayList.add(entity);
                        }

                        InvestmentServiceListAdapter adapter = new InvestmentServiceListAdapter(getContext(), investmentEntityArrayList, applyFor);
                        investmentTypeList.setAdapter(adapter);

                        adapter.setOnClick(new InvestmentServiceListAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, InvestmentDetailsFragment.newInstance(investmentEntityArrayList.get(position)));
                                    transaction.addToBackStack(null).commit();

                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar("Falied to load Investment data !!!");
                        applyForList.setVisibility(View.VISIBLE);
                        investmentTypeLoader.setVisibility(View.GONE);
                        investmentContent.setText(defaultContent);
                    }
                },
                error -> {
                    investmentTypeLoader.setVisibility(View.GONE);
                    applyForList.setVisibility(View.VISIBLE);
                    showSnackBar("Falied to load Investment data !!!");
                    investmentContent.setText(defaultContent);

                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        if (getContext() != null) {
            RequestQueue dataObjectRequest = Volley.newRequestQueue(getContext());
            dataObjectRequest.add(dataRequest);
        }
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
