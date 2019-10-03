package com.zhanggouzi.mumreader;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

/**
 * 后台服务1分钟后会被停止
 * 1. 是foreground service则不停，否则继续
 * 2. 是否在临时白名单中（用于调试），如果是则不停，否则继续
 * 3. 是否是Persistent app（系统app），如果是则不停，否则继续
 * 4. 是否在允许后台运行白名单（系统app），在则不停，否则继续
 * 5. 是否在省电白名单，在则不停，否则继续
 * 6. 是否target sdk version大于等于26，是则1分钟后停，否则继续
 * 7. 如果target sdk version小于26，是否有OP_RUN_IN_BACKGROUND权限，有则不停，否则停
 * <p>
 * 作为普通的APP：
 * 1. 改成前台service, startForegroundService启动五秒内调用startForeground，否则ANR
 * 2. 加入省电白名单
 * 3. 设置persistent属性为true
 * 4. Intent.ACTION_TIME_TICK广播
 */
public class PasteCopyService extends Service {
    private static final String TAG = "PasteCopyService";

    private static final String CHANNEL_ID = "com.zhanggouzi.channel.id";
    private static final String CHANNEL_NAME = "TEST";
    private static final int NOTIFICATION_ID = 101;

    private ReaderHelper readerHelper;
    private ClipboardManager clipboardManager;
    private ClipboardManager.OnPrimaryClipChangedListener primaryClipChangedListener;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate ");
        createNotification();
        createTTS();
        createClipboard();
    }

    private void createTTS() {
        readerHelper = ReaderHelper.getInstance(this);
    }

    private void createClipboard() {
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        primaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                if (clipboardManager.hasPrimaryClip()
                        && clipboardManager.getPrimaryClip().getItemCount() > 0) {
                    // 获取复制、剪切的文本内容
                    CharSequence content =
                            clipboardManager.getPrimaryClip().getItemAt(0).getText();
                    Log.i(TAG, "复制、剪切的内容为：" + content);
                    ReaderHelper.readText(content.toString());
                }

            }
        };
        clipboardManager.addPrimaryClipChangedListener(primaryClipChangedListener);
    }

    private void createNotification() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        Intent intent = new Intent(this, ReadingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("通知服务")
                .setContentText("目的是不被系统杀，点击打开活动...")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand ");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Paste copy service destroy");
        if (clipboardManager != null && primaryClipChangedListener != null) {
            clipboardManager.removePrimaryClipChangedListener(primaryClipChangedListener);
            clipboardManager = null;
            primaryClipChangedListener = null;
        }
        if (readerHelper != null) {
            readerHelper.close();
            readerHelper = null;
        }
    }
}
