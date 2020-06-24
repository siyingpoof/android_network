package com.example.alvinaong.android_network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ConnectivityStatusReceiver mcsr;
    // myservice?

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(startServiceIntent);

        final TextView tv = (TextView) findViewById(R.id.txtConnection);

        ConnectivityStatusReceiver.ConnectionType type = ConnectivityStatusReceiver.getTypeOfConnection(this);
        if (type == ConnectivityStatusReceiver.ConnectionType.None) {
            tv.setText("Disconnected");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Your network update");
            alertDialog.setMessage("You are disconnected.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } else if (type == ConnectivityStatusReceiver.ConnectionType.Mobile) {
            tv.setText("Connected to mobile data");
        } else {
            // ConnectionType.Wifi
            tv.setText("Connected to wifi");
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
