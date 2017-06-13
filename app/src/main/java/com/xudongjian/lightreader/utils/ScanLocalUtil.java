package com.xudongjian.lightreader.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.xudongjian.lightreader.application.App;
import com.xudongjian.lightreader.bean.Book;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xudongjian on 17/4/11.
 * 扫描本地文件工具
 */

public class ScanLocalUtil {


    /**
     * @param context      上下文对象
     * @param collectBooks 已收藏的书籍
     * @return 查询到的所有书籍
     */
    public static List<Book> EzCursorScanLocal(Context context, List<Book> collectBooks) {


        String[] projection = new String[]{
//                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };

        // cache
        String bookpath = FileUtils.createRootPath(App.sContext);

        // 查询后缀名为txt与pdf，并且不位于项目缓存中的文档
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " not like ? and ("
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? or "
                        + MediaStore.Files.FileColumns.DATA + " like ? )",
                new String[]{"%" + bookpath + "%",
                        "%" + ".txt",
                        "%" + ".pdf",
                        "%" + ".epub",
                        "%" + ".chm"}, null);
        List<Book> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
//            int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);


            do {
                String path = cursor.getString(dataindex);

                int dot = path.lastIndexOf("/");
                String name = path.substring(dot + 1);
                if (name.lastIndexOf(".") > 0) {
                    name = name.substring(0, name.lastIndexOf("."));
                }

                Book book = new Book();

                if (collectBooks != null && collectBooks.size() > 0) {
                    for (Book collectBook : collectBooks) {

                        if (collectBook.getPath().equals(path)) {
                            book.setCollect(true);
                            break;
                        }

                    }
                }

                book.setName(name);
                book.setPath(path);
                book.setSize(cursor.getLong(sizeindex));
                book.setEndPos(0);

                list.add(book);
                Comparator compare = new CompareUtil();
                Collections.sort(list, compare);

            } while (cursor.moveToNext());

            cursor.close();

        }
        return list;
    }


}
