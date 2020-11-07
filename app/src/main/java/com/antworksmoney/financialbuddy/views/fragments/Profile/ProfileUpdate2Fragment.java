package com.antworksmoney.financialbuddy.views.fragments.Profile;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.ProfileImageUpload;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.android.material.snackbar.Snackbar;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class ProfileUpdate2Fragment extends Fragment implements View.OnClickListener {

    private static  int mBaseFrameLayout;

    public static ProfileUpdate2Fragment newInstance(int baseFrameLayout) {
        mBaseFrameLayout = baseFrameLayout;
        return new ProfileUpdate2Fragment();
    }

    private SharedPreferences pref;

    private FragmentActivity mActivity;

    private EditText et_reg_fname, et_user_mail, et_user_phone_number;

    private CircleImageView profileImage;

    private TextView profileCompletenessPercentage;

    private static final String TAG = "ProfileUpdateFragment";

    private Button addImageFromCameraButton,
            addImageFromGalleryButton,
            removeImageButton,
            skipButton;

    private final int CAMERA_REQUEST = 500, PICK_IMAGE = 510;

    private RelativeLayout selectDateButton;

    private CoordinatorLayout snackBarView;

    private String gender = "", maritalStatus = "";

    private ProgressBar imageLoader;


    private String arrayCity[] = new String[]{"Male", "Female", "Gender"};

    private String arrayMaritalStatus[] = new String[]{"Single", "Married", "Marital Status"};

    private Spinner genderSelector, maritalStatusSelector;

    private ProgressBar profileCompletenessProgressBar;

    private int progress;

    Handler handler = new Handler(Looper.getMainLooper());

    private static final int DELAY_BEFORE_EXIT = 100;

    private AlertDialog dialogSocialsites;

    private View viewSocialSites;

    private AlertDialog.Builder builderSocialSites;

    private ImageView hamburgerIcon;

    private final Calendar dateCalender = Calendar.getInstance();

    private int mYear, mMonth, mDay;

    private Button nextButton;

    private TextView selectDateTextView;

    private SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

    private TextView fbCodeTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_profile_update, container, false);

        mActivity = getActivity();

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        profileImage = rootView.findViewById(R.id.profileImage);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        profileCompletenessPercentage = rootView.findViewById(R.id.profileCompletenessPercentage);

        profileCompletenessProgressBar = rootView.findViewById(R.id.profileCompletenessProgressBar);

        imageLoader = rootView.findViewById(R.id.imageLoader);

        genderSelector = rootView.findViewById(R.id.genderSelector);

        maritalStatusSelector = rootView.findViewById(R.id.maritalStatusSelector);

        skipButton = rootView.findViewById(R.id.skipButton);

        hamburgerIcon = rootView.findViewById(R.id.hamburgerIcon);

        selectDateButton = rootView.findViewById(R.id.dateOfBirthSelectorLayout);

        nextButton = rootView.findViewById(R.id.nextButton);

        selectDateTextView = rootView.findViewById(R.id.selectDateTextView);

        fbCodeTextView = rootView.findViewById(R.id.fbCodeTextView);

        mYear = dateCalender.get(Calendar.YEAR);
        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(mActivity);
        datePickerDialogBuilder.spinnerTheme(R.style.NumberPickerStyle);
        datePickerDialogBuilder.showTitle(true);
        datePickerDialogBuilder.defaultDate(2000, mMonth, mDay);
        datePickerDialogBuilder.maxDate(2000, mMonth, mDay);
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback(new com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (String.valueOf(year).trim().contains(",")) {
                    year = Integer.parseInt(String.valueOf(year).replace(",","").trim());
                }

                String day;
                if (String.valueOf(dayOfMonth).length()<2){
                    day = "0"+ dayOfMonth;
                }
                else {
                    day =  String.valueOf(dayOfMonth);
                }

                Log.e(TAG, String.valueOf(monthOfYear));

                String month;
                if (String.valueOf(monthOfYear + 1).length()<2){
                    month = "0"+ (monthOfYear + 1);
                }
                else {
                    month = String.valueOf(monthOfYear+1);
                }
                selectDateTextView.setText(year + "-" + month + "-" + day);
            }
        });


        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, arrayCity) {
            @Override
            public int getCount() {
                return 2;
            }
        };
        adapter_city.setDropDownViewResource(R.layout.checked_text_view);
        genderSelector.setAdapter(adapter_city);
        genderSelector.setSelection(2);


        ArrayAdapter<String> adapter_maritalStatus = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, arrayMaritalStatus) {
            @Override
            public int getCount() {
                return 2;
            }
        };
        adapter_maritalStatus.setDropDownViewResource(R.layout.checked_text_view);
        maritalStatusSelector.setAdapter(adapter_maritalStatus);
        maritalStatusSelector.setSelection(2);


        imageLoader.setVisibility(View.GONE);

        if (mBaseFrameLayout == R.id.baseParentForAuthentication) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("profileUpdate", "1");
            editor.apply();
        }

        if (!pref.getString("user_name","").trim().equalsIgnoreCase("")){
            et_reg_fname.setText(pref.getString("user_name", ""));
            progress = 17;
        }

        if (!pref.getString("email_value","").trim().equalsIgnoreCase("")){
            et_user_mail.setText(pref.getString("email_value", ""));
            progress = progress + 17;
        }

        if (!pref.getString("user_phone","").trim().equalsIgnoreCase("")){
            et_user_phone_number.setText(pref.getString("user_phone", ""));
            progress = progress + 17;
            et_user_phone_number.setFocusable(false);
        }

        if (!pref.getString("user_dob","").trim().equalsIgnoreCase("")) {
            if (!pref.getString("user_dob","").trim().equalsIgnoreCase("0000-00-00")) {
                selectDateTextView.setText(pref.getString("user_dob", ""));
                progress = progress + 17;
            }
        }

        if ((!pref.getString("gender","").equalsIgnoreCase("Gender"))
                || (!pref.getString("gender","").equalsIgnoreCase(""))){
            progress = progress + 17;

            int pos = 0;
            for (int i= 0; i<arrayCity.length; i++){
                if (pref.getString("","").trim().equalsIgnoreCase(arrayCity[i])){
                    pos = i;
                }
            }
            genderSelector.setSelection(pos);
        }

        if ((!pref.getString("marital_status","").equalsIgnoreCase("Marital Status"))
                || (!pref.getString("marital_status","").equalsIgnoreCase(""))){
            progress = progress + 15;

            int pos = 0;
            for (int i= 0; i<arrayMaritalStatus.length; i++){
                if (pref.getString("","").trim().equalsIgnoreCase(arrayMaritalStatus[i])){
                    pos = i;
                }
            }
            maritalStatusSelector.setSelection(pos);
        }

        Glide.with(mActivity).load(pref.getString("user_image_url",""))
                .asBitmap()
                .error(null)
                .into(profileImage);

        fbCodeTextView.setText("FB CODE : "+ pref.getString("fbCode",""));

        profileCompletenessPercentage.setText(progress + " %");

        profileCompletenessProgressBar.setProgress(progress);

        viewSocialSites = LayoutInflater.from(mActivity).inflate(R.layout.layout_select_image_dialog, null);

        builderSocialSites = new AlertDialog.Builder(mActivity);

        builderSocialSites.setView(viewSocialSites);

        dialogSocialsites = builderSocialSites.create();
        if (dialogSocialsites.getWindow() != null) {
            dialogSocialsites.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogSocialsites.setCancelable(true);

        addImageFromCameraButton = viewSocialSites.findViewById(R.id.selectImageFromCamera);

        addImageFromGalleryButton = viewSocialSites.findViewById(R.id.selectImageFromGallery);

        removeImageButton = viewSocialSites.findViewById(R.id.removeImage);

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getClass().getSimpleName().trim().equalsIgnoreCase("HomeActivity")) {
                    ((HomeActivity) getActivity()).getmDrawerLayout().openDrawer(GravityCompat.START);
                }
            }
        });

        genderSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = adapter_city.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        maritalStatusSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maritalStatus = adapter_maritalStatus.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addImageFromCameraButton.setOnClickListener(this);

        addImageFromGalleryButton.setOnClickListener(this);

        removeImageButton.setOnClickListener(this);

        profileImage.setOnClickListener(this);

        skipButton.setOnClickListener(this);

        nextButton.setOnClickListener(this);

        selectDateButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        FragmentTransaction transaction = (Objects.requireNonNull(getActivity()))
                .getSupportFragmentManager()
                .beginTransaction();

        Fragment fragmentToReplace = null;

        switch (view.getId()){
            case R.id.profileImage:
                dialogSocialsites.show();
                break;

            case  R.id.skipButton:
                Intent moveToHomeScreen = new Intent(mActivity, HomeActivity.class);
                mActivity.startActivity(moveToHomeScreen);
                mActivity.finish();
                break;

            case R.id.selectImageFromCamera:
                if (dialogSocialsites.isShowing()) dialogSocialsites.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.selectImageFromGallery:
                if (dialogSocialsites.isShowing()) dialogSocialsites.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(pickPhoto, "Select Picture"), PICK_IMAGE);
                break;

            case R.id.removeImage:
                if (dialogSocialsites.isShowing()) dialogSocialsites.dismiss();
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_image_url", "");
                editor.apply();
                profileImage.setImageDrawable(null);
                break;

            case R.id.dateOfBirthSelectorLayout:
                datePickerDialogBuilder.build().show();
                break;

            case R.id.nextButton:
                if (et_reg_fname == null || et_reg_fname.getText().toString().length() < 1) {
                    showSnackBar("Please enter a valid User name");
                    et_reg_fname.requestFocus();
                } else if (et_user_phone_number == null || et_user_phone_number.getText().toString().length() < 10) {
                    showSnackBar("Please enter a valid phone number");
                    et_user_phone_number.requestFocus();
                } else if (et_user_mail == null || (!et_user_mail.getText().toString().contains("@"))) {
                    showSnackBar("Please enter a E-mail id");
                    et_user_mail.requestFocus();
                }
                else {

                    JSONObject outerObject = new JSONObject();



                    try {

                        outerObject.put("name", et_reg_fname.getText().toString().trim());

                        outerObject.put("contact", et_user_phone_number.getText().toString().trim());

                        outerObject.put("mail", et_user_mail.getText().toString().trim());

                        if (selectDateTextView.getText().toString().trim().equalsIgnoreCase("Select Date")) {
                            outerObject.put("date_of_birth", "");
                        } else
                            outerObject.put("date_of_birth", selectDateTextView.getText().toString());


                        if (gender.trim().equalsIgnoreCase("Gender")) {
                            outerObject.put("gender", "");
                        } else outerObject.put("gender", gender);

                        if (maritalStatus.trim().equalsIgnoreCase("Marital Status")) {
                            outerObject.put("marital_status", "");
                        } else outerObject.put("marital_status", maritalStatus);

                        if (profileImage.getDrawable() != null) {

                            try {
                                new ProfileImageUpload(
                                        ((GlideBitmapDrawable) profileImage.getDrawable().getCurrent()).getBitmap(),
                                        getContext(), et_user_phone_number.getText().toString().trim()).execute();

                            } catch (Exception e) {
                                try {
                                    new ProfileImageUpload(
                                            ((BitmapDrawable) profileImage.getDrawable()).getBitmap(),
                                            getContext(), et_user_phone_number.getText().toString().trim()).execute();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Log.e(TAG,outerObject.toString());

                    fragmentToReplace = AdditionalInfoFragment.newInstance(mBaseFrameLayout,outerObject);
                }

                break;



        }

        if (fragmentToReplace != null) {
            transaction.replace(mBaseFrameLayout, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap image = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if (image != null) {
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        Glide.with(mActivity).load(stream.toByteArray())
                                .error(null)
                                .into(profileImage);

                    } else showSnackBar("Failed to get Image !!!");
                }
                break;

            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        imageLoader.setVisibility(View.VISIBLE);

                        Object[] dataObject = new Object[6];
                        dataObject[0] = imageUri;
                        dataObject[1] = profileImage;
                        dataObject[2] = snackBarView;
                        dataObject[3] = imageLoader;
                        dataObject[4] = mActivity;
                        dataObject[5] = pref;
                        new imageLoader().execute(dataObject);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }


    public void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }


    private static class imageLoader extends AsyncTask<Object, String, String> {

        @SuppressLint("StaticFieldLeak")
        ImageView mProfileImage;

        @SuppressLint("StaticFieldLeak")
        CoordinatorLayout snackBarView;

        @SuppressLint("StaticFieldLeak")
        ProgressBar imageLoader;

        @SuppressLint("StaticFieldLeak")
        AppCompatActivity mActivity;

        Bitmap selectedImage;
        SharedPreferences preferences;


        @Override
        protected String doInBackground(Object... objects) {
            Uri imageUri = (Uri) objects[0];
            mProfileImage = (ImageView) objects[1];
            snackBarView = (CoordinatorLayout) objects[2];
            imageLoader = (ProgressBar) objects[3];
            mActivity = (AppCompatActivity) objects[4];
            preferences = (SharedPreferences) objects[5];

            final InputStream imageStream;
            try {
                imageStream = mActivity.getContentResolver().openInputStream(Objects.requireNonNull(imageUri));
                selectedImage = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return imageUri.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            mProfileImage.setImageBitmap(selectedImage);
            imageLoader.setVisibility(View.GONE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_image_url", s);
            editor.apply();

            super.onPostExecute(s);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e(TAG, "onPause()");

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_name", et_reg_fname.getText().toString().trim());
        editor.putString("email_value", et_user_mail.getText().toString().trim());
        editor.putString("user_phone", et_user_phone_number.getText().toString().trim());

        editor.apply();


    }

}