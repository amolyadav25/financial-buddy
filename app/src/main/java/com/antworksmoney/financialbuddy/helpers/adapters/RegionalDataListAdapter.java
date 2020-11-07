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
import android.widget.TextView;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.RegionalDataInfo;
import java.util.ArrayList;


public class RegionalDataListAdapter extends RecyclerView.Adapter<RegionalDataListAdapter.Holder> implements Filterable {

    private Context mContext;
    private ArrayList<RegionalDataInfo> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;


    public RegionalDataListAdapter(Context context, ArrayList<RegionalDataInfo> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regional_data_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(mAllContactsData.get(position).getName());

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

    @Override
    public Filter getFilter() {
        return new DataFilter();
    }

    private ArrayList<RegionalDataInfo> datavalues;



    private class DataFilter extends Filter {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAllContactsData = (ArrayList<RegionalDataInfo>) results.values;
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
                    ArrayList<RegionalDataInfo> list = new ArrayList<>(datavalues);
                    r.values = list;
                    r.count = list.size();
                }
            } else {
                ArrayList<RegionalDataInfo> values = datavalues;
                int count = values.size();
                ArrayList<RegionalDataInfo> list = new ArrayList<>();
                String prefix = constraint.toString().toLowerCase();
                for (int i = 0; i < count; i++) {
                    if ((values.get(i).Name.toLowerCase().contains(prefix))) {
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

        TextView name;

        CardView dataCard;

        Holder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);

            dataCard = itemView.findViewById(R.id.dataCard);

        }
    }
}
