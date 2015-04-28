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

public class MainActivity extends Activity {

    private TextView mTextView;
    IntentFilter mIntentFilter;
    String receive_message = null;
    String message = "";
    String wear_message;
    Vibrator mVibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntentFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(messageReceiver, mIntentFilter);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        setScreen();
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive_message = intent.getStringExtra("message");
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
        wear_message = "今の時間は" + message + "です";
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(wear_message);
                vibrate();
            }
        });
    }


    private void vibrate() {
        mVibrator.vibrate(2000);
    }
}
