package com.xudongjian.lightreader.utils;

import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.xudongjian.lightreader.bean.Book;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xudongjian on 17/5/8.
 */

public class BookReader {

    private final String TAG = "BookReader";

    private WindowManager.LayoutParams mWmParams;

    //展示文字的TextView对象
    private TextView mTv;

    //需要展示的书籍对象
    private Book mBook;

    //行数
    private int mLineNumber;

    //列数
    private int mColumnNumber;

    //高效的文件内存映射
    private MappedByteBuffer mMbb;

    //文件长度
    private int mFileLength;

    //解码方式
    private String charset = "UTF-8";

    /**
     * 构造函数
     *
     * @param book 需要阅读的书籍对象
     * @param tv   用于展示书籍的TextView对象
     */
    public BookReader(Book book, TextView tv, WindowManager.LayoutParams wmParams) {
        mBook = book;
        mTv = tv;
        mWmParams = wmParams;

        //获取文件内存映射对象
        mMbb = initMappedByteBuffer();
        //获取书籍的编码方式
        charset = FileUtils.getCharset(book.getPath());

        //为行数赋值
        mLineNumber = getLineNumber();
        //为列数赋值
        mColumnNumber = initColumnNumber();

    }

    /**
     * 读取一页
     */
    public void readNextPage() {
        Log.e(TAG, "\n\n");
        int moreBytesLength = 0;


        //本页剩余行数
        int pageLineNumber = mLineNumber;


        String nextPage = "";
        //读取的一段文字
        String paraString = "";


        //循环一次添加一行,剩余行数则少一行
        for (; pageLineNumber > 0; pageLineNumber--) {

            Log.e(TAG, "pageLineNumber:" + pageLineNumber);

            Log.e(TAG, "getEndPos:" + mBook.getEndPos());
            //一行文字的字节数组
            byte[] paraBuffer = readLine(mBook.getEndPos());

            Log.e(TAG, "paraBuffer:" + paraBuffer);

            //字节数组长度
            int paraBufferLength = paraBuffer.length;

            try {
                paraString = new String(paraBuffer, charset);
                Log.e(TAG, "paraString:" + paraString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int paraStringLength = paraString.length();
            Log.e(TAG, "paraStringLength:" + paraStringLength);

            //显示行数等于一段字符串的长度除以一行的字数
            double paraLineNumDouble = ((double) paraString.length()) / mColumnNumber;
            Log.e(TAG, "paraLineNumDouble:" + paraLineNumDouble);

            //读取的一段文字实际显示时占用的行数
            int paraLineNum = (int) Math.ceil(paraLineNumDouble);
            Log.e(TAG, "paraLineNum:" + paraLineNum);

            if (paraLineNum > 1) {//读取的一行文字实际显示占用行数大于0

                //实际多占用一行,剩余行数则少一行
                for (int j = 1; j < paraLineNum; j++) {
                    pageLineNumber--;
                    Log.e(TAG, "----pageLineNumber:" + pageLineNumber);
                }


            }

            if (pageLineNumber <= 0) {//剩余行数小于零,即一段文字超出显示范围

                pageLineNumber--;

                Log.e(TAG, "pageLineNumber<=0");

                //显示的最后一行的字数 = 一段文字的长度减去除去最后一行的字数
                int lastLineStringLength = paraString.length() - mColumnNumber * (paraLineNum - 1);
                Log.e(TAG, "lastLineStringLength:" + lastLineStringLength);

                //多出的行数
                int moreLineNumber = Math.abs(pageLineNumber);

                Log.e(TAG, "moreLineNumber:" + moreLineNumber);

                //多出的字符串的长度
                int moreStringLength = (moreLineNumber - 1) * mColumnNumber + lastLineStringLength;
                Log.e(TAG, "moreStringLength:" + moreStringLength);

                int beginIndex = paraString.length() - moreStringLength;
                int endIndex = paraString.length();
                Log.e(TAG, "beginIndex:" + beginIndex + ";endIndex:" + endIndex);

                //多出的字符串
                String moreString = paraString.substring(beginIndex, endIndex);
                Log.e(TAG, "moreString:" + moreString);

                //多出的字符串转byte[]
                byte[] moreString2Bytes = new byte[0];
                try {
                    moreString2Bytes = moreString.getBytes(charset);
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "转码异常");
                    e.printStackTrace();
                }
                Log.e(TAG, "moreString2Bytes:" + moreString2Bytes);

                //多出的字节数组长度
                moreBytesLength = moreString2Bytes.length;
                Log.e(TAG, "moreBytesLength:" + moreBytesLength);


            }
            nextPage = nextPage + paraString;
            Log.e(TAG, "nextPage:" + nextPage);

        }//for()

        int callbackPos = mBook.getEndPos() - moreBytesLength;
        Log.e(TAG, "callbackPos:" + callbackPos);
        mBook.setEndPos(callbackPos);

        //更新数据库(读取下一页时保存读到的进度)
        SQLiteUtil.getInstance().update(mBook);

        mTv.setText(nextPage);
    }

    /**
     * 读取上一页
     */
    public String readLastPage() {
        String lastPage = null;

        return lastPage;
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

        //更新指针的位置
        mBook.setEndPos(i);
        Log.e(TAG, "endPos:" + i);

        //一行的长度
        int nParaSize = i - curEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mMbb.get(curEndPos + i);
        }

        return buf;
    }


    /**
     * 获取行数
     *
     * @return 行数
     */
    private int getLineNumber() {

        int lineNumber = mWmParams.height / mTv.getLineHeight();


        return lineNumber;
    }

    /**
     * 获取列数(一行多少字)
     *
     * @return 列数
     */
    private int initColumnNumber() {

        int columnNumber = (int) (mWmParams.width / mTv.getTextSize());

        return columnNumber;

    }

    /**
     * 获取文件内存映射对象
     *
     * @return mMbb
     */
    private MappedByteBuffer initMappedByteBuffer() {

        File file = new File(mBook.getPath());
        mFileLength = (int) file.length();
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


}
