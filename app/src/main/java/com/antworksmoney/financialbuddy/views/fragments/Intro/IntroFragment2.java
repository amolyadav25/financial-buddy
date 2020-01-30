package com.antworksmoney.financialbuddy.views.fragments.Intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antworksmoney.financialbuddy.R;


public class IntroFragment2 extends Fragment {
    public static IntroFragment2 newInstance() {
        return new IntroFragment2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearest_hospital_intro, container, false);
    }
}
