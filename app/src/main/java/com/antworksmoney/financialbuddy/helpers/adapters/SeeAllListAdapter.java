package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;
import com.antworksmoney.financialbuddy.helpers.service.UpdateProductStatus;
import com.antworksmoney.financialbuddy.views.fragments.Training.PdfReadFragment;
import com.antworksmoney.financialbuddy.views.fragments.Training.TrainingDetailsFragment;
import com.antworksmoney.financialbuddy.views.fragments.Training.VideosPlayFragment;

import java.util.ArrayList;


public class SeeAllListAdapter extends RecyclerView.Adapter<SeeAllListAdapter.Holder> {

    private ArrayList<TrainingEntity> mServicePojoArrayList;
    private Context mContext;
    private String mType;
    private static final String TAG = "ServiceListAdapter";

    public SeeAllListAdapter(Context context, ArrayList<TrainingEntity> servicePojoArrayList, String type) {
        this.mContext = context;
        this.mServicePojoArrayList = servicePojoArrayList;
        this.mType = type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.see_all_list_layout, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        TrainingEntity data = mServicePojoArrayList.get(position);

        holder.serviceName.setText(data.getDescription());

        if (mType.trim().equalsIgnoreCase("videos")) {
            Glide.with(mContext).load("https://img.youtube.com/vi/"+data.getThumbnail().trim().split("=")[1]+"/0.jpg")
                    .error(R.drawable.nothing_to_show)
                    .centerCrop()
                    .into(holder.serviceImage);
        }
        else {
            Glide.with(mContext).load(data.getThumbnail())
                    .error(R.drawable.nothing_to_show)
                    .into(holder.serviceImage);
        }

        if (mType.trim().equalsIgnoreCase("videos")) {
            holder.playButton.setVisibility(View.VISIBLE);
        } else {
            holder.playButton.setVisibility(View.GONE);
        }

        if (data.getIsSeen()!= null){


            if (data.getIsSeen().trim().equalsIgnoreCase("1")){
                holder.seenImage.setVisibility(View.VISIBLE);
            }else {

                holder.seenImage.setVisibility(View.GONE);
            }



        }


        holder.dataCard.setOnClickListener(view -> {

            FragmentTransaction transaction = ((FragmentActivity) mContext)
                    .getSupportFragmentManager()
                    .beginTransaction();

            Fragment toReplaceFragment;

            if (mType.trim().equalsIgnoreCase("videos")) {
                new UpdateProductStatus(mContext,"video",mServicePojoArrayList.get(position).getId());
                toReplaceFragment = VideosPlayFragment.newInstance(data, mServicePojoArrayList);
            } else if (mType.trim().equalsIgnoreCase("pdfs")) {
                new UpdateProductStatus(mContext,"pdf",mServicePojoArrayList.get(position).getId());
                toReplaceFragment = PdfReadFragment.newInstance(data);
            } else {
                new UpdateProductStatus(mContext,"blog",mServicePojoArrayList.get(position).getId());
                toReplaceFragment = TrainingDetailsFragment.newInstance(data, mType);

            }

            transaction.replace(R.id.homeParent, toReplaceFragment);
            transaction.addToBackStack(null).commit();

        });


        holder.shareButton.setOnClickListener(view -> {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, data.getDescription());
            sharingIntent.putExtra(Intent.EXTRA_TEXT, data.getUrl());
            mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));

        });


    }


    @Override
    public int getItemCount() {
        return mServicePojoArrayList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView serviceName;
        ImageView serviceImage;
        CardView dataCard;
        RelativeLayout playButton;
        ImageView shareButton;
        ImageView seenImage;

        Holder(View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.userName);
            serviceImage = itemView.findViewById(R.id.servicePic);
            dataCard = itemView.findViewById(R.id.dataCard);
            playButton = itemView.findViewById(R.id.playButton);
            shareButton = itemView.findViewById(R.id.shareButton);
            seenImage = itemView.findViewById(R.id.seenImage);
        }
    }
}
