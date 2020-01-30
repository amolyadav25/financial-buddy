package com.antworksmoney.financialbuddy.helpers.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.helpers.Graphics.ColorGenerator;

import java.util.ArrayList;


public class ContactsCompleteListDataViewAdapter extends RecyclerView.Adapter<ContactsCompleteListDataViewAdapter.Holder> implements Filterable {

    private Context mContext;
    private ArrayList<ProfileInfo> mAllContactsData;
    private Db_Helper dataBaseObject;
    private RecyclerView recyclerView;
    private ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
    private OnItemClicked onClick;


    public ContactsCompleteListDataViewAdapter(Context context, ArrayList<ProfileInfo> contactData, RecyclerView mRecyclerView) {
        this.mAllContactsData = contactData;
        this.mContext = context;
        dataBaseObject = new Db_Helper(mContext);
        recyclerView = mRecyclerView;

    }

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_complete_list_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(mAllContactsData.get(position).getName());

        Drawable mDrawable = ContextCompat.getDrawable(mContext, R.drawable.contact_view_circle_black);

        if (mDrawable != null) {
            mDrawable.setColorFilter(new PorterDuffColorFilter(
                    colorGenerator.getColor(mAllContactsData.get(position).getName().substring(0,1)),
                    PorterDuff.Mode.SRC_IN));
            holder.contact_text.setBackground(mDrawable);
        }

        holder.contact_text.setText(mAllContactsData.get(position).getName().substring(0,1));

        holder.contacts_number.setText(mAllContactsData.get(position).getPhoneNumber());

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

    private ArrayList<ProfileInfo> datavalues;



    private class DataFilter extends Filter {
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAllContactsData = (ArrayList<ProfileInfo>) results.values;
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
                    ArrayList<ProfileInfo> list = new ArrayList<>(datavalues);
                    r.values = list;
                    r.count = list.size();
                }
            } else {
                ArrayList<ProfileInfo> values = datavalues;
                int count = values.size();
                ArrayList<ProfileInfo> list = new ArrayList<>();
                String prefix = constraint.toString().toLowerCase();
                for (int i = 0; i < count; i++) {
                    if ((values.get(i).name.toLowerCase().contains(prefix))) {
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

        TextView name, contact_text, contacts_number;

        RelativeLayout dataCard;

        ImageView contact_image;

        Holder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contacts_name);
            contact_text = itemView.findViewById(R.id.contact_text);
            contacts_number = itemView.findViewById(R.id.contacts_number);
            dataCard = itemView.findViewById(R.id.dataCard);
            contact_image = itemView.findViewById(R.id.contact_image);

        }
    }
}
