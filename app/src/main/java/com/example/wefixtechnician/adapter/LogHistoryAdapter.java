package com.example.wefixtechnician.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.ui.LogHistoryDetailsActivity;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.model.Category;
import com.example.wefixtechnician.model.Category1Response;
import com.example.wefixtechnician.model.Logs;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogHistoryAdapter extends RecyclerView.Adapter<LogHistoryAdapter.LogViewHolder> {

    private Context mContext;
    private List<Logs> logsList;

    public LogHistoryAdapter(Context mContext, List<Logs> logsList) {
        this.mContext = mContext;
        this.logsList = logsList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.log_history, parent, false);
        return new LogViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {

        Logs logs = logsList.get(position);

        holder.id.setText(String.valueOf(logs.getCallLogId()));
        holder.date.setText(logs.getCallLogDate());
        holder.charge.setText(String.valueOf(logs.getAmount()));
        holder.company.setText(logs.getProductCompany());

        int tbl_category_id = logs.getRefCatId();

        Call<Category1Response> call = RetrofitClient
                .getInstance()
                .getApi()
                .getCategoryByID(tbl_category_id, "app");

        call.enqueue(
                new Callback<Category1Response>() {
                    @Override
                    public void onResponse(Call<Category1Response> call, Response<Category1Response> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            Category category = response.body().getCategory();
                            holder.name.setText(category.getTbl_category_name());
                            holder.name1.setText(category.getTbl_category_name());
//                            Toast.makeText(mContext, category.getTbl_category_image(), Toast.LENGTH_LONG).show();
//                            Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image);
                            Picasso.get().load("https://wefixservice/product/" + category.getTbl_category_image()).into(holder.image);
//                            Glide.with(mContext).load("http://wefix.sitdoxford.org/product/" + category.getTbl_category_image()).into(holder.image);
                        }
                    }

                    @Override
                    public void onFailure(Call<Category1Response> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        holder.itemView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(mContext, LogHistoryDetailsActivity.class);
                    intent.putExtra("logs", logsList.get(position));
                    mContext.startActivity(intent);
                }
        );
    }

    @Override
    public int getItemCount() {
        return logsList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {

        TextView date, id, details, name;
        ImageView image;
        TextView name1, company, charge;
        private RelativeLayout layout;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            id = itemView.findViewById(R.id.id);
            details = itemView.findViewById(R.id.details);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            name1 = itemView.findViewById(R.id.name1);
            company = itemView.findViewById(R.id.company);
            charge = itemView.findViewById(R.id.charge);
            layout = itemView.findViewById(R.id.layout);

        }
    }

}
