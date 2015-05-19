package com.example.taku.teachertimer;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by taku on 2015/04/22.
 */
public class DataLayerLisenerService extends WearableListenerService {
    private static final String TAG = DataLayerLisenerService.class.getSimpleName();
    public static final String SETTING_TIME_PATH = "/setting/time";
    public static final String NOTICE_SETTING_TIME_PATH = "/notice/setting/time";
    public static final String CLASS_SETTING_TIME_PATH = "/class/setting/time";
    public static final String GOOD_BUTTON_PUSH = "/good/button/push";
    public static final String FINISH_BUTTON_PUSH = "/finish/button/push";
    String wear_message;
    String good_text;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals(SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, "setting" + wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            intent.putExtra("vibrate_time", 500);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (messageEvent.getPath().equals(NOTICE_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, "notice" + wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            intent.putExtra("vibrate_time", 100);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (messageEvent.getPath().equals(CLASS_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, "class" + wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            intent.putExtra("timer_start", 50 * 60);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (messageEvent.getPath().equals(GOOD_BUTTON_PUSH)) {

            good_text = new String(messageEvent.getData());
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("good", good_text);
            intent.putExtra("vibrate_time", 200);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (messageEvent.getPath().equals(FINISH_BUTTON_PUSH)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("finish", 0);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else {
            Log.d(TAG, "error");
            Log.d(TAG, messageEvent.getPath());
        }

    }
}
