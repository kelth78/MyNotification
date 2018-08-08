package com.kelth.mynotification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            CharSequence str = remoteInput.getCharSequence(MainActivity.NOTIFICATION_REPLY);
            Log.d(TAG, "Received String: " + str);

            // Build a new notification, which informs the user that the system
            // handled their interaction with the previous notification.
            Notification repliedNotification = new Notification.Builder(context, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentText("Sent!")
                    .build();

            // Issue the new notification.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(MainActivity.NOTIFICATION_ID, repliedNotification);
        }
    }
}
