package com.chtj.keepalive.network;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.chtj.keepalive.FBaseTools;

/**
 * wifi工具类
 */
public class FWifiTools {
    /**
     * 开启以太网
     */
    public static void openWifi() {
        WifiManager wifiManager = (WifiManager) FBaseTools.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 24) {
            wifiManager.setWifiEnabled(true);
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭以太网
     */
    public static void closeWifi() {
        WifiManager wifiManager = (WifiManager) FBaseTools.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 24) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(false);
        }
    }

}
