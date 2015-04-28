package com.example.taku.teachertimer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

public class MainActivity extends Activity{

    private TextView mTextView;
    IntentFilter mIntentFilter;
    String receive_message = null;
    String message = "";
    String wear_message;
    Vibrator mVibrator;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String SETTING_TIME_PATH = "/setting/time";
    public static final String NOTICE_SETTING_TIME_PATH = "/notice/setting/time";
    public static final String CLASS_SETTING_TIME_PATH = "/class/setting/time";
    int vibrate_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        mIntentFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(messageReceiver, mIntentFilter);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive_message = intent.getStringExtra("message");
            vibrate_time = intent.getIntExtra("vibrate_time", 100);
            Log.d("MessageReceive", "receivemessage" + receive_message);
            if (receive_message != null) {
                message = receive_message;
                Log.d("MessageReceive", "receivemessage" + receive_message);
            } else {
                receive_message = "No Message";
            }

            setScreen();
        }
    }

    private void setScreen() {
        mTextView.setText(message);
        vibrate();

    }


    private void vibrate() {
        mVibrator.vibrate(vibrate_time);
    }


}
