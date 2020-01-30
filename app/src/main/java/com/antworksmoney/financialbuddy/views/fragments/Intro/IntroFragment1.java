package com.antworksmoney.financialbuddy.views.fragments.Intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antworksmoney.financialbuddy.R;


public class IntroFragment1 extends Fragment {

    public static IntroFragment1 newInstance() {
        return new IntroFragment1();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends_help_intro, container, false);
    }
}
