package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.InvestmentEntity;
import com.antworksmoney.financialbuddy.views.customViews.LabelView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class InvestmentStatusAdapter extends RecyclerView.Adapter<InvestmentStatusAdapter.Holder> implements Filterable {

    private Context mContext;
    private ArrayList<InvestmentEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "LeadStatusAdapter";

    public InvestmentStatusAdapter(Context context, ArrayList<InvestmentEntity> contactData, RecyclerView mRecyclerView) {
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

        holder.bankOfferName.setText(MessageFormat.format("{0}",
                mAllContactsData.get(position).getInvestment_product()));

        holder.bankOfferDetails.setText("Customer Name : " + mAllContactsData.get(position).getFirst_name()+" " + mAllContactsData.get(position).getLast_name() + "\n" +
                "Company Name : " + mAllContactsData.get(position).getCompany_name().trim() + "\n" +
                "Product : " + mAllContactsData.get(position).getInvestment_product() + "\n" +
                "Status : " + mAllContactsData.get(position).getStatus()+"\n"+
                "Applied On : " + mAllContactsData.get(position).getDate_added() + "\n");

        holder.bankImage.setPadding(30,30,30,30);
        holder.bankImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.investment_icon));
        holder.bankImage.setBackground(mContext.getResources().getDrawable(R.drawable.contact_view_circle_accent));

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

        otherDetailsText = otherDetailsText + "State : "+ mAllContactsData.get(position).getState()+"\n";

        otherDetailsText = otherDetailsText + "Int Rate 1 : " + mAllContactsData.get(position).getInt_rate1() + "\n"+
                "Int Rate 2 : " + mAllContactsData.get(position).getInt_rate2() + "\n"+
                "Int Rate 3 : " + mAllContactsData.get(position).getInt_rate3() + "\n"+
                "Int Rate 4 : " + mAllContactsData.get(position).getInt_rate4() + "\n"+
                "Int Rate 5 : " + mAllContactsData.get(position).getInt_rate5() + "\n"+
                "Int Rate 6 : " + mAllContactsData.get(position).getInt_rate6() + "\n"+
                "Int Rate 7 : " + mAllContactsData.get(position).getInt_rate7() + "\n"+
                "Sr. Citizen : "+ mAllContactsData.get(position).get_sr_citizen();

        holder.bankName.setText(otherDetailsText);

        holder.dataCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

        LabelView label = new LabelView(mContext);
        label.setText(mAllContactsData.get(position).getLead_status());
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

    private ArrayList<InvestmentEntity> datavalues;



    private class DataFilter extends Filter {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAllContactsData = (ArrayList<InvestmentEntity>) results.values;
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
                    ArrayList<InvestmentEntity> list = new ArrayList<>(datavalues);
                    r.values = list;
                    r.count = list.size();
                }
            } else {
                ArrayList<InvestmentEntity> values = datavalues;
                int count = values.size();
                ArrayList<InvestmentEntity> list = new ArrayList<>();
                String prefix = constraint.toString().toLowerCase();
                for (int i = 0; i < count; i++) {
                    if (((values.get(i).first_name+" " + values.get(i).last_name).toLowerCase().contains(prefix))) {
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
