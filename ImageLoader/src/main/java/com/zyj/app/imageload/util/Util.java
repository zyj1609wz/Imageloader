package com.zyj.app.imageload.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class Util {

    /**
     * Returns a copy of the given list that is safe to iterate over and perform actions that may
     * modify the original list.
     *
     * <p> See #303 and #375. </p>
     */
    public static <T> List<T> getSnapshot(Collection<T> other) {
        // toArray creates a new ArrayList internally and this way we can guarantee entries will not
        // be null. See #322.
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }

}
