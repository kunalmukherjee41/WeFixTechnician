package com.example.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wefixtechnician.Api.RetrofitClient;
import com.example.wefixtechnician.R;
import com.example.wefixtechnician.adapter.PartListAdapter;
import com.example.wefixtechnician.model.Logs;
import com.example.wefixtechnician.model.Parts;
import com.example.wefixtechnician.model.PartsResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllPartsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView data;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_parts);

        Intent intent = getIntent();
        Logs logs = (Logs) intent.getSerializableExtra("logs");
        assert logs != null;
        getParts(logs);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Parts");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = findViewById(R.id.data);
        data.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

    }

    private void getParts(Logs logs) {
        Call<PartsResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getParts(logs.getCallLogId());

        call.enqueue(
                new Callback<PartsResponse>() {
                    @Override
                    public void onResponse(Call<PartsResponse> call, Response<PartsResponse> response) {
                        assert response.body() != null;
                        if (!response.body().getError()) {
                            if (!response.body().getParts().isEmpty()) {
                                List<Parts> partsList = response.body().getParts();
                                recyclerView.setItemViewCacheSize(partsList.size());
                                PartListAdapter adapter = new PartListAdapter(DisplayAllPartsActivity.this, partsList);
                                adapter.setHasStableIds(true);
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                                data.setVisibility(View.GONE);
                            } else {
                                data.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(DisplayAllPartsActivity.this, "Parts Not Fetch!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<PartsResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        data.setVisibility(View.VISIBLE);
                        Toast.makeText(DisplayAllPartsActivity.this, "Parts Not Fetch!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}