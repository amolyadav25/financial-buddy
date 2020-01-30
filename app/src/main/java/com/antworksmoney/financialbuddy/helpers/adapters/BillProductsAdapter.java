package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BillerEntity;

import java.util.ArrayList;


public class BillProductsAdapter extends RecyclerView.Adapter<BillProductsAdapter.Holder> {

    private Context mContext;
    private ArrayList<BillerEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "LeadStatusAdapter";


    public BillProductsAdapter(Context context, ArrayList<BillerEntity> contactData, RecyclerView mRecyclerView) {
        this.mAllContactsData = contactData;
        this.mContext = context;
        recyclerView = mRecyclerView;

    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_list_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.productName.setText(mAllContactsData.get(position).getBillerCategory());

        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(mAllContactsData.get(position).getDrawable()));

        holder.imageView.setImageTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.image_gradient_color)));

        holder.dataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAllContactsData.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView productName;

        ImageView imageView;


        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            dataCard = itemView.findViewById(R.id.dataCard);

            productName = itemView.findViewById(R.id.userName);

            imageView = itemView.findViewById(R.id.servicePic);
        }
    }
}
