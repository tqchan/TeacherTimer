package com.example.taku.teachertimer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by taku on 2015/05/07.
 */
public class Progress extends View {
    private static final String TAG = Progress.class.getSimpleName();

    public Progress(Context context) {
        super(context);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        RectF rect = new RectF(20.0f, 20.0f, 60.0f, 20.0f);
        canvas.drawArc(rect, 0, 45, false, paint);
    }


}
