package com.example.taku.teachertimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by taku on 2015/04/14.
 */
public class MyDialog extends DialogFragment {
    private static final String TAG = MyDialog.class.getSimpleName();
    NumberPicker numberPicker1, numberPicker2, numberPicker3, numberPicker4;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] minutes = new String[] {"0","3"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = inflater.inflate(R.layout.dialog, null);
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
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        builder.setView(content)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //指導過程名取得
                EditText kateimei = (EditText)content.findViewById(R.id.edit_title);
                String katei = kateimei.getText().toString();
                //時間の取得
                int time = (numberPicker1.getValue() * 600) + (numberPicker2.getValue() * 60) + (numberPicker3.getValue() * 30);

                Log.d(TAG, katei + time);
            }
        })
        .setNegativeButton("cansel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
