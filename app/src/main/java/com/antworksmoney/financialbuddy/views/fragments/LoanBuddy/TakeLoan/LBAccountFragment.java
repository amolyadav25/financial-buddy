package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import uk.me.hardill.volley.multipart.MultipartRequest;


public class LBAccountFragment extends Fragment {

    public LBAccountFragment() {
        // Required empty public constructor
    }


    public static LBAccountFragment newInstance() {
        return new LBAccountFragment();
    }

    private TextView selectDateTextView;

    private EditText et_invoice_number, et_invoice_amount;

    private Button submit_button;

    private RelativeLayout progress_bar;

    private static final String TAG = "LBAccountFragment";

    private SharedPreferences mSharedPreferences;

    private Toolbar mToolbar;

    private CoordinatorLayout snackBarView;

    private String testJson = "{\n" +
            "  \"bid_registration_id\":\"29\"\n" +
            "}";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbaccount, container, false);

        selectDateTextView = rootView.findViewById(R.id.selectDateTextView);

        et_invoice_amount = rootView.findViewById(R.id.et_invoice_amount);

        et_invoice_number = rootView.findViewById(R.id.et_invoice_number);

        submit_button = rootView.findViewById(R.id.submit_button);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        selectDateTextView.setOnClickListener(view -> DateDialog());

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("21")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "20");
            editor.apply();
        }

        submit_button.setOnClickListener(view -> {
            if (selectDateTextView.getText().toString().trim().equalsIgnoreCase("")) {
                selectDateTextView.requestFocus();
                selectDateTextView.setError("Invalid Date");
            } else if (et_invoice_number.getText().toString().trim().equalsIgnoreCase("")) {
                et_invoice_number.setError("Invalid Invoice Number");
                et_invoice_number.requestFocus();
            } else if (et_invoice_amount.getText().toString().trim().equalsIgnoreCase("")) {
                et_invoice_amount.setError("Invalid Amount !!");
                et_invoice_amount.requestFocus();
            } else {
                uploadInvoice();
            }
        });

        return rootView;
    }


    private void DateDialog() {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mMonth, mDay, year;

        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);
        year = dateCalender.get(Calendar.YEAR);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(getContext());
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(year, mMonth, mDay);
        datePickerDialogBuilder.maxDate(year, mMonth, mDay);
        datePickerDialogBuilder.minDate(year - 1, 0, 1);
        datePickerDialogBuilder.callback((view, year1, monthOfYear, dayOfMonth) -> {
            if (String.valueOf(year1).trim().contains(",")) {
                year1 = Integer.parseInt(String.valueOf(year1).replace(",", "").trim());
            }
            selectDateTextView.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        });

        datePickerDialogBuilder.build().show();
    }

    private void uploadInvoice() {

        progress_bar.setVisibility(View.VISIBLE);

        try {

            Map<String, String> params = new HashMap<>();
            params.put("date_of_invoice", selectDateTextView.getText().toString().trim());
            params.put("invoice_no", et_invoice_number.getText().toString().trim());
            params.put("amount", et_invoice_amount.getText().toString().trim());

            Log.e(TAG, params.toString());


            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.borrowerBaseUrl + "borrowerloan/uploadInvoice",
                    new JSONObject(params),
                    response -> {

                        Log.e(TAG, response.toString());

                        progress_bar.setVisibility(View.GONE);

                        try {

                            if (response.getString("msg").trim().equalsIgnoreCase("Invoice Added Successfully")) {
                                showSnackBar("Invoice added successfully !!", R.color.green);
                            } else {
                                showSnackBar("Failed to add invoice !!", R.color.red);
                            }

                        } catch (Exception e) {

                            showSnackBar("Failed to add invoice !!", R.color.red);

                            e.printStackTrace();
                        }

                    }, error -> {
                Log.e(TAG, error.toString());
                showSnackBar("Failed to add invoice !!", R.color.red);
                progress_bar.setVisibility(View.GONE);
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                    params.put("Content-Type", "application/json");
                    Log.e(TAG, params.toString());
                    return params;
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getContext()).add(request);

        } catch (Exception e) {
            progress_bar.setVisibility(View.GONE);
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
                if (getActivity() != null && (backgroundColor == R.color.green)) {

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (mSharedPreferences.getString(AppConstant.SELECTED_LOAN_TYPE, "").equalsIgnoreCase("0")) {
                        transaction.replace(R.id.homeParent, LBSaleSuccessfulFragment.newInstance());
                    }
                    else {
                        transaction.replace(R.id.homeParent, LBSaleStatusFragment.newInstance());
                    }
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}
