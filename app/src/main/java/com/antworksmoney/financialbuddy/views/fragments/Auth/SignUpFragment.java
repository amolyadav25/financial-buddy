package com.antworksmoney.financialbuddy.views.fragments.Auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.helpers.messaging.SendOtpToUser;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignUpFragment extends Fragment implements View.OnClickListener {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    private ImageView signInWithLinkedIn;

    private Context mContext;

    private Activity mActivity;

    private static final String TAG = "SignUpFragment";

    private SharedPreferences preferences;

    private LoginButton signInWithFacebook;

    private CallbackManager mCallbackManager;

    private EditText et_reg_fname, etUser_Phone, etMail;

    private Button user_button_login, saveEmailButton;

    private CoordinatorLayout snackBarView;

    private ProgressBar progressHud;

    private static final int PERMISSION_READ_SMS = 100;

    private AlertDialog dialogSocialsites;

    private View viewSocialSites;

    private AlertDialog.Builder builderSocialSites;

    private CheckBox termsAndCondtionsCheckBox;

    private ImageView signInWithFb;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getActivity());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        signInWithLinkedIn = rootView.findViewById(R.id.signInWithLinkedIn);

        mActivity = getActivity();

        mContext = getContext();

        preferences = mContext.getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        signInWithFacebook = rootView.findViewById(R.id.signInWithFacebook);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        etUser_Phone = rootView.findViewById(R.id.etUser_Phone);

        user_button_login = rootView.findViewById(R.id.user_button_login);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progressHud = rootView.findViewById(R.id.progressHud);

        termsAndCondtionsCheckBox = rootView.findViewById(R.id.termsAndCondtionsCheckBox);

        signInWithFb = rootView.findViewById(R.id.signInWithFb);

        mCallbackManager = CallbackManager.Factory.create();

        signInWithFacebook.setFragment(this);

        viewSocialSites = LayoutInflater.from(mActivity).inflate(R.layout.layout_add_additional_info, null);

        builderSocialSites = new AlertDialog.Builder(mActivity);

        builderSocialSites.setView(viewSocialSites);

        dialogSocialsites = builderSocialSites.create();
        if (dialogSocialsites.getWindow() != null) {
            dialogSocialsites.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogSocialsites.setCancelable(false);

        etMail = viewSocialSites.findViewById(R.id.etMail);

        saveEmailButton = viewSocialSites.findViewById(R.id.saveEmailButton);


        signInWithFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest mGraphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {

                            Log.e(TAG, response.toString());

                            SharedPreferences.Editor editor = preferences.edit();

                            try {

                                editor.putString("user_name",
                                        object.getString("first_name") + " " +
                                                object.getString("last_name"));

                                editor.putString("email_value", object.getString("email"));

                                editor.putString("userImageUrl",
                                        "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");


                            } catch (Exception e) {
                                try {

                                    editor.putString("user_name",
                                            object.getString("first_name") + " " +
                                                    object.getString("last_name"));

                                    editor.putString("user_image_url",
                                            "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");

                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                }
                            }

                            editor.apply();


                            if (preferences.getString("email_value", "").trim().equalsIgnoreCase("")) {
                                dialogSocialsites.show();
                            } else {
                                progressHud.setVisibility(View.VISIBLE);
                                user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                                callUserRegistrationApi("faceBook");
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id,picture.type(large)");
                mGraphRequest.setParameters(parameters);
                mGraphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "CANCELED by user");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Exception", error.toString());
            }
        });

        termsAndCondtionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                } else {
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                }
            }
        });

        customTextView(termsAndCondtionsCheckBox);

        termsAndCondtionsCheckBox.setChecked(true);

        saveEmailButton.setOnClickListener(this);

        signInWithLinkedIn.setOnClickListener(this);

        user_button_login.setOnClickListener(this);

        signInWithFb.setOnClickListener(this);

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.signInWithLinkedIn:
                handleLinkedInLogin();
                break;

            case R.id.user_button_login:
                if (termsAndCondtionsCheckBox.isChecked()) {
                    if (et_reg_fname.getText().toString().trim().equalsIgnoreCase("")) {
                        showSnackBar("Invalid name !!!!");
                        et_reg_fname.requestFocus();
                    } else if (etUser_Phone.getText().toString().trim().length() != 10) {
                        showSnackBar("Invalid number !!!!");
                        etUser_Phone.requestFocus();
                    } else {
                        progressHud.setVisibility(View.VISIBLE);
                        user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                        new SendOtpToUser(
                                etUser_Phone.getText().toString().trim(),
                                et_reg_fname.getText().toString(), progressHud,
                                user_button_login, snackBarView, getContext());
                    }
                } else {
                    showSnackBar("Please read and accept Terms and Conditions !!");
                }
                break;

            case R.id.saveEmailButton:
                if (etMail.getText().toString().trim().equalsIgnoreCase("")) {
                    showSnackBar("Invalid Mail id !!!!");
                    et_reg_fname.requestFocus();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email_value", etMail.getText().toString().trim());
                    editor.apply();

                    dialogSocialsites.dismiss();
                    progressHud.setVisibility(View.VISIBLE);
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));

                    callUserRegistrationApi("facebook");
                }

                break;

            case R.id.signInWithFb:
                signInWithFacebook.performClick();
                break;

        }

    }

    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }


    private void handleLinkedInLogin() {
        LISessionManager.getInstance(mContext).init(mActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                fetchPersonInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                Log.e(TAG, error.toString());
            }
        }, true);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        Log.e(TAG, "Permission granted");
