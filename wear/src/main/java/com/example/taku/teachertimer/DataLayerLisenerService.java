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
    String wear_message;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals(SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
//            Log.d(TAG, messageEvent.getPath());
            Log.d(TAG, wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (messageEvent.getPath().equals(NOTICE_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else if (messageEvent.getPath().equals(CLASS_SETTING_TIME_PATH)) {

            wear_message = new String(messageEvent.getData());
            Log.d(TAG, wear_message);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("message", wear_message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            Log.d(TAG, "error");
            Log.d(TAG, messageEvent.getPath());
        }

    }
}
