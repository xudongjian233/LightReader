package com.xudongjian.lightreader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;
import com.xudongjian.lightreader.ui.adapter.ScanLocalAdapter;
import com.xudongjian.lightreader.ui.view.RVItemDecoration;
import com.xudongjian.lightreader.utils.Codes;
import com.xudongjian.lightreader.utils.SQLiteUtil;
import com.xudongjian.lightreader.utils.ScanLocalUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 扫描本地书籍Activity
 */
public class ScanLocalActivity extends AppCompatActivity {

    @Bind(R.id.rv_scanLocal)
    RecyclerView mRv_scanLocal;

    private ScanLocalAdapter mScanLocalAdapter;

    private List<Book> list_books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(new File("file://"
                    + Environment.getExternalStorageDirectory())); //out is your output file
            mediaScanIntent.setData(contentUri);
            ScanLocalActivity.this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }


        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        mRv_scanLocal.setLayoutManager(new LinearLayoutManager(this));
        mRv_scanLocal.addItemDecoration(new RVItemDecoration());
        //扫描本地文件获得的书籍集合
        list_books = ScanLocalUtil.EzCursorScanLocal(ScanLocalActivity.this, SQLiteUtil.getInstance().query());
        mScanLocalAdapter = new ScanLocalAdapter(list_books);
        mRv_scanLocal.setAdapter(mScanLocalAdapter);

        mScanLocalAdapter.setOnItemClickListener(new ScanLocalAdapter.OnItemClickListener() {
            @Override
            public boolean onClick(int position) {

                //设置返回码,意思为收藏内容有改变
                setResult(Codes.ACTIVITY_RESULT_CODE_SCANLOCAL2MAIN_COLLECT_HAS_CHANGE);

                if (!list_books.get(position).isCollect()) {
                    list_books.get(position).setCollect(true);
                    SQLiteUtil.getInstance().insert(list_books.get(position));
                    Toast.makeText(ScanLocalActivity.this, "成功的加入到书架了呢~", Toast.LENGTH_SHORT).show();
                    return true;

                } else {
                    list_books.get(position).setCollect(false);
                    SQLiteUtil.getInstance().delete(SQLiteUtil.DATABASE_KEY_PATH, list_books.get(position).getPath());
                    Toast.makeText(ScanLocalActivity.this, "从书架移除了哦~", Toast.LENGTH_SHORT).show();
                    return false;
                }


            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
