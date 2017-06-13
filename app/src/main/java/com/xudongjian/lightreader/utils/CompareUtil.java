package com.xudongjian.lightreader.utils;

import com.xudongjian.lightreader.bean.Book;

import java.util.Comparator;

/**
 * Created by xudongjian on 17/4/12.
 */

public class CompareUtil implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Book book = (Book) o1;
        Book book1 = (Book) o2;
        return (int) (book1.getSize()-book.getSize());
    }
}
