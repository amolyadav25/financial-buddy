package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.ChangeRequests.LBChangePasswordSendOtpFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBDashBoardFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant.REQUEST_CHECK_SETTINGS_ATT;

public class LBLoginFragment extends Fragment implements View.OnClickListener {

    public LBLoginFragment() {
        // Required empty public constructor
    }

    private Toolbar mToolbar;

    private EditText et_user_mail, et_user_password;

    private CheckBox rememberMeCheckBox;

    private TextView forgetPasswordTextView;

    private Button ProceedButton;

    private LinearLayout signUpLinkLayout;

    private RelativeLayout progress_bar;

    private LocationRequest mLocationRequest;

    private static final String TAG = "LBLoginFragment";

    private CoordinatorLayout snackBarView;

    private RequestQueue mRequestQueue;

    private String saltKey = "", tokenValue = "";

    private SharedPreferences preferences;


    public static LBLoginFragment newInstance() {
        return new LBLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_loan_buddy_login, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_password = rootView.findViewById(R.id.et_user_password);

        rememberMeCheckBox = rootView.findViewById(R.id.rememberMeCheckBox);

        forgetPasswordTextView = rootView.findViewById(R.id.forgetPasswordTextView);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        signUpLinkLayout = rootView.findViewById(R.id.signUpLinkLayout);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        preferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mRequestQueue = Volley.newRequestQueue(getContext());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mToolbar.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        signUpLinkLayout.setOnClickListener(this);

        ProceedButton.setOnClickListener(this);

        forgetPasswordTextView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchTokenWithoutLogin();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signUpLinkLayout:
                checkLocationPermission();
                break;

            case R.id.ProceedButton:
                if (et_user_mail.getText().toString().trim().equalsIgnoreCase("")) {
                    et_user_mail.setError("Invalid Mail Id !! ");
                    et_user_mail.requestFocus();
                } else if (et_user_password.getText().toString().trim().equalsIgnoreCase("")) {
                    et_user_password.setError("Invalid Password !! ");
                    et_user_password.requestFocus();
                } else {
                    loginToApp();
                }
                break;

            case R.id.forgetPasswordTextView:
                if (getActivity() != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBChangePasswordSendOtpFragment.newInstance(false));
                    transaction.addToBackStack(null).commit();
                }
                break;
        }
    }

    private void checkLocationPermission() {

        progress_bar.setVisibility(View.VISIBLE);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(permissions, AppConstant.PERMISSION_LOC_SIGN_IN);
            } else {
                displayLocationSettingsRequest(getContext());
            }
        } else {
            displayLocationSettingsRequest(getContext());
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    getDeviceLocation();
                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS_ATT);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(TAG, "PendingIntent unable to execute request.");
                        e.printStackTrace();
                    }
                    break;

                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder
                            .setTitle(Html.fromHtml("<h4>" + getString(R.string.permissions_heading) + "</h4>"))
                            .setMessage(Html.fromHtml("<h5>" + getString(R.string.location_enable_request) + "</h5>"))
                            .setCancelable(false)
                            .setPositiveButton(getString(android.R.string.ok),
                                    (dialog, id) -> {
                                        getActivity().moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(0);
                                    }).show();
                    break;
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
            Task<Location> mLocation = mFusedLocationProviderClient.getLastLocation();
            mLocation.addOnSuccessListener(location -> {
                if (location != null) {
                    changeFragment(location.getLatitude(), location.getLongitude());
                } else {
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            List<Location> locationList = locationResult.getLocations();
                            changeFragment(locationList.get(0).getLatitude(), locationList.get(0).getLongitude());
                        }
                    }, Looper.myLooper());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar("Can't proceed without location", android.R.color.holo_red_dark);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS_ATT) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e(TAG, "Clicked OK");
                    getDeviceLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e(TAG, "Clicked CANCEL");
                    progress_bar.setVisibility(View.GONE);
                    showSnackBar("Can't proceed without location", android.R.color.holo_red_dark);
                    break;
            }
        }
    }

    private void changeFragment(Double latitude, double longitude) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, LBSignUpFragment.newInstance(latitude, longitude));
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.PERMISSION_LOC_HOME) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayLocationSettingsRequest(getContext());
            } else {
                showSnackBar("Can't get location without permission !! -> Application Settings", android.R.color.holo_red_dark);
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

                    Log.i(TAG, response.toString());

                    try {

                        tokenValue = response.getString("token");

                        fetchSalt(response.getString("token"));

                    } catch (Exception e) {

                        progress_bar.setVisibility(View.GONE);

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

    private void fetchSalt(String tokenData) {

        progress_bar.setVisibility(View.VISIBLE);

        StringRequest dataRequest = new StringRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "Auth/getSaltkey",
                response -> {

                    Log.e("Response", response);

                    progress_bar.setVisibility(View.GONE);

                    try {

                        JSONObject dataObject = new JSONObject(response);

                        saltKey = dataObject.getString("salt_key");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e("ERROR", "error => " + error.toString());
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", tokenData);
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(dataRequest);

    }

    private void loginToApp() {

        progress_bar.setVisibility(View.VISIBLE);

        String hashedPassword = AppConstant.getPasswordHash(
                AppConstant.getPasswordHash(et_user_password.getText().toString().trim()) + saltKey);

        Map<String, String> params = new HashMap<>();
        params.put("user", et_user_mail.getText().toString().trim());
        params.put("pwd", hashedPassword);
        params.put("salt_key", saltKey);

        Log.e(TAG, new JSONObject(params).toString());
        Log.e(TAG, tokenValue);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.commonAPIUrl + "Auth/token",
                new JSONObject(params),
                response -> {

                    progress_bar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {

                        if (response.getString("user_type").trim().equalsIgnoreCase("borrower")) {

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("loginToken", response.getString("token"));
                            editor.putString("loginUserName", et_user_mail.getText().toString().trim());
                            editor.putString("loginPassword", et_user_password.getText().toString().trim());

//                        if (rememberMeCheckBox.isChecked()){
                            editor.putString("loginSave", "1");
//                        }
//                        else {
//                            editor.putString("loginSave","0");
//                        }
                            editor.apply();

                            showSnackBar("Login Successful !!", R.color.green);
                        }
                        else {
                            showSnackBar("Failed to Login !!", R.color.red);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar("Failed to Login !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            showSnackBar("Failed to Login !!", R.color.red);
            progress_bar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", tokenValue);
                params.put("Content-Type", "application/json");
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
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBDashBoardFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}
