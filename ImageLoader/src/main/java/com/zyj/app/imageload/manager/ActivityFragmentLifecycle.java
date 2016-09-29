package com.zyj.app.imageload.manager;

import com.zyj.app.imageload.util.Util;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class ActivityFragmentLifecycle implements Lifecycle {

    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());

    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(LifecycleListener listener) {
          lifecycleListeners.add( listener ) ;

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    void onStart() {
        isStarted = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : Util.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
    }
}
