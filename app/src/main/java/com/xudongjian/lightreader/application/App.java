package com.xudongjian.lightreader.application;

import android.app.Application;

/**
 * Created by xudongjian on 17/4/12.
 */

public class App extends Application{

    public static App sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext =this;
    }


}
