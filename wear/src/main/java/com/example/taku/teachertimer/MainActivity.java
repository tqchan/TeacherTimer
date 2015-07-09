package com.example.taku.teachertimer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.GestureDetector.*;

public class MainActivity extends Activity{

    TextView mTextView;
    TextView good_TextView;
    LinearLayout bg_layout;
    IntentFilter mIntentFilter;
    String receive_message = null;
    String receive_message_good = null;
    String message = null;
    String message_good = null;
    Vibrator mVibrator;
    private static final String TAG = MainActivity.class.getSimpleName();
    int vibrate_time = 0;
    int bg_color_number = 0;
    int class_time = 0;
    int now_class_time = 50 * 60;
//    int now_class_time = 120 * 60;
    int now_katei_time = 1;
    int tmp_now_katei_time;
    int finish_time = 1;
    ProgressBar mProgressBar;
    ProgressBar mProgressBar_katei;
    int testtesttest;
    long finish_vibrate[] = {500, 100, 500, 100};
    BackGrounds backGrounds;
    BackGrounds change_bg;
//    GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                good_TextView = (TextView) stub.findViewById(R.id.goodtext);
                bg_layout = (LinearLayout) stub.findViewById(R.id.bg);
                mProgressBar = (ProgressBar) stub.findViewById(R.id.progressBar);
                mProgressBar_katei = (ProgressBar) stub.findViewById(R.id.progressBar_katei);
                backGrounds = (BackGrounds) stub.findViewById(R.id.canvas_view);
            }
        });
        mIntentFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(messageReceiver, mIntentFilter);
        mVibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        change_bg = new BackGrounds(getApplication());

        //ジェスチャー
//        mGestureDetector = new GestureDetector(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        change_bg.finish();
        this.finish();
        now_class_time = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void onTouch(int i) {
        switch (i) {
            case 1:
                Log.d(TAG, "single tap");
                break;
        }
    }


    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive_message = intent.getStringExtra("message");
            receive_message_good = intent.getStringExtra("good");
            class_time = intent.getIntExtra("timer_start", 0);
            vibrate_time = intent.getIntExtra("vibrate_time", 100);
            finish_time = intent.getIntExtra("finish", 0);
//            now_katei_time = intent.getIntExtra("time", 0);
            tmp_now_katei_time = intent.getIntExtra("time", -1);
//            now_katei_time = intent.getIntExtra("time", 0);
            if (tmp_now_katei_time != -1) {
                now_katei_time = intent.getIntExtra("time", 0);
            }
            Log.d("MessageReceive", "receivemessage" + receive_message);
            Log.d("MessageReceive", "katei_receivemessage" + now_katei_time);
            if (class_time != 0) {
                Log.d(TAG, "testtesttest");
                mProgressBar.setMax(class_time);
                mProgressBar_katei.setMax(now_katei_time);
                progress_thread();
                class_time = 0;
            } if (receive_message != null) {
                message = receive_message;
                if (tmp_now_katei_time != -1) {
                    mProgressBar_katei.setMax(now_katei_time);
                    backGrounds.changeColor();
                    backGrounds.invalidate();
                }
                bg_color_number ++;
                receive_message =null;
                Log.d("MessageReceive", "receivemessage" + receive_message);
            } else if (receive_message_good != null) {
                message_good = receive_message_good;
                backGrounds.changeColor();
//                backGrounds.invalidate();
                receive_message_good = null;
            } else if (finish_time == 0) {
                Log.d("TAG", "finish_time");
                now_class_time = 0;
                finish_time = 1;
            }
            else {
                receive_message = "No Message";
            }
            setScreen();
        }
    }



    private void setScreen() {
        if (message != null) {
            mTextView.setText(message);
            message = null;
        } else if (message_good != null) {
            good_TextView.setText(message_good);
            message_good = null;
            Log.d(TAG,"good");
        }
        vibrate();
    }

    private void vibrate() {
        mVibrator.vibrate(vibrate_time);
    }

    private void progress_thread(){
        final Timer timer = new Timer(true);
        testtesttest = testtesttest+1;
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        now_class_time = now_class_time -1;
                        now_katei_time = now_katei_time -1;
                        mProgressBar.setProgress(now_class_time);
                        mProgressBar_katei.setProgress(now_katei_time);
                        Log.d(TAG, "now_time" + testtesttest + ":" + now_class_time);
                        Log.d(TAG, "now_katei_time" + testtesttest + ":" + now_katei_time);

                        //現在の過程の残り時間表示

                        if (now_class_time <= 0) {
                            timer.cancel();
                            mVibrator.vibrate(finish_vibrate, -1);
                        }
                    }
                },0, 1000
        );

    }

}
