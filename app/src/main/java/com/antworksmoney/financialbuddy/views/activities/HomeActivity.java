package com.antworksmoney.financialbuddy.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.fragments.Home.NotificationFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBEstimatedTimeFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBPaymentRegistrationFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBSaleStatusFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBSaleSuccessfulFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBSoftApprovalFragment;
import com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan.LBWaitingForApprovalFragment;
import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.fragments.BillPayment.BillPaymentHomeFramgment;
import com.antworksmoney.financialbuddy.views.fragments.Home.FeedBackFragment;
import com.antworksmoney.financialbuddy.views.fragments.Home.HomeFragment;
import com.antworksmoney.financialbuddy.views.fragments.Home.SettingsFragment;
import com.antworksmoney.financialbuddy.views.fragments.Insurance.InsuranceHomeFragment;
import com.antworksmoney.financialbuddy.views.fragments.Investment.InvestmentApplyForFragment;
import com.antworksmoney.financialbuddy.views.fragments.LeadStatus.LeadStatusFragment;
import com.antworksmoney.financialbuddy.views.fragments.Loan.ApplyForFragment;
import com.antworksmoney.financialbuddy.views.fragments.Network.MyNetworkHome;
import com.antworksmoney.financialbuddy.views.fragments.Profile.ProfileUpdateFragment;
import com.antworksmoney.financialbuddy.views.fragments.Training.TrainingFragment;
import com.antworksmoney.financialbuddy.views.fragments.Wallet.MyWalletFragment;

import java.text.MessageFormat;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private NavigationView navigationView;

    private static final String TAG = "HomeActivity";

    private DrawerLayout drawer;

    private boolean doubleBackToExitPressedOnce = false;

    private CoordinatorLayout snackBarView;

    private LinearLayout bottomNavigationView;

    private static final int PERMISSION_LOCATION_REQUEST = 0x1;

    Fragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.nav_view);

        drawer = findViewById(R.id.drawer_layout);

        snackBarView = findViewById(R.id.homeParent);

        bottomNavigationView = findViewById(R.id.bottomNavBar);

        settingsFragment = new SettingsFragment();


        pref = getApplicationContext().getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("first_run", "1");
        editor.apply();

        setNavigationViewData(
                pref.getString("user_name", "User"),
                pref.getString("email_value", "abc@xyz.com"),
                pref.getString("user_image_url", ""));

        navigationView.getMenu().getItem(0).setChecked(true);


        navigationView.setNavigationItemSelectedListener(menuItem -> {

            int[] menuItems = {
                    R.id.home,
                    R.id.menu_remainders,
                    R.id.menu_pocket_money,
                    R.id.menu_loan,
                    R.id.menu_investment,
                    R.id.menu_insurance,
                    R.id.menu_bill,
                    R.id.menu_policies,
                    R.id.menu_training,
                    R.id.menu_settings,
                    R.id.menu_feedback,
                    R.id.menu_refer
            };

            for (int item : menuItems) {
                MenuItem customMenuItem = navigationView.getMenu().findItem(item);
                if (customMenuItem.getItemId() == menuItem.getItemId()) {
                    customMenuItem.setChecked(true);
                } else {
                    customMenuItem.setChecked(false);
                }
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeParent);

            Fragment fragmentToReplace = null;

            switch (menuItem.getItemId()) {
                case R.id.home:

                    if (!(currentFragment instanceof HomeFragment)) {
                        fragmentToReplace = HomeFragment.newInstance();
                    }
                    break;

                case R.id.menu_bill:
                    if (!(currentFragment instanceof BillPaymentHomeFramgment)) {
                        fragmentToReplace = BillPaymentHomeFramgment.newInstance();
                    }
                    break;

                case R.id.menu_remainders:
                    addEvent();
                    break;

                case R.id.menu_pocket_money:
                    if (!(currentFragment instanceof MyWalletFragment)) {
                        fragmentToReplace = MyWalletFragment.newInstance();
                    }
                    break;


                case R.id.menu_loan:

                    if (!(currentFragment instanceof ApplyForFragment)) {
                        fragmentToReplace = ApplyForFragment.newInstance();
                    }
                    break;

                case R.id.menu_investment:

                    if (!(currentFragment instanceof InvestmentApplyForFragment)) {
                        fragmentToReplace = InvestmentApplyForFragment.newInstance();
                    }
                    break;

                case R.id.menu_insurance:

                    if (!(currentFragment instanceof InsuranceHomeFragment)) {
                        fragmentToReplace = InsuranceHomeFragment.newInstance();
                    }
                    break;

//                case R.id.menu_app_download:
//
//                    if (!(currentFragment instanceof AppHomeFragment)) {
//                        fragmentToReplace = AppHomeFragment.newInstance();
//                    }
//                    break;

//                case R.id.menu_network:
//
//                    if (!(currentFragment instanceof MyNetworkHome)) {
//                        fragmentToReplace = MyNetworkHome.newInstance();
//                    }
//                    break;

                case R.id.menu_policies:

                    if (!(currentFragment instanceof LeadStatusFragment)) {
                        fragmentToReplace = LeadStatusFragment.newInstance();
                    }
                    break;

                case R.id.menu_training:
                    if (!(currentFragment instanceof TrainingFragment)) {
                        fragmentToReplace = TrainingFragment.newInstance();
                    }
                    break;

                case R.id.menu_settings:
                    if (!(currentFragment instanceof SettingsFragment)) {
                        fragmentToReplace = SettingsFragment.newInstance();
                    }
                    break;

                case R.id.menu_feedback:
                    if (!(currentFragment instanceof FeedBackFragment)) {
                        fragmentToReplace = FeedBackFragment.newInstance();
                    }
                    break;

                case R.id.menu_refer:
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Refer Financial Buddy App");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Refer Financial buddy app http://bit.ly/2WhwwPa");
                    startActivity(Intent.createChooser(sharingIntent, "Refer via"));
                    break;
            }

            if (fragmentToReplace != null) {

                transaction.replace(R.id.homeParent, fragmentToReplace);
                transaction.addToBackStack(null).commitAllowingStateLoss();
            }

            drawer.closeDrawers();

            return true;
        });

        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(view -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeParent);
            Fragment fragmentToReplace = null;
            if (!(currentFragment instanceof ProfileUpdateFragment)) {
                fragmentToReplace = ProfileUpdateFragment.newInstance(R.id.homeParent);
            }

            if (fragmentToReplace != null) {
                transaction.replace(R.id.homeParent, fragmentToReplace);
                transaction.commitAllowingStateLoss();
            }

            drawer.closeDrawers();
        });

        changeFragment();
    }

