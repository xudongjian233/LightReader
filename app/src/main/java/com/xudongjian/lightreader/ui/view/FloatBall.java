package com.xudongjian.lightreader.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xudongjian.lightreader.R;

/**
 * Created by xudongjian on 17/6/8.
 */

public class FloatBall extends View {

    private final String TAG = "FloatBall";

    public FloatBall(Context context) {
        this(context, null);
    }

    public FloatBall(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint mPaint;
    private Paint mPaint1;


    public FloatBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.floatBallBack));

        mPaint1 = new Paint();
        mPaint1.setColor(getResources().getColor(R.color.floatBall));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 50, mPaint);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, 30, mPaint1);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        Log.e(TAG, "onTouchEvent");
//
//        //获取触摸动作
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN://按下
//                Log.e(TAG, "down");
//                break;
//            case MotionEvent.ACTION_MOVE://移动
//                Log.e(TAG, "move");
//                break;
//            case MotionEvent.ACTION_UP://抬起
//                Log.e(TAG, "up");
//                break;
//        }
//
//
//        return super.onTouchEvent(event);
//    }
}
