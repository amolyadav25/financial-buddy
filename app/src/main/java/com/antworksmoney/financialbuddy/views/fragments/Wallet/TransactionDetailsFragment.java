package com.antworksmoney.financialbuddy.views.fragments.Wallet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
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
import com.antworksmoney.financialbuddy.helpers.Entity.WalletEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

public class TransactionDetailsFragment extends Fragment {
    public TransactionDetailsFragment() {
        // Required empty public constructor
    }

    private static WalletEntity mWalletEntity;

    public static TransactionDetailsFragment newInstance(WalletEntity walletEntity) {
        mWalletEntity = walletEntity;
        return new TransactionDetailsFragment();
    }

    private TextView title,
            avalability,
            info;

    private Toolbar topToolBar;

    private CoordinatorLayout snackBarView;

    private FloatingActionButton downloadButton;

    private String FILE;

    private Context mContext;

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 415;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction_details, container, false);

        title = rootView.findViewById(R.id.title);

        avalability = rootView.findViewById(R.id.avalability);

        info = rootView.findViewById(R.id.info);

        topToolBar = rootView.findViewById(R.id.toolbar);

        downloadButton = rootView.findViewById(R.id.fab);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        FILE = Environment.getExternalStorageDirectory()
                + "/"+mWalletEntity.getLeadId()+".pdf";

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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title.setText("Rs. " + mWalletEntity.getAmount() + " recieved.");

        avalability.setText("Money Earned : Rs." + mWalletEntity.getAmount() + "\n" +
                "Credited on : " + mWalletEntity.getCreatedDate() + "\n" +
                "Lead id : " + mWalletEntity.getLeadId() + "\n" +
                "Payout Percentage : " + mWalletEntity.getPayoutPercentage() + " % \n" +
                "Payout Id : " + mWalletEntity.getPayoutId() + "\n");

        info.setText("Borrower Name : " + mWalletEntity.getBorrowerName() + "\n" +
                "Loan Amount : Rs. " + mWalletEntity.getLoanAmount() + "\n" +
                "Loan Type : " + mWalletEntity.getLoanType() + "\n" +
                "Disbursed loan Amount : Rs. " + mWalletEntity.getDisburseLoanAmount() + "\n");

        downloadButton.setOnClickListener(new View.OnClickListener() {
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

    private void showSnackBar(String message) {
        Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT).show();
    }


    private void createPdf() {
        Document objDocument = new Document();
        objDocument.setCreator("Financial Buddy");
        objDocument.setAuthor("Vikash Parashar");
        objDocument.setTitle("Transaction Reciept");

        Page objPage = new Page(PageSize.LETTER, PageOrientation.PORTRAIT, 54.0f);
        String strText = "Financial Buddy @ Transaction Reciept\n---------------------------------------\n\n"+
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
}
