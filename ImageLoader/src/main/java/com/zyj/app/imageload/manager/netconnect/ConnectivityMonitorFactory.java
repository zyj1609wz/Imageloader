package com.zyj.app.imageload.manager.netconnect;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class ConnectivityMonitorFactory {

    public ConnectivityMonitor build(Context context, ConnectivityMonitor.ConnectivityListener listener) {
        // 这一个方法也是检查用户是否拥有相应的权限，如果没有权限就会抛出异常。显然这个方法不适合在这里使用
        // final int res1 = context.enforceCallingOrSelfPermission( String permission, String message );

        //检查用户是否拥有相应的权限，只检查，不会报错
        final int res = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE");

        // 如果调用方拥有相应的权限，则权限验证的返回值为PackageManager. PERMISSION_GRANTED否则返回PackageManager.PERMISSION_DENIED。

        final boolean hasPermission = res == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            return new DefaultConnectivityMonitor(context, listener);
        } else {
            return new NullConnectivityMonitor();
        }
    }

}
