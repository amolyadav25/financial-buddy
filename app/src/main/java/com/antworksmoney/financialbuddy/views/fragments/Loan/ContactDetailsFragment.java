package com.antworksmoney.financialbuddy.views.fragments.Loan;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.antworksmoney.financialbuddy.PersonalLoanPOJO.LoginPersonalBody;
import com.antworksmoney.financialbuddy.PersonalLoanPOJO.PersonalLoanSuccessPOJO;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.AllApiInterface;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Loan.PaySenseURL.ApiClient;
import com.google.gson.Gson;

import org.apache.http.client.protocol.RequestDefaultHeaders;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.Part;

import static android.content.Context.MODE_PRIVATE;


public class ContactDetailsFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public static ContactDetailsFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new ContactDetailsFragment();
    }

    private EditText et_reg_fname,
            et_user_mail,et_reg_lname,
            et_user_phone_number,
            nationality;

    private SharedPreferences pref;

    private AppCompatActivity mActivity;

    private ProgressBar journeyCompletedProgressBar;

    private TextView journeyCompletedProgressText;

    private Toolbar top_toolBar;

    private Button previousButtonForQuestions,
            nextButtonForQuestions;

    private static final String TAG = "ContactDetailsFragment";

    private TextView loanHeading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        mActivity = (AppCompatActivity) getActivity();

        pref = mActivity.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        et_reg_fname = rootView.findViewById(R.id.et_reg_fname);

        et_user_mail = rootView.findViewById(R.id.et_user_mail);

        et_reg_lname = rootView.findViewById(R.id.et_reg_lname);

        et_user_phone_number = rootView.findViewById(R.id.et_user_phone_number);

        nationality = rootView.findViewById(R.id.et_nationality);

        previousButtonForQuestions = rootView.findViewById(R.id.previousButtonForQuestions);

        nextButtonForQuestions = rootView.findViewById(R.id.nextButtonForQuestions);

        journeyCompletedProgressBar = rootView.findViewById(R.id.journeyCompletedProgressBar);

        journeyCompletedProgressText = rootView.findViewById(R.id.journeyCompletedProgressText);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        loanHeading = rootView.findViewById(R.id.loanHeading);

        loanHeading.setText(mLoanInfoEntity.getLoanName());

        if (mLoanInfoEntity.getLoanApplyFor().trim().equalsIgnoreCase("Self")) {

            et_reg_fname.setText(mLoanInfoEntity.getName());

            et_user_mail.setText(mLoanInfoEntity.getEmail());

            et_user_phone_number.setText(mLoanInfoEntity.getPhoneNumber());
        }


        nextButtonForQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoanInfoEntity.setName(et_reg_fname.getText().toString().trim());
                mLoanInfoEntity.setEmail(et_user_mail.getText().toString().trim());
                mLoanInfoEntity.setPhoneNumber(et_user_phone_number.getText().toString().trim());
                mLoanInfoEntity.setNationality(nationality.getText().toString().trim());

                if (mLoanInfoEntity.getName().equals("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Name !!!");
                }  else if(et_reg_lname.getText().toString().isEmpty())
                {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Last Name !!!");
                }
                else if (mLoanInfoEntity.getEmail().trim().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Email !!!");
                } else if (mLoanInfoEntity.getPhoneNumber().trim().equalsIgnoreCase("")) {
                    ((HomeActivity) mActivity).showSnackBar("Please enter borrower Phone Number !!!");
                } else changeFragment();
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

        journeyCompletedProgressBar.setProgress(37);
        journeyCompletedProgressText.setText(MessageFormat.format("{0} %",
                String.valueOf(journeyCompletedProgressBar.getProgress())));


        return rootView;
    }
    private void personalLoanRegister() {
        mLoanInfoEntity.setLast_name(et_reg_lname.getText().toString());

        AllApiInterface apiService =
                ApiClient.getClient().create(AllApiInterface.class);
        Log.e("Mytag","phone"+mLoanInfoEntity.getPhoneNumber());
        Log.e("Mytag","emailpayu"+mLoanInfoEntity.getEmail());
        RequestBody phoneBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,  mLoanInfoEntity.getPhoneNumber());
        RequestBody emailBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,  mLoanInfoEntity.getEmail());
        RequestBody phone_verifiedBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, "true");
        RequestBody consumer_bureau_pull_consentBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,  "true");
        RequestBody terms_acceptedBody =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,  "true");
