package com.xudongjian.lightreader.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.utils.SQLiteUtil;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xudongjian on 17/5/31.
 * 阅读视图
 */

public class ReaderTextView extends View {

    //阅读的书籍对象
    private Book mBook;

    //高效的文件内存映射
    private MappedByteBuffer mMbb;

    //字体大小
    private float mTextSize = 90.0f;

    //行间距
    private float mLinePadding = 10.0f;

    //本类中log的标签
    private String TAG = "ReaderTextView";

    //文件大小(单位kb)
    private long mFileLength;

    //编码方式
    private String mCharset = "UTF-8";

    private Paint mPaint;

    private Rect mBounds;

    private String POS = "pos";

    //是否是在调整悬浮窗大小
    private boolean mIsAdjustingSize = false;

    private int goBackEndPos;


    public ReaderTextView(Context context) {
        super(context);
    }

    public ReaderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


    }

    public ReaderTextView(Context context, Book book) {
        super(context);

        Log.e(TAG, "Constructor");

        mBook = book;
        //初始化文件内存映射对象
        mMbb = initMappedByteBuffer();


        //获取编码方式
        mCharset = getFileIncode(new File(mBook.getPath()));
//                EncodingDetect.getJavaEncode(mBook.getPath());
//                FileUtils.getCharset(mBook.getPath());

//        EncodingDetect.readFile(mBook.getPath(),mCharset);

        Log.e("encode", mCharset);


        mPaint = new Paint();

        mPaint.setTextSize(90);

        mBounds = new Rect();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mIsAdjustingSize) {
            return;
        }

        //可绘制高度
        int height = getMeasuredHeight();

        //行数(浮点)
        float lineNumberF = (height - mLinePadding) / (mTextSize + mLinePadding);

        //行数
        int lineNumber = (int) Math.round(lineNumberF - 0.5);

        //控制是否继续循环
        boolean flag = true;


        //已经绘制了的行数
        int hasDrawLine = 0;

        goBackEndPos = mBook.getEndPos();

        while (flag) {
            //读取的一段的字节数组
            byte[] bytes = readLine(mBook.getEndPos());

            Log.e(TAG, "bytes.length  字节数组的长度:" + bytes.length);

            //如果一行为空并且已经填满绘制区域
            if (bytes.length == 2 && hasDrawLine + 1 >= lineNumber) {
                Log.e(TAG, "break");
                break;
            }

            //一段文字(paragraph)
            String paraString = bytes2String(bytes);

            Log.e(TAG, "paraString  一段文字:" + paraString + "|");


            char[] chars = paraString.toCharArray();

            //一行已经绘制了的文字
            String lineHasDrawString = "";


            for (int i = 0; i < chars.length; i++) {

                //一个字
                String oneChar = String.valueOf(chars[i]);

                //将绘制完成的字加入到已绘制的字符串中
                lineHasDrawString = lineHasDrawString + oneChar;

                //获取字符串的边界
                mPaint.getTextBounds(lineHasDrawString, 0, lineHasDrawString.length(), mBounds);


                Rect oneCharBounds = new Rect();

                mPaint.getTextBounds(oneChar, 0, oneChar.length(), oneCharBounds);

                //绘制文本的x坐标
                float drawX = mBounds.width() - oneCharBounds.width();

                //如果要绘制的文本长度大于显示范围的宽度
                if (mBounds.width() > getMeasuredWidth()) {
                    //已经绘制的行数+1
                    hasDrawLine++;
                    //如果已经绘制的行数大于可显示的行数(本页已经绘制完毕)
                    if (hasDrawLine >= lineNumber) {

                        //多出的字符串
                        String moreString = paraString.substring(i, paraString.length());

                        //多出的字节数组
                        byte[] moreBytes;

                        try {
                            //将多出的字符串转化为字节数组
                            moreBytes = moreString.getBytes(mCharset);
                            //获取指针位置
                            int endPos = mBook.getEndPos();

                            Log.e(POS, "一页绘制完成指针原本的位置:" + endPos);


                            int setEndPos = endPos - moreBytes.length;

                            Log.e(POS, "一页绘制完成后指针修正的位置:" + setEndPos);

                            //将指针位置回调到显示完的位置
                            mBook.setEndPos(setEndPos);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();

                        }

                        flag = false;
                        Log.e(TAG, "false");
                        break;
                    }
                    //一行已经绘制的字数清空并赋值为超出的那个字
                    lineHasDrawString = oneChar;

                    //获取一行已绘制的字数
                    mPaint.getTextBounds(lineHasDrawString, 0, lineHasDrawString.length(), mBounds);

                    float drawY = (hasDrawLine + 1) * (mTextSize + mLinePadding) - mLinePadding;

                    canvas.drawText(oneChar, 0, drawY, mPaint);
                    Log.e(TAG, "oneChar  绘制的多余的文字:" + oneChar);

                } else {

                    //绘制文本的y坐标
                    float drawY = (hasDrawLine + 1) * (mTextSize + mLinePadding) - mLinePadding;

                    //绘制一个字
                    canvas.drawText(oneChar, drawX, drawY, mPaint);
                    Log.e(TAG, "oneChar  绘制的文字:" + oneChar + " |");

                }
            }
            hasDrawLine++;
        }
        //如果是在调整悬浮窗大小,则游标不增加到下一页的位置
