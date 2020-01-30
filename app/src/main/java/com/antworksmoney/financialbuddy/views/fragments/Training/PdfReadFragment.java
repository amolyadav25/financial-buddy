package com.antworksmoney.financialbuddy.views.fragments.Training;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.service.UpdateTrainingProgress;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import java.util.Objects;


public class PdfReadFragment extends Fragment {

    public PdfReadFragment() {
        // Required empty public constructor
    }

    private static TrainingEntity mTrainingEntity;

    public static PdfReadFragment newInstance(TrainingEntity trainingEntity) {
        mTrainingEntity = trainingEntity;
        return new PdfReadFragment();
    }

    private Toolbar topToolBar;

    private WebView documentView;

    private ProgressBar progressBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pdf_read, container, false);

        topToolBar = rootView.findViewById(R.id.top_toolBar);

        documentView = rootView.findViewById(R.id.documentView);

        progressBar = rootView.findViewById(R.id.progressBar);


        if (topToolBar != null && getActivity()!= null) {

            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            Objects.requireNonNull(((HomeActivity) getActivity())
                    .getSupportActionBar())
                    .setDisplayShowTitleEnabled(false);

            topToolBar.setTitle(mTrainingEntity.getDescription());

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            topToolBar.setNavigationOnClickListener(v -> ((HomeActivity) getActivity())
                    .getmDrawerLayout()
                    .openDrawer(GravityCompat.START));

        }

        documentView.getSettings().setJavaScriptEnabled(true);

        documentView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        //;
        documentView.loadUrl("http://docs.google.com/gview?embedded=true&url="+mTrainingEntity.getUrl());

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
        if (getActivity() != null) {
            getActivity().startService(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
        if (getActivity() != null){
            getActivity().stopService(intent);
        }
        if (UpdateTrainingProgress.timer != null){
            UpdateTrainingProgress.timer.cancel();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
        if (getActivity() != null){
            getActivity().stopService(intent);
        }

        if (UpdateTrainingProgress.timer != null){
            UpdateTrainingProgress.timer.cancel();
        }
    }
}
