package com.antworksmoney.financialbuddy.views.fragments.Training;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.TrainingDataListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.helpers.service.UpdateProductStatus;
import com.antworksmoney.financialbuddy.helpers.service.UpdateTrainingProgress;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import java.util.ArrayList;
import java.util.Objects;


public class VideosPlayFragment extends Fragment  implements View.OnClickListener {
    public VideosPlayFragment() {
        // Required empty public constructor
    }

    private static TrainingEntity mTrainingEntity;

    private static ArrayList<TrainingEntity> mTrainingEntityArrayList;

    public static VideosPlayFragment newInstance(TrainingEntity trainingEntity, ArrayList<TrainingEntity> mServicePojoArrayList) {
        mTrainingEntityArrayList = mServicePojoArrayList;
        mTrainingEntity = trainingEntity;
        return new VideosPlayFragment();
    }

    private YouTubePlayer YPlayer;

    private TextView videoName, videoCompleteDetails;

    private ImageView videoDropDownArrow;

    private RelativeLayout videoDetailsLayout;

    private android.support.v7.widget.Toolbar mToolBar;

    private ImageView shareButton,favouriteButton;

    private CoordinatorLayout snackBarView;

    private RecyclerView videosList;

    private ScrollView videoScroller;

    private LinearLayout playerParent;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_videos_play, container, false);

        videoName = rootView.findViewById(R.id.videoName);

        videoDropDownArrow = rootView.findViewById(R.id.videoDropDownArrow);

        videoDetailsLayout = rootView.findViewById(R.id.videoDetailsLayout);

        videoCompleteDetails = rootView.findViewById(R.id.videoCompleteDetails);

        shareButton = rootView.findViewById(R.id.shareButton);

        favouriteButton = rootView.findViewById(R.id.favouriteButton);

        mToolBar = rootView.findViewById(R.id.top_toolBar);

        videoScroller = rootView.findViewById(R.id.videoScroller);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        videosList  = rootView.findViewById(R.id.videosList);

        playerParent = rootView.findViewById(R.id.playerParent);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(AppConstant.YouTubeApiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    YPlayer.loadVideo(mTrainingEntity.getUrl().split("=")[1]);
                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Toast.makeText(getContext(),"Failed to start",Toast.LENGTH_SHORT).show();

            }
        });


        if (mToolBar != null && getActivity()!= null) {

            ((HomeActivity) getActivity()).setSupportActionBar(mToolBar);

            Objects.requireNonNull(((HomeActivity) getActivity())
                    .getSupportActionBar())
                    .setDisplayShowTitleEnabled(false);


            mToolBar.setNavigationIcon(R.drawable.ic_dehaze_grey_24dp);

            mToolBar.setNavigationOnClickListener(v -> ((HomeActivity) getActivity())
                    .getmDrawerLayout()
                    .openDrawer(GravityCompat.START));

        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mTrainingEntity.getDescription());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, mTrainingEntity.getUrl());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favouriteButton.setColorFilter(favouriteButton.getContext().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                Snackbar.make(snackBarView,"Marked as Favourite",Snackbar.LENGTH_SHORT).show();

            }
        });



        videoName.setText(mTrainingEntity.getDescription());

        videoCompleteDetails.setText(mTrainingEntity.getLongDescription());

        videosList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        videosList.setHasFixedSize(true);

        videoDetailsLayout.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.videoDetailsLayout:
                if (!videoCompleteDetails.isShown()){
                    videoDropDownArrow.setRotation(180);
                    videoCompleteDetails.setVisibility(View.VISIBLE);
                }
                else {
                    videoDropDownArrow.setRotation(0);
                    videoCompleteDetails.setVisibility(View.GONE);

                }
                break;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TrainingDataListAdapter dataListAdapter = new TrainingDataListAdapter(getContext(), mTrainingEntityArrayList, "Videos");
        videosList.setAdapter(dataListAdapter);
        dataListAdapter.setOnClick(new TrainingDataListAdapter.OnItemClicked() {
            @Override
            public void onItemClick(int position) {
                new UpdateProductStatus(getContext(),"video",mTrainingEntityArrayList.get(position).getId());
                YPlayer.loadVideo(mTrainingEntityArrayList.get(position).getUrl().split("=")[1]);
                videoName.setText(mTrainingEntityArrayList.get(position).getDescription());
                videoCompleteDetails.setText(mTrainingEntityArrayList.get(position).getLongDescription());
                YPlayer.play();
                videoScroller.fullScroll(ScrollView.FOCUS_UP);
            }
        });

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
