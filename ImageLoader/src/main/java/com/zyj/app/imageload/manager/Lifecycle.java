package com.zyj.app.imageload.manager;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public interface Lifecycle {

    /**
     * Adds the given listener to the set of listeners managed by this Lifecycle implementation.
     */
    void addListener(LifecycleListener listener);

}
