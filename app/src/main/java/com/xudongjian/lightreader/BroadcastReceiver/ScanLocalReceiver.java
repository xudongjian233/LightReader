package com.xudongjian.lightreader.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScanLocalReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.e("log","onReceiver");
        if (intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {

        }
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
