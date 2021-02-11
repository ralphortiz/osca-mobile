package com.example.oscaandroiddev;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionViewHolder> {

    Context mContext;
    List<TransactionHistory> mTransactionHistoryList = new ArrayList<>();
    OnDetailsClickListener mOnDetailsClickListener;

    public TransactionHistoryAdapter(Context mContext, List<TransactionHistory> transactionHistoryList, OnDetailsClickListener mOndetailsClickListener) {
        this.mContext = mContext;
        this.mTransactionHistoryList = transactionHistoryList;
        this.mOnDetailsClickListener = mOndetailsClickListener;
    }

    public interface OnDetailsClickListener {
        void onDetailsClick(TransactionHistory transactionHistory);
    }

    @NonNull
    @Override
    public TransactionHistoryAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_history, viewGroup, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryAdapter.TransactionViewHolder holder, int position) {
        TransactionHistory transactionHistory = mTransactionHistoryList.get(position);
        holder.tvCompanyName.setText(transactionHistory.getCompanyName());
        holder.tvTransDate.setText(transactionHistory.getTransDate());
    }

    @Override
    public int getItemCount() {
        return mTransactionHistoryList.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompanyName;
        TextView tvTransDate;
        TextView btnDetails;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvTransDate = itemView.findViewById(R.id.tvTransDate);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDetailsClickListener.onDetailsClick(mTransactionHistoryList.get(getAdapterPosition()));
                }
            });
        }
    }
}

