package com.antworksmoney.financialbuddy.views.fragments.Training;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.SeeAllListAdapter;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.util.ArrayList;

public class SeeAllFragment extends Fragment {

    private static ArrayList<TrainingEntity> mDataList;

    private static String mPageType;

    public SeeAllFragment() {
        // Required empty public constructor
    }


    private Toolbar topToolBar;

    private RecyclerView serviceList;

    private Context mContext;



    public static SeeAllFragment newInstance(ArrayList<TrainingEntity> dataList, String pageType) {
        mPageType = pageType;
        mDataList = dataList;
        return new SeeAllFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_see_all, container, false);

        topToolBar = rootView.findViewById(R.id.toolbar);

        serviceList = rootView.findViewById(R.id.serviceList);

        mContext = getContext();


        if (topToolBar != null && getActivity() != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            topToolBar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));

            topToolBar.setTitle(mPageType);

        }

        serviceList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        serviceList.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SeeAllListAdapter dataListAdapter = new SeeAllListAdapter(getContext(), mDataList, mPageType);
        serviceList.setAdapter(dataListAdapter);
    }
}
