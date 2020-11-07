package com.antworksmoney.financialbuddy.views.fragments.Investment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.activities.RegionalDataFetch;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;
import com.google.android.material.snackbar.Snackbar;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class InvestmentApplyNowFragment extends Fragment implements View.OnClickListener {
//    private static final int REQUEST_CHECK_SETTINGS = 0x10;

    private static final int SELECT_REGIONAL_DATA = 0x11;

    public InvestmentApplyNowFragment() {
        // Required empty public constructor
    }

    private CoordinatorLayout snackBarView;

//    private LocationRequest mLocationRequest;

    private AppCompatActivity mActivity;

    private static InvestmentEntity investmentEntity;

    public static InvestmentApplyNowFragment newInstance(InvestmentEntity mInvestmentEntity) {
        investmentEntity = mInvestmentEntity;
        return new InvestmentApplyNowFragment();
    }


    private ProgressBar stateSeletorProgressBar,
            citySelectorProgressbar;


    private AlertDialog dialogSocialsites;

    private View viewSocialSites;

    private AlertDialog.Builder builderSocialSites;

    private Context mContext;

    private Button returnToHome;

    private ImageView closeButton;

    private TextView sendingNotificationText;

    private TextView sendingNotificationHeading;

    private RelativeLayout stateSelectorLayout, citySelectorLayout;

    private TextView cityName, stateName;

    private static final String TAG = "InvestmentApplyNowFragm";

    private EditText edtFirstName, edtLastName, edtEmailAddress, edtMobileNumber;

    private Button loanApplyButton;

    private TextView selectDateButton;

    private SharedPreferences pref;

    private RelativeLayout dateOfBirthSelectorLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_investment_apply, container, false);

        mActivity = (AppCompatActivity) getActivity();

        mContext = getContext();

        stateSeletorProgressBar = rootView.findViewById(R.id.stateSelectorLoader);

        citySelectorProgressbar = rootView.findViewById(R.id.citySelectorLoader);

        dateOfBirthSelectorLayout = rootView.findViewById(R.id.dateOfBirthSelectorLayout);

        cityName = rootView.findViewById(R.id.cityName);

        stateName = rootView.findViewById(R.id.stateName);

        edtFirstName = rootView.findViewById(R.id.edtFirstName);

        edtLastName = rootView.findViewById(R.id.edtLastName);

        edtEmailAddress = rootView.findViewById(R.id.edtEmailAddress);

        edtMobileNumber = rootView.findViewById(R.id.edtMobileNumber);

        selectDateButton = rootView.findViewById(R.id.selectDateButton);

        stateSelectorLayout = rootView.findViewById(R.id.stateSelectorLayout);

        citySelectorLayout = rootView.findViewById(R.id.citySelectorLayout);

        loanApplyButton = rootView.findViewById(R.id.loanApplyButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        viewSocialSites = LayoutInflater.from(mContext).inflate(R.layout.layout_sending_notification, null);

        builderSocialSites = new AlertDialog.Builder(mContext);

        builderSocialSites.setView(viewSocialSites);

        dialogSocialsites = builderSocialSites.create();
        if (dialogSocialsites.getWindow() != null) {
            dialogSocialsites.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogSocialsites.setCancelable(false);

        closeButton = viewSocialSites.findViewById(R.id.closeButton);

        returnToHome = viewSocialSites.findViewById(R.id.returnToHome);

        sendingNotificationText = viewSocialSites.findViewById(R.id.sendingNotificationText);

        sendingNotificationHeading = viewSocialSites.findViewById(R.id.sendingNotificationHeading);

        sendingNotificationHeading.setText("Sending Notification");

        sendingNotificationText.setText("Please Wait !! We are submitting your application.");

        stateSeletorProgressBar.setVisibility(View.INVISIBLE);

        citySelectorProgressbar.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(this);

        returnToHome.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000);
//        mLocationRequest.setFastestInterval(120000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (investmentEntity.getApplyFor().trim().equalsIgnoreCase("Self")) {

            if (pref.getString("user_dob", "").trim().equalsIgnoreCase("")) {
                selectDateButton.setText("Select Date");
            }
            else {
                selectDateButton.setText(pref.getString("user_dob",""));
            }

            edtFirstName.setText(pref.getString("user_name", ""));

            edtEmailAddress.setText(pref.getString("email_value", ""));

            edtMobileNumber.setText(pref.getString("user_phone", ""));
        }

//        else {
//            if (pref.getString("date_of_birth", "").trim().contains(",")) {
//                selectDateButton.setText(pref.getString("date_of_birth", "").trim().replace(",", ""));
//            } else {
//                selectDateButton.setText(pref.getString("date_of_birth", "").trim());
//            }
//        }


        stateSelectorLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(mActivity, RegionalDataFetch.class);
            intent.putExtra("stateName", "");
            startActivityForResult(intent, SELECT_REGIONAL_DATA);
        });

        citySelectorLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(mActivity, RegionalDataFetch.class);
            intent.putExtra("stateName", stateName.getText().toString().trim());
            startActivityForResult(intent, SELECT_REGIONAL_DATA);
        });


        dateOfBirthSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });

        loanApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToServer();
            }
        });

