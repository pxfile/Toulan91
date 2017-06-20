package com.toulan.ui.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.fast.library.utils.AndroidInfoUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.ToolUtils;
import com.fast.library.utils.UIUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.toulan.utils.TConstant;
import com.toulan91.R;
import java.io.File;

/**
 * 说明：应用更新服务
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/8/30 14:34
 * <p/>
 * 版本：verson 1.0
 */
public class UpdateService extends IntentService{

    public static final String DOWN_LOAD_URL = "downloadurl";
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private int NOTIFICATION_ID_GOING = 1001;
    private int NOTIFICATION_ID_FAIL = 1002;
    private int NOTIFICATION_ID_CRAETE = 1003;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UpdateService() {
        super("appupdate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateNotification(int current){
        mNotificationManager.cancel(NOTIFICATION_ID_CRAETE);
        mNotificationBuilder.setContentTitle(UIUtils.getString(R.string.app_update_download_going))
                .setProgress(100,current,false)
                .setOngoing(true);
        mNotificationManager.notify(NOTIFICATION_ID_GOING,mNotificationBuilder.build());
    }

    private void clearNotification(){
        mNotificationManager.cancel(NOTIFICATION_ID_FAIL);
        mNotificationManager.cancel(NOTIFICATION_ID_GOING);
        mNotificationManager.cancel(NOTIFICATION_ID_CRAETE);
    }

    private void downSuccess(File file){
        clearNotification();
        createNotification();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        mNotificationBuilder.setContentTitle(UIUtils.getString(R.string.app_update_download_success_title))
                .setContentText(UIUtils.getString(R.string.app_update_download_success_text))
                .setTicker(UIUtils.getString(R.string.app_update_download_success_title))
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID_FAIL,mNotificationBuilder.build());
    }

    private void downFail(){
        clearNotification();
        createNotification();
        mNotificationBuilder.setContentTitle(UIUtils.getString(R.string.app_update_download_fail_title))
                .setContentText(UIUtils.getString(R.string.app_update_download_fail_text))
                .setTicker(UIUtils.getString(R.string.app_update_download_fail_title))
                .setOngoing(false)
                .setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID_FAIL,mNotificationBuilder.build());
    }

    public void createNotification(){
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new NotificationCompat.Builder(this);
        mNotificationBuilder.setSmallIcon(R.mipmap.app)
                .setTicker(UIUtils.getString(R.string.app_update_start_download))
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_DEFAULT);
    }

    public void createStartNotification(){
        createNotification();
        mNotificationBuilder.setContentTitle(UIUtils.getString(R.string.app_update_start_download))
                .setTicker(UIUtils.getString(R.string.app_update_start_download))
                .setOngoing(true)
                .setAutoCancel(false);
        mNotificationManager.notify(NOTIFICATION_ID_CRAETE,mNotificationBuilder.build());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String downloadUrl = intent.getStringExtra(DOWN_LOAD_URL);
        createStartNotification();
        if (!StringUtils.isEmpty(downloadUrl)){
            FileDownloader.getImpl().create(downloadUrl)
                    .setPath(getDownloadFile().getAbsolutePath())
                    .setForceReDownload(true)
                    .setListener(new FileDownloadLargeFileListener() {
                        @Override
                        protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

                        }

                        @Override
                        protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {
                            float current = ((soFarBytes*1.0f) / totalBytes) * 100;
                            updateNotification((int)current);
                        }

                        @Override
                        protected void blockComplete(BaseDownloadTask task) {

                        }

                        @Override
                        protected void completed(BaseDownloadTask task) {
                            File file = new File(task.getTargetFilePath());
                            ToolUtils.installApk(UpdateService.this,file);
                            downSuccess(file);
                        }

                        @Override
                        protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            downFail();
                        }

                        @Override
                        protected void warn(BaseDownloadTask task) {

                        }

                        @Override
                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }

                        @Override
                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        }
                    }).start();
        }
    }

    public static File getDownloadFile(){
        StringBuilder stringBuilder = new StringBuilder("toulan91");
        stringBuilder.append("_").append(AndroidInfoUtils.versionName()).append(".apk");
        File file = new File(TConstant.APP_UPDATE_FILE,stringBuilder.toString());
        return file;
    }
}
