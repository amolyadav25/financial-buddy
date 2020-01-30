package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LBSignUpFragment extends Fragment implements View.OnClickListener {

    public LBSignUpFragment() {
        // Required empty public constructor
    }

    private static double mLatitude, mLongitude;

    public static LBSignUpFragment newInstance(Double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
        return new LBSignUpFragment();
    }

    private Toolbar mToolbar;

    private EditText et_reg_fname,
            et_user_mail,
            et_user_phone_number,
            et_user_password,
            et_user_confirm_pass,
            et_user_pan_card;

    private RelativeLayout  progress_bar;

    private Button ProceedButton;

    private SharedPreferences mSharedPreferences;

    private RequestQueue mRequestQueue;

    private static final String TAG = "LBSignUpFragment";

    private CoordinatorLayout snackBarView;

    private String tokenValue;

    private TextView tncCheckBoxView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_buddy_sign_up, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        tncCheckBoxView = rootView.findViewById(R.id.tncCheckBoxView);

        et_user_password = rootView.findViewById(R.id.et_user_password);

        et_user_confirm_pass = rootView.findViewById(R.id.et_user_confirm_pass);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        et_user_pan_card = rootView.findViewById(R.id.et_user_pan_card);

        mRequestQueue = Volley.newRequestQueue(getActivity());

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        if (!mSharedPreferences.getString("user_name", "").trim().equalsIgnoreCase("")) {
            et_reg_fname.setText(mSharedPreferences.getString("user_name", ""));
        }

        if (!mSharedPreferences.getString("email_value", "").trim().equalsIgnoreCase("")) {
            et_user_mail.setText(mSharedPreferences.getString("email_value", ""));
        }

        if (!mSharedPreferences.getString("user_phone", "").trim().equalsIgnoreCase("")) {
            et_user_phone_number.setText(mSharedPreferences.getString("user_phone", ""));
        }

        ProceedButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customTextView(tncCheckBoxView);

        fetchTokenWithoutLogin();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ProceedButton) {
            if (et_reg_fname.getText().toString().trim().equalsIgnoreCase("")) {
                et_reg_fname.setError("Invalid Name !!");
                et_reg_fname.requestFocus();
            } else if (!et_user_mail.getText().toString().trim().contains("@")) {
                et_user_mail.setError("Invalid Email Id !!");
                et_user_mail.requestFocus();
            } else if (et_user_phone_number.getText().toString().trim().length() < 10) {
                et_user_phone_number.setError("Invalid Phone Number !!");
                et_user_phone_number.requestFocus();
            } else if (et_user_pan_card.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_pan_card.setError("Invalid PAN !!");
                et_user_pan_card.requestFocus();
            } else if (et_user_password.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_password.setError("Invalid Password !!");
                et_user_password.requestFocus();
            } else if (!et_user_confirm_pass.getText().toString().trim().equalsIgnoreCase(et_user_password.getText().toString().trim())) {
                et_user_confirm_pass.setError("Passwords don't match !!");
                et_user_confirm_pass.requestFocus();
            } else {
                checkPhoneStatePermission();
            }
        }

    }


    private void fetchTokenWithoutLogin() {

        progress_bar.setVisibility(View.VISIBLE);

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "getauth/token",
                null,
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.i(TAG, response.toString());

                    try {

                        tokenValue = response.getString("token");


                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                },
                error -> {

                    Log.e(TAG, error.toString());

                    progress_bar.setVisibility(View.GONE);

                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);

    }

    private void checkPhoneStatePermission() {
        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(permissions, AppConstant.PERMISSION_READ_PHONE_STATE);
            } else {
                registerAsBorrower();
            }
        } else {
            registerAsBorrower();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.PERMISSION_READ_PHONE_STATE) {
            if (permissions[0].equals(Manifest.permission.READ_PHONE_STATE)) {
                registerAsBorrower();
            } else {
                showSnackBar("Can't proceed without phone state !! -> Application Settings", R.color.red);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void registerAsBorrower() {

        progress_bar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("name", et_reg_fname.getText().toString().trim());
        params.put("email", et_user_mail.getText().toString().trim());
        params.put("mobile", et_user_phone_number.getText().toString().trim());
        params.put("password", AppConstant.getPasswordHash(et_user_password.getText().toString().trim()));
        params.put("cpassword", AppConstant.getPasswordHash(et_user_confirm_pass.getText().toString().trim()));
        params.put(	"pan",et_user_pan_card.getText().toString().trim());
        params.put("imei_no", ((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
        params.put("mobile_token", FirebaseInstanceId.getInstance().getToken());
        params.put("latitude", String.valueOf(mLatitude));
        params.put("longitude", String.valueOf(mLongitude));
        params.put("term_and_condition", "1");

        Log.e(TAG, new JSONObject(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
               AppConstant.borrowerBaseUrl + "borrowerres/registration",
                new JSONObject(params),
                response -> {

            progress_bar.setVisibility(View.GONE);

            Log.e(TAG, response.toString());

            try {
                if (response.getString("status").trim().equalsIgnoreCase("1")){
                    showSnackBar("Borrower Registered Successfully !!", R.color.green);
                }
                else {
                    showSnackBar("Failed to register !!", R.color.red);
                }

            }catch (Exception e){
                e.printStackTrace();
                try {
                    showSnackBar(response.getString("error_msg"), R.color.red);
                }catch (Exception ignored){}

            }

        }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to register !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", tokenValue);
                params.put("Content-Type", "application/json");
                Log.e(TAG, params.toString());
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);

    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green){

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("LBMail", et_user_mail.getText().toString().trim());
                    editor.putString("loginSave","0.5");
                    editor.apply();

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBOTPVerificationFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

    private void customTextView(TextView view) {

        SpannableStringBuilder spanTxt = new SpannableStringBuilder("☑ I have read, understood and agree to the ");

        spanTxt.append("Terms of use");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                showTermsAndConditionsDialog("https://www.antworksmoney.com/financial_buddy/term_and_condition");
            }
        }, spanTxt.length() - "Terms of use".length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.BLUE), 43, spanTxt.length(), 0);

        spanTxt.append(" and ");

        spanTxt.append("Privacy Policy");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                showTermsAndConditionsDialog("https://www.antworksmoney.com/financial_buddy/privacy_and_policy");
            }
        }, spanTxt.length() - "Privacy Policy".length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.BLUE), 60, spanTxt.length(), 0);

        spanTxt.append(". I undertake not to borrow more then 10 Lakhs on all Peer to Peer (P2P) Platform.");

        view.setMovementMethod(LinkMovementMethod.getInstance());

        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    private void showTermsAndConditionsDialog(String url) {
        AlertDialog dialogTermsNConditions;

        View viewTermsAndConditions;

        AlertDialog.Builder builderTermsNConditions;

        viewTermsAndConditions = LayoutInflater.from(getContext()).inflate(R.layout.layout_terms_and_conditions, null);

        builderTermsNConditions = new AlertDialog.Builder(getContext());

        builderTermsNConditions.setView(viewTermsAndConditions);

        dialogTermsNConditions = builderTermsNConditions.create();
        if (dialogTermsNConditions.getWindow() != null) {
            dialogTermsNConditions.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogTermsNConditions.setCancelable(true);

        ImageView closeButton = viewTermsAndConditions.findViewById(R.id.closeButton);

        ProgressBar webViewDataLoaderProgressBar = viewTermsAndConditions.findViewById(R.id.webViewDataLoaderProgressBar);

        WebView dataLoaderView = viewTermsAndConditions.findViewById(R.id.dataLoaderView);

        WebSettings webSettings = dataLoaderView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        dataLoaderView.setWebViewClient(new MyWebViewClient(webViewDataLoaderProgressBar));
        dataLoaderView.loadUrl(url);

        closeButton.setOnClickListener(v -> dialogTermsNConditions.dismiss());

        dialogTermsNConditions.show();
    }

    private class MyWebViewClient extends WebViewClient {
        ProgressBar webViewDataLoaderProgressBar;

        MyWebViewClient(ProgressBar mWebViewDataLoaderProgressBar) {
            webViewDataLoaderProgressBar = mWebViewDataLoaderProgressBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
            } else {
                view.loadUrl(url);
                webViewDataLoaderProgressBar.setVisibility(View.VISIBLE);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webViewDataLoaderProgressBar.setVisibility(View.GONE);
        }
    }





}
