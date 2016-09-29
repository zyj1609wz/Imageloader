package com.zyj.app.imageload.manager.netconnect;

import com.zyj.app.imageload.manager.LifecycleListener;

/**
 * Created by ${zyj} on 2016/9/28.
 * 网络连接监听器
 */

public interface ConnectivityMonitor extends LifecycleListener {


    /**
     * An interface for listening to network connectivity events picked up by the monitor.
     */
    interface ConnectivityListener {
        /**
         * Called when the connectivity state changes.
         *是否已经联网
         * @param isConnected True if we're currently connected to a network, false otherwise.
         */
        void onConnectivityChanged(boolean isConnected);
    }




}
