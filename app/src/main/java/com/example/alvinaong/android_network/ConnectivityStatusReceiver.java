package com.example.alvinaong.android_network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ConnectivityStatusReceiver extends BroadcastReceiver {

    static final String TAG = "ConnectivityStatus";
    static final String INTENT_ACTION = ConnectivityManager.CONNECTIVITY_ACTION;
    static final String CHANNEL_ID = "network";

    enum ConnectionType {
        None,
        Wifi,
        Mobile
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationMessage = "";
        Log.i(TAG, "onReceive");

        if (getTypeOfConnection(context) == ConnectionType.Mobile) {
            String networkClass = getNetworkClass(context);
            notificationMessage = "Connected to mobile (" + networkClass + ")";
        } else if (getTypeOfConnection(context) == ConnectionType.Wifi) {
            notificationMessage = "Connected to wifi";
        } else {
            // ConnectionType.None
            notificationMessage = "Disconnected";
        }
        sendNotification(context, notificationMessage);
    }

    /**
     * This method creates the notification and pushes it to the device's notification bar.
     * @param context               the context of the application
     * @param notificationMessage   the message to be displayed to the user in the notification
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(Context context, String notificationMessage){

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "network notification", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("network change notification");
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Your network status");
        builder.setContentText(notificationMessage);
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());
        Toast.makeText(context, notificationMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * This method tells user if the device has live connection.
     * @return  true if the user is really connected to the internet or false if there is no
     *          live connection.
     */
    private static boolean isConnectedToThisServer() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            Log.i(TAG, "exit value " + exitValue);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    ipProcess.getInputStream()));
            StringBuffer output = new StringBuffer();
            String temp;
            int count = 0;
            String str = "";
            while ((temp = reader.readLine()) != null) {
                output.append(temp);
                count++;
            }
            reader.close();
            if (count > 0) {
                str = output.toString();
            }
            Log.i(TAG, str);
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method tells user what type of connection the device has.
     * @param context   the context of the application
     * @return          ConnectionType.Wifi if connection is live and is connected to the Wifi or
     *                  ConnectionType.Mobile if connection is live and is connected to the mobile data or
     *                  ConnectionType.None if the device does not have live connection.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ConnectionType getTypeOfConnection(Context context){
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() && isConnectedToThisServer()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return ConnectionType.Wifi;
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return ConnectionType.Mobile;
            }
        }
        return ConnectionType.None;
    }

    public String  getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = Objects.requireNonNull(mTelephonyManager).getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: {
                return "2G";
            }
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP: {
                return "3G";
            }
            case TelephonyManager.NETWORK_TYPE_LTE: {
                return "4G";
            }
            case TelephonyManager.NETWORK_TYPE_NR:
                return "5G";
            default:
                return "Unknown";
        }
    }
}
