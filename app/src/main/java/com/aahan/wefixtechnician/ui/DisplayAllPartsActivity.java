package com.aahan.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aahan.wefixtechnician.Api.RetrofitClient;
import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.adapter.PartListAdapter;
import com.aahan.wefixtechnician.model.Logs;
import com.aahan.wefixtechnician.model.Parts;
import com.aahan.wefixtechnician.model.PartsResponse;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllPartsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private TextView data;
    private Logs logs;
    private List<Parts> partsList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_parts);

        mSwipeRefreshLayout = findViewById(R.id.layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Intent intent = getIntent();
        logs = (Logs) intent.getSerializableExtra("logs");
        assert logs != null;
        getParts();

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

    private void getParts() {
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
                                partsList = response.body().getParts();
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

    @Override
    public void onRefresh() {
        if (partsList != null)
            partsList.clear();
        getParts();
        new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
        if (!partsList.isEmpty()) {
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        }
    }
}