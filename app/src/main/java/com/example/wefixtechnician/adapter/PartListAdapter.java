package com.example.wefixtechnician.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.model.My1Response;
import com.example.wefixtechnician.model.Parts;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.ListViewHolder> {

    Context mContext;
    List<Parts> partsList;

    public PartListAdapter(Context mContext, List<Parts> partsList) {
        this.mContext = mContext;
        this.partsList = partsList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parts_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        Parts parts = partsList.get(position);
        holder.partAmount.setText(String.valueOf(parts.getAmount()));
        holder.partName.setText(parts.getPartsDes());
        String txtDate = parts.getEntryDate() + " " + parts.getEntryTime();
        holder.date.setText(txtDate);
        holder.id.setText(String.valueOf(parts.getLogPartsID()));
        holder.cancel.setOnClickListener(
                v -> {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(mContext));
                    builder1.setMessage("You Want Delete the Part?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog1, id1) -> {
                                ProgressDialog progressBar = new ProgressDialog(mContext);
                                progressBar.show();
                                progressBar.setContentView(R.layout.progress_dialog);
                                Objects.requireNonNull(progressBar.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

                                Parts parts1 = partsList.get(position);
                                Call<My1Response> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .deletePartsByID(parts1.getLogPartsID());

                                call.enqueue(
                                        new Callback<My1Response>() {
                                            @Override
                                            public void onResponse(Call<My1Response> call, Response<My1Response> response) {
                                                assert response.body() != null;
                                                if (response.body().getMessage().equals("Parts has been deleted")) {
                                                    partsList.remove(parts1);
                                                    notifyDataSetChanged();
                                                }
                                                progressBar.dismiss();
                                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<My1Response> call, Throwable t) {
                                                progressBar.dismiss();
                                                Toast.makeText(mContext, "Parts Not Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            })
                            .setNegativeButton("No", (dialog1, id1) -> dialog1.cancel());
                    AlertDialog alert = builder1.create();
                    alert.show();

                }
        );
    }

    @Override
    public int getItemCount() {
        return partsList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView id, partAmount;
        private TextView partName, date;
        private ImageButton cancel;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.id);
            partName = itemView.findViewById(R.id.parts_name);
            partAmount = itemView.findViewById(R.id.parts_amount);
            date = itemView.findViewById(R.id.date);
            cancel = itemView.findViewById(R.id.cancel);

        }
    }
}
