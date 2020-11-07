package com.antworksmoney.financialbuddy.views.fragments.Insurance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

public class RegisterAsPosFragment extends Fragment implements View.OnClickListener {
    public RegisterAsPosFragment() {
        // Required empty public constructor
    }

    private TextView stateName, cityName;

    private AppCompatActivity mActivity;

//    private final int REQUEST_CHECK_SETTINGS = 0x04;
//
//    private LocationRequest mLocationRequest;

    private ProgressBar stateSeletorProgressBar, citySelectorProgressbar;

//    private static final String TAG = "RegisterAsPosFragment";

    private Button ProceedButton;

    private CoordinatorLayout snackBarView;

    private Toolbar top_toolBar;

    public static RegisterAsPosFragment newInstance() {
        return new RegisterAsPosFragment();
    }

    private SharedPreferences preferences;

    private EditText et_reg_fname, et_user_mail, et_user_phone_number;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_as_pos, container, false);

        stateName = rootView.findViewById(R.id.stateName);

        cityName = rootView.findViewById(R.id.cityName);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        citySelectorProgressbar = rootView.findViewById(R.id.citySelectorLoader);

        stateSeletorProgressBar = rootView.findViewById(R.id.stateSelectorLoader);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        ProceedButton.setOnClickListener(this);

        mActivity = (AppCompatActivity) getActivity();

        preferences = mActivity.getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000);
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        top_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mActivity)
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        stateSeletorProgressBar.setVisibility(View.INVISIBLE);
        citySelectorProgressbar.setVisibility(View.INVISIBLE);

        et_reg_fname.setText(preferences.getString("user_name",""));

        et_user_phone_number.setText(preferences.getString("user_phone",""));

        et_user_mail.setText(preferences.getString("email_value",""));

//        displayLocationSettingsRequest(mActivity);

    }


//    private void displayLocationSettingsRequest(Context context) {
//        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
//        googleApiClient.connect();
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(10000 / 2);
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//        result.setResultCallback(result1 -> {
//            final Status status = result1.getStatus();
//            switch (status.getStatusCode()) {
//                case LocationSettingsStatusCodes.SUCCESS:
//                    getCurrentLocation();
//                    break;
//
//                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                    try {
//                        status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
//                    } catch (IntentSender.SendIntentException e) {
//                        Log.i(TAG, "PendingIntent unable to execute request.");
//                    }
//                    break;
//
//                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
//                    break;
//            }
//        });
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void getCurrentLocation() {
//        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
//        Task<Location> mLocation = mFusedLocationProviderClient.getLastLocation();
//        mLocation.addOnSuccessListener(location -> {
//            if (location != null) {
//                getStateAndCityName(location);
//            } else {
//                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
//            }
//        });
//    }
//
//    LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            List<Location> locationList = locationResult.getLocations();
//            getStateAndCityName(locationList.get(0));
//
//        }
//    };
//
//    private void getStateAndCityName(Location location) {
//        new Handler().post(() -> {
//            try {
//                stateSeletorProgressBar.setVisibility(View.INVISIBLE);
//                citySelectorProgressbar.setVisibility(View.INVISIBLE);
//
//                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
//                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                if (addresses.size() > 0) {
//                    cityName.setText(addresses.get(0).getLocality());
//                    stateName.setText(addresses.get(0).getAdminArea());
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        });
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        getCurrentLocation();
//                        break;
//
//
//                    case Activity.RESULT_CANCELED:
//                        stateSeletorProgressBar.setVisibility(View.INVISIBLE);
//                        citySelectorProgressbar.setVisibility(View.INVISIBLE);
//                        break;
//
//                }
//                break;
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ProceedButton:
                if (et_reg_fname.getText().toString().trim().equalsIgnoreCase("")) {
                    showSnackBar("Please enter valid name !!");
                } else if (!et_user_mail.getText().toString().trim().contains("@")) {
                    showSnackBar("Please enter valid E-mail id");
                } else if (et_user_phone_number.getText().toString().trim().length() < 10) {
                    showSnackBar("Please enter valid Phone number !!");
                } else if (stateName.getText().toString().equalsIgnoreCase("")) {
                    showSnackBar("Please enter valid State name !!");
                } else if (cityName.getText().toString().equalsIgnoreCase("")) {
                    showSnackBar("Please enter valid City name !!");
                } else {

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("POS_Name", et_reg_fname.getText().toString().trim());
                    editor.putString("POS_Mail", et_user_mail.getText().toString().trim());
                    editor.putString("POS_Phone", et_user_phone_number.getText().toString().trim());
                    editor.putString("POS_State", stateName.getText().toString().trim());
                    editor.putString("POS_City", cityName.getText().toString().trim());
                    editor.putString("registerAsPos", "YES");
                    editor.apply();

                    FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance("http://policysecure.in/Antworks/Register-POS","Register As POS (Point of Sale)"));
                    transaction.addToBackStack(null).commit();
                }
                break;
        }

    }

    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

}
