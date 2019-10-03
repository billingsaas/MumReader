package com.zhanggouzi.mumreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 1. 需要启动一次，动态注册监听启动消息
 * 2. 设置中启动管理允许自启动
 */
public class PowerOnBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerOnBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "launch paste copy service");
        Intent serviceIntent = new Intent(context,PasteCopyService.class);
        context.startForegroundService(serviceIntent);
    }


}
