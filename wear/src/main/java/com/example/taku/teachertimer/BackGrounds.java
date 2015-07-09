package com.example.taku.teachertimer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by taku on 2015/06/17.
 */
public class BackGrounds extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private static final String TAG = BackGrounds.class.getSimpleName();
    int red, green, blue;
    int red_old, green_old, blue_old;
    private Random random;
    boolean ch_bg = false;
    boolean timer_running;
    GestureDetector mGestureDetector;
    MainActivity main;


    public BackGrounds(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(this);
        main = new MainActivity();
    }

    public BackGrounds(Context context) {
        super(context);
        ch_bg = false;
        Log.d(TAG, "backgrouns");
        red = 0;
        green = 0;
        blue = 0;
        red_old = 0;
        green_old = 0;
        blue_old = 0;
    }

    public void changeColor() {
            random = new Random();
            //red
            red = random.nextInt(256);
            //green
            green = random.nextInt(256);
            //blue
            blue = random.nextInt(256);
            red_old = red;
            green_old = green;
            blue_old = blue;
            Log.d(TAG, "changecolor_rand");
        Log.d(TAG, "" + red + ":" + green + ":" + blue + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "draw");
        canvas.drawRGB(red, green, blue);
            Log.d(TAG, "draw_running" + red + ":" + green + ":" + blue + "");
    }

    //タッチイベント
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
//        Log.d(TAG, "SingleTapConfirmed");
        main.onTouch(1);
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, "DoubleTap");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "Down");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "LongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void finish() {
        timer_running = false;
        this.finish();
    }
}
