package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.adapters.ProductsInCategoryPagerAdapter;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

public class LeadStatusFragment extends Fragment {
    public LeadStatusFragment() {
        // Required empty public constructor
    }

    public static LeadStatusFragment newInstance() {
        return new LeadStatusFragment();
    }

    private Toolbar toolbar;






    private TabLayout tabLayout;

    private ViewPager viewPager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lead_status, container, false);



        toolbar = rootView.findViewById(R.id.toolbar);

        viewPager = rootView.findViewById(R.id.htab_viewpager);

        tabLayout = rootView.findViewById(R.id.htab_tabs);



        if (toolbar != null && getActivity() != null) {

            toolbar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            ((HomeActivity) getActivity()).setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));

            toolbar.setTitle("");
        }

        setUpUi();

        return rootView;
    }

    private void setUpUi() {
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        if (getActivity() != null) {
            ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(getActivity().getSupportFragmentManager());
            adapter.addFrag(new InvestmentStatusFragment(),"Loan Leads");
            adapter.addFrag(new LeadStatusTabFramgent(),"Investment Leads");
            viewPager.setAdapter(adapter);
        }
    }
}
