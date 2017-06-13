package com.xudongjian.lightreader.bean;

import android.util.Log;

import com.xudongjian.lightreader.utils.FileUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xudongjian on 17/4/11.
 */

public class Book implements Serializable {
    //书名
    private String name;
    //书路径
    private String path;
    //书大小
    private long size;
    //是否已经收藏
    private boolean isCollect;
    //阅读到的位置
    private int endPos;


    public Book() {
        isCollect = false;
    }

    public Book(String name, String path, long size, int endPos) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.endPos = endPos;
        isCollect = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSizeString() {
        return FileUtils.formatFileSizeToString(size);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public static void LogList(List<Book> list) {

        if (list != null && list.size() > 0)
            for (Book book : list) {

                Log.e("log", "name:" + book.getName() + ";path:" + book.getPath() + ";size:" + book.getSize() + ";isCollect:" + book.isCollect() + ";endPos:" + book.getEndPos());

            }

    }

}