//        displayLocationSettingsRequest(getContext());
    }

    public void DateDialog() {
        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mYear, mMonth, mDay;

        mYear = dateCalender.get(Calendar.YEAR);
        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(mActivity);
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(mYear, mMonth, mDay);
        datePickerDialogBuilder.maxDate(mYear, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (String.valueOf(year).trim().contains(",")) {
                    year = Integer.parseInt(String.valueOf(year).replace(",", "").trim());
                }
                selectDateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        });

        datePickerDialogBuilder.build().show();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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

    private void sendDataToServer() {

        if (edtFirstName == null || edtFirstName.getText().toString().length() < 1) {
            showSnackBar("Please enter a valid First name");
            edtFirstName.requestFocus();
        }  else if (edtMobileNumber == null || edtMobileNumber.getText().toString().length() < 10) {
            showSnackBar("Please enter a valid Phone Number");
            edtMobileNumber.requestFocus();
        } else if (edtEmailAddress == null || (!edtEmailAddress.getText().toString().trim().contains("@"))) {
            showSnackBar("Please enter valid E-mail address");
            edtEmailAddress.requestFocus();
        } else if (selectDateButton.getText().toString().trim().equalsIgnoreCase("Select Date")
                || selectDateButton.getText().toString().trim().equalsIgnoreCase("")) {
            showSnackBar("Select Date Of Birth");
            DateDialog();

        } else {

            dialogSocialsites.show();

            JSONObject dataObject = new JSONObject();
            try {
                JSONObject investmentPlanDetail = new JSONObject();
                investmentPlanDetail.put("investment_detail_id", investmentEntity.getFinanceCompanyId());
                investmentPlanDetail.put("int_rate1", investmentEntity.getInt_rate1());
                investmentPlanDetail.put("int_rate2", investmentEntity.getInt_rate2());
                investmentPlanDetail.put("int_rate3", investmentEntity.getInt_rate3());
                investmentPlanDetail.put("int_rate4", investmentEntity.getInt_rate4());
                investmentPlanDetail.put("int_rate5", investmentEntity.getInt_rate5());
                investmentPlanDetail.put("int_rate6",investmentEntity.getInt_rate6());
                investmentPlanDetail.put("int_rate7", investmentEntity.getInt_rate7());
                investmentPlanDetail.put("sr_citizens", investmentEntity.get_sr_citizen());
                investmentPlanDetail.put("inv_product_list_id", investmentEntity.getRecommendedInvestmentId());
                investmentPlanDetail.put("investment_product_id", investmentEntity.getInvestmentId());

                JSONObject userDetail = new JSONObject();
                userDetail.put("user_id", pref.getString("user_phone", ""));
                userDetail.put("first_name", edtFirstName.getText().toString().trim());
                userDetail.put("last_name", edtLastName.getText().toString().trim());
                userDetail.put("mobile", edtMobileNumber.getText().toString().trim());
                userDetail.put("email", edtEmailAddress.getText().toString().trim());
                userDetail.put("dob", selectDateButton.getText().toString().trim());
                userDetail.put("state_code", stateName.getText().toString().trim());
                userDetail.put("city", cityName.getText().toString().trim());

                if (investmentEntity.getApplyFor().trim().equalsIgnoreCase("Self")) {
                    userDetail.put("is_refer", "0");
                } else {
                    userDetail.put("is_refer", "1");
                }

                dataObject.put("investment_plan", investmentPlanDetail);
                dataObject.put("user_details", userDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("Data",dataObject.toString());

            JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "save_investment_query",
                    dataObject,
                    response -> {
                        Log.e(TAG, response.toString());

                        try {
                            if (response.getJSONObject("data").getString("status").trim().equalsIgnoreCase("1")) {
                                sendingNotificationHeading.setText("Sent Sucessfully");
                                sendingNotificationText.setText("We have received your application, one of our Portfolio Manager would call you shortly");
                                if (investmentEntity.getInvestmentId().trim().equalsIgnoreCase("4")){
                                   new Handler().postDelayed(() -> {
                                       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                       transaction.replace(R.id.homeParent, LoadUrlFragment.newInstance("https://antworksp2p.com/lender","P2P Investment"));
                                       transaction.addToBackStack(null).commit();

                                       dialogSocialsites.dismiss();
                                   },2000);
                                }
                            } else {
                                sendingNotificationHeading.setText("Failed");
                                sendingNotificationText.setText("Failed to submit your application");
                            }

                        } catch (Exception e) {
                            sendingNotificationHeading.setText("Failed");
                            sendingNotificationText.setText("Failed to submit your application");
                            e.printStackTrace();
                        }
                        returnToHome.setVisibility(View.VISIBLE);

                    },
                    error -> {
                        sendingNotificationHeading.setText("Failed");
                        sendingNotificationText.setText("Failed to submit your application");
                        returnToHome.setVisibility(View.VISIBLE);
                    }
            );

            dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataObjectRequest.setShouldCache(false);
            RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
            mRequestQueue.add(dataObjectRequest);
        }

    }


    private void showSnackBar(String message) {
        final Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();

            }
        });
        snackBar.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.returnToHome:
                dialogSocialsites.dismiss();
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intent);
                break;

            case R.id.closeButton:
                dialogSocialsites.dismiss();
                break;
        }
    }
}
