package com.xudongjian.lightreader.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.ui.view.FloatBall;
import com.xudongjian.lightreader.ui.view.ReaderTextView;
import com.xudongjian.lightreader.utils.ScreenUtils;

/**
 * Created by xudongjian on 17/6/6.
 * 悬浮窗管理类
 */

public class FloatWindowManager {

    //上下文对象
    private Context mContext;

    //窗口管理者对象
    private WindowManager mWindowManager;

    //阅读视图
    private ReaderTextView mRtv_float;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     */
    public FloatWindowManager(Context context) {
        mContext = context;

        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

    }


    /**
     * 展示文本悬浮窗
     *
     * @param book 需要展示的文本对象
     */
    public void showTextFloatWindow(Book book) {


        //包裹文本的悬浮窗口
        final FrameLayout mVg_float = new FrameLayout(mContext);

        //悬浮文本
        mRtv_float = new ReaderTextView(mContext, book);

        //设置背景颜色:半透明白色
        mVg_float.setBackgroundColor(mContext.getResources().getColor(R.color.textFloatWindowBackground));

        //将阅读视图添加到悬浮窗口
        mVg_float.addView(mRtv_float);

        //布局参数
        final LayoutParams mParams_text = new LayoutParams();

        //类型为悬浮窗
        mParams_text.type = LayoutParams.TYPE_PHONE;

        /***///设置悬浮窗不可聚焦
        mParams_text.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams_text.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mParams_text.x = 0;
        mParams_text.y = 0;

        //设置图片格式,效果为背景透明
        mParams_text.format = PixelFormat.RGBA_8888;

        //设置宽高
        mParams_text.width = 650;
        mParams_text.height = 650;

        //WindowsManager添加悬浮窗口
        mWindowManager.addView(mVg_float, mParams_text);

        //设置监听浮动窗口的触摸移动
        mVg_float.setOnTouchListener(new View.OnTouchListener() {
            //按下时点击悬浮窗的坐标
            int x = 0, y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //刷新视图时悬浮窗x和y的位置
                        mParams_text.x = (int) (event.getRawX()) - x;
                        mParams_text.y = (int) (event.getRawY() - ScreenUtils.getStatusBarHeight()) - y;
                        mWindowManager.updateViewLayout(mVg_float, mParams_text);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }


                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

    }

    public void showNextButtonFloatWindow() {
//        Button button = new Button(mContext);

        FloatBall button = new FloatBall(mContext);

//        button.setText("下");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtv_float.nextPage();
            }
        });

        LayoutParams params = new LayoutParams();

        //设置类型为悬浮窗
        params.type = LayoutParams.TYPE_PHONE;

        //设置图片格式为可透明
        params.format = PixelFormat.RGBA_8888;


        params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

        params.gravity = Gravity.BOTTOM | Gravity.END;

//        params.width = LayoutParams.WRAP_CONTENT;

        params.width = 100;
        params.height = 100;

//        params.height = LayoutParams.WRAP_CONTENT;

        mWindowManager.addView(button, params);


    }


}
