package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.adapters.CustomAdapter;

import org.jetbrains.annotations.NotNull;

public class TabbedDialog extends DialogFragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    Button okButton;
    ImageView closeButton;


    public interface OnDismissListener {
        void onDismiss(TabbedDialog myDialogFragment);
    }

    private OnDismissListener onDismissListener;
    public void setDissmissListener(@Nullable OnDismissListener dissmissListener) {
        this.onDismissListener = dissmissListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.dialog_sample, container, false);

        okButton = rootview.findViewById(R.id.okButton);
        tabLayout = rootview.findViewById(R.id.tabLayout);
        viewPager = rootview.findViewById(R.id.masterViewPager);
        closeButton = rootview.findViewById(R.id.closeButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == 0){
                    okButton.setText("Finish");
                    viewPager.setCurrentItem(1);
                }
                else {
                    if (getActivity() != null) {
                        dismiss();
                    }
                }
            }
        });

        CustomAdapter adapter = new CustomAdapter(getChildFragmentManager());
        adapter.addFragment("Start Date", CustomFragment.createInstance(tabLayout));
        adapter.addFragment("End Date", CustomFragment.createInstance(tabLayout));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    dismiss();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    okButton.setText("Next");
                }
                else if (tab.getPosition() == 1){
                    okButton.setText("Finish");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootview;
    }


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (Resources.getSystem().getDisplayMetrics().heightPixels)/2;
        params.height = (Resources.getSystem().getDisplayMetrics().heightPixels)/2;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }
    }
}
