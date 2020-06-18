package com.example.alvinaong.android_network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    static final String TAG = "MyService";
    static final String CHANNEL_ID = "network";
    static final String INTENT_ACTION = "network change";

    private ConnectivityStatusReceiver mcsr;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        mcsr = new ConnectivityStatusReceiver();

        Log.i(TAG, "service started and running");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        IntentFilter intentFilter = new IntentFilter(ConnectivityStatusReceiver.INTENT_ACTION);
        registerReceiver(mcsr, intentFilter);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "network notification", NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("network change notification");
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Your network status");
        builder.setContentText("Hello");
        notificationManager.notify(1, builder.build());
        startForeground(1, builder.build());

//        Intent foregroundServiceIntent = new Intent(this, MyService.class);
//        foregroundServiceIntent.putExtra("action", ConnectivityStatusReceiver.INTENT_ACTION);
//        startForegroundService(foregroundServiceIntent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mcsr != null) {
            unregisterReceiver(mcsr);
        }
        Log.i(TAG, "service destroyed");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
