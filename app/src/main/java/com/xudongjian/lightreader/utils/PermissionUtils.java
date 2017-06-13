package com.xudongjian.lightreader.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by xudongjian on 17/5/25.
 */

public class PermissionUtils {

    //读取外部存储的权限的请求码
    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 1;

    //读取外部存储的权限回调接口
    private static OnMyRequestPermissionResult sResult_readExternalStorage;

    //申请权限的字符串数组
    private static String[] sPermissions;

    /**
     * 申请一个权限
     * 调用申请权限的对话框,延时操作,结果通过接口回调
     *
     * @param activity                    当前所在的Activity
     * @param permissionType              请求的权限类型
     * @param onMyRequestPermissionResult 回调接口
     */
    public static void requestOnePermission(Activity activity, int permissionType, OnMyRequestPermissionResult onMyRequestPermissionResult) {

        String permission = setPermissionType(permissionType, onMyRequestPermissionResult);

        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);

        Log.e("log", "requestOnePermission");
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e("log", "没有权限");

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Log.e("log", "true");

            } else {
                Log.e("log", "false");

                ActivityCompat.requestPermissions(activity, sPermissions
                        , permissionType);

            }
        }

    }

    /**
     * 根据传入的权限类型给申请权限的字符串数组和回调接口赋值
     *
     * @param permissionType              申请的权限的类型
     * @param onMyRequestPermissionResult 传入的回调接口
     */
    private static String setPermissionType(int permissionType, OnMyRequestPermissionResult onMyRequestPermissionResult) {
        switch (permissionType) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                sResult_readExternalStorage = onMyRequestPermissionResult;
                sPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        return null;
    }


    /**
     * 请求权限结果的回调,这个方法被所有的请求权限方法回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("log", "onRequestPermissionsResult");
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE:
                Log.e("log", "PERMISSION_READ_EXTERNAL_STORAGE");

                int grantResult = grantResults[0];

                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    sResult_readExternalStorage.onRequestPermissionsResult(true);
                } else {
                    sResult_readExternalStorage.onRequestPermissionsResult(false);
                }
                break;
        }
    }


    public interface OnMyRequestPermissionResult {
        void onRequestPermissionsResult(boolean isGrant);
    }


}
