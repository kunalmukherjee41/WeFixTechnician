package com.example.wefixtechnician.sendNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.wefixtechnician.ui.MainActivity;
import com.example.wefixtechnician.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    String title, message;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        showNotification(title, message);
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext(), "MyChannel")
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle(title)
//                        .setAutoCancel(true)
//                        .setContentText(message);
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        assert manager != null;
//        manager.notify(0, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = new Random().nextInt();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, "MyChannel")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .build();
        assert nm != null;
        nm.notify(notificationID, notification);
        crateNotificationChannel(nm);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void crateNotificationChannel(NotificationManager nm) {
        String channelName = "ANYTHING";
        NotificationChannel nc = new NotificationChannel("MyChannel", channelName, IMPORTANCE_HIGH);
        nm.createNotificationChannel(nc);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
//        MainActivity a = new MainActivity();
//        a.UpdateToken();
    }
}
