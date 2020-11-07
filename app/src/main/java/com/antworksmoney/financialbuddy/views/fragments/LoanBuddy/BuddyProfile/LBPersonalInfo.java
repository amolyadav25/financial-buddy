package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.BuddyProfile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.json.JSONObject;

import java.util.Calendar;


public class LBPersonalInfo extends Fragment {

    public LBPersonalInfo() {
        // Required empty public constructor
    }


    private static JSONObject mResponseObject;



    public static LBPersonalInfo newInstance(JSONObject responseObject) {
        mResponseObject = responseObject;
        return  new LBPersonalInfo();
    }

    private EditText et_reg_fname, et_user_mail;

    private TextView et_user_phone_number;

    private Spinner genderSelector;

    private TextView selectDateTextView;

    private Button continueButton;

    private RelativeLayout dateOfBirthSelectorLayout;

    private String[] arrayGender = new String[]{
            "Male", "Female", "Gender"
    };

    private static final String TAG = "LBPersonalInfo";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_lbpersonal_info, container, false);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        genderSelector = rootView.findViewById(R.id.genderSelector);

        selectDateTextView = rootView.findViewById(R.id.selectDateTextView);

        continueButton = rootView.findViewById(R.id.continueButton);

        dateOfBirthSelectorLayout = rootView.findViewById(R.id.dateOfBirthSelectorLayout);

        continueButton.setVisibility(View.GONE);

        et_reg_fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (String.valueOf(charSequence).trim().equalsIgnoreCase(
                            mResponseObject.getJSONObject("MyPersonalDetails").getString("name").trim())){

                        et_reg_fname.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_check_accent_24dp, 0);
                    }
                    else {
                        et_reg_fname.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, 0, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_user_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (String.valueOf(charSequence).trim().equalsIgnoreCase(
                            mResponseObject.getJSONObject("MyPersonalDetails").getString("email").trim())){

                        et_user_mail.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_check_accent_24dp, 0);
                    }
                    else {
                        et_user_mail.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, 0, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_user_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (String.valueOf(charSequence).trim().equalsIgnoreCase(
                            mResponseObject.getJSONObject("MyPersonalDetails").getString("mobile").trim())){

                        et_user_phone_number.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_check_accent_24dp, 0);
                    }
                    else {
                        et_user_phone_number.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, 0, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        selectDateTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (String.valueOf(charSequence).trim().equalsIgnoreCase(
                            mResponseObject.getJSONObject("MyPersonalDetails").getString("dob").trim())){

                        selectDateTextView.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_check_accent_24dp, 0);
                    }
                    else {
                        selectDateTextView.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, 0, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dateOfBirthSelectorLayout.setOnClickListener(view -> DateDialog());

        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(
                getActivity(), R.layout.checked_text_view, arrayGender) {
            @Override
            public int getCount() {
                return 2;
            }
        };
        adapter_city.setDropDownViewResource(R.layout.checked_text_view);
        genderSelector.setAdapter(adapter_city);
        genderSelector.setSelection(2);




        updateDataToUI();

        return rootView;
    }

    private void updateDataToUI(){

        try {

            et_reg_fname.setFocusableInTouchMode(false);
            et_user_mail.setFocusableInTouchMode(false);
            et_user_phone_number.setFocusableInTouchMode(false);
            dateOfBirthSelectorLayout.setEnabled(false);

            if (!mResponseObject.getJSONObject("MyPersonalDetails").getString("name").trim().equalsIgnoreCase("")){
                et_reg_fname.setText(mResponseObject.getJSONObject("MyPersonalDetails").getString("name").trim());
                et_reg_fname.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_check_accent_24dp, 0);
            }

            if (!mResponseObject.getJSONObject("MyPersonalDetails").getString("email").trim().equalsIgnoreCase("")){
                et_user_mail.setText(mResponseObject.getJSONObject("MyPersonalDetails").getString("email").trim());
                et_user_mail.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_check_accent_24dp, 0);
            }

            if (!mResponseObject.getJSONObject("MyPersonalDetails").getString("mobile").trim().equalsIgnoreCase("")){
                et_user_phone_number.setText(mResponseObject.getJSONObject("MyPersonalDetails").getString("mobile").trim());
                et_user_phone_number.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_check_accent_24dp, 0);
            }

            if (!mResponseObject.getJSONObject("MyPersonalDetails").getString("dob").trim().equalsIgnoreCase("")){
                selectDateTextView.setText(mResponseObject.getJSONObject("MyPersonalDetails").getString("dob").trim());
                selectDateTextView.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_check_accent_24dp, 0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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
        datePickerDialogBuilder.minDate(1950, 0, 1);
        datePickerDialogBuilder.callback((view, year1, monthOfYear, dayOfMonth) -> {
            if (String.valueOf(year1).trim().contains(",")) {
                year1 = Integer.parseInt(String.valueOf(year1).replace(",", "").trim());
            }
            selectDateTextView.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        });

        datePickerDialogBuilder.build().show();
    }
}
