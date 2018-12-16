package com.xudongjian.lightreader.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xudongjian.lightreader.R;

/**
 * Created by xudongjian on 17/6/8.
 * 控制程序的悬浮球
 */

public class FloatBall extends View {


    private int mDownX = -1;
    private int mDownY = -1;
    private int mMoveX;
    private int mMoveY;
    private float mSmallBallSize = 30;
    private boolean mIsMoveing = false;//是否正在上下左右滑动

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

        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        canvas.drawCircle(cx, cy, 50, mPaint);

        int cx_smallBall = getMeasuredWidth() / 2;
        int cy_smallBall = getMeasuredHeight() / 2;

        if (mDownX != -1 && mDownY != -1) {
            int changeX = mMoveX - mDownX;
            int changeY = mMoveY - mDownY;

            int changeX_abs = Math.abs(changeX);
            int changeY_abs = Math.abs(changeY);

            if (changeX_abs > 50 || changeY_abs > 50) {
                setMoveing(true);
                if (changeX_abs > changeY_abs) {//左右滑

                    cx_smallBall = cx_smallBall + changeX;
                    if (cx_smallBall < 40) {
                        cx_smallBall = 40;
                    } else if (cx_smallBall > 140) {
                        cx_smallBall = 140;
                    }
//                if (changeX > 0) {//即往右滑
//
//                } else if (changeX < 0) {//即往左滑
//
//                }
                } else if (changeY_abs > changeX_abs) {//上下滑
                    cy_smallBall = cy_smallBall + changeY;
                    if (cy_smallBall < 40) {
                        cy_smallBall = 40;
                    } else if (cy_smallBall > 140) {
                        cy_smallBall = 140;
                    }
//                if(changeY>0){//即向下滑
//
//                }else if(changeY<0){//即向上滑
//
//                }
                }
            }


        }

        canvas.drawCircle(cx_smallBall, cy_smallBall, mSmallBallSize, mPaint1);

    }


    public void changeSmallBallToBig() {
        mSmallBallSize = 40;
        requestLayout();
        invalidate();
    }

    public void changeSmallBallToSmall() {
        mSmallBallSize = 30;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }

    public void setMoveing(boolean moveing) {
        mIsMoveing = moveing;
    }

    public boolean isMoveing() {
        return mIsMoveing;
    }

    public void setDownXY(float x, float y) {
        mDownX = (int) x;
        mDownY = (int) y;
    }

    public void setMoveXY(float x, float y) {
        mMoveX = (int) x;
        mMoveY = (int) y;
        requestLayout();
        invalidate();
    }

    public void finishMove() {
        mDownX = -1;
        mDownY = -1;
        requestLayout();
        invalidate();
    }
}
