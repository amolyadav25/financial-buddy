package com.antworksmoney.financialbuddy.views.fragments.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.adapters.NotificationListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;

import static android.content.Context.MODE_PRIVATE;

public class NotificationFragment extends Fragment {

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    private SharedPreferences mSharedPreferences;

    private Db_Helper dataBaseObject;

    private static final String TAG = "NotificationFragment";

    private Toolbar mToolbar;

    private RecyclerView notificationList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_notification, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        dataBaseObject = new Db_Helper(getContext());

        mToolbar = rootView.findViewById(R.id.top_toolBar);

        notificationList = rootView.findViewById(R.id.notificationListLoader);
        notificationList.setHasFixedSize(true);


        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(AppConstant.NEW_MESSAGE_RECIEVED,"0");
        editor.apply();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        NotificationListAdapter adapter = new NotificationListAdapter(
                getContext(),dataBaseObject.getAllLeadData(),notificationList);
        notificationList.setAdapter(adapter);
        adapter.setOnClick(position -> {
          if (getActivity() != null){
              dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
              FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
              transaction.replace(R.id.homeParent,
                      LoadUrlFragment.newInstance(dataBaseObject.getAllLeadData().get(position).getEmail(),
                              "Notification"));
              transaction.addToBackStack(null).commit();
          }
        });


    }
}
