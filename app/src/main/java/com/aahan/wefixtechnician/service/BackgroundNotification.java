package com.aahan.wefixtechnician.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.aahan.wefixtechnician.R;
import com.aahan.wefixtechnician.ui.MainActivity;

public class BackgroundNotification {

    private static final String CHANNEL_ID = "WeFix_Service";
    private static final String CHANNEL_NAME = "WeFix Service";
    private static final String CHANNEL_DESC = "WeFix Service Notification";

    public static void displayNotification(String title, String message, Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent intent1 = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(true)
                .setContentIntent(intent1)
                .setOnlyAlertOnce(true)
//                .setSound(notificationUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, mBuilder.build());

    }
}
