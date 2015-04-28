package com.example.taku.teachertimer;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    LinearLayout linearLayout;
    LinearLayout add_linearLayout;
    Button add;
    Button remove;
    Button add_finish;
    LayoutInflater inflater;
    View content;
    AlertDialog.Builder builder;
    String[] minutes = new String[] {"0","3"};
    NumberPicker numberPicker1,numberPicker2,numberPicker3,numberPicker4;
    EditText kateimei;
    String katei;
    String zikan;
    String zikan_old;
    int time;
    TextView add_katei;
    TextView add_time;
    int add_kazu = 0;
    ViewGroup viewGroup;
    TableRow tableRow;
    TableLayout tableLayout;
    ArrayList<Integer> zikan_array;
    ArrayList<String> katei_array;
    Context mcontext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *
         * linearLayout
         * inflater         レイアウトのinflater
         * add_linearLayout 追加するLinearLayout
         * add_katei
         * add_time
         * tableLayout
         * add
         * add_finish
         * remove
         * katei_array
         * zikan_array
         * mcontext
         * viewGroup
         * zikan_old
         *
         */

        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        add_linearLayout = (LinearLayout)inflater.inflate(R.layout.add_layout, null);
        add_katei = (TextView)add_linearLayout.findViewById(R.id.text_katei);
        add_time = (TextView)add_linearLayout.findViewById(R.id.text_time);
        tableLayout = (TableLayout)findViewById(R.id.table);
        add = (Button)findViewById(R.id.add_button);
        add_finish = (Button)findViewById(R.id.end);
        remove = (Button)findViewById(R.id.remove_button);
        katei_array = new ArrayList<String>();
        zikan_array = new ArrayList<Integer>();
        mcontext = this;

        //table layout のグループ取得
        viewGroup = (ViewGroup)findViewById(R.id.table);

        add.setOnClickListener(addOnClickListener);
        add_finish.setOnClickListener(add_finishOnClickListener);
        remove.setOnClickListener(removeOnClickListener);

        zikan_old = "00:00";
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        /**
         * ダイアログの準備
         * numberPickerは左から1,2,3,4
         * 秒数を30区切りにするためにnumberPicker3は1と0のみにして，配列を対応
         */

        //カスタムビューを設定
        content = inflater.inflate(R.layout.dialog, (ViewGroup)findViewById(R.id.dialog));
        //アラートダイアログを生成
        builder = new AlertDialog.Builder(this);
        numberPicker1 = (NumberPicker) content.findViewById(R.id.numberPicker);
        numberPicker2 = (NumberPicker) content.findViewById(R.id.numberPicker2);
        numberPicker3 = (NumberPicker) content.findViewById(R.id.numberPicker3);
        numberPicker4 = (NumberPicker) content.findViewById(R.id.numberPicker4);
        numberPicker1.setMaxValue(5);
        numberPicker1.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker2.setMinValue(0);
        numberPicker3.setMaxValue(1);
        numberPicker3.setMinValue(0);
        numberPicker3.setDisplayedValues(minutes);

        //キーボードブロック
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker3.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker4.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder.setView(content)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //指導過程名取得
                        kateimei = (EditText)content.findViewById(R.id.edit_title);
                        katei = kateimei.getText().toString();
                        katei_array.add(katei);
                        //時間の取得
                        time = (numberPicker1.getValue() * 600) + (numberPicker2.getValue() * 60) + (numberPicker3.getValue() * 30);
                        zikan_array.add(time);
                        Log.d(TAG, katei + time);
                        //取得した値をセット
                        add_katei.setText(katei);
                        if (numberPicker3.getValue() == 1) {
                            zikan = "" + numberPicker1.getValue() + numberPicker2.getValue() + ":" + (numberPicker3.getValue() * 30);
                            add_time.setText(zikan);
                        } else if (numberPicker3.getValue() == 0) {
                            zikan = "" + numberPicker1.getValue() + numberPicker2.getValue() + ":" + numberPicker3.getValue() + numberPicker4.getValue();
                            add_time.setText(zikan);
                        }

                        //行を追加
                        getLayoutInflater().inflate(R.layout.add_table_row, viewGroup);
                        //文字設定
                        tableRow = (TableRow)viewGroup.getChildAt(add_kazu);
                        int add_id = add_kazu + 1;
//                        ((TextView)(tableRow.getChildAt(0))).setText("" + add_id);
                        ((TextView)(tableRow.getChildAt(1))).setText(katei);
                        ((TextView)(tableRow.getChildAt(2))).setText(zikan_old + " - " + zikan);
                        add_kazu ++;
                        zikan_old = zikan;
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    //clicklistener
    OnClickListener addOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v){

            showDialog(0);
        }
    };

    OnClickListener add_finishOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            intent.putStringArrayListExtra("katei", katei_array);
            intent.putIntegerArrayListExtra("time", zikan_array);
            if (katei_array.size() == 0 && zikan_array.size() == 0) {
                Toast.makeText(mcontext,"時間を設定してください",Toast.LENGTH_LONG).show();
            } else if (katei_array.size() > 0 && zikan_array.size() > 0) {
                startActivity(intent);
            }
        }
    };

    OnClickListener removeOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            tableRow.removeAllViews();
        }
    };

}
