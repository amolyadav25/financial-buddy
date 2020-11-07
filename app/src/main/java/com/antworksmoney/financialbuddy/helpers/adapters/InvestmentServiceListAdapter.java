package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.views.fragments.Investment.InvestmentApplyForFragment;

import java.util.ArrayList;


public class InvestmentServiceListAdapter extends RecyclerView.Adapter<InvestmentServiceListAdapter.Holder> {

    private ArrayList<InvestmentEntity> mServicePojoArrayList;
    private Context mContext;
    private String mType;
    private static final String TAG = "ServiceListAdapter";
    private OnItemClicked onClick;

    public InvestmentServiceListAdapter(Context context, ArrayList<InvestmentEntity> servicePojoArrayList, String type) {
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

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.loan_list_layout, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        InvestmentEntity data = mServicePojoArrayList.get(position);

        Glide.with(mContext).load(data.getInvestmentImage())
                .asBitmap()
                .error(R.drawable.investment_icon)
                .into(holder.servicePic);

        holder.userName.setText(data.getInvestmentType());

        holder.userName.setTextColor(mContext.getResources().getColor(R.color.background_color));

        Fragment currentFragment = ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.homeParent);

        if (currentFragment instanceof InvestmentApplyForFragment) {
            holder.userName.setTextColor(mContext.getResources().getColor(R.color.image_gradient_color));
        }

        data.setApplyFor(mType);

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

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView userName;
        ImageView servicePic;
        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            dataCard = itemView.findViewById(R.id.dataCard);
            userName = itemView.findViewById(R.id.userName);
            servicePic = itemView.findViewById(R.id.servicePic);

        }
    }
}
