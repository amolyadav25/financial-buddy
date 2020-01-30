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
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.bumptech.glide.Glide;
import com.cete.dynamicpdf.io.O;

import java.util.ArrayList;

public class LoanListAdapter extends RecyclerView.Adapter<LoanListAdapter.Holder> {

    private Context mContext;
    private ArrayList<LoanInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private View clickedView;

    public LoanListAdapter(Context context, ArrayList<LoanInfoEntity> contactData, RecyclerView mRecyclerView) {
        this.mAllContactsData = contactData;
        this.mContext = context;
        recyclerView = mRecyclerView;

    }

    public interface OnItemClicked {
        void onItemClick(int position, View view);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_loan_list_layout, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LoanListAdapter.Holder holder, int position) {

        holder.userName.setText(mAllContactsData.get(position).getLoanName());

        holder.EMIText.setText("₹" + mAllContactsData.get(position).getEMI());

        holder.accountNumber.setText("A/C No. " + mAllContactsData.get(position).getAccount());

        holder.dueText.setText("Due on : " + mAllContactsData.get(position).getEmi_date());

        holder.BalanceText.setText("₹" + mAllContactsData.get(position).getEmi_balance());

        holder.principalText.setText("₹" + mAllContactsData.get(position).getLoanAmount());

        holder.viewDetailsButton.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition(), view));

        holder.payNowButton.setOnClickListener(view -> onClick.onItemClick(holder.getAdapterPosition(), view));

        if (mAllContactsData.get(position).getState().trim().equalsIgnoreCase("Closed")){
            holder.payNowButton.setVisibility(View.GONE);
        }
        else {
            holder.payNowButton.setVisibility(View.VISIBLE);
        }

    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick=onClick;
    }

    @Override
    public int getItemCount() {
        return mAllContactsData.size();
    }




    class Holder extends RecyclerView.ViewHolder {

        TextView userName, EMIText, accountNumber, dueText, BalanceText, principalText, payNowButton, viewDetailsButton;

        ImageView servicePic;

        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);

            dataCard = itemView.findViewById(R.id.dataCard);

            EMIText = itemView.findViewById(R.id.EMIText);

            accountNumber = itemView.findViewById(R.id.accountNumber);

            dueText = itemView.findViewById(R.id.dueText);

            BalanceText = itemView.findViewById(R.id.BalanceText);

            principalText = itemView.findViewById(R.id.principalText);

            payNowButton = itemView.findViewById(R.id.payNowButton);

            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);

            servicePic = itemView.findViewById(R.id.servicePic);

        }
    }


}
