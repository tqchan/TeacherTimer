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

public class MainActivity extends Activity implements MessageApi.MessageListener{

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
//    String wear_message;

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
//        MessageReceiver messageReceiver = new MessageReceiver();
//        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(messageReceiver, mIntentFilter);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
//            Log.d(TAG, messageEvent.getPath());
            Log.d(TAG, wear_message);

            setScreen();

        } else if (messageEvent.getPath().equals(NOTICE_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, wear_message);

            setScreen();

        } else if (messageEvent.getPath().equals(CLASS_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, wear_message);

            setScreen();

        } else {
            Log.d(TAG, "error");
            Log.d(TAG, messageEvent.getPath());
        }

    }
//    public class MessageReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            receive_message = intent.getStringExtra("message");
//            Log.d("MessageReceive", "receivemessage" + receive_message);
//            if (receive_message != null) {
//                message = receive_message;
//                Log.d("MessageReceive", "receivemessage" + receive_message);
//            } else {
//                receive_message = "No Message";
//            }
//
//            setScreen();
//        }
//    }

    private void setScreen() {
        mTextView.setText(message);
        vibrate();

    }


    private void vibrate() {
        mVibrator.vibrate(1000);
    }


}
