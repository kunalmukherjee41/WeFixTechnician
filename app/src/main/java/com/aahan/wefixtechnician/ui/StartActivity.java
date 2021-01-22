package com.aahan.wefixtechnician.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.aahan.wefixtechnician.R;

public class StartActivity extends AppCompatActivity {

    Handler handler;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

//        Intent intent1 = new Intent(this, BackgroundReceiver.class);
//        intent1.setAction("android.intent.action.BOOT_COMPLETED");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 1, pendingIntent);

        imageView = findViewById(R.id.imageView);
        imageView.animate().alpha(4000).setDuration(0);

        handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }, 3000);

    }
}