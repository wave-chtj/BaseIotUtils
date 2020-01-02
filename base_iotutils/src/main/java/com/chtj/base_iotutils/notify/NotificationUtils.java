package com.chtj.base_iotutils.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.RemoteViews;

import com.chtj.base_iotutils.KLog;
import com.chtj.base_iotutils.R;
import com.chtj.base_iotutils.keeplive.BaseIotUtils;

/**
 * Create on 2019/12/27
 * author chtj
 * desc ：Notification 工具类
 * {@link #getInstance()} 初始化相关参数
 * {@link #setNotifyId(int)} 设置notification唯一标识
 * {@link #setSlideOff(boolean)} 是否设置滑动删除
 * {@link #setmAutoCancel(boolean)} 是否点击时关闭
 * {@link #exeuNotify()} 执行显示或是参数改变时需要重新设置的方法
 * {@link #closeNotify()} 关闭notification
 *
 */
public class NotificationUtils {
    public static final String TAG = "NotificationUtils";
    //系统通知
    private NotificationManager manager = null;
    private Notification.Builder builder = null;
    //自定义的系统通知视图
    private RemoteViews contentView = null;
    private int notifyId = -1; //notification标识
    private boolean mSlideOff = true;//滑动时是否可以删除
    private boolean mAutoCancel = false;//点击的时候是否消失
    private String appName;//app名称
    private String remarks;//备注
    private String prompt;//提示
    private INotificationLinstener mINotificationLinstener;
    private static NotificationUtils notificationUtils;
    //停止该通知务的广播
    public static final String ACTION_CLOSE_NOTIFY = "com.close.service.and.notification";


    //单例模式
    public static NotificationUtils getInstance() {
        if (notificationUtils == null) {
            synchronized (NotificationUtils.class) {
                if (notificationUtils == null) {
                    //初始化
                    notificationUtils = new NotificationUtils();
                    notificationUtils.manager = (NotificationManager) BaseIotUtils.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationUtils.builder = new Notification.Builder(BaseIotUtils.getContext());
                    //点击关闭按钮时效果
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(BaseIotUtils.getContext(),
                            1, new Intent(ACTION_CLOSE_NOTIFY), PendingIntent.FLAG_UPDATE_CURRENT);
                    //自定义视图
                    notificationUtils.contentView = new RemoteViews(BaseIotUtils.getContext().getPackageName(), R.layout.activity_notification);
                    //设置点击关闭按钮"X"时的操作
                    notificationUtils.contentView.setOnClickPendingIntent(R.id.ivClose, pendingIntent);
                    //设置自定义View
                    notificationUtils.builder.setContent(notificationUtils.contentView);
                    //设置点击通知时的操作
                    notificationUtils.builder.setContentIntent(pendingIntent);
                    //app通知栏图标
                    notificationUtils.builder.setSmallIcon(R.drawable.app_img);  //小图标，在大图标右下角
                    notificationUtils.builder.setLargeIcon(BitmapFactory.decodeResource(BaseIotUtils.getContext().getResources(), R.drawable.app_img)); //大图标，没有设置时小图标就是大图标
                }
            }
        }
        return notificationUtils;
    }

    /**
     * 设置监听notification是否点击关闭的接口
     *
     * @param INotificationLinstener 注册接口
     * @return this
     */
    public NotificationUtils setINotificationLinstener(INotificationLinstener INotificationLinstener) {
        this.mINotificationLinstener = INotificationLinstener;
        return notificationUtils;
    }

    /**
     * 设置设置唯一标识符
     *
     * @param notifyId 设置唯一标识符
     * @return this
     */
    public NotificationUtils setNotifyId(int notifyId) {
        this.notifyId = notifyId;
        return notificationUtils;
    }

    /**
     * 设置APP名称
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * @param appName APP名称
     * @return this
     */
    public void setAppName(String appName) {
        this.appName = appName;
        String appendStr="";
        if (appName != null && !appName.equals("")) {
            appendStr = appName;
        } else {
            appendStr = "无";
        }
        contentView.setTextViewText(R.id.tvAppName, appendStr);
        manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
    }

