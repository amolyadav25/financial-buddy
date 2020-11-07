package com.antworksmoney.financialbuddy.views.fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.MutualFunds.MutualLogin;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.ServiceListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.InsuranceHomeFragment;
import com.antworksmoney.financialbuddy.views.fragments.Investment.InvestmentApplyForFragment;
import com.antworksmoney.financialbuddy.views.fragments.Loan.ApplyForFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Authentication.LBOTPVerificationFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.LBfirstFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBDashBoardFragment;
import com.antworksmoney.financialbuddy.views.fragments.Training.SeeAllFragment;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private Context mContext;

    private SharedPreferences pref;

    private RecyclerView blogsList, offersList, videosList;

    private RelativeLayout loanLayout, loanBuddyLayout, mutualfund, insuranceLayout, investmentLayout, offerslayout;

    private static final String TAG = "HomeFragment";

    private TextView seeAllVideos, seeAllBlogs;

    private Toolbar toolbar;

    private AppCompatActivity mActivity;

    private ProgressBar friendSListLoader, bestHospitalLoader, chemistShopLoader;

    private RequestQueue mRequestQueue;

    private ImageView offerListErrorImage, friendErrorListImage, blogsErrorListImage;

    private ScrollView scrollView;

    private ImageView notificationIcon;

    public TextView newNotificationText;

    public Db_Helper mDatabaseObject;

    private ImageView banner_image;

    HomeActivity homeActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();

        pref = Objects.requireNonNull(getActivity()).getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        View header = ((HomeActivity) mContext).getNavigationView().getHeaderView(0);

        ProgressBar progressBar = header.findViewById(R.id.splashLoaderProgress);

        TextView progessText = header.findViewById(R.id.progessText);

        loanLayout = rootView.findViewById(R.id.loanLayout);

        bestHospitalLoader = rootView.findViewById(R.id.bestHospitalLoader);

        offerListErrorImage = rootView.findViewById(R.id.offerListErrorImage);

        friendErrorListImage = rootView.findViewById(R.id.friendErrorListImage);

        scrollView = rootView.findViewById(R.id.scroll);

        mRequestQueue = Volley.newRequestQueue(mContext);

        friendSListLoader = rootView.findViewById(R.id.friendSListLoader);

        loanBuddyLayout = rootView.findViewById(R.id.loanBuddyLayout);
        mutualfund = rootView.findViewById(R.id.mutualfund);

        seeAllVideos = rootView.findViewById(R.id.friendSeeText);

        seeAllBlogs = rootView.findViewById(R.id.bestHospitalText);

        banner_image = rootView.findViewById(R.id.banner_image);

        insuranceLayout = rootView.findViewById(R.id.insuranceLayout);

        investmentLayout = rootView.findViewById(R.id.investmentLayout);

        chemistShopLoader = rootView.findViewById(R.id.chemistShopLoader);

        blogsErrorListImage = rootView.findViewById(R.id.blogsErrorListImage);

        toolbar = rootView.findViewById(R.id.top_toolBar);

        notificationIcon = rootView.findViewById(R.id.notificationIcon);

        newNotificationText = rootView.findViewById(R.id.newNotificationText);

        mDatabaseObject = new Db_Helper(getContext());

        mActivity = (AppCompatActivity) getActivity();

        scrollView.smoothScrollTo(0, loanLayout.getBottom());

        //this will change status bar colour to give a new theme effect
//        Window window = mActivity.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(ContextCompat.getColor(mActivity,R.color.colorPrimaryDark));
//        if (getActivity() != null){
//            ((HomeActivity) getActivity())
//                    .getBottomNavigationView()
//                    .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
//        }

        int progress = 0;

        if (!pref.getString("user_name", "").trim().equalsIgnoreCase("")) {
            progress = 17;
        }

        if (!pref.getString("email_value", "").trim().equalsIgnoreCase("")) {
            progress = progress + 17;
        }

        if (!pref.getString("user_phone", "").trim().equalsIgnoreCase("")) {
            progress = progress + 17;
        }

        if (!pref.getString("user_dob", "").trim().equalsIgnoreCase("")) {
            if (!pref.getString("user_dob", "").trim().equalsIgnoreCase("0000-00-00")) {
                progress = progress + 17;
            }
        }

        if ((!pref.getString("gender", "").equalsIgnoreCase("Gender"))
                || (!pref.getString("gender", "").equalsIgnoreCase(""))) {
            progress = progress + 17;
        }

        if ((!pref.getString("marital_status", "").equalsIgnoreCase("Marital Status"))
                || (!pref.getString("marital_status", "").equalsIgnoreCase(""))) {
            progress = progress + 15;
        }

        progessText.setText(MessageFormat.format("{0} %", progress));

        progressBar.setProgress(progress);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((HomeActivity) getActivity())
                                .getmDrawerLayout()
                                .openDrawer(GravityCompat.START);

                        ((HomeActivity) getActivity())
                                .getBottomNavigationView()
                                .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });

