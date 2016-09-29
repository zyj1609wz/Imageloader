package com.zyj.app.imageload.manager;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class ApplicationLifecycle implements Lifecycle {

    @Override
    public void addListener(LifecycleListener listener) {
        listener.onStart();
    }
}
