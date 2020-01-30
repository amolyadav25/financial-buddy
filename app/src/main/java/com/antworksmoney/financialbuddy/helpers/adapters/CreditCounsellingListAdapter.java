package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;

import java.util.ArrayList;


public class CreditCounsellingListAdapter extends RecyclerView.Adapter<CreditCounsellingListAdapter.Holder> {

    private Context mContext;
    private ArrayList<String> mAllContactsData;
    private RecyclerView recyclerView;
    private static final String TAG = "LeadStatusAdapter";


    public CreditCounsellingListAdapter(Context context, ArrayList<String> contactData, RecyclerView mRecyclerView) {
        this.mAllContactsData = contactData;
        this.mContext = context;
        recyclerView = mRecyclerView;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_counselling_text_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.text1.setText(mAllContactsData.get(position));

        if (position%2 ==0){
            holder.text1.setBackgroundColor(mContext.getResources().getColor(R.color.gradientbackgroundColor));
        }
    }

    @Override
    public int getItemCount() {
        return mAllContactsData.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView text1;

        Holder(View itemView) {
            super(itemView);

            text1 = itemView.findViewById(R.id.text1);


        }
    }
}
