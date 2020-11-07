package com.antworksmoney.financialbuddy.views.fragments.Network;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddNetworkDetails extends Fragment implements View.OnClickListener {

    public AddNetworkDetails() {
        // Required empty public constructor
    }

    private static ProfileInfo mProfileInfo;

    public static AddNetworkDetails newInstance(ProfileInfo profileInfo) {
        mProfileInfo = profileInfo;
        return new AddNetworkDetails();
    }

    private TextView alreadyFilledDetails;

    private Button proceedButton;

    private Context mContext;

    private static final int PERMISSION_REQUEST_CALENDER = 410;

    private CoordinatorLayout snackBarView;

    private EditText et_user_mail, et_remainder_text;

    private TextView et_remainder_date, et_reminder_time;

    private static final String TAG = "AddNetworkDetails";

    private ProgressBar detailsSaveProgressBar;

    private LinearLayout remainderDateSelectorLayout, remainderTimeSelectorLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_network_details, container, false);

        alreadyFilledDetails = rootView.findViewById(R.id.alreadyFilledDetails);

        proceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        et_remainder_date = rootView.findViewById(R.id.et_remainder_date);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_remainder_text = rootView.findViewById(R.id.et_remainder_text);

        et_reminder_time = rootView.findViewById(R.id.et_reminder_time);

        detailsSaveProgressBar = rootView.findViewById(R.id.detailsSaveProgressBar);

        remainderDateSelectorLayout = rootView.findViewById(R.id.remainderDateSelectorLayout);

        remainderTimeSelectorLayout = rootView.findViewById(R.id.remainderTimeSelectorLayout);

        mContext = getContext();

        alreadyFilledDetails.setText("Contact Name : " + mProfileInfo.getName() + "\n" +
                "Contact Number : " + mProfileInfo.getPhoneNumber());

        proceedButton.setOnClickListener(this);

        remainderDateSelectorLayout.setOnClickListener(this);

        remainderTimeSelectorLayout.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ProceedButton:
                if (et_user_mail == null || (!et_user_mail.getText().toString().trim().contains("@"))) {
                    showSnackBar("Please enter valid E-mail address");
                    et_user_mail.requestFocus();
                } else if (et_remainder_text == null || et_remainder_text.getText().toString().length() < 1) {
                    showSnackBar("Please enter a valid reminder");
                    et_remainder_text.requestFocus();
                } else if (et_remainder_date == null || et_remainder_date.getText().toString().trim().equalsIgnoreCase("")) {
                    showSnackBar("Please enter a valid reminder date");
                    et_remainder_date.requestFocus();
                } else if (et_reminder_time == null || et_reminder_time.getText().toString().trim().equalsIgnoreCase("")) {
                    showSnackBar("Please enter a valid reminder time");
                    et_reminder_time.requestFocus();
                } else
                    askForContactPermission();

                break;

            case R.id.remainderDateSelectorLayout:
                DateDialog();
                break;

            case R.id.remainderTimeSelectorLayout:
                timeDialog();
                break;

        }
    }

    private void timeDialog() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                et_reminder_time.setText(MessageFormat.format("{0}:{1}", selectedHour, selectedMinute));
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_CALENDER);
            } else {
                writeIntoCalender();
            }
        } else {
            writeIntoCalender();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CALENDER) {
            if (permissions[0].equals(Manifest.permission.WRITE_CALENDAR) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writeIntoCalender();
            } else {
                showSnackBar("Can't proceed further, Please provide permission !!! (-> App settings) ");
            }
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

    private void writeIntoCalender() {
        try {

            detailsSaveProgressBar.setVisibility(View.VISIBLE);
            proceedButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));

            String myDate = et_remainder_date.getText().toString().trim() + " "+et_reminder_time.getText().toString().trim()+":00";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);
            Date date = sdf.parse(myDate);
            long millis = date.getTime();

            final ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID, 1);

            event.put(CalendarContract.Events.TITLE, mProfileInfo.getName() + "\n" + et_user_mail.getText().toString().trim());
            event.put(CalendarContract.Events.DESCRIPTION, et_remainder_text.getText().toString().trim());
            event.put(CalendarContract.Events.EVENT_LOCATION, "At his Place");

            event.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
            event.put(CalendarContract.Events.DTEND, millis);
            event.put(CalendarContract.Events.ALL_DAY, 0);   // 0 for false, 1 for true
            event.put(CalendarContract.Events.HAS_ALARM, 1); // 0 for false, 1 for true

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            Uri baseUri = Uri.parse("content://com.android.calendar/events");

            mContext.getContentResolver().insert(baseUri, event);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailsSaveProgressBar.setVisibility(View.GONE);
                    proceedButton.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                    showSnackBar("Added successfully !!");
                }
            },1000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DateDialog() {

        SpinnerDatePickerDialogBuilder datePickerDialogBuilder;

        Calendar dateCalender = Calendar.getInstance();

        int mYear, mMonth, mDay;

        mYear = dateCalender.get(Calendar.YEAR);
        mMonth = dateCalender.get(Calendar.MONTH);
        mDay = dateCalender.get(Calendar.DAY_OF_MONTH);

        datePickerDialogBuilder = new SpinnerDatePickerDialogBuilder();
        datePickerDialogBuilder.context(mContext);
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
                et_remainder_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        });

        datePickerDialogBuilder.build().show();
    }
}
