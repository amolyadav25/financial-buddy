package com.antworksmoney.financialbuddy.views.fragments.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.WalletEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.FeedbackListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class FeedBackFragment extends Fragment implements View.OnClickListener {

    public FeedBackFragment() {
        // Required empty public constructor
    }

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    private EditText name, email, subject, message;

    private Button submitFeedbackButton;

    private SharedPreferences pref;

    private Activity mActivity;

    private CoordinatorLayout snackBarView;

    private AlertDialog dialogSocialsites;

    private View viewSocialSites;

    private AlertDialog.Builder builderSocialSites;

    private Context mContext;

    private Button returnToHome;

    private ImageView closeButton;

    private TextView sendingNotificationText;

    private TextView sendingNotificationHeading;

    private ImageView saveButton;

    private RequestQueue requestQueue;

    private ProgressBar feedbackProgressBar;

    private RecyclerView feedbackList;

    private static final String TAG = "FeedBackFragment";



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_feed_back, container, false);


        name = rootView.findViewById(R.id.nameEditText);

        email = rootView.findViewById(R.id.numberEditText);

        subject = rootView.findViewById(R.id.subjectEditText);

        message = rootView.findViewById(R.id.messageEditText);

        submitFeedbackButton = rootView.findViewById(R.id.sendFeedBackButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        saveButton = rootView.findViewById(R.id.saveButton);

        feedbackList = rootView.findViewById(R.id.feedbackList);

        feedbackProgressBar = rootView.findViewById(R.id.feedbackProgressBar);

        mActivity = getActivity();

        mContext = getContext();

        assert mActivity != null;
        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);


        name.setText(pref.getString("user_name", ""));

        email.setText(pref.getString("user_phone", ""));

        feedbackList.setLayoutManager(new LinearLayoutManager(mContext));
        feedbackList.setHasFixedSize(true);

        requestQueue = Volley.newRequestQueue(mContext);

        viewSocialSites = LayoutInflater.from(mContext).inflate(R.layout.layout_sending_notification, null);

        builderSocialSites = new AlertDialog.Builder(mContext);

        builderSocialSites.setView(viewSocialSites);

        dialogSocialsites = builderSocialSites.create();
        if (dialogSocialsites.getWindow() != null) {
            dialogSocialsites.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialogSocialsites.setCancelable(false);

        closeButton = viewSocialSites.findViewById(R.id.closeButton);

        returnToHome = viewSocialSites.findViewById(R.id.returnToHome);

        sendingNotificationText = viewSocialSites.findViewById(R.id.sendingNotificationText);

        sendingNotificationHeading = viewSocialSites.findViewById(R.id.sendingNotificationHeading);

        returnToHome.setVisibility(View.INVISIBLE);

        sendingNotificationHeading.setText("Submitting feedback");

        sendingNotificationText.setText("Please wait !! We are sending your feedback.");


        submitFeedbackButton.setOnClickListener(this);
        returnToHome.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        loadFeedbackList();

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendFeedBackButton:

                if (name == null || name.getText().toString().length() < 1) {
                    showSnackBar("Please enter a valid Name !!");
                    name.requestFocus();
                } else if (subject == null || subject.getText().toString().length() < 1) {
                    showSnackBar("Please enter a valid subject !!");
                    subject.requestFocus();
                } else if (email == null || email.getText().toString().length() < 10) {
                    showSnackBar("Please enter a valid Phone number !!");
                    email.requestFocus();
                } else {
                    hideKeyboard(mActivity);
                    dialogSocialsites.show();
                    sendFeedBackDataTOServer();
                }
                break;

            case R.id.closeButton:
                dialogSocialsites.dismiss();
                loadFeedbackList();
                break;

            case R.id.returnToHome:
                dialogSocialsites.dismiss();
                FragmentTransaction transaction = (Objects.requireNonNull(getActivity()))
                        .getSupportFragmentManager()
                        .beginTransaction();

                transaction.replace(R.id.homeParent, HomeFragment.newInstance());
                transaction.addToBackStack(null).commit();
                break;

            case R.id.saveButton:
                if (mActivity.getClass().getSimpleName().trim().equalsIgnoreCase("HomeActivity")) {
                    ((HomeActivity) mActivity).getmDrawerLayout().openDrawer(GravityCompat.START);
                }
                break;
        }

    }

    public void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendFeedBackDataTOServer(){

        JSONObject outerObject = null;
        try {

            outerObject = new JSONObject();

            JSONObject innerObject = new JSONObject();
            innerObject.put("mobile",pref.getString("user_phone", ""));
            innerObject.put("user_feedback",subject.getText().toString().trim());

            outerObject.put("userData",innerObject);

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl+"save_user_feedback",
                outerObject,
                response -> {
                    returnToHome.setVisibility(View.VISIBLE);
                    try {
                        sendingNotificationHeading.setText(response.getString("status"));
                        sendingNotificationText.setText("Your feedback has been successfully sent to us. We will get back to you shortly");
                    }catch (Exception e){
                        sendingNotificationHeading.setText("Failed !!");
                        sendingNotificationText.setText("Failed to submit your feedback ..");
                        e.printStackTrace();
                    }

                },
                error -> {
                    returnToHome.setVisibility(View.VISIBLE);
                    dialogSocialsites.show();
                    sendingNotificationHeading.setText("Failed !!");
                    sendingNotificationText.setText("Failed to submit your feedback ..");
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequest.setShouldCache(false);

        requestQueue.add(dataRequest);
    }

    private void loadFeedbackList(){
        feedbackProgressBar.setVisibility(View.VISIBLE);

        JSONObject outerObject = null;
        try {

            outerObject = new JSONObject();

            JSONObject innerObject = new JSONObject();
            innerObject.put("mobile",pref.getString("user_phone", ""));

            outerObject.put("userData",innerObject);

        } catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl+"get_user_feedback",
                outerObject,
                response -> {
                    feedbackProgressBar.setVisibility(View.GONE);
                    ArrayList<WalletEntity> walletEntityArrayList = new ArrayList<>();
                    try {
                        for (int i=0; i<response.getJSONArray("UserData").length(); i++){
                            WalletEntity entity = new WalletEntity();
                            entity.setLeadId(response.getJSONArray("UserData").getJSONObject(i).getString("user_id"));
                            entity.setBorrowerName(response.getJSONArray("UserData").getJSONObject(i).getString("user_feedback"));
                            entity.setCreatedDate(response.getJSONArray("UserData").getJSONObject(i).getString("created_date"));

                            walletEntityArrayList.add(entity);
                        }

                        FeedbackListAdapter adapter = new FeedbackListAdapter(mContext,walletEntityArrayList,feedbackList);
                        feedbackList.setAdapter(adapter);

                        adapter.setOnClick(new FeedbackListAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                                //do something you want
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                },
                error -> {
                   feedbackProgressBar.setVisibility(View.INVISIBLE);
                    Log.e(TAG,error.toString());
                });

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequest.setShouldCache(false);

        requestQueue.add(dataRequest);

    }
}
