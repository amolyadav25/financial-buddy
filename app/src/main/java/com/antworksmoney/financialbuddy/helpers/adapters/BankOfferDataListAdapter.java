package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.BankInfoEntity;

import java.text.MessageFormat;
import java.util.ArrayList;


public class BankOfferDataListAdapter extends RecyclerView.Adapter<BankOfferDataListAdapter.Holder> {

    private Context mContext;
    private ArrayList<BankInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;


    public BankOfferDataListAdapter(Context context, ArrayList<BankInfoEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bank_offer, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bankOfferName.setText(MessageFormat.format("Get loan at {0}% from {1}",
                mAllContactsData.get(position).getMin_int_rate(),
                mAllContactsData.get(position).getName()));

        holder.bankOfferDetails.setText(String.format("Interest Rate : %s - %s %% (Floating)\nEMI : Rs. %s\nLoan Amount : Rs. %s\nTenure time : %s months",
                mAllContactsData.get(position).getMin_int_rate(),
                mAllContactsData.get(position).getMax_int_rate(),
                mAllContactsData.get(position).getMin_processing_fee(),
                mAllContactsData.get(position).getMin_loan_amount(),
                mAllContactsData.get(position).getTenure_month_start()));


        Glide.with(mContext).load(mAllContactsData.get(position).getImage()).error(R.drawable.loan).into(holder.bankImage);

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

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView bankOfferName, bankOfferDetails;

        ImageView bankImage;

        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            dataCard = itemView.findViewById(R.id.dataCard);

            bankOfferDetails = itemView.findViewById(R.id.bankOfferDetails);

            bankOfferName = itemView.findViewById(R.id.bankOfferName);

            bankImage = itemView.findViewById(R.id.bankImage);

        }
    }
}
