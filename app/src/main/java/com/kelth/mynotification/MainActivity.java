package com.kelth.mynotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_REPLY = "KeyNotificationReply";
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "channel_name";
    public static final String TEXT_REPLY = "text_reply";

    public static final int REQUEST_CODE_REPLY = 100;
    public static final int NOTIFICATION_ID = 200;

    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Choose 1
        //setupNotification();
        //setupNotificationWithLargerTextArea();
        //setupNotificationWithAction();
        setupNotificationWithReplyAction();

        // Trigger notification
        findViewById(R.id.button_trigger_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        createNotificationChannel();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupNotification() {
        // Default
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.title))
                .setContentText(getString(R.string.content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void setupNotificationWithLargerTextArea() {
        // Larger text area
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void setupNotificationWithAction() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private void setupNotificationWithReplyAction() {

        // Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(
                this,
                        REQUEST_CODE_REPLY,
                        new Intent(this, MyBroadcastReceiver.class).putExtra(TEXT_REPLY, REQUEST_CODE_REPLY),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // We need this object for getting direct input from notification
        RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_REPLY)
                .setLabel(getResources().getString(R.string.reply_label))
                .build();

        // Create the reply action and add the remote input.
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.notification_icon,
                        getString(R.string.reply_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification and add the action.
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.title))
                .setContentText(getString(R.string.content))
                .setContentIntent(replyPendingIntent)
                .addAction(action);
    }
}

