package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.adapters.CreditCounsellingListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;

import java.util.ArrayList;

public class CreditCounsellingFragment extends Fragment {


    public CreditCounsellingFragment() {
        // Required empty public constructor
    }


    public static CreditCounsellingFragment newInstance() {
        return new CreditCounsellingFragment();
    }

    private TextView howItWorksText;

    private Toolbar mToolbar;

    private RelativeLayout progress_bar;

    private TextView package1Name, package1Cost, package2Name, package2Cost, package3Name, package3Cost;

    private RecyclerView package1List, package2List, package3List;

    private Button package1BuyNowButton, package2BuyNowButton, package3BuyNowButton;

    private static final String TAG = "CreditCounsellingFragme";

    private RequestQueue mRequestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_credit_counsellling, container, false);

        howItWorksText = rootView.findViewById(R.id.howItWorksText);

        mToolbar = rootView.findViewById(R.id.top_toolBar);

        package1Name = rootView.findViewById(R.id.package1Name);

        package1Cost = rootView.findViewById(R.id.package1Cost);

        package2Name = rootView.findViewById(R.id.package2Name);

        package2Cost = rootView.findViewById(R.id.package2Cost);

        package3Name = rootView.findViewById(R.id.package3Name);

        package3Cost = rootView.findViewById(R.id.package3Cost);

        package1List = rootView.findViewById(R.id.package1List);
        package1List.setLayoutManager(new LinearLayoutManager(getContext()));
        package1List.setHasFixedSize(true);

        package2List = rootView.findViewById(R.id.package2List);
        package2List.setLayoutManager(new LinearLayoutManager(getContext()));
        package2List.setHasFixedSize(true);

        package3List = rootView.findViewById(R.id.package3List);
        package3List.setLayoutManager(new LinearLayoutManager(getContext()));
        package3List.setHasFixedSize(true);

        package1BuyNowButton = rootView.findViewById(R.id.package1BuyNowButton);

        package2BuyNowButton = rootView.findViewById(R.id.package2BuyNowButton);

        package3BuyNowButton = rootView.findViewById(R.id.package3BuyNowButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mRequestQueue = Volley.newRequestQueue(getContext());

        customTextView(howItWorksText);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        package1List.setNestedScrollingEnabled(false);
        package2List.setNestedScrollingEnabled(false);
        package3List.setNestedScrollingEnabled(false);

        loadDataFromWeb();

        return rootView;
    }

    private void customTextView(TextView view) {

        view.setText(Html.fromHtml("How It" + "<font color=#B71E3B> Works </font>"));
    }

    private void loadDataFromWeb() {

        progress_bar.setVisibility(View.VISIBLE);

        JsonArrayRequest dataRequest = new JsonArrayRequest(
                Request.Method.GET,
                AppConstant.BaseUrl + "creditcounselling/getPlan",
                null,
                response -> {
                    progress_bar.setVisibility(View.GONE);

                    try {

                        package1Name.setText(response.getJSONObject(0).getString("package_name"));
                        package2Name.setText(response.getJSONObject(1).getString("package_name"));
                        package3Name.setText(response.getJSONObject(2).getString("package_name"));

                        package1Cost.setText(response.getJSONObject(0).getString("package_fee"));
                        package2Cost.setText(response.getJSONObject(1).getString("package_fee"));
                        package3Cost.setText(response.getJSONObject(2).getString("package_fee"));

                        package1BuyNowButton.setOnClickListener(view -> {
                            try {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(
                                            response.getJSONObject(0).getString("package_url"), "Credit Counselling"));
                                    transaction.addToBackStack(null).commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        package2BuyNowButton.setOnClickListener(view -> {
                            try {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(
                                            response.getJSONObject(1).getString("package_url"), "Credit Counselling"));
                                    transaction.addToBackStack(null).commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });

                        package3BuyNowButton.setOnClickListener(view -> {
                            try {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(
                                            response.getJSONObject(2).getString("package_url"), "Credit Counselling"));
                                    transaction.addToBackStack(null).commit();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        ArrayList<String> package1ArrayList = new ArrayList<>();
                        for (int i=0; i<response.getJSONObject(0).getJSONArray("package_content").length(); i++){
                            package1ArrayList.add("☑\t" +response.getJSONObject(0).getJSONArray("package_content").getString(i));
                        }
                        package1List.setAdapter(new CreditCounsellingListAdapter(getContext(),package1ArrayList, package1List));


                        ArrayList<String> package2ArrayList = new ArrayList<>();
                        for (int i=0; i<response.getJSONObject(1).getJSONArray("package_content").length(); i++){
                            package2ArrayList.add("☑\t" +response.getJSONObject(1).getJSONArray("package_content").getString(i));
                        }
                        package2List.setAdapter(new CreditCounsellingListAdapter(getContext(),package2ArrayList, package2List));


                        ArrayList<String> package3ArrayList = new ArrayList<>();
                        for (int i=0; i<response.getJSONObject(2).getJSONArray("package_content").length(); i++){
                            package3ArrayList.add("☑\t" +response.getJSONObject(2).getJSONArray("package_content").getString(i));
                        }
                        package3List.setAdapter(new CreditCounsellingListAdapter(getContext(),package3ArrayList, package3List));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    progress_bar.setVisibility(View.GONE);
                    Log.w(TAG, error.toString());
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);

    }


}
