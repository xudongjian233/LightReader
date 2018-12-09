package com.xudongjian.lightreader.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.ui.view.AdjustSizeView;
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

    //悬浮窗
    private FrameLayout mFloatWindow;

    //悬浮文本
    private ReaderTextView mRtv_float;

    //调整悬浮窗大小控件
    private AdjustSizeView mAdjustSizeView;

    //是否正处于长按悬浮球
    private boolean mIsLongClick = false;

    //悬浮窗口的X坐标(左上角)
    private float mFloatWindowX = 0;

    //悬浮窗口的Y坐标(右上角)
    private float mFloatWindowY = 0;

    //悬浮窗宽
    private int mFloatWindowWidth = 650;

    //悬浮窗高
    private int mFloatWindowHeight = 650;

    //悬浮窗口添加到窗口管理器的布局参数
    private LayoutParams mLp_floatWindow;
    private String EVENT="event";


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
    @SuppressLint("ClickableViewAccessibility")
    public void showTextFloatWindow(Book book) {


        //包裹文本的悬浮窗口
        mFloatWindow = new FrameLayout(mContext);

        //悬浮文本
        mRtv_float = new ReaderTextView(mContext, book);

        //设置背景颜色:半透明白色
        mFloatWindow.setBackgroundColor(mContext.getResources().getColor(R.color.textFloatWindowBackground));

        //将阅读视图添加到悬浮窗口
        mFloatWindow.addView(mRtv_float);

        //将调整悬浮窗大小控件添加到悬浮窗口
        addAdjustSizeView();


        //布局参数
        mLp_floatWindow = new LayoutParams();

        //类型为悬浮窗
        mLp_floatWindow.type = LayoutParams.TYPE_PHONE;

        /***///设置悬浮窗不可聚焦
        mLp_floatWindow.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

        //调整悬浮窗显示的停靠位置为左侧置顶
        mLp_floatWindow.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mLp_floatWindow.x = 0;
        mLp_floatWindow.y = 0;

        //设置图片格式,效果为背景透明
        mLp_floatWindow.format = PixelFormat.RGBA_8888;

        //设置宽高
        mLp_floatWindow.width = mFloatWindowWidth;
        mLp_floatWindow.height = mFloatWindowHeight;

        //WindowsManager添加悬浮窗口
        mWindowManager.addView(mFloatWindow, mLp_floatWindow);

        //设置监听浮动窗口的触摸移动
        mFloatWindow.setOnTouchListener(new View.OnTouchListener() {
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
                        mLp_floatWindow.x = (int) (event.getRawX()) - x;
                        mLp_floatWindow.y = (int) (event.getRawY() - ScreenUtils.getStatusBarHeight()) - y;
                        mWindowManager.updateViewLayout(mFloatWindow, mLp_floatWindow);
                        break;
                    case MotionEvent.ACTION_UP:

                        mFloatWindowX = mLp_floatWindow.x;

                        mFloatWindowY = mLp_floatWindow.y;

                        if (mFloatWindowX < 0) {
                            mFloatWindowX = 0;
                        }
                        if (mFloatWindowY < 0) {
                            mFloatWindowY = 0;
                        }

                        if (mFloatWindowX > ScreenUtils.getScreenWidth() - mFloatWindowWidth) {
                            mFloatWindowX = ScreenUtils.getScreenWidth() - mFloatWindowWidth;
                        }

                        if (mFloatWindowY > ScreenUtils.getScreenHeight() - ScreenUtils.getStatusBarHeight() - mFloatWindowHeight) {
                            mFloatWindowY = ScreenUtils.getScreenHeight() - ScreenUtils.getStatusBarHeight() - mFloatWindowHeight;
                        }


                        Log.e(this.toString(), "x:" + mFloatWindowX + ";y:" + mFloatWindowY);

                        break;
                }


                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

    }

    //调整大小控件
    @SuppressLint("ClickableViewAccessibility")
    private void addAdjustSizeView() {
        //调节悬浮窗大小控件
        AdjustSizeView adjustSizeView = new AdjustSizeView(mContext);

        FrameLayout.LayoutParams params_adjust = new FrameLayout.LayoutParams(100, 100);

        params_adjust.gravity = Gravity.END | Gravity.BOTTOM;

        mFloatWindow.addView(adjustSizeView, params_adjust);

        adjustSizeView.setOnTouchListener(new View.OnTouchListener() {

            int i = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRtv_float.setIsAdjustingSize(true);
                        return true;
                    case MotionEvent.ACTION_MOVE:

//                        if (i < 6) {
//                            i++;
//                            return true;
//                        }

                        mLp_floatWindow.width = (int) (event.getRawX() - mFloatWindowX);
                        mLp_floatWindow.height = (int) (event.getRawY() - mFloatWindowY);
                        mWindowManager.updateViewLayout(mFloatWindow, mLp_floatWindow);

                        return true;
                        default:
                            Log.e(EVENT,"default:"+event.getAction());
                            mRtv_float.setIsAdjustingSize(false);
                            break;
                }

                return true;
            }
        });

    }

    //悬浮球
    public void showNextButtonFloatWindow() {
//        Button button = new Button(mContext);

        final FloatBall button = new FloatBall(mContext);

//        button.setText("下");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtv_float.nextPage();
            }
        });

        final LayoutParams params = new LayoutParams();

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

        button.setOnTouchListener(new View.OnTouchListener() {
            int x = 0, y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(toString(), "onTouchEvent");

                //获取触摸动作
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN://按下
                        Log.e(toString(), "down");
                        x = (int) event.getX();
                        y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        if (mIsLongClick) {
                            Log.e(toString(), "eventX:" + event.getX() + ";x:" + x);
                            Log.e(toString(), "eventY:" + event.getY() + ";y:" + y);
                            params.x = (int) (ScreenUtils.getScreenWidth() - event.getRawX()) - x;
                            params.y = (int) (ScreenUtils.getScreenHeight() - event.getRawY() - ScreenUtils.getStatusBarHeight()) - y;
                            mWindowManager.updateViewLayout(button, params);
                        }
                        Log.e(toString(), "move");
                        break;
                    case MotionEvent.ACTION_UP://抬起
                        mIsLongClick = false;
                        Log.e(toString(), "up");
                        break;
                }

                return false;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e(toString(), "onLongClick");
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

                vibrator.vibrate(80);

                mIsLongClick = true;
                return false;
            }
        });


    }


}
