package com.antworksmoney.financialbuddy.helpers.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.Entity.ProfileInfo;
import com.antworksmoney.financialbuddy.views.fragments.Network.AddNetworkDetails;
import com.antworksmoney.financialbuddy.views.fragments.Network.MyNetworkHome;

import java.util.ArrayList;


public class ContactsDataViewAdapter extends RecyclerView.Adapter<ContactsDataViewAdapter.Holder> {

    private Context mContext;
    private ArrayList<ProfileInfo> mAllContactsData;
    private Db_Helper dataBaseObject;
    private RecyclerView recyclerView;

    public ContactsDataViewAdapter(Context context, ArrayList<ProfileInfo> contactData, RecyclerView mRecyclerView) {
        this.mAllContactsData = contactData;
        this.mContext = context;
        dataBaseObject = new Db_Helper(mContext);
        recyclerView = mRecyclerView;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.name.setText(mAllContactsData.get(position).getName());

        holder.contact_text.setText(mAllContactsData.get(position).getName().substring(0, 2));

        holder.contacts_number.setText(mAllContactsData.get(position).getPhoneNumber());

        holder.removeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(holder.getAdapterPosition(),
                        "Do you want to remove " +
                        ((TextView) recyclerView.findViewHolderForAdapterPosition(holder.getAdapterPosition())
                        .itemView.findViewById(R.id.contacts_name)).getText().toString().trim() + " from your Financial Buddy Network ?");

            }
        });

        holder.addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.homeParent, AddNetworkDetails.newInstance(mAllContactsData.get(holder.getAdapterPosition())));
                transaction.addToBackStack(null).commit();
            }
        });

        Fragment currentFragment = ((FragmentActivity) mContext).getSupportFragmentManager().findFragmentById(R.id.homeParent);

        if (currentFragment instanceof MyNetworkHome) {
            holder.removeContact.setVisibility(View.INVISIBLE);
        }
        else {
            holder.addDetails.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mAllContactsData.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView name, contact_text, contacts_number;
        ImageView removeContact;
        ImageView addDetails;

        Holder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.contacts_name);
            contact_text = itemView.findViewById(R.id.contact_text);
            contacts_number = itemView.findViewById(R.id.contacts_number);
            removeContact = itemView.findViewById(R.id.removeContact);
            addDetails = itemView.findViewById(R.id.addDetails);

        }
    }

    private void showAlertDialog(int position, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.ic_delete_black_24dp);
        builder1.setTitle("Confirm Deletion");
        builder1.setPositiveButton("Yes", (dialog, id) -> {
            removeAt(position);
            if (recyclerView.findViewHolderForAdapterPosition(position)!= null) {
                dataBaseObject.deleteContactFromDb(
                        ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.contacts_number)).getText().toString().trim());
            }
            dialog.cancel();
        });

        builder1.setNegativeButton("NO", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void removeAt(int position) {
        try {
            mAllContactsData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mAllContactsData.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
