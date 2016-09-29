package com.zyj.app.imageload.manager;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.zyj.app.imageload.RequestManager;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by ${zyj} on 2016/9/29.
 */

public class RequestManagerRetriever implements Handler.Callback  {

    /** The singleton instance of RequestManagerRetriever. */
    private static final RequestManagerRetriever INSTANCE = new RequestManagerRetriever();

    private volatile RequestManager requestManager ;

    static final String FRAGMENT_TAG = "com.bumptech.glide.manager";

    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;

    // Visible for testing.
    /** Pending adds for RequestManagerFragments. */
    final Map<FragmentManager, RequestManagerFragment> pendingRequestManagerFragments =
            new HashMap<FragmentManager, RequestManagerFragment>();

    // Visible for testing.
    /** Pending adds for SupportRequestManagerFragments. */
   /* final Map<FragmentManager, SupportRequestManagerFragment> pendingSupportRequestManagerFragments =
            new HashMap<FragmentManager, SupportRequestManagerFragment>();*/

    /** Main thread handler to handle cleaning up pending fragment maps. */
    private final Handler handler;


    // Visible for testing.
    RequestManagerRetriever() {
        //这个handler是在Android 主线程中创建的
        handler = new Handler(Looper.getMainLooper(), this /* Callback */);
    }

    /**
     * Retrieves and returns the RequestManagerRetriever singleton.
     */
    public static RequestManagerRetriever getInstance() {
        return INSTANCE;
    }

    private RequestManager getApplicationManager(Context context) {
        // Either an application context or we're on a background thread.
        if (requestManager == null) {
            synchronized (this) {
                if (requestManager == null) {
                    // Normally pause/resume is taken care of by the fragment we add to the fragment or activity.
                    // However, in this case since the manager attached to the application will not receive lifecycle
                    // events, we must force the manager to start resumed using ApplicationLifecycle.
                    requestManager = new RequestManager( context.getApplicationContext(),
                            new ApplicationLifecycle() );
                }
            }
        }
        return requestManager ;
    }

    /**
     * 通过用户的界面的 FragmentManager 来查找和这个 FragmentManager 匹配的 Fragment
     * @param fm
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    RequestManagerFragment getRequestManagerFragment(final android.app.FragmentManager fm) {
        //通过 Tag值获取fragment
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            //首先从集合中获取
            current = pendingRequestManagerFragments.get(fm);
            if (current == null) {
                current = new RequestManagerFragment();
                pendingRequestManagerFragments.put(fm, current);
                // FragmentTransaction 里面有commit() 和 commitAllowingStateLoss() 两个方法把fragment添加到activity中
               // android里提供了另外一种形式的提交方式commitAllowingStateLoss()，从名字上就能看出，这种提交是允许状态值丢失的
                fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER, fm).sendToTarget();
            }
        }
        return current;
    }


    @Override
    public boolean handleMessage(Message message) {
        boolean handled = true;  //如果返回true 则拦截消息
        Object removed = null;
        Object key = null;
        switch (message.what) {
            case ID_REMOVE_FRAGMENT_MANAGER:
                android.app.FragmentManager fm = (android.app.FragmentManager) message.obj;
                key = fm;
                removed = pendingRequestManagerFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) message.obj;
                key = supportFm;
            //    removed = pendingSupportRequestManagerFragments.remove(supportFm);
                break;
            default:
                handled = false;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }
}
