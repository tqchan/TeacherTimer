package com.example.taku.teachertimer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;

public class MainActivity extends Activity{

    private TextView mTextView;
    LinearLayout bg_layout;
    IntentFilter mIntentFilter;
    String receive_message = null;
    String message = "";
    Vibrator mVibrator;
    private static final String TAG = MainActivity.class.getSimpleName();
    int vibrate_time = 0;
    int bg_color_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                bg_layout = (LinearLayout) stub.findViewById(R.id.bg);
            }
        });
        mIntentFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(messageReceiver, mIntentFilter);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive_message = intent.getStringExtra("message");
            vibrate_time = intent.getIntExtra("vibrate_time", 100);
            Log.d("MessageReceive", "receivemessage" + receive_message);
            if (receive_message != null) {
                message = receive_message;
                bg_color_number ++;
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
