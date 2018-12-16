package com.xudongjian.lightreader.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xudongjian.lightreader.application.App;


/**
 * Created by Administrator on 2016/5/3.
 */
public class SPUtil {

    public static final String KEY_FLOAT_WINDOW_X = "KEY_FLOAT_WINDOW_X";
    public static final String KEY_FLOAT_WINDOW_Y = "KEY_FLOAT_WINDOW_Y";
    public static final String KEY_FLOAT_WINDOW_WIDTH = "KEY_FLOAT_WINDOW_WIDTH";
    public static final String KEY_FLOAT_WINDOW_HEIGHT = "KEY_FLOAT_WINDOW_HEIGHT";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences sSharedPreferences_notClear;
    private static final String FILE_NAME_NOT_CLEAR = "fileNameNotClear";

    public static final String KEY_BALL_X = "KEY_BALL_X";
    public static final String KEY_BALL_Y = "KEY_BALL_Y";

    private static synchronized SharedPreferences getNotClearPreferences() {
        if (sSharedPreferences_notClear == null) {
            sSharedPreferences_notClear = App.sContext.getSharedPreferences(FILE_NAME_NOT_CLEAR, Context.MODE_PRIVATE);
        }
        return sSharedPreferences_notClear;
    }

    private static synchronized SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            // mSharedPreferences = App.context.getSharedPreferences(
            // PREFERENCE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.sContext);
        }
        return mSharedPreferences;
    }

    /**
     * 打印所有
     */
    public static void print() {
        System.out.println(getPreferneces().getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear() {
        getPreferneces().edit().clear().commit();
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(String key, String value) {
        getPreferneces().edit().putString(key, value).commit();
    }


    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getPreferneces().getString(key, null);

    }

    /**
     * 保存整型值
     *
     * @return
     */
    public static void putInt(String key, int value) {
        getPreferneces().edit().putInt(key, value).commit();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getPreferneces().getInt(key, 0);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(String key, Boolean value) {
        getPreferneces().edit().putBoolean(key, value).commit();
    }

    public static void putBooleanNotClear(String key, Boolean value) {
        getNotClearPreferences().edit().putBoolean(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getPreferneces().edit().putLong(key, value).commit();
    }

    public static long getLong(String key) {
        return getPreferneces().getLong(key, 0);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferneces().getBoolean(key, defValue);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBooleanNotClear(String key, boolean defValue) {
        return getNotClearPreferences().getBoolean(key, defValue);
    }


    /**
     * 移除字段
     *
     * @return
     */
    public static void removeString(String key) {
        getPreferneces().edit().remove(key).commit();
    }

    public static void removeNotClear(String key) {
        getNotClearPreferences().edit().remove(key).commit();
    }

    public static void putStringNotClear(String key, String value) {
        getNotClearPreferences().edit().putString(key, value).commit();
    }

    public static String getStringNotClear(String key) {
        return getNotClearPreferences().getString(key, "");
    }

    public static void putLongNotClear(String key, long value) {
        getNotClearPreferences().edit().putLong(key, value).commit();
    }

    public static void putIntNotClear(String key, int value) {
        getNotClearPreferences().edit().putInt(key, value).commit();
    }

    public static int getIntNotClear(String key) {
        return getNotClearPreferences().getInt(key, 0);
    }

    public static long getLongNotClear(String key) {
        return getNotClearPreferences().getLong(key, 0);
    }
}
