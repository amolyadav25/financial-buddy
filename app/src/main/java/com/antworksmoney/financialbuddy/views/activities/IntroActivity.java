package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Graphics.OnSwipeTouchListener;
import com.antworksmoney.financialbuddy.views.fragments.Intro.IntroFragment1;
import com.antworksmoney.financialbuddy.views.fragments.Intro.IntroFragment2;
import com.antworksmoney.financialbuddy.views.fragments.Intro.IntroFragment3;
import com.antworksmoney.financialbuddy.views.fragments.Intro.IntroFragment4;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton;
    private RelativeLayout sliderLayout;
    private TextView firstSelected,
            secondSelected,
            thirdSelected,
            fourthSelected;
    private static final String TAG = "IntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        nextButton = findViewById(R.id.nextButton);

        firstSelected = findViewById(R.id.firstSelected);

        secondSelected = findViewById(R.id.secondSelected);

        sliderLayout = findViewById(R.id.sliderLayout);

        thirdSelected = findViewById(R.id.thirdSelected);

        fourthSelected = findViewById(R.id.fourthSelected);


        sliderLayout.setOnTouchListener(new OnSwipeTouchListener(IntroActivity.this) {

            public void onSwipeRight() {
                Log.w(TAG, "onSwipeRight");
                changeToNextFragment("Back");
            }

            public void onSwipeLeft() {
                Log.w(TAG, "onSwipeLeft");
                changeToNextFragment("Next");
            }
        });


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.introParentFrame, IntroFragment1.newInstance());
        transaction.commit();


        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.introParentFrame);

        Fragment fragmentToReplace = null;

        switch (view.getId()) {
            case R.id.nextButton:

                if (currentFragment instanceof IntroFragment1) {
                    fragmentToReplace = IntroFragment2.newInstance();
                    firstSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    secondSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                    thirdSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    fourthSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                } else if (currentFragment instanceof IntroFragment2) {
                    fragmentToReplace = IntroFragment3.newInstance();
                    firstSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    secondSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    thirdSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                    fourthSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                } else if (currentFragment instanceof IntroFragment3) {
                    fragmentToReplace = IntroFragment4.newInstance();
                    firstSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    secondSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    thirdSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
                    fourthSelected.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                    nextButton.setText("Finish !!");
                } else if (currentFragment instanceof IntroFragment4) {
                    fragmentToReplace = null;
                }

                if (fragmentToReplace != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(
                            R.anim.enter_from_right,
                            R.anim.exit_to_left,
                            R.anim.enter_from_left,
                            R.anim.exit_to_right
                           );
                    transaction.replace(R.id.introParentFrame, fragmentToReplace);
                    transaction.commit();
                } else {
                    Intent nextPageIntent = new Intent(IntroActivity.this, UserAuthActivity.class);
                    nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(nextPageIntent);
                    finish();
                }

                break;
        }

    }


    private void changeToNextFragment(String toChangeText){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.introParentFrame);
        Fragment fragmentToReplace = null;

        if (toChangeText.trim().equalsIgnoreCase("Next")) {
            if (currentFragment instanceof IntroFragment1) {
                fragmentToReplace = IntroFragment2.newInstance();
                changeUi(1);
            } else if (currentFragment instanceof IntroFragment2) {
                fragmentToReplace = IntroFragment3.newInstance();
                changeUi(2);

            } else if (currentFragment instanceof IntroFragment3) {
                fragmentToReplace = IntroFragment4.newInstance();
                changeUi(3);
                nextButton.setText("Finish !!");
            }

            if (fragmentToReplace != null)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                );
                transaction.replace(R.id.introParentFrame, fragmentToReplace);
                transaction.addToBackStack(null).commit();
            } else
            {
                Intent nextPageIntent = new Intent(IntroActivity.this, UserAuthActivity.class);
                nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                nextPageIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(nextPageIntent);
                finish();
            }

        }
        else if (toChangeText.trim().equalsIgnoreCase("Back")){

            if (currentFragment instanceof IntroFragment2) {
                getSupportFragmentManager().popBackStackImmediate();
                changeUi(0);
            } else if (currentFragment instanceof IntroFragment3) {
                getSupportFragmentManager().popBackStackImmediate();
                changeUi(1);

            } else if (currentFragment instanceof IntroFragment4) {
                getSupportFragmentManager().popBackStackImmediate();
                nextButton.setText("Next !!");
                changeUi(2);
            }
        }

    }

    private void changeUi(int position){
        RelativeLayout selectorLayout = findViewById(R.id.selectorLayout);
        for (int i=0; i<selectorLayout.getChildCount();i++){
            if (i==position){
                TextView view = (TextView) selectorLayout.getChildAt(i);
                view.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
            }
            else {
                TextView view = (TextView) selectorLayout.getChildAt(i);
                view.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.contact_view_circle_black));
            }
        }

    }
}

