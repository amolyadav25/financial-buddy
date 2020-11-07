package com.antworksmoney.financialbuddy.views.fragments.Network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.helpers.adapters.ContactsDataViewAdapter;
import com.antworksmoney.financialbuddy.views.activities.ContactsPickerActivity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED;


public class MyNetworkHome extends Fragment {

    public MyNetworkHome() {
        // Required empty public constructor
    }

    public static MyNetworkHome newInstance() {
        return new MyNetworkHome();
    }

    private Toolbar top_toolBar;

    private FloatingActionButton addMoreContacts;

    private RecyclerView myNetworkContactList;

    private Db_Helper databaseObject;

    private ArrayList<ProfileInfo> sosContactList;

    private Context mContext;

    private ContactsDataViewAdapter contactDataAdapter;

    private static final int PERMISSION_REQUEST_CONTACT = 405;

    private final int SELECT_PHONE_NUMBER = 400;

    private CoordinatorLayout snackBarView;

    private static final String TAG = "MyNetworkHome";

    private AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_my_network_home, container, false);

        mContext = getContext();

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        addMoreContacts = rootView.findViewById(R.id.addMoreContacts);

        myNetworkContactList = rootView.findViewById(R.id.myNetworkContactList);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        databaseObject = new Db_Helper(mContext);

        mActivity = (AppCompatActivity) getActivity();

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        sosContactList = new ArrayList<>();

        myNetworkContactList.setLayoutManager(new LinearLayoutManager(mContext));

        myNetworkContactList.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (databaseObject.getSosContacts().size() > 0) {
            sosContactList = databaseObject.getSosContacts();
            contactDataAdapter = new ContactsDataViewAdapter(mContext, sosContactList, myNetworkContactList);
            myNetworkContactList.setAdapter(contactDataAdapter);
        }

        top_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mActivity)
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        addMoreContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForContactPermission();
            }
        });
    }


    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CONTACT);
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }


    private void getContact() {

        Intent intent = new Intent(mContext, ContactsPickerActivity.class);
        startActivityForResult(intent, SELECT_PHONE_NUMBER);
    }

    public void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "On Activity result");

        if (requestCode == SELECT_PHONE_NUMBER) {
            if (resultCode == Activity.RESULT_OK) {

                ArrayList<ProfileInfo> myList = (ArrayList<ProfileInfo>) data.getSerializableExtra("contactList");

                for (int i = 0; i < myList.size(); i++) {
                    String phoneNumber = myList.get(i).getPhoneNumber();

                    if (phoneNumber.contains("+91")) {
                        Log.e("LIST DATA", "+91");
                        phoneNumber = phoneNumber.replace("+91", "").trim();
                    }

                    if (phoneNumber.contains("-")) {
                        Log.e("LIST DATA", "contains -");
                        phoneNumber = phoneNumber.replace("-", "").trim();
                    }

                    if (phoneNumber.trim().contains(" ")) {
                        Log.e("LIST DATA", "empty space");
                        phoneNumber = phoneNumber.trim().replace(" ", "").trim();
                    }

                    databaseObject.insertContactNumber(phoneNumber, myList.get(i).getName());
                    sosContactList.add(new ProfileInfo(myList.get(i).getName(), phoneNumber));

                }

                if (contactDataAdapter != null) {
                    contactDataAdapter.notifyDataSetChanged();
                } else {
                    contactDataAdapter = new ContactsDataViewAdapter(mContext, sosContactList, myNetworkContactList);
                    myNetworkContactList.setAdapter(contactDataAdapter);
                }

            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CONTACT) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContact();
            } else {
                showSnackBar("Can't proceed further, Please provide permission !!! (-> App settings) ");
            }
        }
    }

}
