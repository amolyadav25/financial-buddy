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

import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.Entity.WalletEntity;

import java.util.ArrayList;


public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.Holder> {

    private Context mContext;
    private ArrayList<WalletEntity> mAllContactsData;
    private RecyclerView recyclerView;
    private OnItemClicked onClick;
    private static final String TAG = "LeadStatusAdapter";


    public TransactionListAdapter(Context context, ArrayList<WalletEntity> contactData, RecyclerView mRecyclerView) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_history, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

//        Log.e(TAG,mAllContactsData.toString());

        holder.bankOfferName.setText("Rs. "+ mAllContactsData.get(position).getAmount() + " recieved.");

        holder.bankOfferDetails.setText("Money Earned : Rs."+mAllContactsData.get(position).getAmount()+"\n"+
                                        "Credited on : "+ mAllContactsData.get(position).getCreatedDate()+"\n"+
                                        "Lead id : "+ mAllContactsData.get(position).getLeadId()+"\n"+
                                        "Payout Percentage : "+ mAllContactsData.get(position).getPayoutPercentage()+" % \n"+
                                        "Payout Id : "+mAllContactsData.get(position).getPayoutId()+"\n");

        holder.bankName.setText("Borrower Name : "+ mAllContactsData.get(position).getBorrowerName()+"\n"+
                                    "Loan Amount : Rs. " + mAllContactsData.get(position).getLoanAmount()+"\n"+
                                    "Loan Type : "+ mAllContactsData.get(position).getLoanType()+"\n"+
                                    "Disbursed loan Amount : Rs. "+ mAllContactsData.get(position).getDisburseLoanAmount()+"\n");

        holder.borowwerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View showMoreView = recyclerView.findViewHolderForAdapterPosition(holder.getAdapterPosition()).itemView;
                TextView showMoreTextView = showMoreView.findViewById(R.id.borowwerName);
                TextView officeAddressTextView = showMoreView.findViewById(R.id.bankName);

                if (officeAddressTextView.isShown()) {
                    showMoreTextView.setText("Show Less");
                    showMoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_up_black_24dp, 0);
                    officeAddressTextView.setVisibility(View.GONE);
                } else {
                    showMoreTextView.setText("Show More");
                    showMoreTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_keyboard_arrow_down_black_24dp, 0);
                    officeAddressTextView.setVisibility(View.VISIBLE);
                }
            }
        });


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


    class Holder extends RecyclerView.ViewHolder {

        TextView bankName, bankOfferName, bankOfferDetails, borowwerName;

        ImageView bankImage, statusView;

        RelativeLayout dataCard;

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
