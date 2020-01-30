package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Loan.DateOfBirthFragment;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LBGenderSelectorFragment extends Fragment {
    public LBGenderSelectorFragment() {
        // Required empty public constructor
    }

    private String genderId = "";

    public static LBGenderSelectorFragment newInstance() {
        return new LBGenderSelectorFragment();
    }


    private ImageView genderMale, genderFemale;

    private Activity mActivity;

    private Context mContext;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private TextView journeyCompletedProgressText;

    private ProgressBar genderSelectorProgressBar, journeyCompletedProgressBar;

    private Toolbar top_toolBar;

    private static final String TAG = "GenderSelectorFragment";

    private TextView loanHeading;

    private SharedPreferences mSharedPreferences;

    private CoordinatorLayout snackBarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gender_selector, container, false);

        mActivity = getActivity();

        mContext = getContext();

        genderFemale = rootView.findViewById(R.id.genderFemale);

        genderMale = rootView.findViewById(R.id.genderMale);

        genderSelectorProgressBar = rootView.findViewById(R.id.genderSelectorProgressBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        genderSelectorProgressBar.setVisibility(View.INVISIBLE);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);


        genderMale.setOnClickListener(view -> {
            genderId = "1";
            genderMale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            genderFemale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
        });

        genderFemale.setOnClickListener(view -> {
            genderId = "2";
            genderFemale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            genderMale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
        });

        nextButtonForQuestions.setOnClickListener(v -> {
            if (genderId.equalsIgnoreCase("")) {
                showSnackBar("Please select gender !!!", R.color.red);
            } else {
                addGenderDetails();
            }
        });

        previousButtonForQuestions.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        loanHeading.setText(mSharedPreferences.getString(AppConstant.LOAN_NAME, ""));

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        journeyCompletedProgressBar.setProgress(10);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));

        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("3")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "2");
            editor.apply();
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (genderId.trim().equalsIgnoreCase("1")) {
            genderId = "1";
            genderMale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            genderFemale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
        } else if (genderId.trim().equalsIgnoreCase("2")) {
            genderId = "2";
            genderFemale.setBackground(mContext.getResources().getDrawable(R.drawable.buttonbackgroundenabled));
            genderMale.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_white));
        }
    }

    private void addGenderDetails() {

        genderSelectorProgressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("gender", genderId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/genderUpdate",
                new JSONObject(params),
                response -> {

                    genderSelectorProgressBar.setVisibility(View.GONE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Gender added successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to add !!", R.color.red);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar("Failed to add !!", R.color.red);
                    }

                }, error -> {
            Log.e(TAG, error.toString());
            genderSelectorProgressBar.setVisibility(View.GONE);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBDateOfBirthFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}
