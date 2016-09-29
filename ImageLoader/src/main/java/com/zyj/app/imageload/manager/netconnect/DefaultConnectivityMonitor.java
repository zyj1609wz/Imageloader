package com.zyj.app.imageload.manager.netconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ${zyj} on 2016/9/28.
 * 网络监听器实现者
 */

public class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private Context context ;
    private boolean isConnected;     //网络是否已经连通
    private boolean isRegistered;   //是否已经注册广播接收器

    private final ConnectivityListener listener;

    public DefaultConnectivityMonitor( Context context , ConnectivityListener listener ){
        //这个地方要注意，注册网络监听广播接收器，要ApplicationContext ，可以有效的防止内存泄漏
        this.context = context.getApplicationContext() ;
        this.listener = listener ;
    }

    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean wasConnected = isConnected;
            isConnected = isConnected(context);
            if (wasConnected != isConnected) {
                //只要当前网络状态和以前的网络状态不一样，就会走下面的接口回调。
                // 这个点值得学习，网络变化不一定会引起回调。但是网络状态变化就会回调。从有网到没网，或者从没网到有网

                listener.onConnectivityChanged(isConnected);
            }
        }
    };


    @Override
    public void onStart() {
        register();
    }

    @Override
    public void onStop() {
        unregister();
    }

    @Override
    public void onDestroy() {
        // Do nothing.
    }

    /**
     * 注册广播接收器
     */
    private void register() {
        if (isRegistered) {
            return;
        }

        isConnected = isConnected(context);
        context.registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        isRegistered = true;
    }

    private void unregister() {
        if (!isRegistered) {
            return;
        }

        context.unregisterReceiver(connectivityReceiver);
        isRegistered = false;
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
