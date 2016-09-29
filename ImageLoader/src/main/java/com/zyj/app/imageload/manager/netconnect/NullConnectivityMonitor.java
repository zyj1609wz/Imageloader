package com.zyj.app.imageload.manager.netconnect;

/**
 * Created by ${zyj} on 2016/9/28.
 * 这是一个空的网络监听实现者，没有做任何事情
 * 为什么需要的一个空的网络监听实现者？？？？
 * 因为app的权限问题，网络状态监听是需要用户权限的，如果有权限那么就会启用真正的 DefaultConnectivityMonitor。
 * 如果没有权限，那么就会启用NullConnectivityMonitor
 */

public class NullConnectivityMonitor implements ConnectivityMonitor {
    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
