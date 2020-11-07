package com.antworksmoney.financialbuddy.views.fragments.Insurance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;


public class InsuranceHomeFragment extends Fragment implements View.OnClickListener {

    private Button proceedbutton;
    private LinearLayout otherLayout_insurance;
    private LinearLayout insurance_menulist;

    public InsuranceHomeFragment() {
        // Required empty public constructor
    }

    public static InsuranceHomeFragment newInstance() {
        return new InsuranceHomeFragment();
    }

    private LinearLayout healthInsuranceProduct,
            carInsuranceProduct,
            twoWheelerProduct,
            travelProduct,
            lifeProduct;

    private static final String TAG = "InsuranceHomeFragment";

    private AppCompatActivity mActivity;

    private CoordinatorLayout snackBarView;

    private Button registerAsPOSButton;

    private Toolbar top_toolBar;

    private SharedPreferences mSharedPreferences;

    private TextView insurance_text;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_insurance_home, container, false);

        healthInsuranceProduct = rootView.findViewById(R.id.healthInsuranceProduct);

        carInsuranceProduct = rootView.findViewById(R.id.carInsuranceProduct);

        twoWheelerProduct = rootView.findViewById(R.id.twoWheelerProduct);

        travelProduct = rootView.findViewById(R.id.travelProduct);

        lifeProduct = rootView.findViewById(R.id.lifeProduct);

        otherLayout_insurance = rootView.findViewById(R.id.otherLayout_insurance);

        insurance_menulist = rootView .findViewById(R.id.insurance_menulist);

        registerAsPOSButton = rootView.findViewById(R.id.registerAsPOSButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        insurance_text = rootView.findViewById(R.id.insurance_text);

        proceedbutton = rootView.findViewById(R.id.applyForOthers);

        mActivity = (AppCompatActivity) getActivity();

        otherLayout_insurance.setVisibility(View.VISIBLE);
        insurance_menulist.setVisibility(View.GONE);

        insurance_text.setText(Html.fromHtml("Buy Insurance online at <a heref = 'www.Policysecure.in'>www.Policysecure.in</a>"));

        mSharedPreferences = mActivity.getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        top_toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mActivity)
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        healthInsuranceProduct.setOnClickListener(this);

        carInsuranceProduct.setOnClickListener(this);

        twoWheelerProduct.setOnClickListener(this);

        travelProduct.setOnClickListener(this);

        lifeProduct.setOnClickListener(this);

        registerAsPOSButton.setOnClickListener(this);


        proceedbutton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Fragment fragmentToReplace = null;
        switch (view.getId()) {

            case R.id.applyForOthers:

                otherLayout_insurance.setVisibility(View.GONE);

                insurance_menulist.setVisibility(View.VISIBLE);

                break;

            case R.id.healthInsuranceProduct:
                fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/Home/Health-Insurance", "Health Insurance");
                break;

            case R.id.carInsuranceProduct:
                fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/antworks/index", "Car Insurance");
                break;

            case R.id.twoWheelerProduct:
                fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/antworks/index", "Bike Insurance");
                break;

            case R.id.travelProduct:
                fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/Antworks/Travel-Insurance", "Travel Insurance");
                break;

            case R.id.lifeProduct:
                fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/Antworks/Life-Insurance", "Life Insurance");
                break;

            case R.id.registerAsPOSButton:
                if (mSharedPreferences.getString("registerAsPos", "").equalsIgnoreCase("Yes")) {
                    fragmentToReplace = LoadUrlFragment.newInstance("https://www.policysecure.in/Antworks/Register-POS", "Register As POS (Point of Sale)");
                } else {
                    fragmentToReplace = RegisterAsPosFragment.newInstance();
                }
                break;
        }
        if (fragmentToReplace != null) {
            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, fragmentToReplace);
            transaction.addToBackStack(null).commit();
        }
    }


    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }

}
