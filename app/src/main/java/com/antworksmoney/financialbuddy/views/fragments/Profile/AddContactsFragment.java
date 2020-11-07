package com.antworksmoney.financialbuddy.views.fragments.Profile;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.helpers.adapters.ContactsDataViewAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.ContactsPickerActivity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED;


public class AddContactsFragment extends Fragment implements View.OnClickListener {

    private static int baseFrameLayout;

    private static final int PERMISSION_REQUEST_CONTACT = 405;

    private static JSONObject mDataObject;

    public static AddContactsFragment newInstance(int mBaseFrameLayout, JSONObject dataObject) {
        mDataObject = dataObject;
        baseFrameLayout = mBaseFrameLayout;
        return new AddContactsFragment();
    }


    private CoordinatorLayout snackBarView;

    private TextView openContactsButton;

    private final int SELECT_PHONE_NUMBER = 400,
            MAX_PICK_CONTACT = 20;

    private Context mContext;

    private FragmentActivity mActivity;

    private RecyclerView contactslist;

    private Button submit_button;

    private SharedPreferences pref;

    private ProgressBar progressHUD;

    private static final String TAG = "SosContactSelector";

    private Db_Helper databaseObject;

    private ImageView howItWorksText;

    private ArrayList<ProfileInfo> sosContactList, completeSOSList;

    private Button skipButton;

    private ContactsDataViewAdapter contactDataAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_contacts, container, false);

        mContext = getContext();

        mActivity =  getActivity();

        openContactsButton = rootView.findViewById(R.id.openContactsButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        contactslist = rootView.findViewById(R.id.contactslist);

        submit_button = rootView.findViewById(R.id.submit_button);

        progressHUD = rootView.findViewById(R.id.progressHud);

        skipButton = rootView.findViewById(R.id.skipButton);

        howItWorksText = rootView.findViewById(R.id.howItWorksText);

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        sosContactList = new ArrayList<>();

        completeSOSList = new ArrayList<>();

        databaseObject = new Db_Helper(mContext);

        contactslist.setLayoutManager(new LinearLayoutManager(mContext));

        contactslist.setHasFixedSize(true);

        if (databaseObject.getSosContacts().size() > 0) {
            sosContactList = databaseObject.getSosContacts();
            contactDataAdapter = new ContactsDataViewAdapter(mContext, sosContactList, contactslist);
            contactslist.setAdapter(contactDataAdapter);
        }

        skipButton.setOnClickListener(this);

        openContactsButton.setOnClickListener(this);

        submit_button.setOnClickListener(this);

        howItWorksText.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openContactsButton:
                askForContactPermission();
                break;

            case R.id.skipButton:
                Intent moveToHomeScreen = new Intent(mActivity, HomeActivity.class);
                mContext.startActivity(moveToHomeScreen);
                mActivity.finish();

            case R.id.submit_button:
                submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgrounddisabled));
                sendDataToServer();
                break;

            case R.id.howItWorksText:
                showHowItWorksDialog();
                break;


        }

    }

    private void showHowItWorksDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("It is important to focus on growing your network, so that you create an infinite number of opportunities for yourself\n\nYou can share Blogs, videos and URLs with your friends in Buddy Network and can earn referral rewards\n\nSo, stop thinking and start creating your Buddy Network");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_info_outline_black_24dp);
        builder1.setTitle("How it Works ?");
        builder1.setPositiveButton("OK", (dialog, id) -> {
            dialog.cancel();
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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

        Intent intent = new Intent(mActivity, ContactsPickerActivity.class);
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
                        phoneNumber = phoneNumber.replace("+91", "").trim();
                    }

                    if (phoneNumber.contains("-")) {
                        phoneNumber = phoneNumber.replace("-", "").trim();
                    }

                    if (phoneNumber.trim().contains(" ")) {
                        phoneNumber = phoneNumber.trim().replace(" ", "").trim();
                    }

                    databaseObject.insertContactNumber(phoneNumber, myList.get(i).getName());
                    sosContactList.add(new ProfileInfo(myList.get(i).getName(), phoneNumber));

                }

                completeSOSList = (ArrayList<ProfileInfo>) data.getSerializableExtra("completeList");

