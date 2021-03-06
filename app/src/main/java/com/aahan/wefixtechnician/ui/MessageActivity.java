package com.aahan.wefixtechnician.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aahan.wefixtechnician.R;

import java.util.Objects;

public class MessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Message");

        TextView name = findViewById(R.id.log_submission);

        String s = "Complete Call Log";

        name.setText(s);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 4000);

    }
}