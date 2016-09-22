package com.zyj.app.imageload.cache;

import android.graphics.Bitmap;

/**
 * Created by ${zyj} on 2016/9/22.
 */

public class MemoryCacheFactory implements MemoryCache {

    @Override
    public Bitmap getBitmapFromCache(String key) {
        return null;
    }

    @Override
    public void setBitmapToCache(String key, Bitmap bitmap) {

    }
}
