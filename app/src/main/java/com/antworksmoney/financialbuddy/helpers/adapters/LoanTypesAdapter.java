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
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import java.util.ArrayList;

public class LoanTypesAdapter extends RecyclerView.Adapter<LoanTypesAdapter.Holder> {

    private Context mContext;
    private ArrayList<LoanInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private LoanTypesAdapter.OnItemClicked onClick;

    public LoanTypesAdapter(Context context, ArrayList<LoanInfoEntity> contactData, RecyclerView mRecyclerView) {
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
    public void onBindViewHolder(@NonNull LoanTypesAdapter.Holder holder, int position) {

        holder.userName.setText(mAllContactsData.get(position).getLoanName());

        Glide.with(mContext).load(mAllContactsData.get(position).getLoanImageUrl())
                .asBitmap()
                .error(R.drawable.loan)
                .into(holder.servicePic);

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

    public void setOnClick(LoanTypesAdapter.OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView userName;

        ImageView servicePic;

        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);

            dataCard = itemView.findViewById(R.id.dataCard);

            servicePic = itemView.findViewById(R.id.servicePic);

        }
    }
}
