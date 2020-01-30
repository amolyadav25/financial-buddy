package com.antworksmoney.financialbuddy.views.fragments.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.UserAuthActivity;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private SwitchButton allNotificationCheckButton,
            myContactsCheckButton,
            consistentTopBarCheckButton;

    private ImageView saveButton;

    private LoginButton loginButton;

    private CallbackManager mCallbackManager;

    private RelativeLayout googleSignInLayout,
            facebookSignInLayout,
            termsAndCondtionsLayout,
            privacyPolicyLayout,
            logoutLayout;

    private static final String TAG = "SettingsFragment";

    private TextView googleText, facebookText, fbText;

    private ImageView facebookUserImage;

    private SharedPreferences preferences;

    private TextView googleHeading;

    private ImageView googleImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        AppEventsLogger.activateApp(getActivity());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        allNotificationCheckButton = rootView.findViewById(R.id.allNotificationCheckButton);

        myContactsCheckButton = rootView.findViewById(R.id.myContactsCheckButton);

        consistentTopBarCheckButton = rootView.findViewById(R.id.consistentTopBarCheckButton);

        saveButton = rootView.findViewById(R.id.saveButton);

        loginButton = rootView.findViewById(R.id.loginButton);

        googleSignInLayout = rootView.findViewById(R.id.googleSignInLayout);

        facebookSignInLayout = rootView.findViewById(R.id.facebookSignInLayout);

        googleHeading = rootView.findViewById(R.id.googleHeading);

        termsAndCondtionsLayout = rootView.findViewById(R.id.termsAndCondtionsLayout);

        privacyPolicyLayout = rootView.findViewById(R.id.privacyPolicyLayout);

        loginButton.setFragment(this);

        mCallbackManager = CallbackManager.Factory.create();

        googleText = rootView.findViewById(R.id.googleText);

        facebookText = rootView.findViewById(R.id.facebookText);

        facebookUserImage = rootView.findViewById(R.id.facebookUserImage);

        fbText = rootView.findViewById(R.id.fbText);

        googleImage = rootView.findViewById(R.id.googleImage);

        logoutLayout = rootView.findViewById(R.id.logoutLayout);

        preferences = getContext().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        if (preferences.getString("connectedWithFb", "").trim().equalsIgnoreCase("1")) {
            Glide.with(getContext()).load(preferences.getString("user_image_url", ""))
                    .asBitmap()
                    .error(R.drawable.fb)
                    .into(facebookUserImage);
            fbText.setText("Already Connected !!");
            facebookText.setText(preferences.getString("fb_name", ""));
        }

        if (preferences.getString("connectedWithLinkedIn", "").trim().equalsIgnoreCase("1")) {
            Glide.with(getContext()).load(preferences.getString("user_image_url", ""))
                    .asBitmap()
                    .error(R.drawable.linkedinbutton)
                    .into(googleImage);
            googleHeading.setText("Already Connected !!");
            googleText.setText(preferences.getString("linkedIn_name", ""));

        }

        if (preferences.getString("offer","").trim().equalsIgnoreCase("Yes")){
            myContactsCheckButton.setChecked(true);
        }
        else {
            myContactsCheckButton.setChecked(false);
        }

        if (preferences.getString("contacts","").trim().equalsIgnoreCase("Yes")){
            consistentTopBarCheckButton.setChecked(true);
        }
        else {
            consistentTopBarCheckButton.setChecked(false);
        }

        try {
            PackageInfo info = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getClass().getSimpleName().trim().equalsIgnoreCase("HomeActivity")) {
                    ((HomeActivity) getActivity()).getmDrawerLayout().openDrawer(GravityCompat.START);
                }
            }
        });

        facebookSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        allNotificationCheckButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                if (!isChecked) {
                    myContactsCheckButton.setChecked(false);
                    consistentTopBarCheckButton.setChecked(false);

                    myContactsCheckButton.setEnabled(false);
                    consistentTopBarCheckButton.setEnabled(false);

                    editor.putString("all_notifications","No");

                } else {
                    myContactsCheckButton.setEnabled(true);
                    consistentTopBarCheckButton.setEnabled(true);
                    editor.putString("all_notifications","Yes");
                }

                editor.apply();
            }
        });

        myContactsCheckButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked){
                    editor.putString("offer","Yes");
                }
                else {
                    editor.putString("offer","No");
                }
                editor.apply();


            }
        });

        consistentTopBarCheckButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked){
                    editor.putString("contacts","Yes");
                }
                else {
                    editor.putString("contacts","No");
                }
                editor.apply();
            }
        });

        consistentTopBarCheckButton.setChecked(true);

        myContactsCheckButton.setChecked(true);


        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest mGraphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        (object, response) -> {
                            try {

                                Glide.with(getContext()).load("https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large")
                                        .asBitmap()
                                        .error(R.drawable.fb)
                                        .into(facebookUserImage);

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("connectedWithFb", "1");
                                editor.putString("user_image_url", "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");
                                editor.putString("fb_name", object.getString("first_name") + " " + object.getString("last_name"));
                                editor.apply();

                                fbText.setText("Connected Successfully !!");
                                facebookText.setText(
                                        MessageFormat.format("Name : {0} {1}\n{2}",
                                                object.getString("first_name"),
                                                object.getString("last_name"),
                                                object.getString("email")));

                            } catch (JSONException e) {
                                try {
                                    facebookText.setText(
                                            MessageFormat.format("Name : {0} {1}",
                                                    object.getString("first_name"),
                                                    object.getString("last_name")));
                                } catch (JSONException ignored) {
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name,last_name,email,id,picture.type(large)");
                mGraphRequest.setParameters(parameters);
                mGraphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("CANCEL", "CANCELED by user");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Exception", error.toString());
            }
        });

        googleSignInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLinkedInLogin();
            }
        });

        termsAndCondtionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(
                        "https://www.antworksmoney.com/financial_buddy/term_and_condition",
                        "Terms and Conditions"));
                transaction.addToBackStack(null).commit();
            }
        });

        privacyPolicyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance(
                        "https://www.antworksmoney.com/financial_buddy/privacy_and_policy",
                        "Privacy Policy"));
                transaction.addToBackStack(null).commit();
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogOutDialog();
            }
        });

        return rootView;
    }

    private void showLogOutDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Do you really want to logout from the app ?\nWarning : This will clear all the data from the app.");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.logout);
        builder1.setTitle("Log Out");
        builder1.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("first_run", "0.5");
            editor.putString("profileUpdate","0");
            editor.apply();

            Intent intent = new Intent(getActivity(), UserAuthActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        builder1.setNegativeButton("NO", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else {
            LISessionManager.getInstance(getContext()).onActivityResult(
                    getActivity(), requestCode, resultCode, data);
        }
    }


    private void handleLinkedInLogin() {
        LISessionManager.getInstance(getContext()).init(getActivity(), buildScope(), new AuthListener() {
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

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    private void fetchPersonInfo() {

        String url = "https://api.linkedin.com/v1/people/~:(" +
                "id,first-name,last-name,picture-url,email-address)";

        final APIHelper apiHelper = APIHelper.getInstance(getContext());
        apiHelper.getRequest(getContext(), url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                JSONObject dataObject = apiResponse.getResponseDataAsJson();
                try {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("connectedWithLinkedIn", "1");
                    editor.putString("linkedIn_name",
                            dataObject.getString("firstName") + " " +
                                    dataObject.getString("lastName"));
                    editor.putString("user_image_url", dataObject.getString("pictureUrl"));
                    editor.apply();

                    googleHeading.setText("Connected Successfully !!");
                    googleText.setText(dataObject.getString("firstName") + " " +
                            dataObject.getString("lastName"));
                    Glide.with(getContext()).load(dataObject.getString("pictureUrl"))
                            .asBitmap()
                            .error(R.drawable.linkedinbutton)
                            .into(googleImage);

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

}
