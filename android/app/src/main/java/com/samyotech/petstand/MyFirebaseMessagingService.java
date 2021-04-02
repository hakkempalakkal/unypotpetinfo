package com.samyotech.petstand;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.samyotech.petstand.activity.chat.ChatNotificationActivity;
import com.samyotech.petstand.activity.notification.NotificationActivity;
import com.samyotech.petstand.sharedprefrence.SharedPrefrence;
import com.samyotech.petstand.utils.Consts;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    SharedPrefrence prefrence;
    int i = 0;
    String refreshedToken;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        prefrence = SharedPrefrence.getInstance(this);
        Log.e(TAG, "From: " + remoteMessage.getData());

      /*
        if (remoteMessage.getData() != null) {

            if (remoteMessage.getNotification().getTitle().equalsIgnoreCase("Chat")) {*/
        if (remoteMessage.getData() != null) {
            if (remoteMessage.getData().get("type").equalsIgnoreCase("Chat")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Consts.BROADCAST);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                i = prefrence.getIntValue("Value");
                i++;
                prefrence.setIntValue("Value", i);
                // sendNotification(remoteMessage.getNotification().getBody());
                sendChat(getValue(remoteMessage.getData(), "sender_id"), getValue(remoteMessage.getData(), "senderName"), getValue(remoteMessage.getData(), "title"), getValue(remoteMessage.getData(), "body"));
            } else {
                sendNotification(getValue(remoteMessage.getData(), "body"));
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Consts.TOKAN, token);
        editor.commit();


        sendRegistrationToServer(token);
        SharedPreferences userDetails = MyFirebaseMessagingService.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Log.e(TAG, "my token: " + userDetails.getString(Consts.TOKAN, ""));

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    public String getValue(Map<String, String> data, String key) {
        try {
            if (data.containsKey(key))
                return data.get(key);
            else
                return getString(R.string.app_name);
        } catch (Exception ex) {
            ex.printStackTrace();
            return getString(R.string.app_name);
        }
    }


    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Consts.PET_CARE)
                .setSound(defaultSoundUri)
                /* .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))*/
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

    private void sendChat(String id, String name, String title, String messageBody) {

        Intent intent = new Intent(this, ChatNotificationActivity.class);
        intent.putExtra(Consts.USER_ID, id);
        intent.putExtra(Consts.NAME, name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";

        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Consts.PET_CARE)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody).setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }

}


//e7q2YZRtQEA:APA91bGua6Fg6xP33iq-e0s7OEPDI_Rc19V7pYt_LR7u32OFwUuDlHCwP_sx1YVTUPywQGp4OlfPrD45QJThrH9Do2y1jgIq1yUputQLNATAah7IxHAIN9rMITHrMZI0zTi7yyyiXFWH
