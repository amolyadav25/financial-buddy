package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.RegionalDataFetch;

import java.text.MessageFormat;


public class WhereYouLiveFragment extends Fragment {
    public WhereYouLiveFragment() {
        // Required empty public constructor
    }

    private static LoanInfoEntity mLoanInfoEntity;

    public static WhereYouLiveFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new WhereYouLiveFragment();
    }

    private ProgressBar stateSelectorLoader,
            citySelectorLoader,
            journeyCompletedProgressBar;

    private static final int REQUEST_CHECK_SETTINGS = 0x02;

    private static final int SELECT_REGIONAL_DATA = 0x03;

    private TextView cityName,
            stateName,
            journeyCompletedProgressText;

    private RelativeLayout stateSelectorLayout, citySelectorLayout;

//    private LocationRequest mLocationRequest;

//    private Context mContext;

    private FragmentActivity mActivity;

    private Button nextButtonForQuestions;

    private Toolbar top_toolBar;

    private static final String TAG = "WhereYouLiveFragment";

    private TextView loanHeading;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_where_you_live, container, false);

        stateSelectorLoader = rootView.findViewById(R.id.stateSelectorLoader);

        citySelectorLoader = rootView.findViewById(R.id.citySelectorLoader);

        cityName = rootView.findViewById(R.id.cityName);

        stateName = rootView.findViewById(R.id.stateName);

        stateSelectorLayout = rootView.findViewById(R.id.stateSelectorLayout);

        citySelectorLayout = rootView.findViewById(R.id.citySelectorLayout);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        loanHeading = rootView.findViewById(R.id.loanHeading);

//        mContext = getContext();

        mActivity = getActivity();

        Log.e(TAG, mLoanInfoEntity.toString());

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000);
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                    && (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//
//                stateSelectorLoader.setVisibility(View.INVISIBLE);
//                citySelectorLoader.setVisibility(View.INVISIBLE);
//            } else {
//                displayLocationSettingsRequest(mContext);
//            }
//        } else {
//            displayLocationSettingsRequest(mContext);
//        }

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        citySelectorLoader.setVisibility(View.GONE);

        stateSelectorLoader.setVisibility(View.GONE);

        citySelectorLayout.setOnClickListener(view1 -> {

            Intent intent = new Intent(mActivity, RegionalDataFetch.class);
            intent.putExtra("stateName", stateName.getText().toString().trim());
            intent.putExtra("theme", R.style.MyAppbarTheme);
            startActivityForResult(intent, SELECT_REGIONAL_DATA);
        });

        stateSelectorLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(mActivity, RegionalDataFetch.class);
            intent.putExtra("stateName", "");
            intent.putExtra("theme", R.style.MyAppbarTheme);
            startActivityForResult(intent, SELECT_REGIONAL_DATA);
        });

        nextButtonForQuestions.setOnClickListener(v -> {
            mLoanInfoEntity.setCity(cityName.getText().toString().trim());
            mLoanInfoEntity.setState(stateName.getText().toString().trim());

            Fragment fragmentToReplace;

            Log.e(TAG, mLoanInfoEntity.getLoanName());

            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Housing Loan")
                    || mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")) {
                fragmentToReplace = PropertyTypeFragment.newInstance(mLoanInfoEntity);
            }
            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Credit Card")){
             fragmentToReplace = DateOfBirthFragment.newInstance(mLoanInfoEntity);
            }
//            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Instant Loan")){
//                fragmentToReplace = ContactDetailsFragment.newInstance(mLoanInfoEntity);
//            }
            else {
                fragmentToReplace = GenderSelectorFragment.newInstance(mLoanInfoEntity);
            }

            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        journeyCompletedProgressBar.setProgress(9);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));
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
//        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
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


//    private void getStateAndCityName(Location location) {
//        new Handler().post(() -> {
//            try {
//                stateSelectorLoader.setVisibility(View.INVISIBLE);
//                citySelectorLoader.setVisibility(View.INVISIBLE);
//
//                Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        getCurrentLocation();
//                        break;
//
//
//                    case Activity.RESULT_CANCELED:
//                        stateSelectorLoader.setVisibility(View.INVISIBLE);
//                        citySelectorLoader.setVisibility(View.INVISIBLE);
//                        break;
//
//                }
//                break;

            case SELECT_REGIONAL_DATA:
                if (resultCode == Activity.RESULT_OK) {
                    if (((String) data.getSerializableExtra("name")).trim().contains("cityName")) {
                        cityName.setText(((String) data.getSerializableExtra("name")).trim().split("=")[1]);
                    } else if (((String) data.getSerializableExtra("name")).trim().contains("stateName")) {
                        stateName.setText(((String) data.getSerializableExtra("name")).trim().split("=")[1]);
                    }
                }
                break;

        }
    }
}