//                Log.e("COMPLETE-LIST", String.valueOf(completeSOSList.size()));
//
//                for (int i = 0; i < completeSOSList.size(); i++) {
//
//                    String phoneNumber = completeSOSList.get(i).getPhoneNumber();
//
//                    if (phoneNumber.contains("+91")) {
//                        phoneNumber = phoneNumber.replace("+91", "").trim();
//                    }
//
//                    if (phoneNumber.contains("-")) {
//                        phoneNumber = phoneNumber.replace("-", "").trim();
//                    }
//
//                    if (phoneNumber.trim().contains(" ")) {
//                        phoneNumber = phoneNumber.trim().replace(" ", "").trim();
//                    }
//
//                    completeSOSList.add(new ProfileInfo(myList.get(i).getName(), phoneNumber));
//
//                }


                if (contactDataAdapter != null) {
                    contactDataAdapter.notifyDataSetChanged();
                } else {
                    contactDataAdapter = new ContactsDataViewAdapter(mContext, sosContactList, contactslist);
                    contactslist.setAdapter(contactDataAdapter);
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


    private void sendDataToServer() {

        progressHUD.setVisibility(View.VISIBLE);

        try {

            JSONObject contactObject;

            JSONArray contactArray = new JSONArray();

            for (int i = 0; i < completeSOSList.size(); i++) {
                ProfileInfo info = completeSOSList.get(i);
                contactObject = new JSONObject();
                contactObject.put("name", info.getName());
                contactObject.put("number", info.getPhoneNumber());
                contactArray.put(contactObject);
            }

            mDataObject.put("my_network_contacts", contactArray);

            JSONObject outerObject = new JSONObject();
            outerObject.put("userData", mDataObject);

            Log.e("AmolTAG",outerObject.toString());

            JsonObjectRequest updateProfileDataReques = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "update-profile",
                    outerObject, response -> {
                try {
                    progressHUD.setVisibility(View.GONE);
                    submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));

                    Log.e(TAG, response.toString());

                    JSONObject data = response.getJSONObject("respone").getJSONObject("UserData");

                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("email_value", data.getString("mail"));
                    editor.putString("user_name", data.getString("name"));
                    editor.putString("user_phone", data.getString("contact"));
                    editor.putString("date_of_birth", data.getString("date_of_birth"));
                    editor.putString("gender", data.getString("gender"));
                    editor.putString("marital_status", data.getString("marital_status"));
                    editor.putString("profession", data.getString("profession"));
                    editor.putString("education", data.getString("education"));
                    editor.putString("invest_in_market", data.getString("invest_in_market"));
                    editor.putString("own_a_house", data.getString("own_a_house"));
                    editor.putString("user_dob",data.getString("date_of_birth"));
                    editor.putString("net_monthly_income", data.getString("net_monthly_income"));
                    editor.putString("provide_financial_consultancy_service", data.getString("provide_financial_consultancy_service"));
                    editor.putString("interested_in_financial_consultancy", data.getString("interested_in_financial_consultancy"));
                    editor.apply();

                    databaseObject.deleteContactsFromTable();

                    for (int i = 0; i < data.getJSONArray("my_network_contacts").length(); i++) {
                        databaseObject.insertContactNumber(
                                data.getJSONArray("my_network_contacts")
                                        .getJSONObject(i)
                                        .getString("number"),

                                data.getJSONArray("my_network_contacts")
                                        .getJSONObject(i)
                                        .getString("name"));

                    }

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(mActivity, HomeActivity.class);
                        mContext.startActivity(intent);
                    }, 100);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            },
                    error -> {
                        progressHUD.setVisibility(View.GONE);
                        submit_button.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
                        Log.e("error", error.toString());
                        showSnackBar("Failed to send data to server.....");
                    });

            updateProfileDataReques.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            updateProfileDataReques.setShouldCache(false);

            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(updateProfileDataReques);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
