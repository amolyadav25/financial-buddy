package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LBOccupationSelectorFragment extends Fragment {

    public LBOccupationSelectorFragment() {
        // Required empty public constructor
    }


    public static LBOccupationSelectorFragment newInstance() {
        return new LBOccupationSelectorFragment();
    }

    private Spinner occupationSelector;

    private ProgressBar occpationSelectorLoader, journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private ArrayAdapter<String> adapterOccupation;

    private static final String TAG = "OccupationSelectorFragm";

    private RequestQueue dataRequestQueue;

    private AppCompatActivity mActivity;

    private TextView loanHeading;

    private SharedPreferences mSharedPreferences;

    private CoordinatorLayout snackBarView;

    private HashMap<String, String> dataIdMap, dataNameMap;

    private String occupationId = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_occupation_status_selector, container, false);

        mActivity = (AppCompatActivity) getActivity();

        occpationSelectorLoader = rootView.findViewById(R.id.occupationSelectorLoader);

        occupationSelector = rootView.findViewById(R.id.occupationSelector);

        dataRequestQueue = Volley.newRequestQueue(getContext());

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        dataIdMap = new HashMap<>();

        dataNameMap = new HashMap<>();

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        loanHeading.setText(mSharedPreferences.getString(AppConstant.LOAN_NAME, ""));

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getOccupationDataFromAPI();

        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (occupationSelector.getSelectedItem().toString().equals("Select Occupation")) {
                    ((HomeActivity) mActivity).showSnackBar("Please select Occupation !!!");
                } else updateOccupation();
            }
        });

        previousButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        journeyCompletedProgressBar.setProgress(51);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        if (!(mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7A"))
                && (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7AA")
                && (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7B")
                && (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7C")
                && (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("7D")))))) {

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "6");
            editor.apply();

        }


    }

    private void getOccupationDataFromAPI() {

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.commonAPIUrl + "commonapi/getOccuption",
                null,
                response -> {
                    Log.i(TAG, response.toString());
                    occpationSelectorLoader.setVisibility(View.INVISIBLE);

                    try {
                        JSONArray dataArray = response.getJSONArray("occupation_list");

                        ArrayList<String> dataList = new ArrayList<>();

                        for (int i = 0; i < dataArray.length(); i++) {

                            dataList.add(dataArray.getJSONObject(i).getString("name"));

                            dataIdMap.put(dataArray.getJSONObject(i).getString("name").trim(),
                                    dataArray.getJSONObject(i).getString("id").trim());

                            dataNameMap.put(dataArray.getJSONObject(i).getString("id").trim(),
                                    dataArray.getJSONObject(i).getString("name").trim());

                        }
                        dataList.add("Select Occupation");

                        adapterOccupation = new ArrayAdapter<String>(mActivity, R.layout.checked_text_view, dataList) {
                            @Override
                            public int getCount() {
                                return dataList.size() - 1;
                            }
                        };

                        adapterOccupation.setDropDownViewResource(R.layout.checked_text_view);

                        occupationSelector.setAdapter(adapterOccupation);

                        if (!occupationId.trim().equalsIgnoreCase("")) {
                            String selectedQualification = dataNameMap.get(occupationId.trim());
                            int selectedPosition = dataList.size() - 1;
                            for (int i = 0; i < dataList.size(); i++) {
                                if (selectedQualification.trim().equalsIgnoreCase(dataList.get(i))) {
                                    selectedPosition = i;
                                    break;
                                }
                            }
                            occupationSelector.setSelection(selectedPosition);
                        } else {
                            occupationSelector.setSelection(dataList.size() - 1);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    occupationSelector.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", mSharedPreferences.getString("loginToken", ""));
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataRequestQueue.add(dataRequest);
    }

    private void updateOccupation() {

        occpationSelectorLoader.setVisibility(View.VISIBLE);

        occupationId = dataIdMap.get(occupationSelector.getSelectedItem().toString().trim());

        Map<String, String> params = new HashMap<>();
        params.put("occuption_id", occupationId);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.borrowerBaseUrl + "borrowerres/occuptionUpdate",
                new JSONObject(params),
                response -> {

                    occpationSelectorLoader.setVisibility(View.INVISIBLE);

                    Log.e(TAG, response.toString());

                    try {
                        if (response.getString("status").trim().equalsIgnoreCase("1")) {
                            showSnackBar("Occupation added successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to add !!", R.color.red);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            showSnackBar("Failed to add !!", R.color.red);
                        } catch (Exception ignored) {
                        }

                    }

                }, error -> {
            Log.e(TAG, error.toString());
            occpationSelectorLoader.setVisibility(View.INVISIBLE);
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

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(AppConstant.OCCUPATION_ID, occupationId);
                    editor.apply();

                    if (occupationId.trim().equalsIgnoreCase("1")) {
                        transaction.replace(R.id.homeParent, LBCompanySelectorFragment.newInstance());
                    } else if (occupationId.trim().equalsIgnoreCase("2")) {
                        transaction.replace(R.id.homeParent, LBSelfEmployedBusinessFragment.newInstance());
                    } else if (occupationId.trim().equalsIgnoreCase("3")) {
                        transaction.replace(R.id.homeParent, LBSelfEmployedProfessionalFragment.newInstance());
                    } else {
                        transaction.replace(R.id.homeParent, LBSelfOtherProfessionFragment.newInstance());
                    }
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }
}
