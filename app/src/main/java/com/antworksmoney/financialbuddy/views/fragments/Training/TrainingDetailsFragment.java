package com.antworksmoney.financialbuddy.views.fragments.Training;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.service.UpdateTrainingProgress;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

public class TrainingDetailsFragment extends Fragment {


    public TrainingDetailsFragment() {
        // Required empty public constructor
    }

    private CoordinatorLayout snackBarView;

    private static TrainingEntity mTrainingEntity;

    private static String mType;

    public static TrainingDetailsFragment newInstance(TrainingEntity trainingEntity, String type) {

        mTrainingEntity = trainingEntity;

        mType = type;

        return new TrainingDetailsFragment();
    }

    private ImageView kenBurnsView;

    private Toolbar topToolBar;

    private FloatingActionButton mFloatingActionButton, fab2;

    private TextView title,
            description;

    private LinearLayout categoryTags, categoryTagsBottom;

    private LinearLayout.LayoutParams params;

    private static final String TAG = "TrainingDetailsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blogs_and_offer, container, false);


        kenBurnsView = rootView.findViewById(R.id.image);

        topToolBar = rootView.findViewById(R.id.toolbar);

        mFloatingActionButton = rootView.findViewById(R.id.fab);

        title = rootView.findViewById(R.id.title);

        description = rootView.findViewById(R.id.description);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        categoryTags = rootView.findViewById(R.id.categoryTags);

        categoryTagsBottom = rootView.findViewById(R.id.categoryTagsBottom);

        fab2 = rootView.findViewById(R.id.fab2);

        if (topToolBar != null) {

            topToolBar.setNavigationIcon(R.drawable.ic_dehaze_white_24dp);

            assert getActivity() != null;


            ((HomeActivity) getActivity()).setSupportActionBar(topToolBar);

            topToolBar.setNavigationOnClickListener(v ->
                    ((HomeActivity) getActivity())
                            .getmDrawerLayout()
                            .openDrawer(GravityCompat.START));

            topToolBar.setTitle(mType);


        }

        title.setText(mTrainingEntity.getDescription());

        description.setText(Html.fromHtml(mTrainingEntity.getLongDescription().trim()));

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);


        if (mTrainingEntity.getCategoryTags().size() > 2) {
            for (int i = 0; i < 2; i++) {
                TextView textView = new TextView(getContext());
                textView.setPadding(20, 20, 20, 20);
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                textView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                textView.setText(mTrainingEntity.getCategoryTags().get(i));
                textView.setTextAppearance(getContext(), R.style.customTextViewStyle);
                textView.setLayoutParams(params);

                categoryTags.addView(textView);
            }
        } else {
            for (int i = 0; i < mTrainingEntity.getCategoryTags().size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setPadding(20, 20, 20, 20);
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
                textView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                textView.setText(mTrainingEntity.getCategoryTags().get(i));
                textView.setTextAppearance(getContext(), R.style.customTextViewStyle);
                textView.setLayoutParams(params);

                categoryTags.addView(textView);
            }
        }

        for (int i = 0; i < mTrainingEntity.getCategoryTags().size(); i++) {

            TextView textView = new TextView(getContext());
            textView.setPadding(20, 20, 20, 20);
            textView.setBackground(getContext().getResources().getDrawable(R.drawable.contact_view_circle_accent));
            textView.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            textView.setText(mTrainingEntity.getCategoryTags().get(i));
            textView.setTextAppearance(getContext(), R.style.customTextViewStyle);
            textView.setLayoutParams(params);

            categoryTagsBottom.addView(textView);
        }

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab2.setColorFilter(fab2.getContext().getResources().getColor(
                        R.color.backgroundColor),
                        PorterDuff.Mode.SRC_ATOP);
                Snackbar.make(snackBarView, "Marked as Favourite", Snackbar.LENGTH_SHORT).show();

            }
        });


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTrainingEntity.getDescription());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mTrainingEntity.getUrl());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        Log.e("Mytag","mTrainingEntity.getThumbnail("+mTrainingEntity.getThumbnail());
       // kenBurnsView.setImageDrawable(getIR.drawable.pdfimage);
        kenBurnsView.setImageResource(R.drawable.pdf_banner);
       // Glide.with(getContext()).load(mTrainingEntity.getThumbnail()).error(null).into(kenBurnsView);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mType.trim().equalsIgnoreCase("Blogs")){
            Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
            if (getActivity() != null) {
                getActivity().startService(intent);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mType.trim().equalsIgnoreCase("Blogs")) {
            Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
            if (getActivity() != null) {
                getActivity().stopService(intent);
            }
            if (UpdateTrainingProgress.timer != null) {
                UpdateTrainingProgress.timer.cancel();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mType.trim().equalsIgnoreCase("Blogs")) {
            Intent intent = new Intent(getContext(), UpdateTrainingProgress.class);
            if (getActivity() != null) {
                getActivity().stopService(intent);
            }

            if (UpdateTrainingProgress.timer != null) {
                UpdateTrainingProgress.timer.cancel();
            }
        }
    }
}
