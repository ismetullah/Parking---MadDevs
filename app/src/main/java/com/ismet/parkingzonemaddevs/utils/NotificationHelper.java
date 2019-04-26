package com.ismet.parkingzonemaddevs.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.TaskStackBuilder;
import com.ismet.parkingzonemaddevs.R;
import com.ismet.parkingzonemaddevs.data.model.CurrentParking;
import com.ismet.parkingzonemaddevs.data.model.ParkingZone;
import com.ismet.parkingzonemaddevs.ui.main.MainActivity;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager mNotificationManager;
    public static final String NOTIFICATION_CHANNEL = "channel";
    public static final String NOTIFICATION_REQUEST = "NOTIFICATION_REQUEST";
    public static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";

    public NotificationHelper(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        // Create the channel object with the unique ID NOTIFICATION_CHANNEL
        NotificationChannel channel =
                new NotificationChannel(
                        NOTIFICATION_CHANNEL,
                        getString(R.string.notification_channel),
                        NotificationManager.IMPORTANCE_DEFAULT);

        // Configure the channel's initial settings
        channel.setLightColor(Color.GREEN);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);

        // Submit the notification channel object to the notification manager
        getNotificationManager().createNotificationChannel(channel);
    }

    public Notification.Builder getNotificationToStopParking(String title, String body, String request, CurrentParking currentParking) {
        Intent openMainIntent = new Intent(this, MainActivity.class);
        openMainIntent.putExtra(NOTIFICATION_REQUEST, request);
        openMainIntent.putExtra(NOTIFICATION_DATA, currentParking);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(openMainIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL);
        }
        return builder;
    }

    public Notification.Builder getNotificationToPark(String title, String body, String request, ParkingZone parkingZone) {
        Intent openMainIntent = new Intent(this, MainActivity.class);
        openMainIntent.putExtra(NOTIFICATION_REQUEST, request);
        openMainIntent.putExtra(NOTIFICATION_DATA, parkingZone);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(openMainIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL);
        }
        return builder;
    }

    public void notify(int id, Notification.Builder builder) {
        Notification n;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }
        getNotificationManager().notify(id, n);
    }

    private int getSmallIcon() {
        return R.drawable.ic_info;
    }

    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

}