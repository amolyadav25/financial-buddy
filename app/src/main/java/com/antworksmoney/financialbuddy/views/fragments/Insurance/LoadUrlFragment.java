package com.antworksmoney.financialbuddy.views.fragments.Insurance;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.antworksmoney.financialbuddy.R;

public class LoadUrlFragment extends Fragment {
    public LoadUrlFragment() {
        // Required empty public constructor
    }

    private static String InsuranceUrl = "";

    private static String pageType = "";


    public static LoadUrlFragment newInstance(String Url, String mPageType) {
        InsuranceUrl = Url;
        pageType = mPageType;
        return new LoadUrlFragment();
    }

    WebView web_view;
    ProgressBar kProgressHUD;
    private TextView insuranceType, insuranceUrl;
    private Toolbar top_toolBar;
    private static final String TAG = "LoadUrlFragment";
    private ImageView closeButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_load_url, container, false);

        kProgressHUD= rootView.findViewById(R.id.progressBar);

        web_view = rootView.findViewById(R.id.webview);

        insuranceType = rootView.findViewById(R.id.insuranceType);

        insuranceUrl = rootView.findViewById(R.id.insuranceUrl);

        top_toolBar = rootView.findViewById(R.id.top_toolBar);

        closeButton = rootView.findViewById(R.id.cancel_Button);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Please wait while you are being redirected to our partner's website www.policysecure.in");
            builder1.setCancelable(true);
            builder1.setIcon(R.drawable.ic_arrow_forward_black_24dp);
            builder1.setTitle("Redirection");
            builder1.setPositiveButton("Cancel", (dialog, id) -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                dialog.cancel();
            });

            AlertDialog alert11 = builder1.create();
            if (!(pageType.contains("Terms")
                    || pageType.contains("Privacy")
                    || pageType.contains("P2P")
                    || pageType.contains("Home")
                    || pageType.contains("Credit Counselling")
                    || pageType.contains("Notification")
                    || pageType.contains("Pay"))) {
                alert11.show();
            }

        insuranceUrl.setText(InsuranceUrl);

        insuranceType.setText(pageType);

        top_toolBar.setNavigationOnClickListener(v -> {
            if (getActivity() != null){
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        closeButton.setOnClickListener(v -> {
            if (getActivity() != null){
                web_view.stopLoading();
            }
        });

        try {
            if (isInternetOn())
            {
                web_view.getSettings().setJavaScriptEnabled(true);
                web_view.setWebChromeClient(new WebChromeClient() {
                    public void onProgressChanged(WebView view, int progress) {
                        kProgressHUD.setProgress(progress);
                        kProgressHUD.setVisibility(View.VISIBLE);
                        closeButton.setVisibility(View.VISIBLE);
                    }
                });
                web_view.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        web_view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        kProgressHUD.setVisibility(View.GONE);
                        closeButton.setVisibility(View.GONE);
                        alert11.dismiss();

                    }
                });

                web_view.loadUrl(InsuranceUrl);


            }
        }catch (Exception e) {
            Log.e("Exception",e.getMessage());
        }


        return rootView;
    }

    public boolean isInternetOn() {
        try {
            ConnectivityManager connec = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            } else if (
                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                return false;
            }
        }catch (Exception e)
        {
            Log.e("Exception",e.getMessage());
        }
        return false;
    }

}
