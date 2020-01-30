package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.WalletEntity;

import java.util.ArrayList;


public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.Holder> {

    private Context mContext;
    private ArrayList<WalletEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "LeadStatusAdapter";


    public FeedbackListAdapter(Context context, ArrayList<WalletEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feedback_history, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Log.e(TAG,mAllContactsData.toString());

        holder.bankOfferName.setText(mAllContactsData.get(position).getCreatedDate());

        holder.bankOfferDetails.setText("User Id : " + mAllContactsData.get(position).getLeadId());

        holder.bankName.setText("Feedback : " + mAllContactsData.get(position).getBorrowerName());



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

        TextView bankName, bankOfferName, bankOfferDetails;

        ImageView bankImage;

        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            bankName = itemView.findViewById(R.id.bankName);

            dataCard = itemView.findViewById(R.id.dataCard);

            bankOfferDetails = itemView.findViewById(R.id.bankOfferDetails);

            bankOfferName = itemView.findViewById(R.id.bankOfferName);

            bankImage = itemView.findViewById(R.id.bankImage);



        }
    }
}
