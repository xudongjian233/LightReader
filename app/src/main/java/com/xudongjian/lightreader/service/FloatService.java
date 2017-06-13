package com.xudongjian.lightreader.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.manager.FloatWindowManager;
import com.xudongjian.lightreader.ui.view.ReaderTextView;

public class FloatService extends Service {

    //包含文本悬浮窗的布局
    private ViewGroup mLayout_textView;

    //用于显示文本
    private ReaderTextView mRtv;

    //包含下一页按钮悬浮窗的布局
    private ViewGroup mLayout_button;

    //下一页按钮
    private Button mB_next;

    LayoutParams wmParams_textView;
    LayoutParams wmParams_button;

    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    //用于显示书籍内容的类
//    private BookReader mBookReader;

    //需要展示的书籍对象
    private Book mBook;

    private static final String TAG = "FxService";

    //Intent传递书籍对象的key
    public static final String INTENT_KEY_BOOK = "book";

    private FloatWindowManager mFloatWindowManager;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand");
        mBook = (Book) intent.getSerializableExtra(INTENT_KEY_BOOK);

        mFloatWindowManager=new FloatWindowManager(getBaseContext());

        mFloatWindowManager.showTextFloatWindow(mBook);

        mFloatWindowManager.showNextButtonFloatWindow();

        //获取的是WindowManagerImpl.CompatModeWrapper
//        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

//        addNextButton();

//        addTextView();


        return super.onStartCommand(intent, flags, startId);
    }

    private void addTextView() {

        mLayout_textView = new FrameLayout(getBaseContext());

        mLayout_textView.setBackgroundColor(getResources().getColor(R.color.textFloatWindowBackground));

        mRtv = new ReaderTextView(mLayout_textView.getContext(), mBook);

        mLayout_textView.addView(mRtv);

        wmParams_textView = new LayoutParams();

        Log.i(TAG, "mWindowManager--->" + mWindowManager);
        //设置window type
        wmParams_textView.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams_textView.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams_textView.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams_textView.gravity = Gravity.START | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams_textView.x = 0;
        wmParams_textView.y = 0;

        //设置悬浮窗口长宽数据
        wmParams_textView.width = 700;
        wmParams_textView.height = 700;

        //添加mFloatLayout
        mWindowManager.addView(mLayout_textView, wmParams_textView);


        //浮动窗口按钮

//        mLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
//                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + mLayout_textView.getMeasuredWidth() / 2);
        Log.i(TAG, "Height/2--->" + mLayout_textView.getMeasuredHeight() / 2);
        //设置监听浮动窗口的触摸移动
        mLayout_textView.setOnTouchListener(new OnTouchListener() {
            //按下时点击悬浮窗的坐标
            int x = 0, y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.e(TAG, "onTouch");
//                return false;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.e(TAG, "ACTION_DOWN");
                        x = (int) event.getX();
                        y = (int) event.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //刷新视图时悬浮窗x和y的位置
//                        Log.e(TAG, "ACTION_MOVE");
                        wmParams_textView.x = (int) (event.getRawX()) - x;
                        wmParams_textView.y = (int) (event.getRawY() - getStatusBarHeight()) - y;
                        mWindowManager.updateViewLayout(mLayout_textView, wmParams_textView);
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.e(TAG, "ACTION_MOVE");
                        break;
                }


                return false;  //此处必须返回false，否则OnClickListener获取不到监听
            }
        });

    }

    private void addNextButton() {

        mLayout_button = new FrameLayout(getBaseContext());

        mB_next = new Button(getBaseContext());

        mLayout_button.addView(mB_next);

        mB_next.setText("下一页");

        mB_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRtv.nextPage();
            }
        });


        wmParams_button = new LayoutParams();

        //设置类型为悬浮模式
        wmParams_button.type = LayoutParams.TYPE_PHONE;

        //设置图片格式,效果为背景可透明
        wmParams_button.format = PixelFormat.RGBA_8888;

        //设置模式.为不可聚焦
        wmParams_button.flags = LayoutParams.FLAG_NOT_FOCUSABLE;

        //调整悬浮窗的位置为
        wmParams_button.gravity = Gravity.END | Gravity.BOTTOM;

        wmParams_button.width = LayoutParams.WRAP_CONTENT;
        wmParams_button.height = LayoutParams.WRAP_CONTENT;

        mWindowManager.addView(mLayout_button, wmParams_button);


    }

    private int getStatusBarHeight() {
        /**
         * 获取状态栏高度——方法
         * */
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private int getPositiveNumber(int number) {

        if (number < 0) {
            number = number * -1;
        }

        return number;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mLayout_textView != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mLayout_textView);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
