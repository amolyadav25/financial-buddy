package com.antworksmoney.financialbuddy.views.fragments.Wallet;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.WalletEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.TransactionListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.ResponseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class MyWalletFragment extends Fragment {
    public MyWalletFragment() {
        // Required empty public constructor
    }


    private Toolbar topToolBar;

    private LinearLayout transferToBankLayout;

    private NestedScrollView bottomSheet;

    private ImageView closeButton;

    private TextView walletAmount;

    private ProgressBar walletBalanceLoader;

    private SharedPreferences pref;

    private static final String TAG = "MyWalletFragment";

    private RecyclerView transactionHistoryList;

    private RequestQueue requestQueue;

    private BottomSheetBehavior bottomSheetBehavior;

    private Button tryAgainPermissions;

    private EditText countDownTimer;

    private ProgressBar resetPasswordProgressBar;


    public static MyWalletFragment newInstance() {
        return new MyWalletFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_wallet, container, false);

        topToolBar = rootView.findViewById(R.id.toolbar);

        transferToBankLayout = rootView.findViewById(R.id.transferToBankLayout);

        walletAmount = rootView.findViewById(R.id.walletAmount);

        walletBalanceLoader = rootView.findViewById(R.id.amountLoaderProgressBar);

        transactionHistoryList = rootView.findViewById(R.id.TransactionHistoryList);

        bottomSheet = rootView.findViewById(R.id.bottom_sheet);

        requestQueue = Volley.newRequestQueue(getContext());

        pref = getActivity().getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        if (topToolBar != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            assert getActivity() != null;


            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            topToolBar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));

        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(0);

        transactionHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionHistoryList.setHasFixedSize(true);

        closeButton = rootView.findViewById(R.id.closeButton);

        tryAgainPermissions = rootView.findViewById(R.id.tryAgainPermissions);

        resetPasswordProgressBar = rootView.findViewById(R.id.resetPasswordProgressBar);

        countDownTimer = rootView.findViewById(R.id.countDownTimer);

        transferToBankLayout.setOnClickListener(view -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        closeButton.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        tryAgainPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!countDownTimer.getText().toString().trim().equalsIgnoreCase("")){
                    showAlertDialog();
                }
                else {
                    Toast.makeText(getContext(), "Please enter valid amount !!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loadWalletBalance();

        return rootView;
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Do you want to transfer ₹ "+
                countDownTimer.getText().toString().trim() +
                " to your registered number. i.e. " + pref.getString("user_phone",""));
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_check_circle_grey_50dp);
        builder1.setTitle("Confirm Transfer");
        builder1.setPositiveButton("Yes", (dialog, id) -> {
            if (Integer.parseInt(walletAmount.getText().toString().trim())<Integer.parseInt(countDownTimer.getText().toString().trim())){
                Toast.makeText(getContext(),"Your wallet doesn't contain requested payout amount !!", Toast.LENGTH_LONG).show();
            }
            else {
                transferMoneyToPayTmWallet();
            }
            dialog.cancel();
        });

        builder1.setNegativeButton("NO", (dialog, id) -> {
            dialog.cancel();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void transferMoneyToPayTmWallet() {
        try {

            tryAgainPermissions.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgrounddisabled));

            resetPasswordProgressBar.setVisibility(View.VISIBLE);

            JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "fund_transfer",
                    new JSONObject().put("mobile",pref.getString("user_phone",""))
                                    .put("amount_to_paid",countDownTimer.getText().toString().trim())
                                    .put("payeeEmailId", pref.getString("email_value","")),
                    response -> {

                        Log.e(TAG, response.toString());

                        resetPasswordProgressBar.setVisibility(View.GONE);

                        tryAgainPermissions.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                        try {
                            response.put("amount",countDownTimer.getText().toString().trim());
                            response.put("dateTime", Calendar.getInstance().getTime());
                            response.put("accountNumber", pref.getString("user_phone",""));

                            Intent intent = new Intent(getContext(), ResponseActivity.class);
                            intent.putExtra("Data",response.toString());
                            getActivity().startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    },
                    error -> {

                        Log.e(TAG, error.toString());

                        Toast.makeText(getContext(), "Please try after some time !!", Toast.LENGTH_SHORT).show();

                        resetPasswordProgressBar.setVisibility(View.GONE);

                        tryAgainPermissions.setBackground(getContext().getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                    });

            dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataObjectRequest.setShouldCache(false);

            requestQueue.add(dataObjectRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadWalletBalance() {

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("userData", new JSONObject()
                    .put("contact", pref.getString("user_phone", "")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        walletBalanceLoader.setVisibility(View.VISIBLE);

        walletAmount.setVisibility(View.GONE);

        JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "my_payout",
                dataObject,
                response -> {

                    walletBalanceLoader.setVisibility(View.GONE);

                    walletAmount.setVisibility(View.VISIBLE);

                    try {

                        JSONArray dataArray = response.getJSONObject("respone").getJSONArray("UserData");

                        int amount = 0;

                        for (int i = 0; i < dataArray.length(); i++) {
                            for (int j = 0; j < dataArray.getJSONObject(i).getJSONArray("Payout").length(); j++) {
                                JSONObject payoutObject = dataArray.getJSONObject(i).getJSONArray("Payout").getJSONObject(j);
                                amount = amount + Integer.parseInt(payoutObject.getString("amount"));
                            }
                        }

                        walletAmount.setText(MessageFormat.format("{0} ", amount));

                        ArrayList<WalletEntity> walletEntityArrayList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            WalletEntity entity = new WalletEntity();
                            entity.setLoanAmount(dataArray.getJSONObject(i).getString("Loan Amount"));
                            entity.setLoanType(dataArray.getJSONObject(i).getString("Loan Type"));
                            entity.setBorrowerName(dataArray.getJSONObject(i).getString("Borrower Name"));

                            for (int j = 0; j < dataArray.getJSONObject(i).getJSONArray("Payout").length(); j++) {
                                JSONObject payoutObject = dataArray.getJSONObject(i).getJSONArray("Payout").getJSONObject(j);
                                entity.setAmount(payoutObject.getString("amount"));
                                entity.setApproved(payoutObject.getString("approved"));
                                entity.setPayoutId(payoutObject.getString("payout_id"));
                                entity.setDisburseLoanAmount(payoutObject.getString("disburse_loan_amount"));
                                entity.setPayoutPercentage(payoutObject.getString("payout_percentage"));
                                entity.setPayoutType(payoutObject.getString("payout_type"));
                                entity.setStatus(payoutObject.getString("status"));
                                entity.setLeadId(payoutObject.getString("lead_id"));
                                entity.setCreatedDate(payoutObject.getString("created_date"));
                            }
                            walletEntityArrayList.add(entity);

                            TransactionListAdapter adapter = new TransactionListAdapter(getContext(), walletEntityArrayList, transactionHistoryList);
                            transactionHistoryList.setAdapter(adapter);

                            adapter.setOnClick(position -> {
                                if (getActivity() != null) {
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.homeParent, TransactionDetailsFragment.newInstance(walletEntityArrayList.get(position)));
                                    transaction.addToBackStack(null).commit();
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        walletAmount.setText(" 0");
                    }
                },
                error -> {

                    walletBalanceLoader.setVisibility(View.GONE);

                    walletAmount.setVisibility(View.VISIBLE);

                    walletAmount.setText(" 0");
                }
        );


        dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(dataObjectRequest);
    }



}