//        RequestDefaultHeaders xpssourcekeyBody =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM,  "X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
       // RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), mLoanInfoEntity.getPhoneNumber());
       //// RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), mLoanInfoEntity.getEmail());
       // RequestBody phone_verifiedBody = RequestBody.create(MediaType.parse("text/plain"), "true");
       // RequestBody consumer_bureau_pull_consentBody = RequestBody.create(MediaType.parse("text/plain"), "true");
       // RequestBody terms_acceptedBody = RequestBody.create(MediaType.parse("text/plain"), "true");
    // RequestBody xpssourcekeyBody = RequestBody.create(MediaType.parse("text/plain"), "X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
        Call<PersonalLoanSuccessPOJO> call = apiService.personalloanregistration(phoneBody,emailBody,phone_verifiedBody,consumer_bureau_pull_consentBody,terms_acceptedBody,"X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
        call.enqueue(new Callback<PersonalLoanSuccessPOJO>() {
            @Override
            public void onResponse(Call<PersonalLoanSuccessPOJO>call, Response<PersonalLoanSuccessPOJO> response) {
Log.e("Mytag","ResponseProblem"+response.errorBody());

                if (response.code() == 400 ) {
                    Log.d(TAG, "onResponse - Status : " + response.code());
                    Gson gson = new Gson();
                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                    try {
                        if (response.errorBody() != null)
                            Log.e("Mytag","response"+response.errorBody().string());
                        AllApiInterface apiService =
                                ApiClient.getClient().create(AllApiInterface.class);
                        Log.e("Mytag","phone"+mLoanInfoEntity.getPhoneNumber());
                        Log.e("Mytag","email"+mLoanInfoEntity.getEmail());
                        Map<String, String> map = new HashMap<>();
                        map.put("x-ps-source-key",  "X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
                        map.put("x-sessionid",  "a5r59cwcyx0eb8lljq9ded1gfnchouft");
                        LoginPersonalBody body = new LoginPersonalBody();
                        body.setPhone(mLoanInfoEntity.getPhoneNumber());
                        Call<PersonalLoanSuccessPOJO> call2 = apiService.personalloanlogin(map,body);
                        call2.enqueue(new Callback<PersonalLoanSuccessPOJO>() {
                            @Override
                            public void onResponse(Call<PersonalLoanSuccessPOJO>call, Response<PersonalLoanSuccessPOJO> response) {

                                if (response.code() == 400 ) {
                                    Log.d(TAG, "onResponse - Status : " + response.code());
                                    Gson gson = new Gson();
                                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                                    try {
                                        if (response.errorBody() != null)
                                            Log.e("Mytag","response"+response.errorBody().string());

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else
                                {
                                    Log.e("Mytag","ResponseSessionBody"+response.body());
                                    Log.e("Mytag","ResponseSession"+response.body().getSessionToken());
                                    mLoanInfoEntity.setX_sessionid(response.body().getSessionToken());
                                }

                            }

                            @Override
                            public void onFailure(Call<PersonalLoanSuccessPOJO> call2, Throwable t) {
                                // Log error here since request failed
                                Log.e("Mytag", t.toString());
                            }
                        });
                        //   registerResponse =
                        //// adapter.fromJson(
                        //   response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else
                {
                    Log.d(TAG, "onResponse - Status : " + response.code());
                    Gson gson = new Gson();
                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                    try {
                        if (response.errorBody() != null)
                            Log.e("Mytag","response"+response.errorBody().string());
                        AllApiInterface apiService =
                                ApiClient.getClient().create(AllApiInterface.class);
                        Log.e("Mytag","phone"+mLoanInfoEntity.getPhoneNumber());
                        Log.e("Mytag","email"+mLoanInfoEntity.getEmail());
                        Map<String, String> map = new HashMap<>();
                        map.put("x-ps-source-key",  "X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
                        Log.e("Mytag","getSessionToken"+response.body().getSessionToken());
                        map.put("x-sessionid",  response.body().getSessionToken());
                        LoginPersonalBody body = new LoginPersonalBody();
                        body.setPhone(mLoanInfoEntity.getPhoneNumber());
                        Call<PersonalLoanSuccessPOJO> call2 = apiService.personalloanlogin(map,body);
                        call2.enqueue(new Callback<PersonalLoanSuccessPOJO>() {
                            @Override
                            public void onResponse(Call<PersonalLoanSuccessPOJO>call, Response<PersonalLoanSuccessPOJO> response) {

                                if (response.code() == 400 ) {
                                    Log.d(TAG, "onResponse - Status : " + response.code());
                                    Gson gson = new Gson();
                                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                                    try {
                                        if (response.errorBody() != null)
                                            Log.e("Mytag","response"+response.errorBody().string());

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else
                                {
                                    Log.e("Mytag","ResponseSession"+response.body().getSessionToken());
                                    mLoanInfoEntity.setX_sessionid(response.body().getSessionToken());
                                }

                            }

                            @Override
                            public void onFailure(Call<PersonalLoanSuccessPOJO> call2, Throwable t) {
                                // Log error here since request failed
                                Log.e("Mytag", t.toString());
                            }
                        });
                        //   registerResponse =
                        //// adapter.fromJson(
                        //   response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Log.e("Mytag","response"+response.errorBody().string());
//                if(response.body()!=null) {
//                    Log.e("Mytag", "response" + response.body().getMessage());
//                    Log.e("Mytag", "response" + response.body().getCode());
//                    Log.e("Mytag", "response" + response.body().getDetails().size());
//                }
                //List<Movie> movies = response.body().getResults();
                //  Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<PersonalLoanSuccessPOJO> call, Throwable t) {
                // Log error here since request failed
                Log.e("Mytag", t.toString());
            }
        });
    }
    private void personalLoanLogin() {
    }
    private void changeFragment() {
        if (getActivity() != null) {
            personalLoanRegister();

            Fragment fragmentToReplace;
            if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Loan Against Property")) {
                fragmentToReplace = OtherDetailsFragment.newInstance(mLoanInfoEntity);
            }
            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Credit Card")){
                fragmentToReplace = SalaryProcessFragment.newInstance(mLoanInfoEntity);
            }
            else if (mLoanInfoEntity.getLoanName().trim().equalsIgnoreCase("Instant Loan")){
                fragmentToReplace = LoanAmountFragment.newInstance(mLoanInfoEntity);
            }
            else {
                fragmentToReplace = QualificationSelectorFragment.newInstance(mLoanInfoEntity);
            }

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }
}