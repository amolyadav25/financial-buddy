package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.LeadInfoEntity;
import com.antworksmoney.financialbuddy.views.customViews.LabelView;

import java.text.MessageFormat;
import java.util.ArrayList;


public class LeadStatusAdapter extends RecyclerView.Adapter<LeadStatusAdapter.Holder> implements Filterable {

    private Context mContext;
    private ArrayList<LeadInfoEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "LeadStatusAdapter";


    public LeadStatusAdapter(Context context, ArrayList<LeadInfoEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lead_status, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.bankOfferName.setText(MessageFormat.format("{0} of Rs. {1}",
                mAllContactsData.get(position).getLoanType(),
                mAllContactsData.get(position).getLoanAmount()));

        holder.bankOfferDetails.setText("Customer Name : " + mAllContactsData.get(position).getBorowwerName() + "\n" +
                "Loan Amount : " + mAllContactsData.get(position).getLoanAmount() + "\n" +
                "Product : " + mAllContactsData.get(position).getLoanType() + "\n" +
                "Status : " + mAllContactsData.get(position).getSubmittedToBanks()+"\n"+
                "Applied On : " + mAllContactsData.get(position).getCreated_date() + "\n");

        holder.borowwerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View showMoreView = recyclerView.findViewHolderForAdapterPosition(holder.getAdapterPosition()).itemView;
                TextView showMoreTextView = showMoreView.findViewById(R.id.borowwerName);
                TextView officeAddressTextView = showMoreView.findViewById(R.id.bankName);

                if (officeAddressTextView.isShown()) {
                    showMoreTextView.setText("Less Details");
                    showMoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    officeAddressTextView.setVisibility(View.GONE);
                } else {
                    showMoreTextView.setText("More Details");
                    showMoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    officeAddressTextView.setVisibility(View.VISIBLE);
                }
            }
        });

        String otherDetailsText = "Other Details : \n";
        if (mAllContactsData.get(position).getEmail().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Email : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Email : " + mAllContactsData.get(position).getEmail() + "\n";
        }

        if (mAllContactsData.get(position).getCity().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "City : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "City : " + mAllContactsData.get(position).getCity() + "\n";
        }

        if (mAllContactsData.get(position).getAddress().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Address : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Address : " + mAllContactsData.get(position).getAddress() + "\n";
        }

        if (mAllContactsData.get(position).getPin().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Pin : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Pin : " + mAllContactsData.get(position).getPin() + "\n";
        }

        if (mAllContactsData.get(position).getOccupation().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Occupation : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Occupation : " + mAllContactsData.get(position).getOccupation() + "\n";
        }

        if (mAllContactsData.get(position).getCompanyType().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Company Type : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Company Type : " + mAllContactsData.get(position).getCompanyType() + "\n";
        }

        if (mAllContactsData.get(position).getCompanyName().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Company Name : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Company Name : " + mAllContactsData.get(position).getCompanyName() + "\n";
        }

        if (mAllContactsData.get(position).getIncome().trim().equalsIgnoreCase("")) {
            otherDetailsText = otherDetailsText + "Income : Not Provided\n";
        } else {
            otherDetailsText = otherDetailsText + "Income : " + mAllContactsData.get(position).getIncome() + "\n";
        }

        holder.bankName.setText(otherDetailsText);

        holder.dataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

        LabelView label = new LabelView(mContext);
        label.setText(mAllContactsData.get(position).getLeadStatus());
        label.setBackgroundColor(0xffE91E63);
        label.setTargetView(holder.statusView, 10,
                LabelView.Gravity.RIGHT_TOP);

    }

    @Override
    public int getItemCount() {
        return mAllContactsData.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public Filter getFilter() {
        return new DataFilter();
    }

    private ArrayList<LeadInfoEntity> datavalues;



    private class DataFilter extends Filter {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAllContactsData = (ArrayList<LeadInfoEntity>) results.values;
            notifyDataSetChanged();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults r = new FilterResults();
            if (datavalues == null) {
                synchronized (mAllContactsData) {
                    datavalues = new ArrayList<>(mAllContactsData);
                }
            }
            if (constraint == null || constraint.length() == 0) {
                synchronized (mAllContactsData) {
                    ArrayList<LeadInfoEntity> list = new ArrayList<>(datavalues);
                    r.values = list;
                    r.count = list.size();
                }
            } else {
                ArrayList<LeadInfoEntity> values = datavalues;
                int count = values.size();
                ArrayList<LeadInfoEntity> list = new ArrayList<>();
                String prefix = constraint.toString().toLowerCase();
                for (int i = 0; i < count; i++) {
                    if ((values.get(i).borowwerName.toLowerCase().contains(prefix))) {
                        list.add(values.get(i));
                    }
                }
                r.values = list;
                r.count = list.size();
            }
            return r;
        }
    }


    class Holder extends RecyclerView.ViewHolder {

        TextView bankName, bankOfferName, bankOfferDetails, borowwerName;

        ImageView bankImage, statusView;

        CardView dataCard;

        Holder(View itemView) {
            super(itemView);

            bankName = itemView.findViewById(R.id.bankName);

            dataCard = itemView.findViewById(R.id.dataCard);

            bankOfferDetails = itemView.findViewById(R.id.bankOfferDetails);

            bankOfferName = itemView.findViewById(R.id.bankOfferName);

            bankImage = itemView.findViewById(R.id.bankImage);

            borowwerName = itemView.findViewById(R.id.borowwerName);

            statusView = itemView.findViewById(R.id.statusView);


        }
    }
}
