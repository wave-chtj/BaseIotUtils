package com.wave_chtj.example.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.face_chtj.base_iotutils.KLog;
import com.wave_chtj.example.download.DownLoadAty;
import com.wave_chtj.example.socket.SocketAty;

/**
 * Create on 2020/6/4
 * author chtj
 * desc
 */
public class BaseIotReceiver extends BroadcastReceiver {
    private static final String TAG="BaseIotReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        //开机启动
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            KLog.d(TAG, "onReceive: ACTION_BOOT_COMPLETED");
            //context.startActivity(new Intent(context, SocketAty.class));
            //context.startActivity(new Intent(context, DownLoadAty.class));
        }
    }
}
