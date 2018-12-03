package com.xudongjian.lightreader.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xudongjian.lightreader.R;

import butterknife.ButterKnife;

/**
 * 启动Activity
 */
public class StartActivity extends AppCompatActivity{

    TextView mTv_skip;

    private Runnable mRunnable;

    private boolean mIsFinish=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        mTv_skip=findViewById(R.id.tv_skip);

        mRunnable=new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        };

        mTv_skip.postDelayed(mRunnable,1);

        mTv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });


    }

    private synchronized void goHome(){
        if(!mIsFinish){
            mIsFinish=true;
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsFinish=true;
        mTv_skip.removeCallbacks(mRunnable);
    }
}
