package com.example.taku.teachertimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static android.view.View.*;
import static com.google.android.gms.wearable.MessageApi.*;


public class TimerActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = TimerActivity.class.getSimpleName();
    ArrayList<Integer> zikan_array;
    ArrayList<String> katei_array;
    CountDownTimer countDownTimer;
    int jugyou = 50 * 60;
    TextView time_left;
    Button cdt_st;
    Button cdt_fi;
    Button good;
    int notification_time;
    int notification_time_number = 0;
    int notification_id = 1;
    String notification_title;
    String notification_text = "確認してください";
    NotificationCompat.Builder notificationBuilder;
    Context mcontext;
    Intent viewIntent;
    PendingIntent pendingIntent;
    NotificationManagerCompat notificationManagerCompat;
    String mNode;
    GoogleApiClient mGoogleApiClient;
    public static final String SETTING_TIME_PATH = "/setting/time";
    public static final String NOTICE_SETTING_TIME_PATH = "/notice/setting/time";
    public static final String CLASS_SETTING_TIME_PATH = "/class/setting/time";
    NodeApi.GetConnectedNodesResult nodes;
    SendMessageResult result;
    String path;
    String handheldmessage;
    String notice_text;
    String kaisi;
    String settingtime_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        /**
         *
         * time_left         時間の残りを表示するテキスト
         * cdt_st            カウントダウン開始のボタン
         * cdt_fi            カウントダウン終了のボタン
         * katei_array       設定した過程名が入る配列
         * zikan_array       設定した時間が入る配列
         * countDownTimer    カウントダウンタイマーを生成
         *
         */

        time_left = (TextView)findViewById(R.id.timer_text);
        cdt_st = (Button)findViewById(R.id.cdt_start);
        cdt_fi = (Button)findViewById(R.id.cdt_finish);
        good = (Button)findViewById(R.id.iine_button);

        cdt_st.setOnClickListener(cdt_stOnclickListener);
        cdt_fi.setOnClickListener(cdt_fiOnclickListener);
        good.setOnClickListener(goodOnClickListener);

        katei_array = new ArrayList<String>();
        zikan_array = new ArrayList<Integer>();

        Intent intent = getIntent();
        katei_array = intent.getStringArrayListExtra("katei");
        zikan_array = intent.getIntegerArrayListExtra("time");

//        Log.d(TAG, katei_array.get(0));
//        Log.d(TAG, "" + zikan_array.get(0));

        //count down timer 初期化
        countDownTimer = new MyCountDownTimer(jugyou * 1000, 1000);

        mcontext = this;

        viewIntent = new Intent(this, TimerActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);
        notificationManagerCompat = NotificationManagerCompat.from(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        SendDataThread.interrupted();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    OnClickListener cdt_stOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            countDownTimer.start();
            kaisi();
        }
    };

    OnClickListener cdt_fiOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            countDownTimer.onFinish();
            SendDataThread.interrupted();
        }
    };

    OnClickListener goodOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            notificationBuilder = new NotificationCompat.Builder(mcontext)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("GOOD")
                    .setContentText("いいね！が押されました")
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent);
            notificationManagerCompat.notify(notification_id, notificationBuilder.build());
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class MyCountDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //インターバル(1秒)毎に呼ばれる
            if (zikan_array.size() > notification_time_number) {
                notification_time = zikan_array.get(notification_time_number);
                Log.d(TAG, "notification_time_number:" + notification_time_number);
                Log.d(TAG, "millisUntilFinished:" + millisUntilFinished/1000);
            } else if (zikan_array.size() == notification_time_number) {

                notification_time = jugyou;
                Log.d(TAG, "jugyou:endtime");
            }


            if ((jugyou - (millisUntilFinished/1000)) == notification_time) {
                //設定した時間のmessage
                settingtime();
            } else if ((jugyou - (millisUntilFinished/1000)) == (notification_time - 60)) {
                //設定した1分前
                notice_settingtime();
            }

            //残り時間表示
            if ((millisUntilFinished/1000/60) >=10 ) {
                if ((millisUntilFinished/1000%60) >= 10) {
                    time_left.setText(Long.toString(millisUntilFinished/1000/60) + ":" + Long.toString(millisUntilFinished/1000%60));
                } else if ((millisUntilFinished/1000%60) <10) {
                    time_left.setText(Long.toString(millisUntilFinished/1000/60) + ":0" + Long.toString(millisUntilFinished/1000%60));
                }
            } else if ((millisUntilFinished/1000/60) < 9) {
                if ((millisUntilFinished/1000%60) >= 10) {
                    time_left.setText("0" + Long.toString(millisUntilFinished/1000/60) + ":" + Long.toString(millisUntilFinished/1000%60));
                } else if ((millisUntilFinished/1000%60) <10) {
                    time_left.setText("0" + Long.toString(millisUntilFinished/1000/60) + ":0" + Long.toString(millisUntilFinished/1000%60));
                }
            }
        }

        @Override
        public void onFinish() {
            countDownTimer.cancel();
            time_left.setText("00:00");
        }
    }

    private void kaisi() {

        notification_title = katei_array.get(notification_time_number);
        kaisi = "今は" + notification_title + "の時間です";
        new SendDataThread(CLASS_SETTING_TIME_PATH, kaisi).start();

    }

    private void notice_settingtime() {

        notification_title = katei_array.get(notification_time_number);
        notice_text = "まもなく" + notification_title + "が終了です";
        new SendDataThread(NOTICE_SETTING_TIME_PATH, notice_text).start();

    }

    private void settingtime() {

        Log.d(TAG, "" + notification_time_number);
        notification_title = katei_array.get(notification_time_number);
        settingtime_text = "今は" + notification_title + "の時間です";
        new SendDataThread(SETTING_TIME_PATH, settingtime_text).start();
        notification_time_number = notification_time_number + 1;

    }


    class SendDataThread extends Thread {
        public SendDataThread(String pth, String message) {
            path = pth;
            handheldmessage = message;
        }

        public void run() {
            Log.d(TAG, "senddata thread start");
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, handheldmessage.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.d(TAG, "To:" + node.getDisplayName());
                    Log.d(TAG, path);
                    Log.d(TAG, handheldmessage);
                } else {
                    Log.d(TAG, "error");
                }
            }
        }
    }

}
