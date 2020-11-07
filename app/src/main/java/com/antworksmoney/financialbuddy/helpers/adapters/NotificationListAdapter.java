package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.Holder> {

    private Context mContext;
    private ArrayList<LoanInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "SuccessLeadStatusAdapter";


    public NotificationListAdapter(Context context, ArrayList<LoanInfoEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Glide.with(mContext).load(mAllContactsData.get(position).getLoanImageUrl())
                .asBitmap()
                .into(holder.notificationIcon);


        holder.bankOfferNames.setText(mAllContactsData.get(position).getLoanName());


        if (mAllContactsData.get(position).getAuditDone().trim().equalsIgnoreCase("0")){
            holder.bankOfferNames.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.bankOfferNames.setTypeface(Typeface.DEFAULT_BOLD);
        }

        holder.notificatonDateTime.setText(printDifference(
                new Date(mAllContactsData.get(position).getDateOfIncorporation()), Calendar.getInstance().getTime()));

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

        TextView bankOfferNames, notificatonDateTime;

        ImageView notificationIcon;

        RelativeLayout dataCard;

        Holder(View itemView) {
            super(itemView);

            dataCard = itemView.findViewById(R.id.dataCard);

            bankOfferNames = itemView.findViewById(R.id.notificationText);

            notificatonDateTime = itemView.findViewById(R.id.notificatonDateTime);

            notificationIcon = itemView.findViewById(R.id.notificationIcon);


        }
    }

    private String printDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String difference = "";
        if (elapsedDays > 0){
            difference = difference + elapsedDays + " days ";
        }
        else if (elapsedHours > 0){
            difference = difference + elapsedHours + " hours ";
        }
        else if (elapsedMinutes > 0){
            difference = difference + elapsedMinutes + " minutes ";
        }
        else if (elapsedSeconds > 0){
            difference = difference + elapsedSeconds + " seconds ";
        }

        return difference + "ago";
    }
}
