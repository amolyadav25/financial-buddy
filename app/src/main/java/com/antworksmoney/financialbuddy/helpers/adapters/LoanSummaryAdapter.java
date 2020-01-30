package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;

import java.util.ArrayList;


public class LoanSummaryAdapter extends RecyclerView.Adapter<LoanSummaryAdapter.Holder> {

    private Context mContext;
    private ArrayList<LoanInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;


    public LoanSummaryAdapter(Context context, ArrayList<LoanInfoEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_summary_item_row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        LoanInfoEntity entity = mAllContactsData.get(position);

        holder.loanType.setText(entity.getEmi_date());

        holder.reference1.setText(entity.getEMI());

        holder.reference2.setText(entity.getEmi_interest());

        holder.dateText.setText(entity.getEmi_principal());

        holder.debitText.setText(entity.getEmi_balance());

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

        TextView loanType, reference1, reference2, dateText, debitText, creditText;

        Holder(View itemView) {
            super(itemView);

            loanType = itemView.findViewById(R.id.loanType);

            reference1 = itemView.findViewById(R.id.reference1);

            reference2 = itemView.findViewById(R.id.reference2);

            dateText = itemView.findViewById(R.id.dateText);

            debitText = itemView.findViewById(R.id.debitText);

        }
    }
}
