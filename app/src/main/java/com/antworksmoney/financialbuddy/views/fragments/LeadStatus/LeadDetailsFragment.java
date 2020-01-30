package com.antworksmoney.financialbuddy.views.fragments.LeadStatus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cete.dynamicpdf.Document;
import com.cete.dynamicpdf.Font;
import com.cete.dynamicpdf.Page;
import com.cete.dynamicpdf.PageOrientation;
import com.cete.dynamicpdf.PageSize;
import com.cete.dynamicpdf.TextAlign;
import com.cete.dynamicpdf.pageelements.Label;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LeadInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;


public class LeadDetailsFragment extends Fragment {
    public LeadDetailsFragment() {
        // Required empty public constructor
    }

    private static LeadInfoEntity mLeadInfoEntity;

    public static LeadDetailsFragment newInstance(LeadInfoEntity leadInfoEntity) {
        mLeadInfoEntity = leadInfoEntity;
        return  new LeadDetailsFragment();
    }

    private TextView title,
            avalability,
            info;

    private Toolbar topToolBar;

    private CoordinatorLayout snackBarView;

    private FloatingActionButton callButton;

    private Context mContext;

    private String FILE;

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 420;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_lead_details, container, false);

        title = rootView.findViewById(R.id.title);

        avalability = rootView.findViewById(R.id.avalability);

        info = rootView.findViewById(R.id.info);

        topToolBar = rootView.findViewById(R.id.toolbar);

        callButton = rootView.findViewById(R.id.fab);

        snackBarView = rootView.findViewById(R.id.snackBarView);


        mContext = getContext();

        if (topToolBar != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            assert getActivity() != null;


            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            topToolBar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));

            topToolBar.setTitle("");

        }

        FILE = Environment.getExternalStorageDirectory()
                + "/"+mLeadInfoEntity.getCreated_date()+".pdf";


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setText("Applied for "+ mLeadInfoEntity.getLoanType()+ " of Rs. "+ mLeadInfoEntity.getLoanAmount());

        avalability.setText("Applied On : " + mLeadInfoEntity.getCreated_date() + "\n" +
                "Borrower Name : " + mLeadInfoEntity.getBorowwerName() + "\n" +
                "Loan type : " + mLeadInfoEntity.getLoanType() + "\n" +
                "Loan Amount : " + mLeadInfoEntity.getLoanAmount() + "\n" +
                "Lead Status : " + mLeadInfoEntity.getLeadStatus() + "\n" +
                "Call status : " + mLeadInfoEntity.getSubmittedToBanks());

        String otherDetailsText = "Other Details : \n";
        if (mLeadInfoEntity.getEmail().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Email : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Email : " + mLeadInfoEntity.getEmail() + "\n";
        }

        if (mLeadInfoEntity.getCity().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "City : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "City : " + mLeadInfoEntity.getCity() + "\n";
        }

        if (mLeadInfoEntity.getAddress().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Address : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Address : " + mLeadInfoEntity.getAddress() + "\n";
        }

        if (mLeadInfoEntity.getPin().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Pin : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Pin : " + mLeadInfoEntity.getPin() + "\n";
        }

        if (mLeadInfoEntity.getOccupation().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Occupation : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Occupation : " + mLeadInfoEntity.getOccupation() + "\n";
        }

        if (mLeadInfoEntity.getCompanyType().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Company Type : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Company Type : " + mLeadInfoEntity.getCompanyType() + "\n";
        }

        if (mLeadInfoEntity.getCompanyName().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Company Name : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Company Name : " + mLeadInfoEntity.getCompanyName() + "\n";
        }

        if (mLeadInfoEntity.getIncome().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Income : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Income : " + mLeadInfoEntity.getIncome() + "\n";
        }

        info.setText(otherDetailsText);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForContactPermission();
            }
        });
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                createPdf();
            }
        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPdf();
            } else {
                showSnackBar("Can't proceed further, Please provide permission !!! (-> App settings) ");
            }
        }
    }

    private void createPdf() {
        Document objDocument = new Document();
        objDocument.setCreator("Financial Buddy");
        objDocument.setAuthor("Vikash Parashar");
        objDocument.setTitle("Transaction Reciept");

        Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT, 54.0f);
        String strText = "Financial Buddy @ Lead Reciept\n---------------------------------------\n\n"+
                title.getText().toString().trim() + "\n\n"+
                avalability.getText().toString().trim() + "\n\n"+
                info.getText().toString().trim();
        Label objLabel = new Label(strText, 0, 0, 504, 300, Font.getHelvetica(), 12, TextAlign.CENTER);
        objPage.getElements().add(objLabel);
        objDocument.getPages().add(objPage);

        try {
            objDocument.draw(FILE);
            showSnackBar("Reciept downloaded successfully...");

        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar("Failed to download reciept..");
        }
    }



    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }




}
