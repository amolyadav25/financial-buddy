package com.antworksmoney.financialbuddy.views.fragments.Intro;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antworksmoney.financialbuddy.R;


public class IntroFragment3 extends Fragment {
    public static IntroFragment3 newInstance() {
        return new IntroFragment3();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearest_volunteer_intro, container, false);
    }
}
