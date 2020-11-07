package com.antworksmoney.financialbuddy.views.fragments.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.NotificationListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.helpers.service.UpdateProductStatus;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.LoadUrlFragment;
import com.antworksmoney.financialbuddy.views.fragments.Loan.WhereYouLiveFragment;
import com.antworksmoney.financialbuddy.views.fragments.Training.TrainingDetailsFragment;

import java.util.ArrayList;

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

    private SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String mType;
    private ArrayList<TrainingEntity> mServicePojoArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_notification, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", MODE_PRIVATE);
        mServicePojoArrayList = new ArrayList<>();
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
            Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));
            if(mSharedPreferences.getString("click_action",null)!=null) {
//            mDataBaseObject.insertNotificationData("0",
//                    getIntent().getStringExtra("message"),
//                    getIntent().getStringExtra("click_action"),
//                    String.valueOf(Calendar.getInstance().getTime()),
//                    getIntent().getStringExtra("title"));
                if (mSharedPreferences.getString("click_action",null).equals("Loan Buddy P2P Loan")) {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));

                    try{
                        fragment = (Fragment) Class.forName("com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.LBfirstFragment").newInstance();
                        transaction.replace(R.id.homeParent, fragment);
                        transaction.commit();
                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }

                }
                else if(mSharedPreferences.getString("click_action",null).equals("Health Insurance"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));

                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/Home/Health-Insurance", "Health Insurance");
                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/Home/Health-Insurance", "Health Insurance");
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Car Insurance"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));
                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/antworks/index", "Car Insurance");
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Two Wheeler Insurance"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));
                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/antworks/index", "Bike Insurance");
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Travel Insurance"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));
                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/Antworks/Travel-Insurance", "Travel Insurance");
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Life Insurance"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    Log.e(TAG, "click_actionclick_actionclick_action"+mSharedPreferences.getString("click_action",null));
                    fragment = LoadUrlFragment.newInstance("https://www.policysecure.in/Antworks/Life-Insurance", "Life Insurance");
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Housing Loan"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Housing Loan");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Loan Against Property"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Loan Against Property");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Business Loan"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Business Loan");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Credit Card"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Credit Card");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Personal Loan"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Personal Loan");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Home Loan Balance Transfer"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Home Loan Balance Transfer");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Instant Loan"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Instant Loan");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Credit Counselling"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    LoanInfoEntity mLoanInfoEntity = new LoanInfoEntity();
                    mLoanInfoEntity.setLoanName("Credit Counselling");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, WhereYouLiveFragment.newInstance(mLoanInfoEntity));
                    transaction.commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Offer1"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    TrainingEntity data = new TrainingEntity();
                    data.setDescription(mSharedPreferences.getString("title",null));
                    data.setLongDescription(mSharedPreferences.getString("message",null));
                    mType = "Offers";
                    fragment = TrainingDetailsFragment.newInstance(data, mType);
                    mServicePojoArrayList.add(data);
                    new UpdateProductStatus(requireActivity(),"blog",mServicePojoArrayList.get(1).getId());
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.addToBackStack(null).commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Offer2"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    TrainingEntity data = new TrainingEntity();
                    data.setDescription(mSharedPreferences.getString("title",null));
                    data.setLongDescription(mSharedPreferences.getString("message",null));
                    mType = "Offers";
                    fragment = TrainingDetailsFragment.newInstance(data, mType);
                    mServicePojoArrayList.add(data);
                    new UpdateProductStatus(requireActivity(),"blog",mServicePojoArrayList.get(2).getId());
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.addToBackStack(null).commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Offer3"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    TrainingEntity data = new TrainingEntity();
                    data.setDescription(mSharedPreferences.getString("title",null));
                    data.setLongDescription(mSharedPreferences.getString("message",null));
                    mType = "Offers";
                    fragment = TrainingDetailsFragment.newInstance(data, mType);
                    mServicePojoArrayList.add(data);
                    new UpdateProductStatus(requireActivity(),"blog",mServicePojoArrayList.get(3).getId());
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.addToBackStack(null).commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Offer4"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    TrainingEntity data = new TrainingEntity();
                    data.setDescription(mSharedPreferences.getString("title",null));
                    data.setLongDescription(mSharedPreferences.getString("message",null));
                    mType = "Offers";
                    fragment = TrainingDetailsFragment.newInstance(data, mType);
                    mServicePojoArrayList.add(data);
                    new UpdateProductStatus(requireActivity(),"blog",mServicePojoArrayList.get(4).getId());
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.addToBackStack(null).commit();
                }
                else if(mSharedPreferences.getString("click_action",null).equals("Offer5"))
                {
                    dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    Fragment fragment;
                    TrainingEntity data = new TrainingEntity();
                    data.setDescription(mSharedPreferences.getString("title",null));
                    data.setLongDescription(mSharedPreferences.getString("message",null));
                    mType = "Offers";
                    fragment = TrainingDetailsFragment.newInstance(data, mType);
                    mServicePojoArrayList.add(data);
                    new UpdateProductStatus(requireActivity(),"blog",mServicePojoArrayList.get(5).getId());
                    transaction.replace(R.id.homeParent, fragment);
                    transaction.addToBackStack(null).commit();
                }else
                {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, HomeFragment.newInstance());
                    transaction.commitAllowingStateLoss();
                }
            }
//          if (getActivity() != null){
//              dataBaseObject.updateLeadStatus(dataBaseObject.getAllLeadData().get(position).getLoanId(),"1");
//              FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//              transaction.replace(R.id.homeParent,
//                      LoadUrlFragment.newInstance(dataBaseObject.getAllLeadData().get(position).getEmail(),
//                              "Notification"));
//              transaction.addToBackStack(null).commit();
//          }
        });
    }
}
