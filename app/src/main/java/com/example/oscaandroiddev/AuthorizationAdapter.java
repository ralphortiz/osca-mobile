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

public class AuthorizationAdapter extends RecyclerView.Adapter<AuthorizationAdapter.MyViewHolder> {

    Context mContext;
    List<AuthorizeItem> mAuthorizeList = new ArrayList<>();
    OnEditClickListener mOnEditClickListener;
    OnDeleteClickListener mOnDeleteClickListener;

    public AuthorizationAdapter(Context mContext, List<AuthorizeItem> mAuthorizeList, OnEditClickListener mOnEditClickListener, OnDeleteClickListener mOnDeleteClickListener) {
        this.mContext = mContext;
        this.mAuthorizeList = mAuthorizeList;
        this.mOnEditClickListener = mOnEditClickListener;
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnEditClickListener {
        void onEditClick(AuthorizeItem authorizeItem);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_authorization, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AuthorizeItem authorizeItem = mAuthorizeList.get(position);
        holder.tvProdName.setText(authorizeItem.getProductName());
        holder.tvProdQuantity.setText("Quantity: " + authorizeItem.getProductQuantity());
        if (authorizeItem.getAdditionalNotes().equals("") ) {
            holder.tvAddNotes.setText("None");
        } else {
            holder.tvAddNotes.setText(authorizeItem.getAdditionalNotes());
        }
    }


    @Override
    public int getItemCount() {
        return mAuthorizeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProdName, tvProdQuantity, tvAddNotes, btnEdit, btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProdName = itemView.findViewById(R.id.tvProdName);
            tvProdQuantity = itemView.findViewById(R.id.tvProdQuantity);
            tvAddNotes = itemView.findViewById(R.id.tvAddNotes);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    deleteItem(getAdapterPosition());
                    mOnDeleteClickListener.onDeleteClick(getAdapterPosition());
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnEditClickListener.onEditClick(mAuthorizeList.get(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

//    public void deleteItem(int position) {
//        mAuthorizeList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
//    }
}
