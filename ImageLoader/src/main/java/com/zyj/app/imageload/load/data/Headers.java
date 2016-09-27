package com.zyj.app.imageload.load.data;

import java.util.Collections;
import java.util.Map;

/**
 * Created by ${zyj} on 2016/9/27.
 */

public interface Headers {

    /**
     * An empty Headers object that can be used if users don't want to provide headers.
     *
     * @deprecated Use {@link #DEFAULT} instead.
     */
    @Deprecated
    Headers NONE = new Headers() {
        @Override
        public Map<String, String> getHeaders() {
            return Collections.emptyMap();
        }
    };

    /**
     * A Headers object containing reasonable defaults that should be used when users don't want
     * to provide their own headers.
     */
    Headers DEFAULT = new LazyHeaders.Builder().build();

    Map<String, String> getHeaders();

}
