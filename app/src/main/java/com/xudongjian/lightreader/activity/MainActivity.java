package com.xudongjian.lightreader.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.service.FloatService;
import com.xudongjian.lightreader.ui.adapter.CollectAdapter;
import com.xudongjian.lightreader.ui.view.RVItemDecoration;
import com.xudongjian.lightreader.utils.Codes;
import com.xudongjian.lightreader.utils.FloatPermissionUtils;
import com.xudongjian.lightreader.utils.SQLiteUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 主Activity
 */
public class MainActivity extends AppCompatActivity {


    RecyclerView mRv_books;

    private CollectAdapter mCollectAdapter;

    private List<Book> mCollectList;

    //点击已收藏书籍的位数
    private int mClickItemPosition;

    private FloatPermissionUtils mFloatPermissionUtils;

    public static boolean sIsStartFloatPermission = false;

    /**
     * 收藏书籍的条目点击事件
     */
    private CollectAdapter.OnItemClickListener mOnItemClickListener = new CollectAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {

            //如果点击的是最后一个条目(即添加书籍)
            if (position == mCollectAdapter.getItemCount() - 1) {
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    Log.e("log", "没有权限");

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("log", "true");
                        Toast.makeText(MainActivity.this, "快给人家扫描SD卡的权限啦,不然没法扫描书籍呢Σ( ￣□￣||)", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.e("log", "false");

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                            , Codes.PERMISSION_READ_EXTERNAL_STORAGE);


                } else {

                    startActivityForResult(new Intent(MainActivity.this, ScanLocalActivity.class), 0);
                }
            } else {

                mClickItemPosition = position;

                mFloatPermissionUtils = new FloatPermissionUtils();


                if (mFloatPermissionUtils.checkPermission(MainActivity.this)) {
                    startFloatService(mCollectList.get(mClickItemPosition));
                } else {
                    mFloatPermissionUtils.applyPermission(MainActivity.this);
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Paint().measureText("dasdadas");

        mRv_books=findViewById(R.id.rv_books);

        mRv_books.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mRv_books.addItemDecoration(new RVItemDecoration());

        mCollectList = SQLiteUtil.getInstance().query();

        mCollectList=new ArrayList<>();
        mCollectList.add(new Book("斗罗大陆","/storage/emulated/0/1/斗罗大陆.txt",23333,0));
        
        mCollectAdapter = new CollectAdapter(mCollectList);
        mRv_books.setAdapter(mCollectAdapter);
        mCollectAdapter.setOnItemClickListener(mOnItemClickListener);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            //如果返回码为ScanLocalActivity向MainActivity的返回码,并且收藏有变化
            case Codes.ACTIVITY_RESULT_CODE_SCANLOCAL2MAIN_COLLECT_HAS_CHANGE:
                List<Book> list = SQLiteUtil.getInstance().query();
                if (mCollectList != null && mCollectList.size() > 0) {
                    mCollectList.clear();
                    if (list != null && list.size() > 0) {
                        mCollectList = list;
                        mCollectAdapter.refresh(mCollectList);
                    } else {
                        mRv_books.removeAllViews();
                    }
                } else {
                    if (list != null && list.size() > 0) {
                        mCollectList = list;
                        Book.LogList(mCollectList);
                        mCollectAdapter = new CollectAdapter(mCollectList);
                        mRv_books.setAdapter(mCollectAdapter);
                        mCollectAdapter.setOnItemClickListener(mOnItemClickListener);
                    }
                }
                break;
        }


    }

    /**
     * 启动悬浮窗服务
     *
     * @param book 需要展示的数据对象
     */
    private void startFloatService(Book book) {
        Intent statServiceIntent = new Intent(MainActivity.this, FloatService.class);

        statServiceIntent.putExtra(FloatService.INTENT_KEY_BOOK, book);

        startService(statServiceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case Codes.PERMISSION_READ_EXTERNAL_STORAGE://读取外部存储权限
                Log.e("log", "PERMISSION_READ_EXTERNAL_STORAGE");

                //请求授权返回结果
                int grantResult = grantResults[0];

                if (grantResult == PackageManager.PERMISSION_GRANTED) {//授权成功
                    startActivityForResult(new Intent(MainActivity.this, ScanLocalActivity.class), 0);
                } else if (grantResult == PackageManager.PERMISSION_DENIED) {//不给授权

                }
                break;
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (sIsStartFloatPermission) {
            sIsStartFloatPermission = false;
            //启动悬浮窗服务
            if (mFloatPermissionUtils.checkPermission(MainActivity.this)) {
                //启动悬浮窗服务
                startFloatService(mCollectList.get(mClickItemPosition));
            } else {
                Toast.makeText(this, "获取悬浮窗权限失败了呢", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
