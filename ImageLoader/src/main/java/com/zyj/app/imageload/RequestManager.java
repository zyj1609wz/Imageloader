package com.zyj.app.imageload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.zyj.app.imageload.manager.netconnect.ConnectivityMonitor;
import com.zyj.app.imageload.manager.netconnect.ConnectivityMonitorFactory;
import com.zyj.app.imageload.manager.Lifecycle;
import com.zyj.app.imageload.manager.LifecycleListener;
import com.zyj.app.imageload.util.ThreadUtil;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class RequestManager implements LifecycleListener {
    private final Context context;
    private final ImageLoad imageLoad ;
    private final Lifecycle lifecycle;

    public RequestManager(Context context , final Lifecycle lifecycle  ){
        this( context , lifecycle , new ConnectivityMonitorFactory() ) ;

    }

    public RequestManager(Context context , final Lifecycle lifecycle , ConnectivityMonitorFactory factory  ){
        this.context = context ;
        imageLoad = ImageLoad.get( context ) ;
        this.lifecycle = lifecycle ;

        //如果当前线程在子线程，那么立刻切换到主线程
        if (ThreadUtil.isOnBackgroundThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    lifecycle.addListener(RequestManager.this);
                }
            });
        } else {
            lifecycle.addListener(this);
        }

        ConnectivityMonitor connectivityMonitor = factory.build( context , new Connect() ) ;

        //增加一个生命周期加上网络监听者
        lifecycle.addListener(connectivityMonitor);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * @see android.content.ComponentCallbacks2#onTrimMemory(int)
     * 在内存资源不足的时候  清除内存一部分缓存
     */
    public void onTrimMemory(int level) {
        // imageLoad.trimMemory(level);
    }

    /**
     * @see android.content.ComponentCallbacks2#onLowMemory()
     * 在内存资源不足的时候  清除内存全部缓存
     */
    public void onLowMemory() {
        imageLoad.clearMemoryCache();
    }

    class Connect implements ConnectivityMonitor.ConnectivityListener {

        @Override
        public void onConnectivityChanged(boolean isConnected) {
            Toast.makeText( context , "网络发生变化了" , Toast.LENGTH_SHORT).show();
        }
    }

}