//    private void checkLocationPermission(Context mContext) {
//        String permissions[] = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//        };
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if ((ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                    && (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//
//                ActivityCompat.requestPermissions(this, permissions, PERMISSION_LOCATION_REQUEST);
//            } else {
//                changeFragment();
//            }
//        } else {
//            changeFragment();
//        }
//
//    }

    private void changeFragment() {
        changeUI(R.id.menu_home);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (pref.getString(AppConstant.NEW_MESSAGE_RECIEVED, "").trim().equalsIgnoreCase("1")){
            transaction.replace(R.id.homeParent, NotificationFragment.newInstance());
        }
        else {
            transaction.replace(R.id.homeParent, HomeFragment.newInstance());
        }
        transaction.commitAllowingStateLoss();
    }

    private void setNavigationViewData(String username, String phoneNumber, String user_image_url) {
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.userName);
        TextView email = header.findViewById(R.id.contactNumber);
        ImageView imageView = header.findViewById(R.id.imageView);
        ProgressBar splashLoaderProgress = header.findViewById(R.id.splashLoaderProgress);
        TextView progessText = header.findViewById(R.id.progessText);

        name.setText(MessageFormat.format("{0}", username));
        email.setText(MessageFormat.format("{0}", phoneNumber));
        Glide.with(getApplicationContext()).load(user_image_url).error(null).into(imageView);

        int progress = 0;
        if (!pref.getString("user_name","").trim().equalsIgnoreCase("")){
            progress = 17;
        }

        if (!pref.getString("email_value","").trim().equalsIgnoreCase("")){
            progress = progress + 17;
        }

        if (!pref.getString("user_phone","").trim().equalsIgnoreCase("")){
            progress = progress + 17;
        }

        if (!pref.getString("user_dob","").trim().equalsIgnoreCase("")) {
            if (!pref.getString("user_dob","").trim().equalsIgnoreCase("0000-00-00")) {
                progress = progress + 17;
            }
        }

        if ((!pref.getString("gender","").equalsIgnoreCase("Gender"))
                || (!pref.getString("gender","").equalsIgnoreCase(""))){
            progress = progress + 17;
        }

        if ((!pref.getString("marital_status","").equalsIgnoreCase("Marital Status"))
                || (!pref.getString("marital_status","").equalsIgnoreCase(""))){
            progress = progress + 15;
        }

        progessText.setText(progress + " %");
        splashLoaderProgress.setProgress(progress);

    }

    public DrawerLayout getmDrawerLayout() {
        return drawer;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public LinearLayout getBottomNavigationView() {return  bottomNavigationView; }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == PERMISSION_LOCATION_REQUEST) {
//            changeFragment();
//        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        settingsFragment.onActivityResult(requestCode, resultCode, data);


        Fragment fragmentHome = getSupportFragmentManager().findFragmentById(R.id.homeParent);
        if (fragmentHome != null) {
            fragmentHome.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeParent);
            if (currentFragment instanceof HomeFragment) {
                if (doubleBackToExitPressedOnce) {
                    try {
                        moveTaskToBack(true);
                    } catch (Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }
                this.doubleBackToExitPressedOnce = true;

                Toast.makeText(getApplicationContext(), "Press Back again to exit !!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
            else if (currentFragment instanceof LBEstimatedTimeFragment
                    || currentFragment instanceof LBSoftApprovalFragment
                    || currentFragment instanceof LBSaleStatusFragment
                    || currentFragment instanceof LBPaymentRegistrationFragment
                    || currentFragment instanceof LBWaitingForApprovalFragment
                    || currentFragment instanceof NotificationFragment
                    || currentFragment instanceof LBSaleSuccessfulFragment){

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                getSupportFragmentManager().popBackStackImmediate();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Fragment currentFragment1 = getSupportFragmentManager().findFragmentById(R.id.homeParent);
                    if (currentFragment1 instanceof HomeFragment) {
                        changeUI(R.id.menu_home);
                    } else if (currentFragment1 instanceof LeadStatusFragment) {
                        changeUI(R.id.menu_leads);
                    } else if (currentFragment1 instanceof MyWalletFragment) {
                        changeUI(R.id.menu_wallet);
                    } else if (currentFragment1 instanceof ProfileUpdateFragment) {
                        changeUI(R.id.menu_profile);
                    }

                }, 100);
            }
        }
    }


    public void addEvent() {
        Uri calendarUri = CalendarContract.CONTENT_URI
                .buildUpon()
                .appendPath("time")
                .build();
        startActivity(new Intent(Intent.ACTION_VIEW, calendarUri));
    }

    public void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

    public void chnageFragmentWithUI(View view) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.homeParent);
        Fragment fragmentToReplace = null;

        switch (view.getId()) {
            case R.id.menu_home:
                changeUI(R.id.menu_home);
                if (!(currentFragment instanceof HomeFragment)) {
                    fragmentToReplace = HomeFragment.newInstance();
                }
                break;

            case R.id.menu_leads:
                changeUI(R.id.menu_leads);
                if (!(currentFragment instanceof LeadStatusFragment)) {
                    fragmentToReplace = LeadStatusFragment.newInstance();
                }
                break;

            case R.id.menu_profile:
                changeUI(R.id.menu_profile);
                if (!(currentFragment instanceof ProfileUpdateFragment)) {
                    fragmentToReplace = ProfileUpdateFragment.newInstance(R.id.homeParent);
                }
                break;

            case R.id.menu_wallet:
                changeUI(R.id.menu_wallet);
                if (!(currentFragment instanceof MyWalletFragment)) {
                    fragmentToReplace = MyWalletFragment.newInstance();
                }
                break;
        }

        if (fragmentToReplace != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commitAllowingStateLoss();
        }

    }

    private void changeUI(int selectedOptionId) {
        for (int i = 0; i < bottomNavigationView.getChildCount(); i++) {
            TextView textView = (TextView) bottomNavigationView.getChildAt(i);
            if (bottomNavigationView.getChildAt(i).getId() == selectedOptionId) {
                textView.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_selected_color));
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), R.color.menu_selected_color), PorterDuff.Mode.SRC_IN));
                    }
                }
            } else {
                textView.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (drawable != null) {
                        drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), R.color.white), PorterDuff.Mode.SRC_IN));
                    }
                }
            }
        }

    }
}