    /**
     * 设置需要显示备注信息
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * @param remarks 需要显示备注信息
     * @return this
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
        String appendStr="";
        if (remarks != null && !remarks.equals("")) {
            appendStr = "备注:" + remarks;
        } else {
            appendStr = "";
        }
        contentView.setTextViewText(R.id.tvRemarks, appendStr);
        manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
    }

    /**
     * 设置需要提示的消息
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * @param prompt 需要提示的消息
     * @return this
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
        String appendStr="";
        if (prompt != null && !prompt.equals("")) {
            appendStr = "提示:" + prompt;
        } else {
            appendStr = "";
        }
        contentView.setTextViewText(R.id.tvPrompt, appendStr);
        manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
    }


    /**
     * 设置滑动是否删除
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * @param mSlideOff true|false
     * @return this
     */
    public void setSlideOff(boolean mSlideOff) {
        this.mSlideOff = mSlideOff;
        this.builder.setOngoing(!mSlideOff);
        manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
    }

    /**
     * 设置点击是否消失
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * @param mAutoCancel true|false
     * @return this
     */
    public void setmAutoCancel(boolean mAutoCancel) {
        this.mAutoCancel = mAutoCancel;
        builder.setAutoCancel(mAutoCancel);
        manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
    }

    /**
     * 为各个参数赋值
     *
     * @param appName     app名称
     * @param remarks     备注
     * @param prompt      提示
     * @param mSlideOff    滑动是否删除
     * @param mAutoCancel 点击是否消失
     *                    该步骤执行完成之后需要执行下一个方法{@link #exeuNotify()}
     * @return this
     */
    public NotificationUtils setNotificationParm(String appName, String remarks
            , String prompt, boolean mSlideOff, boolean mAutoCancel) {
        this.mSlideOff = mSlideOff;
        this.mAutoCancel = mAutoCancel;
        String appendStr = "";
        if (appName != null && !appName.equals("")) {
            appendStr = appName;
        } else {
            appendStr = "无";
        }
        contentView.setTextViewText(R.id.tvAppName, appendStr);

        if (remarks != null && !remarks.equals("")) {
            appendStr = "备注:" + remarks;
        } else {
            appendStr = "";
        }
        contentView.setTextViewText(R.id.tvRemarks, appendStr);

        if (prompt != null && !prompt.equals("")) {
            appendStr = "提示:" + prompt;
        } else {
            appendStr = "";
        }
        contentView.setTextViewText(R.id.tvPrompt, appendStr);

        return notificationUtils;
    }

    /**
     * 显示notifacation时执行
     * 更改参数时执行
     */
    public void exeuNotify() {
        if (manager != null && builder != null) {
            if (notifyId != -1) {
                KLog.e(TAG, "mSlideOff=" + mSlideOff + ",mAutoCancel=" + mAutoCancel);
                builder.setOngoing(!mSlideOff);//滑动不能清除
                builder.setAutoCancel(mAutoCancel);//点击的时候消失
                manager.notify(notifyId, builder.build());  //参数一为ID，用来区分不同APP的Notification
                if (mINotificationLinstener != null) {
                    mINotificationLinstener.enableStatus(true);
                }
            } else {
                throw new NullPointerException("notifyId ==null:method > setNotifyId(int notifyId)");
            }
        } else {
            throw new NullPointerException("manager or builder ==null");
        }
    }

    /**
     * 调用此方法前，必须首先执行过{@link #exeuNotify()}
     * 外部调用此方法时，请先调用{@link #getInstance()}
     * 关闭消息通知
     */
    public void closeNotify() {
        if (manager != null) {
            if (notifyId != -1) {
                manager.cancel(notifyId);//参数一为ID，用来区分不同APP的Notification
                if (mINotificationLinstener != null) {
                    mINotificationLinstener.enableStatus(false);
                }
            }
        } else {
            throw new NullPointerException("manager or builder ==null");
        }
    }

    //关闭notification的广播
    public static class NotifyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CLOSE_NOTIFY)) {
                //关闭通知
                notificationUtils.closeNotify();
            }
        }
    }
}
