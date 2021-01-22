package com.aahan.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.adapter.LogHistoryAdapter;
import com.aahan.wefixtechnician.model.Logs;

import java.util.List;
import java.util.Objects;

public class PaymentLogActivity extends AppCompatActivity {

    private List<Logs> logsList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Logs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        logsList = (List<Logs>) intent.getSerializableExtra("logs");
        String mess = intent.getStringExtra("message");

        TextView message = findViewById(R.id.message);
        message.setText(mess);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemViewCacheSize(logsList.size());
        LogHistoryAdapter adapter = new LogHistoryAdapter(this, logsList);
        adapter.setHasStableIds(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}