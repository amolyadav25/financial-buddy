package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayUCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayUCreditFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;
WebView webViewURL;
    private ImageView toolBarIcon;
    public PayUCreditFragment() {
        // Required empty public constructor
    }


    public static PayUCreditFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        PayUCreditFragment fragment = new PayUCreditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_pay_u_credit, container, false);
        toolBarIcon = rootview.findViewById(R.id.toolBarIcon);
        webViewURL = rootview.findViewById(R.id.webViewURL);
        toolBarIcon.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });
   ProgressDialog     progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        webViewURL.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webViewURL.getSettings().setLoadsImagesAutomatically(true);
        webViewURL.getSettings().setJavaScriptEnabled(true);
        webViewURL.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewURL.loadUrl(mLoanInfoEntity.getUrl());
        return  rootview;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}