//
//
//        if (permissions[0].equals(Manifest.permission.RECEIVE_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//
//            fetchValidVerificationRequest();
//
////            new SendOtpToUser(
////                    etUser_Phone.getText().toString().trim(),
////                    et_reg_fname.getText().toString(), progressHud,
////                    user_button_login, snackBarView, getContext());
//        } else {
//
//            progressHud.setVisibility(View.VISIBLE);
//            user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
//
////            new SendOtpToUser(
////                    etUser_Phone.getText().toString().trim(),
////                    et_reg_fname.getText().toString(), progressHud,
////                    user_button_login, snackBarView, getContext());
//
//            fetchValidVerificationRequest();
//        }
//    }

//    private void fetchValidVerificationRequest() {
//
//        progressHud.setVisibility(View.VISIBLE);
//        user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
//
//        JSONObject dataObject = null;
//        try {
//            dataObject = new JSONObject().put("androidId", String.valueOf(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Log.e(TAG,dataObject.toString());
//
//        JsonObjectRequest dataRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                "http://estateahead.com/uphar_finvest/api/login-wtih-fbuddy",
//                dataObject,
//                response -> {
//                    progressHud.setVisibility(View.GONE);
//                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//
//                    try {
//                        Log.e(TAG,response.toString());
//                        if (response.getString("status").equalsIgnoreCase("success")){
//                            new SendOtpToUser(
//                                    etUser_Phone.getText().toString().trim(),
//                                    et_reg_fname.getText().toString(), progressHud,
//                                    user_button_login, snackBarView, getContext());
//                        }else {
//                            showSnackBar("Failed to login !!!");
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        showSnackBar("Failed to login !!!");
//                    }
//
//                },
//                error -> {
//                    progressHud.setVisibility(View.GONE);
//                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
//                    showSnackBar("Failed to login !!!");
//                });
//
//        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
//                AppConstant.MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        dataRequest.setShouldCache(false);
//
//        RequestQueue queue = Volley.newRequestQueue(mContext);
//        queue.add(dataRequest);
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            LISessionManager.getInstance(mContext).onActivityResult(
                    mActivity, requestCode, resultCode, data);
        }
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS, Scope.RW_COMPANY_ADMIN);
    }

    private void fetchPersonInfo() {

        String url = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";

        final APIHelper apiHelper = APIHelper.getInstance(mContext);
        apiHelper.getRequest(mContext, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject dataObject = apiResponse.getResponseDataAsJson();
                try {

                    Log.e(TAG, dataObject.toString());

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("user_name",
                            dataObject.getString("firstName") + " " +
                                    dataObject.getString("lastName"));

                    editor.putString("email_value", dataObject.getString("emailAddress"));

                    editor.putString("user_image_url", dataObject.getString("pictureUrl"));

                    editor.apply();

                    progressHud.setVisibility(View.VISIBLE);
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));

                    callUserRegistrationApi("linkedIn");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                Log.e(TAG, liApiError.toString());
            }
        });
    }


    private void callUserRegistrationApi(String loginMethod) {
        try {

            Log.e("Token", FirebaseInstanceId.getInstance().getToken());

            JSONObject innerObject = new JSONObject();
            innerObject.put("name", preferences.getString("user_name", ""));
            innerObject.put("email", preferences.getString("email_value", ""));
            innerObject.put("contact", "");
            innerObject.put("login_method", loginMethod);
            innerObject.put("userType","0");
            innerObject.put("token", FirebaseInstanceId.getInstance().getToken());

            JSONObject outerObject = new JSONObject();
            outerObject.put("userData", innerObject);

            Log.e(TAG, outerObject.toString());

            JsonObjectRequest userVerifyRequest = new JsonObjectRequest(Request.Method.POST,
                    AppConstant.BaseUrl + "verify-user", outerObject, response -> {
                try {

                    Log.e("data", response.toString());

                    progressHud.setVisibility(View.VISIBLE);
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));

                    JSONObject userDetails = response.getJSONObject("respone");
                    SharedPreferences.Editor editor = preferences.edit();
                    if (userDetails.getString("status").trim().equalsIgnoreCase("1")) {
                        editor.putString("user_name", userDetails.getString("name"));
                        editor.putString("user_phone", userDetails.getString("mobile"));
//                    editor.putString("email_value", userDetails.getString("email"));
//                    editor.putString("user_image_url", userDetails.getString("profile_image_url"));
//                    editor.putString("user_donor_name", userDetails.getString("blood_group_donor"));
//                    editor.putString("user_donor_number", userDetails.getString("contact_number_of_donor"));
//                    editor.putString("dateOfBirth", userDetails.getString("dob"));
//                    editor.putString("medicalConditions", userDetails.getString("medical_conditions"));
//                    editor.putString("otherNotes", userDetails.getString("other_notes"));
//                    editor.putString("allergiesAndReaction", userDetails.getString("allergy"));
//                    editor.putString("heightofperson", userDetails.getString("height"));
//                    editor.putString("weightofperson", userDetails.getString("weight"));
//                    editor.putString("familyDoctorName", userDetails.getString("family_doctor_name"));
//                    editor.putString("volunteer", userDetails.getString("volanteer"));
//                    editor.putString("gender", userDetails.getString("gender"));

//                    if (isValid(userDetails.getString("profile_image_url"))) {
//                        editor.putString("user_image_url", userDetails.getString("profile_image_url"));
//
//                    }

//                    dataBaseObject.deleteContactsFromTable();

//                    if (!userDetails.getString("sos_contact").trim().equalsIgnoreCase("null")) {
//                        JSONArray dataArray = new JSONArray(userDetails.getString("sos_contact"));
//                        for (int i = 0; i < dataArray.length(); i++) {
//                            dataBaseObject.insertContactNumber(
//                                    ((JSONObject) dataArray.get(i)).getString("number"),
//                                    ((JSONObject) dataArray.get(i)).getString("name"));
//                        }
//                    }


                    }
//                else {
//                    editor.putString("user_name", userDetails.getString("First Name"));
//                    editor.putString("user_phone", userDetails.getString("Phone Number"));
//                }

                    editor.apply();

                    progressHud.setVisibility(View.GONE);
                    user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                    FragmentTransaction transaction = ((FragmentActivity) mContext)
                            .getSupportFragmentManager()
                            .beginTransaction();

                    transaction.replace(R.id.baseParentForAuthentication, ProfileUpdateFragment.newInstance(R.id.baseParentForAuthentication));
                    transaction.commit();

                } catch (Exception e) {
                    e.printStackTrace();
                    showSnackBar("Some error occured !! Please try again later.");
                }
            },
                    error ->
                    {
                        progressHud.setVisibility(View.GONE);
                        user_button_login.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                        Log.e(TAG, error.toString());
                        showSnackBar("Some error occured !! Please try again later.");
                    }
            );

            userVerifyRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(userVerifyRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showTermsAndConditionsDialog(String url) {
        AlertDialog dialogTermsNConditions;

        View viewTermsAndConditions;

        AlertDialog.Builder builderTermsNConditions;

        viewTermsAndConditions = LayoutInflater.from(mContext).inflate(R.layout.layout_terms_and_conditions, null);

        builderTermsNConditions = new AlertDialog.Builder(mContext);

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

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTermsNConditions.dismiss();
            }
        });

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


    private void customTextView(CheckBox view) {

        SpannableStringBuilder spanTxt = new SpannableStringBuilder("By continuing, I agree to ");

        spanTxt.append("Terms of use");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NotNull View widget) {
                showTermsAndConditionsDialog("https://www.antworksmoney.com/financial_buddy/term_and_condition");
            }
        }, spanTxt.length() - "Terms of use".length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.BLUE), 26, spanTxt.length(), 0);

        spanTxt.append(" and ");

        spanTxt.append("Privacy Policy");

        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showTermsAndConditionsDialog("https://www.antworksmoney.com/financial_buddy/privacy_and_policy");
            }
        }, spanTxt.length() - "Privacy Policy".length(), spanTxt.length(), 0);

        spanTxt.setSpan(new ForegroundColorSpan(Color.BLUE), 43, spanTxt.length(), 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());

        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }



}
