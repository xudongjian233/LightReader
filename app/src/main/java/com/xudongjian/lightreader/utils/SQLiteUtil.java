package com.xudongjian.lightreader.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xudongjian.lightreader.application.App;
import com.xudongjian.lightreader.bean.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xudongjian on 17/4/12.
 */

public class SQLiteUtil {

    private static final String DATABASE_NAME = "LightReader.db";

    private final String DATABASE_TABLE_NAME = "collect_list";
    public static final String DATABASE_KEY_NAME = "name";
    public static final String DATABASE_KEY_PATH = "path";
    public static final String DATABASE_KEY_SIZE = "size";
    public static final String DATABASE_KEY_ENDPOS = "endPos";

    private static SQLiteUtil sSQLiteUtil;

    private SQL mSQL;

    private SQLiteDatabase mSQLiteDatabase;

    private SQLiteUtil(Context context) {
        mSQL = new SQL(context, DATABASE_NAME, null, 1);
        mSQLiteDatabase = mSQL.getReadableDatabase();
    }

    public static SQLiteUtil getInstance() {

        if (sSQLiteUtil == null) {
            sSQLiteUtil = new SQLiteUtil(App.sContext);
        }

        return sSQLiteUtil;
    }

    /**
     * 插入数据库
     *
     * @param book 需要插入的book对象
     */
    public void insert(Book book) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DATABASE_KEY_NAME, book.getName());
        contentValues.put(DATABASE_KEY_PATH, book.getPath());
        contentValues.put(DATABASE_KEY_SIZE, book.getSize());
        contentValues.put(DATABASE_KEY_ENDPOS, book.getEndPos());

        mSQLiteDatabase.insert(DATABASE_TABLE_NAME, null, contentValues);
    }

    /**
     * 修改数据库
     *
     * @param book 新的内容的对象
     */
    public void update(Book book) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATABASE_KEY_NAME, book.getName());
        contentValues.put(DATABASE_KEY_PATH, book.getPath());
        contentValues.put(DATABASE_KEY_SIZE, book.getSize());
        contentValues.put(DATABASE_KEY_ENDPOS, book.getEndPos());

        mSQLiteDatabase.update(DATABASE_TABLE_NAME, contentValues, DATABASE_KEY_PATH + "=?", new String[]{book.getPath()});
    }

    /**
     * 删除数据库
     *
     * @param whereClause 根据哪一条key删除
     * @param whereArg    key的内容
     */
    public void delete(String whereClause, String whereArg) {
        String[] str = new String[]{whereArg};
        mSQL.getReadableDatabase().delete(DATABASE_TABLE_NAME, whereClause + "=?", str);

    }

    /**
     * 查询数据库
     *
     * @return 数据库中的所有数据, 如果没有则返回null
     */
    public List<Book> query() {
        Cursor cursor = mSQL.getReadableDatabase().query(DATABASE_TABLE_NAME, null, null, null, null, null, null);
        List<Book> list = null;
        //判断游标是否为空
        if (cursor.moveToFirst()) {
            list = new ArrayList<>();
            //遍历游标
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(0);
                String path = cursor.getString(1);
                long size = cursor.getLong(2);
                int endPos = cursor.getInt(3);
                list.add(new Book(name, path, size, endPos));
                cursor.moveToNext();
            }
        }


        cursor.close();

        return list;

    }


    private class SQL extends SQLiteOpenHelper {


        private final String DATABASE_TABLE_CREAT = "CREATE TABLE " + DATABASE_TABLE_NAME + " (\n" +
                "  " + DATABASE_KEY_NAME + " text NOT NULL,\n" +
                "  " + DATABASE_KEY_PATH + " text NOT NULL,\n" +
                "  " + DATABASE_KEY_SIZE + " integer NOT NULL,\n" +
                "  " + DATABASE_KEY_ENDPOS + " integer NOT NULL\n" +
                ");";

        public SQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_TABLE_CREAT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }
}
