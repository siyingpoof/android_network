package com.example.alvinaong.android_network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ConnectivityStatusReceiver extends BroadcastReceiver {

    static final String TAG = "ConnectivityStatus";
    static final String INTENT_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
    static final String CHANNEL_ID = "network";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        if (!isConnected(context)) {
            Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "network notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("network change notification");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Your network status");
            builder.setContentText("Disconnected");
            notificationManager.notify(1, builder.build());
        } else {
            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
