package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.antworksmoney.financialbuddy.R;
import org.jetbrains.annotations.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.antworksmoney.financialbuddy.views.fragments.LeadStatus.InvestmentStatusFragment.endDate;
import static com.antworksmoney.financialbuddy.views.fragments.LeadStatus.InvestmentStatusFragment.startDate;

public class CustomFragment extends Fragment {
    private TabLayout tabLayout;
    private DatePicker datePicker;

    public static CustomFragment createInstance(TabLayout txt) {
        CustomFragment fragment = new CustomFragment();
        fragment.tabLayout = txt;
        return fragment;
    }

    private static final String TAG = "CustomFragment";


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sample, container, false);

        datePicker = v.findViewById(R.id.datePicker);
        try {
            Calendar dateCalender = Calendar.getInstance();

            String dateString = dateCalender.get(Calendar.YEAR) + "/" +
                    (dateCalender.get(Calendar.MONTH) + 1) + "/" +
                    dateCalender.get(Calendar.YEAR);

            startDate = endDate = datePicker.getDayOfMonth() + "/" +
                    (datePicker.getMonth() + 1) + "/" +
                    String.valueOf(datePicker.getYear()).substring(2, 4);

            datePicker.setMinDate(new SimpleDateFormat("dd/MM/yyyy", Locale.UK).parse("01/01/2018").getTime());
            datePicker.setMaxDate(new SimpleDateFormat("dd/MM/yyyy", Locale.UK).parse(dateString).getTime());

            datePicker.init(dateCalender.get(Calendar.YEAR),
                    dateCalender.get(Calendar.MONTH),
                    dateCalender.get(Calendar.DAY_OF_MONTH),
                    (view, year, monthOfYear, dayOfMonth) -> {
                        if (tabLayout.getSelectedTabPosition() == 0) {
                            startDate = dayOfMonth + "/" +
                                    (monthOfYear + 1) + "/" +
                                    String.valueOf(year).substring(2, 4);
                        } else if (tabLayout.getSelectedTabPosition() == 1) {
                            endDate = dayOfMonth + "/" +
                                    (monthOfYear + 1) + "/" +
                                    String.valueOf(year).substring(2, 4);
                        }

                    });

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return v;
    }
}