//        if (mIsAdjustingSize) {
//            mBook.setEndPos(goBackEndPos);
//        }

    }

    public void setIsAdjustingSize(boolean is) {
        mIsAdjustingSize = is;
        if(!is){
            mBook.setEndPos(goBackEndPos);
            invalidate();
        }
    }

//    private final String MEASURE = "Measure";
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        //一般为match_content
//        if (widthMode == MeasureSpec.EXACTLY) {
//            Log.e(MEASURE, "widthMode:EXACTLY");
//        } else if (widthMode == MeasureSpec.AT_MOST) {
//            Log.e(MEASURE, "widthMode:AT_MOST");
//        }
//        Log.e(MEASURE, "widthSize:" + widthSize);
//
//        if (heightMode == MeasureSpec.EXACTLY) {
//            Log.e(MEASURE, "heightMode:EXACTLY");
//        } else if (heightMode == MeasureSpec.AT_MOST) {
//            Log.e(MEASURE, "heightMode:AT_MOST");
//        }
//        Log.e(MEASURE, "heightSize:" + heightSize);
//
//    }

    /**
     * 下一页
     */
    public void nextPage() {
        //保存进度
        SQLiteUtil.getInstance().update(mBook);
        Log.e(POS, "点击下一页存储的位置:" + mBook.getEndPos());
        invalidate();


    }

    /**
     * 上一页
     */
    public void lastPage() {

    }

    /**
     * 获取文件内存映射对象
     *
     * @return mMbb
     */
    private MappedByteBuffer initMappedByteBuffer() {

        File file = new File(mBook.getPath());
        mFileLength = file.length();
        MappedByteBuffer mbb = null;
        if (file.exists()) {
            try {
                mbb = new RandomAccessFile(file, "r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mbb;
    }

    /**
     * 读取一行
     *
     * @param curEndPos 指针位置
     * @return 一行文字的字节数组
     */
    private byte[] readLine(int curEndPos) {
        byte b0;
        //这行读完时指针所在的位置
        int i = curEndPos;
        while (i < mFileLength) {
            b0 = mMbb.get(i++);
            if (b0 == 0x0a) {
                break;
            }
        }

        Log.e(POS, "读取前的位置:" + mBook.getEndPos());
        //更新指针的位置
        mBook.setEndPos(i);
        Log.e(POS, "读取到的位置:" + i);

        //一行的长度
        int nParaSize = i - curEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mMbb.get(curEndPos + i);
        }

        return buf;
    }

    private String bytes2String(byte[] bytes) {
        String str;
        try {
            str = new String(bytes, mCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = "非常抱歉由于未知的原因,文字解析错误,请尝试重启应用或联系客服,谢谢";
        }
        return str;
    }

    public static String getFileIncode(File file) {

        if (!file.exists()) {
            System.err.println("getFileIncode: file not exists!");
            return null;
        }

        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // (1)
            UniversalDetector detector = new UniversalDetector(null);

            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();

            // (4)
            String encoding = detector.getDetectedCharset();
            if (encoding != null) {
                System.out.println("Detected encoding = " + encoding);
            } else {
                System.out.println("No encoding detected.");
            }

            // (5)
            detector.reset();
            fis.close();
            return encoding;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
