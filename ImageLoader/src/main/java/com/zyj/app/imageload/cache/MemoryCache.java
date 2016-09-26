package com.zyj.app.imageload.cache;

import android.graphics.Bitmap;

/**
 * Created by ${zyj} on 2016/9/22.
 */

public interface MemoryCache {

    Bitmap getBitmapFromCache(String key) ;
    void setBitmapToCache( String key , Bitmap bitmap) ;

    int getMemoryCacheSize();
    void clearMemoryCache() ;
}
