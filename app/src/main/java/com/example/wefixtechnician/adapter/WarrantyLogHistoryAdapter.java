package com.example.wefixtechnician.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.WarrantyLogHistoryActivity;
import com.example.wefixtechnician.model.Company;
import com.example.wefixtechnician.model.Company1Response;
import com.example.wefixtechnician.model.WarrantyLog;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarrantyLogHistoryAdapter extends RecyclerView.Adapter<WarrantyLogHistoryAdapter.LogViewHolder> {

    Context mContext;
    List<WarrantyLog> warrantyLogsList;
    Company company1;

    public WarrantyLogHistoryAdapter(Context mContext, List<WarrantyLog> warrantyLogsList) {
        this.mContext = mContext;
        this.warrantyLogsList = warrantyLogsList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.warranty_recycler_view, parent, false);
        return new WarrantyLogHistoryAdapter.LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        WarrantyLog warrantyLog = warrantyLogsList.get(position);

        Call<Company1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCompanyById(warrantyLog.getRefCompanyId(), "app");

        call.enqueue(
                new Callback<Company1Response>() {
                    @Override
                    public void onResponse(@NonNull Call<Company1Response> call, @NonNull Response<Company1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            company1 = response.body().getCompany();
                            holder.problemDes.setText(warrantyLog.getProblem());
                            holder.name.setText(warrantyLog.getClientName());
                            assert company1 != null;
//                            Toast.makeText(mContext, company1.getTblCompanyName(), Toast.LENGTH_SHORT).show();
                            holder.company.setText(company1.getTblCompanyName());
                            holder.date.setText(warrantyLog.getCallLogDate());
                            holder.id.setText(String.valueOf(warrantyLog.getCallLogId()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Company1Response> call, @NonNull Throwable t) {

                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> {
                    WarrantyLog log = warrantyLogsList.get(position);
                    Intent intent = new Intent(mContext, WarrantyLogHistoryActivity.class);
                    intent.putExtra("log", (Serializable) log);
                    intent.putExtra("company", company1.getTblCompanyName());
                    mContext.startActivity(intent);
                }
        );

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return warrantyLogsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, name, category, company, problemDes;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            company = itemView.findViewById(R.id.company1);
            problemDes = itemView.findViewById(R.id.problem_des);

        }
    }

}
