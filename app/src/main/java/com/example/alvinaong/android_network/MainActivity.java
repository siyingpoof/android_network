package com.example.alvinaong.android_network;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ConnectivityStatusReceiver mcsr;
    // myservice?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mcsr = new ConnectivityStatusReceiver();

        Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(startServiceIntent);

//        final Button button = (Button) findViewById(R.id.buttonConnection);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "Starting service", Toast.LENGTH_LONG).show();
//                Intent startServiceIntent = new Intent(getApplicationContext(), MyService.class);
//                startService(startServiceIntent);
//            }
//        });

    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
//        IntentFilter intentFilter = new IntentFilter(ConnectivityStatusReceiver.INTENT_ACTION);
//        registerReceiver(mcsr, intentFilter);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
//        if (mcsr != null) {
//            unregisterReceiver(mcsr);
//        }
    }
}
