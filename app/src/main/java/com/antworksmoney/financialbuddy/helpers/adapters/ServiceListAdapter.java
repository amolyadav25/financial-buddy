package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.Holder> {

    private ArrayList<TrainingEntity> mServicePojoArrayList;
    private Context mContext;
    private String mType;
    private static final String TAG = "ServiceListAdapter";

    public ServiceListAdapter(Context context, ArrayList<TrainingEntity> servicePojoArrayList, String type) {
        this.mContext = context;
        this.mServicePojoArrayList = servicePojoArrayList;
        this.mType = type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.service_list_layout, parent, false);
        int width = parent.getMeasuredWidth() * 3 / 4;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        params.width = Math.round(width);
        itemView.setLayoutParams(params);
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
            Log.e(TAG, data.getThumbnail());
            Glide.with(mContext).load(data.getThumbnail())
                    .error(R.drawable.nothing_to_show)
                    .into(holder.serviceImage);
        }

        if (mType.trim().equalsIgnoreCase("videos")) {
            holder.playButton.setVisibility(View.VISIBLE);
        } else {
            holder.playButton.setVisibility(View.GONE);
        }

        if (mType.trim().equalsIgnoreCase("Offers")){
            holder.seenImage.setVisibility(View.GONE);
        }
        else {
            if (data.getIsSeen().trim().equalsIgnoreCase("1")){
                holder.seenImage.setVisibility(View.VISIBLE);
            }
            else {
                holder.seenImage.setVisibility(View.GONE);
            }
        }

        holder.dataCard.setOnClickListener(view -> {
            FragmentTransaction transaction = ((FragmentActivity) mContext).
                    getSupportFragmentManager()
                    .beginTransaction();
            Fragment toReplaceFragment;
            Log.e("Mytag","Offer");
Log.e("Mytag","Offer"+(mType));
//            if (mType.trim().equalsIgnoreCase("videos")) {
//                new UpdateProductStatus(mContext,"video",mServicePojoArrayList.get(position).getId());
//                toReplaceFragment = VideosPlayFragment.newInstance(data, mServicePojoArrayList);
//            } else if (mType.trim().equalsIgnoreCase("pdfs")) {
//                toReplaceFragment = PdfReadFragment.newInstance(data);
//                new UpdateProductStatus(mContext,"pdf",mServicePojoArrayList.get(position).getId());
//            } else {
                toReplaceFragment = TrainingDetailsFragment.newInstance(data, mType);
                new UpdateProductStatus(mContext,"blog",mServicePojoArrayList.get(position).getId());

       //     }
            transaction.replace(R.id.homeParent, toReplaceFragment);
            transaction.addToBackStack(null).commit();
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
        ImageView seenImage;

        Holder(View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.userName);
            serviceImage = itemView.findViewById(R.id.servicePic);
            dataCard = itemView.findViewById(R.id.dataCard);
            playButton = itemView.findViewById(R.id.playButton);
            seenImage = itemView.findViewById(R.id.seenImage);
        }
    }
}
