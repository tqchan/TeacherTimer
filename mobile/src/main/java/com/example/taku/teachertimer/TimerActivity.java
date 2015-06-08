package com.example.taku.teachertimer;

import android.app.Notification;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

import static android.view.View.*;
import static com.google.android.gms.wearable.MessageApi.*;


public class TimerActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = TimerActivity.class.getSimpleName();
    ArrayList<Integer> zikan_array;
    ArrayList<String> katei_array;
    CountDownTimer countDownTimer;
//    int jugyou = 50 * 60;
    int jugyou = 10 * 60;
    TextView time_left;
    Button cdt_st;
    Button cdt_fi;
    Button good;
    int notification_time;
    int notification_time_number = 0;
    String notification_title;
    Context mcontext;
    Intent viewIntent;
    PendingIntent pendingIntent;
    NotificationManagerCompat notificationManagerCompat;
    GoogleApiClient mGoogleApiClient;
    public static final String SETTING_TIME_PATH = "/setting/time";
    public static final String NOTICE_SETTING_TIME_PATH = "/notice/setting/time";
    public static final String CLASS_SETTING_TIME_PATH = "/class/setting/time";
    public static final String GOOD_BUTTON_PUSH = "/good/button/push";
    public static final String FINISH_BUTTON_PUSH = "/finish/button/push";
    String path;
    String handheldmessage;
    String notice_text;
    String kaisi;
    String settingtime_text;
    String good_text;
    DataMap dataMap;
    String good_time;
    int send_time;


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

        time_left = (TextView) findViewById(R.id.timer_text);
        cdt_st = (Button) findViewById(R.id.cdt_start);
        cdt_fi = (Button) findViewById(R.id.cdt_finish);
        good = (Button) findViewById(R.id.iine_button);

        cdt_st.setOnClickListener(cdt_stOnclickListener);
        cdt_fi.setOnClickListener(cdt_fiOnclickListener);
        good.setOnClickListener(goodOnClickListener);

        katei_array = new ArrayList<>();
        zikan_array = new ArrayList<>();

        Intent intent = getIntent();
        katei_array = intent.getStringArrayListExtra("katei");
        zikan_array = intent.getIntegerArrayListExtra("time");

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


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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
            dataMap = new DataMap();
            countDownTimer.onFinish();
            dataMap.putString("title", "");
            new SendDataThread(FINISH_BUTTON_PUSH, dataMap).start();
            SendDataThread.interrupted();

        }
    };

    OnClickListener goodOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dataMap = new DataMap();
            good_text = "いいね！" + good_time;
            dataMap.putString("title", good_text);
            new SendDataThread(GOOD_BUTTON_PUSH, dataMap).start();
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

            } else if (zikan_array.size() == notification_time_number) {
                notification_time = jugyou;
            }
            keikazikan(millisUntilFinished);

            if ((jugyou - (millisUntilFinished / 1000)) == notification_time) {
                if (notification_time_number < katei_array.size()) {
                    notification_time_number = notification_time_number + 1;
                    //設定した時間のmessage
                    settingtime();
                } else if (notification_time_number == katei_array.size()) {
                    settingtime();
                }

            } else if ((jugyou - (millisUntilFinished / 1000)) == (notification_time - 60)) {
                //設定した1分前
                notice_settingtime();
            }

            //残り時間表示
            if ((millisUntilFinished / 1000 / 60) >= 10) {
                if ((millisUntilFinished / 1000 % 60) >= 10) {
                    time_left.setText(Long.toString(millisUntilFinished / 1000 / 60) + ":" + Long.toString(millisUntilFinished / 1000 % 60));
                } else if ((millisUntilFinished / 1000 % 60) < 10) {
                    time_left.setText(Long.toString(millisUntilFinished / 1000 / 60) + ":0" + Long.toString(millisUntilFinished / 1000 % 60));
                }
            } else if ((millisUntilFinished / 1000 / 60) < 10) {
                if ((millisUntilFinished / 1000 % 60) >= 10) {
                    time_left.setText("0" + Long.toString(millisUntilFinished / 1000 / 60) + ":" + Long.toString(millisUntilFinished / 1000 % 60));
                } else if ((millisUntilFinished / 1000 % 60) < 10) {
                    time_left.setText("0" + Long.toString(millisUntilFinished / 1000 / 60) + ":0" + Long.toString(millisUntilFinished / 1000 % 60));
                }
            }
        }

        @Override
        public void onFinish() {
            countDownTimer.cancel();
            time_left.setText("00:00");
        }
    }

    private void keikazikan(long millisUntilFinished) {
        long tmp_keikazikan;
        tmp_keikazikan = (jugyou - (millisUntilFinished/1000));
        Log.d(TAG, ""+tmp_keikazikan);
        if (tmp_keikazikan / 60 >= 10){
            if ((tmp_keikazikan % 60) >= 10) {
                good_time = (tmp_keikazikan / 60) + ":" + (tmp_keikazikan % 60);
            } else if ((tmp_keikazikan % 60) < 10) {
                good_time = (tmp_keikazikan / 60) + ":0" + (tmp_keikazikan % 60);
            }
        } else if (tmp_keikazikan / 60 < 9) {
            if ((tmp_keikazikan % 60) >= 10) {
                good_time = "0" +  (tmp_keikazikan / 60) + ":" + (tmp_keikazikan % 60);
            } else if ((tmp_keikazikan % 60) < 10) {
                good_time = "0" + (tmp_keikazikan / 60) + ":0" + (tmp_keikazikan % 60);
            }
        }
    }

    private void kaisi() {
        dataMap = new DataMap();
        notification_title = katei_array.get(notification_time_number);
        kaisi = "今は" + notification_title + "の時間です";
        dataMap.putString("title", kaisi);
        dataMap.putInt("time", zikan_array.get(notification_time_number));
        new SendDataThread(CLASS_SETTING_TIME_PATH, dataMap).start();
    }

    private void notice_settingtime() {
        dataMap = new DataMap();
        notification_title = katei_array.get(notification_time_number);
        notice_text = "まもなく" + notification_title + "が終了です";
        dataMap.putString("title", notice_text);
        new SendDataThread(NOTICE_SETTING_TIME_PATH, dataMap).start();
    }

    private void settingtime() {
        dataMap = new DataMap();
        Log.d(TAG, "" + notification_time_number);
        if (katei_array.size() > notification_time_number) {
            send_time = zikan_array.get(notification_time_number) - zikan_array.get(notification_time_number-1);
            notification_title = katei_array.get(notification_time_number);
            settingtime_text = "今は" + notification_title + "の時間です";
            dataMap.putString("title", settingtime_text);
            dataMap.putInt("time", send_time);
        } else if (katei_array.size() == notification_time_number) {
            settingtime_text = "設定した過程は終了です。\nまもなく授業終了です。";
            dataMap.putString("title", settingtime_text);
            dataMap.putInt("time", (jugyou - zikan_array.get(notification_time_number-1)));
//            dataMap.putInt("time", zikan_array.get(notification_time_number-1));
        }

        new SendDataThread(SETTING_TIME_PATH, dataMap).start();
    }


    class SendDataThread extends Thread {
        DataMap tmp_datamap;
        public SendDataThread(String pth, DataMap message) {
            path = pth;
            tmp_datamap = message;
        }

        public void run() {
            Log.d(TAG, "senddata thread start");
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
                putDataMapRequest.getDataMap().putAll(dataMap);
                PutDataRequest request = putDataMapRequest.asPutDataRequest();
                DataApi.DataItemResult result = Wearable.DataApi.putDataItem(mGoogleApiClient,request).await();

                if (result.getStatus().isSuccess()) {
                    Log.d(TAG, "DataMap: " + dataMap + " sent to: " + node.getDisplayName());
                } else {
                    Log.d(TAG, "ERROR");
                }
            }
        }
    }

}
