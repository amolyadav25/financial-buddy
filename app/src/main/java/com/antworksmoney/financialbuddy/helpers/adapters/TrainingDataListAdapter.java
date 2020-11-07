package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.TrainingEntity;

import java.util.ArrayList;


public class TrainingDataListAdapter extends RecyclerView.Adapter<TrainingDataListAdapter.Holder> {

    private ArrayList<TrainingEntity> mServicePojoArrayList;
    private Context mContext;
    private String mType;
    private static final String TAG = "ServiceListAdapter";
    private TrainingDataListAdapter.OnItemClicked onClick;

    public TrainingDataListAdapter(Context context, ArrayList<TrainingEntity> servicePojoArrayList, String type) {
        this.mContext = context;
        this.mServicePojoArrayList = servicePojoArrayList;
        this.mType = type;
    }


    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.training_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        TrainingEntity data = mServicePojoArrayList.get(position);

        holder.serviceName.setText(data.getDescription());

        holder.serviceDetails.setText(data.getLongDescription());

        Glide.with(mContext).load("https://img.youtube.com/vi/"+data.getThumbnail().trim().split("=")[1]+"/0.jpg")
                .error(R.drawable.nothing_to_show)
                .into(holder.serviceImage);


        holder.dataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });



    }


    @Override
    public int getItemCount() {
        return mServicePojoArrayList.size();
    }


    public void setOnClick(TrainingDataListAdapter.OnItemClicked onClick) {
        this.onClick=onClick;
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView serviceName;
        ImageView serviceImage;
        RelativeLayout dataCard;
        TextView serviceDetails;


        Holder(View itemView) {
            super(itemView);

            serviceName = itemView.findViewById(R.id.userName);
            serviceImage = itemView.findViewById(R.id.servicePic);
            dataCard = itemView.findViewById(R.id.dataCard);
            serviceDetails = itemView.findViewById(R.id.userDetails);
        }
    }
}
