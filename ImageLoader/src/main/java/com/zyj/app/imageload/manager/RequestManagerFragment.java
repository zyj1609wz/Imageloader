package com.zyj.app.imageload.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;

import com.zyj.app.imageload.RequestManager;

import java.util.HashSet;


/**
 * Created by ${zyj} on 2016/9/28.
 */


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestManagerFragment extends Fragment {

    private final ActivityFragmentLifecycle lifecycle;
    private RequestManager requestManager;
    private final HashSet<RequestManagerFragment> childRequestManagerFragments
            = new HashSet<RequestManagerFragment>();

    private RequestManagerFragment rootRequestManagerFragment;

    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    RequestManagerFragment(ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        rootRequestManagerFragment = RequestManagerRetriever.getInstance()
                .getRequestManagerFragment(getActivity().getFragmentManager());
        if (rootRequestManagerFragment != this) {
            rootRequestManagerFragment.addChildRequestManagerFragment(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (rootRequestManagerFragment != null) {
            rootRequestManagerFragment.removeChildRequestManagerFragment(this);
            rootRequestManagerFragment = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }

    @Override
    public void onTrimMemory(int level) {
        // If an activity is re-created, onTrimMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onTrimMemory(level);
        }
    }

    @Override
    public void onLowMemory() {
        // If an activity is re-created, onLowMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onLowMemory();
        }
    }

    private void addChildRequestManagerFragment(RequestManagerFragment child) {
        childRequestManagerFragments.add(child);
    }

    private void removeChildRequestManagerFragment(RequestManagerFragment child) {
        childRequestManagerFragments.remove(child);
    }

}
