package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
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

import java.util.List;
import java.util.Objects;

import static com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant.REQUEST_CHECK_SETTINGS_ATT;

public class LBHomeFragment extends Fragment implements View.OnClickListener {

    public LBHomeFragment() {
        // Required empty public constructor
    }

    public static LBHomeFragment newInstance() {
        return new LBHomeFragment();
    }

    private Toolbar mToolbar;

    private Button signUpButton, signInButton;

    private CoordinatorLayout snackBarView;

    private static final String TAG = "LBLoanHomeFragment";

    private RelativeLayout mProgressBar;

    private LocationRequest mLocationRequest;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_loan_buddy_home, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        signInButton = rootView.findViewById(R.id.signInButton);

        signUpButton = rootView.findViewById(R.id.signUpButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        mProgressBar = rootView.findViewById(R.id.mProgressBar);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000);
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        signUpButton.setOnClickListener(this);

        signInButton.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View view) {
        Fragment fragmentToRepalce = null;
        switch (view.getId()){
            case R.id.signInButton:
                fragmentToRepalce = LBLoginFragment.newInstance();
                break;

            case R.id.signUpButton:
                checkLocationPermission();
                break;
        }

        if (fragmentToRepalce != null && getActivity() != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToRepalce);
            transaction.addToBackStack(null).commit();
        }

    }

    private void checkLocationPermission() {

        mProgressBar.setVisibility(View.VISIBLE);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(permissions, AppConstant.PERMISSION_LOC_HOME);
            }
            else {
                displayLocationSettingsRequest(getContext());
            }
        }
        else {
            displayLocationSettingsRequest(getContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.PERMISSION_LOC_HOME) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayLocationSettingsRequest(getContext());
            } else {
                mProgressBar.setVisibility(View.GONE);
                showSnackBar("Can't get location without permission !! -> Application Settings", android.R.color.holo_red_dark);
            }
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
                    mProgressBar.setVisibility(View.GONE);
                    changeFragment(location.getLatitude(), location.getLongitude());
                } else {
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            List<Location> locationList = locationResult.getLocations();
                            mProgressBar.setVisibility(View.GONE);
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
                    mProgressBar.setVisibility(View.GONE);
                    showSnackBar("Can't proceed without location", android.R.color.holo_red_dark);
                    break;
            }
        }
    }

    private void changeFragment(Double latitude, double longitude){
        if (getActivity() != null){
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, LBSignUpFragment.newInstance(latitude, longitude));
            transaction.addToBackStack(null).commit();
        }
    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.show();
    }
}
