package com.xudongjian.lightreader.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xudongjian.lightreader.R;

/**
 * Created by xudongjian on 17/6/19.
 * 调整悬浮窗大小控件
 */

public class AdjustSizeView extends View {

    private Context mContext;

    private Paint mPaint;

    public AdjustSizeView(Context context) {
        this(context, null);
    }

    public AdjustSizeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustSizeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();

        mPaint.setColor(getResources().getColor(R.color.TianYiLan));

        mPaint.setStrokeWidth(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        canvas.drawLine(0, height, width, height, mPaint);
        canvas.drawLine(width, 0, width, height, mPaint);

    }
}
