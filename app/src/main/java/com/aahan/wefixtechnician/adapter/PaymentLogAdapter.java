package com.aahan.wefixtechnician.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.model.PaidLog;
import com.aahan.wefixtechnician.storage.SharedPrefManager;

import java.util.List;

public class PaymentLogAdapter extends RecyclerView.Adapter<PaymentLogAdapter.LogViewHolder> {

    Context mContext;
    List<PaidLog> paidLogList;

    public PaymentLogAdapter(Context mContext, List<PaidLog> paidLogList) {
        this.mContext = mContext;
        this.paidLogList = paidLogList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.payment_details, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        PaidLog paidLog = paidLogList.get(position);

        holder.id.setText(String.valueOf(paidLog.getTblPaymentID()));
        holder.date.setText(paidLog.getPaymentDate());
        holder.delearName.setText(SharedPrefManager.getInstance(mContext).getTechnician().getTechnician_name());
        holder.naration.setText(paidLog.getNaration());
        holder.noOfCall.setText(String.valueOf(paidLog.getNoOfCall()));
        holder.amount.setText(String.valueOf(paidLog.getTotalAmount()));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return paidLogList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, delearName;
        TextView naration, noOfCall, amount;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            delearName = itemView.findViewById(R.id.delear_name);
            naration = itemView.findViewById(R.id.naration);
            noOfCall = itemView.findViewById(R.id.no_of_call);
            amount = itemView.findViewById(R.id.amount);

        }
    }
}