//            userWelcomeTitle.setText("Hi "+ pref.getString("user_name", "").trim()+",\nWelcome to " + mContext.getResources().getString(R.string.app_name));


        }


        Glide.with(getContext()).load("https://www.antworksmoney.com/financial_buddy/assets/offers-img/top-banner.jpg").asBitmap().into(banner_image);


        blogsList = rootView.findViewById(R.id.blogList);
        blogsList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        blogsList.setHasFixedSize(true);

        offersList = rootView.findViewById(R.id.offerList);
        offersList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        offersList.setHasFixedSize(true);

        videosList = rootView.findViewById(R.id.videoList);
        videosList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        videosList.setHasFixedSize(true);

        loanLayout.setOnClickListener(this);

        loanBuddyLayout.setOnClickListener(this);

        insuranceLayout.setOnClickListener(this);

        investmentLayout.setOnClickListener(this);

        notificationIcon.setOnClickListener(this);

        newNotificationText.setOnClickListener(this);

        mutualfund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MutualLogin.class);
                view.getContext().startActivity(intent);
                getActivity().finish();

            }
        });
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadVideos();
        loadBlogs();
        loadOffers();

    }

    @Override
    public void onClick(View view) {

        FragmentTransaction transaction = ((FragmentActivity) mContext)
                .getSupportFragmentManager()
                .beginTransaction();

        Fragment fragmentToReplace = null;

        switch (view.getId()) {
            case R.id.loanLayout:
                fragmentToReplace = ApplyForFragment.newInstance();
                break;

            case R.id.loanBuddyLayout:
                if (pref.getString("loginSave", "").trim().equalsIgnoreCase("0.5")) {
                    fragmentToReplace = LBOTPVerificationFragment.newInstance();
                } else if (pref.getString("loginSave", "").trim().equalsIgnoreCase("1")) {
                    fragmentToReplace = LBDashBoardFragment.newInstance();
                }
//                else if (pref.getString("loginSave","").trim().equalsIgnoreCase("2")){
//                    fragmentToReplace = LBCompanyDetails.newInstance();
//                }
//                else if (pref.getString("loginSave","").trim().equalsIgnoreCase("3")){
//                    fragmentToReplace = LBBankDetailsFragment.newInstance();
//                }
//                else if (pref.getString("loginSave","").trim().equalsIgnoreCase("4")){
//                    fragmentToReplace = LBKYCUploadFragment.newInstance();
//                }
//                else if (pref.getString("loginSave","").trim().equalsIgnoreCase("5")){
//                    fragmentToReplace = LBDashBoardFragment.newInstance();
//                }
                else {
                    fragmentToReplace = LBfirstFragment.newInstance();
                }
                break;

            case R.id.investmentLayout:
                fragmentToReplace = InvestmentApplyForFragment.newInstance();
                break;

            case R.id.insuranceLayout:
                fragmentToReplace = InsuranceHomeFragment.newInstance();
                break;

            case R.id.newNotificationText:
            case R.id.notificationIcon:

                fragmentToReplace = NotificationFragment.newInstance();
                break;
        }
        if (fragmentToReplace != null) {
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDatabaseObject.getUnreadMessagegeCount() > 0) {
            newNotificationText.setVisibility(View.VISIBLE);
        } else {
            newNotificationText.setVisibility(View.GONE);
        }
    }

    private void loadVideos() {
        friendSListLoader.setVisibility(View.VISIBLE);
        friendErrorListImage.setVisibility(View.GONE);

        try {

//            Log.e("DATA", new JSONArray().put(new JSONObject().put("mobile",pref.getString("user_phone",""))).toString());

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "offer-videos",
                    new JSONObject().put("mobile", pref.getString("user_phone", "")),
                    response -> {
                        Log.e("Video Response", response.toString());
                        friendSListLoader.setVisibility(View.GONE);
                        try {
                            ArrayList<TrainingEntity> dataList = new ArrayList<>();
                            JSONArray dataArray = response.getJSONArray("videoUrl");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);

                                TrainingEntity data = new TrainingEntity();
                                data.setId(dataObject.getString("id"));
                                data.setDescription(dataObject.getString("title"));
                                data.setUrl(dataObject.getString("url"));
                                data.setLongDescription(dataObject.getString("title"));
                                data.setThumbnail(dataObject.getString("url"));
                                data.setIsSeen(dataObject.getString("is_view"));

                                dataList.add(data);
                            }

                            ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), dataList, "Videos");
                            videosList.setAdapter(dataListAdapter);

                            seeAllVideos.setOnClickListener(v -> {
                                FragmentTransaction transaction = ((FragmentActivity) mContext)
                                        .getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.homeParent, SeeAllFragment.newInstance(dataList, "Videos"));
                                transaction.addToBackStack(null).commit();

                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            friendErrorListImage.setVisibility(View.VISIBLE);
                        }

                    },
                    error -> {
                        friendSListLoader.setVisibility(View.GONE);
                        friendErrorListImage.setVisibility(View.VISIBLE);
                        Log.e("Video Response", error.toString());
                    });

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataRequest.setShouldCache(false);

            mRequestQueue.add(dataRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void loadBlogs() {
        bestHospitalLoader.setVisibility(View.VISIBLE);
        blogsErrorListImage.setVisibility(View.GONE);

        try {

            JsonObjectRequest dataRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    AppConstant.BaseUrl + "blogs",
                    new JSONObject().put("mobile", pref.getString("user_phone", "")),
                    response -> {
                        bestHospitalLoader.setVisibility(View.GONE);
                        ArrayList<TrainingEntity> dataList = new ArrayList<>();
                        ArrayList<TrainingEntity> nextPageList = new ArrayList<>();

                        try {
                            JSONArray dataArray = response.getJSONArray("blogs_data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                TrainingEntity data = new TrainingEntity();
                                data.setId(dataObject.getString("ID"));
                                data.setDescription(dataObject.getString("post_title"));
                                data.setLongDescription(dataObject.getString("post_content"));
                                data.setUrl(dataObject.getString("guid"));
                                data.setThumbnail(dataObject.getString("blog_fetured_image"));
                                data.setIsSeen(dataObject.getString("is_view"));
                                for (int k = 0; k < dataObject.getJSONArray("categories").length(); k++) {
                                    data.getCategoryTags().add(dataObject.getJSONArray("categories")
                                            .getJSONObject(k)
                                            .getString("category"));
                                }
                                if (i < 5) {
                                    dataList.add(data);
                                }

                                nextPageList.add(data);

                            }

                            ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), dataList, "Blogs");
                            blogsList.setAdapter(dataListAdapter);

                            seeAllBlogs.setOnClickListener(v -> {
                                FragmentTransaction transaction = ((FragmentActivity) mContext)
                                        .getSupportFragmentManager()
                                        .beginTransaction();
                                transaction.replace(R.id.homeParent, SeeAllFragment.newInstance(nextPageList, "Blogs"));
                                transaction.addToBackStack(null).commit();
                            });


                        } catch (Exception e) {
                            blogsErrorListImage.setVisibility(View.VISIBLE);
                            bestHospitalLoader.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    },
                    error -> {
                        blogsErrorListImage.setVisibility(View.VISIBLE);
                        bestHospitalLoader.setVisibility(View.GONE);
                        Log.e(TAG, error.toString());
                    }
            );

            dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                    AppConstant.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            dataRequest.setShouldCache(false);

            mRequestQueue.add(dataRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadOffers() {

        chemistShopLoader.setVisibility(View.VISIBLE);
        offerListErrorImage.setVisibility(View.GONE);

        JsonObjectRequest dataObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                AppConstant.BaseUrl + "offers",
                null,
                response -> {
                    try {
                        chemistShopLoader.setVisibility(View.GONE);

                        ArrayList<TrainingEntity> datalist = new ArrayList<>();
                        ArrayList<TrainingEntity> nextPageList = new ArrayList<>();

                        JSONArray dataArray = response.getJSONArray("offers");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            TrainingEntity data = new TrainingEntity();

                            Log.e(TAG, dataObject.getString("offer_banner_image"));

                            data.setId(dataObject.getString("offer_id"));
                            data.setDescription(dataObject.getString("offer_tagline"));
                            data.setLongDescription(dataObject.getString("offer_details"));
                            data.setUrl(dataObject.getString("offer_banner_image"));
                            data.setThumbnail(dataObject.getString("offer_banner_image"));
                            datalist.add(data);

                            nextPageList.add(data);
                        }


                        ServiceListAdapter dataListAdapter = new ServiceListAdapter(getContext(), datalist, "Offers");
                        offersList.setAdapter(dataListAdapter);


                      /*  offerslayout.setOnClickListener(v -> {
                            FragmentTransaction transaction = ((FragmentActivity) mContext)
                                    .getSupportFragmentManager()
                                    .beginTransaction();
                            transaction.replace(R.id.homeParent, SeeAllFragment.newInstance(nextPageList, "Offers"));
                            transaction.addToBackStack(null).commit();
                        });*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                        offerListErrorImage.setVisibility(View.VISIBLE);
                        chemistShopLoader.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Log.e(TAG, error.toString());
                    offerListErrorImage.setVisibility(View.VISIBLE);
                    chemistShopLoader.setVisibility(View.GONE);
                }
        );

        dataObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        dataObjectRequest.setShouldCache(false);

        mRequestQueue.add(dataObjectRequest);

    }

    /*public class CustomAdapter extends FragmentPagerAdapter {
        List<Fragment> mFragmentCollection = new ArrayList<>();
        List<String> mTitleCollection = new ArrayList<>();

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(String title, Fragment fragment) {
            mTitleCollection.add(title);
            mFragmentCollection.add(fragment);

        }

        //Needed for
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleCollection.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentCollection.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentCollection.size();
        }
    }*/
}
