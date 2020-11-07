package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class LBEstimatedTimeFragment extends Fragment {

    public LBEstimatedTimeFragment() {
        // Required empty public constructor
    }


    public static LBEstimatedTimeFragment newInstance() {
        return new LBEstimatedTimeFragment();
    }

    private Toolbar mToolbar;

    private Button checkStatusButton;

//    private CountDownTimer timer;

    private SharedPreferences mSharedPreferences;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBEstimatedTimeFragment";

    private RequestQueue mRequestQueue;

    private Timer mTimer;

    private CoordinatorLayout snackBarView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_estimated_time, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        checkStatusButton = rootView.findViewById(R.id.checkStatusButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mRequestQueue = Volley.newRequestQueue(getContext());

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("20")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "19");
            editor.apply();
        }

        loadEstimatedTimeLeft();


        return rootView;
    }

    private void loadEstimatedTimeLeft() {
        try {
            progress_bar.setVisibility(View.VISIBLE);

            Map<String, String> params = new HashMap<>();
            params.put("bid_registration_id", new JSONObject(mSharedPreferences.getString(
                    AppConstant.LOAN_DETAILS, "")).getString("bid_registration_id"));

            Log.e(TAG, params.toString());


            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/estimatedTime",
                    new JSONObject(params),
                    response -> {

                        progress_bar.setVisibility(View.GONE);

                        Log.e(TAG, response.toString());

                        try {

                            checkStatusButton.setText(response.getString("estimated_time"));

                            mTimer = new Timer();
                            mTimer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    moveToNextPage();
                                }
                            }, 2000, 2000);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void moveToNextPage() {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("bid_registration_id", new JSONObject(mSharedPreferences.getString(
                    AppConstant.LOAN_DETAILS, "")).getString("bid_registration_id"));

            Log.e(TAG, params.toString());


            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/estimatedTime",
                    new JSONObject(params),
                    response -> {

                        Log.e(TAG, response.toString());

                        try {

                            checkStatusButton.setText(response.getString("estimated_time"));

                            if (response.getString("is_disburse").trim().equalsIgnoreCase("1")) {
                                showSnackBar("Loan Disbursed !!", R.color.green);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (mSharedPreferences.getString(AppConstant.SELECTED_LOAN_TYPE, "").equalsIgnoreCase("0")) {
                        transaction.replace(R.id.homeParent, LBSaleSuccessfulFragment.newInstance());
                    }
                    else {
                        transaction.replace(R.id.homeParent, LBAccountFragment.newInstance());
                    }

                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimer.cancel();
    }


}
