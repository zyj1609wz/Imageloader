package com.zyj.app.imageload.cache;
import com.zyj.app.imageload.disklrucache.DiskLruCache;
import com.zyj.app.imageload.util.ThreadUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by ${zyj} on 2016/9/21.
 */

public abstract class DiskCacheFactory implements DiskCache {

    private DiskLruCache diskLruCache ;

    @Override
    public DiskLruCache getDiskLruCache() {
        File cacheDir = getCacheDirectory() ;

        if ( cacheDir == null) {
            return null ;
        }

        boolean check = !cacheDir.mkdirs() && (!cacheDir.exists() || !cacheDir.isDirectory()) ;
        if ( check ) {
            return null ;
        }

        if ( diskLruCache == null || ( diskLruCache != null && diskLruCache.isClosed() )){
            try {
                diskLruCache =  DiskLruCache.open( cacheDir , APP_VERSION , VALUE_COUNT , getCacheSize() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return diskLruCache ;
    }

    @Override
    public void clearCache() {
        ThreadUtil.assertBackgroundThread();

        if ( getDiskLruCache() != null ){
            try {
                getDiskLruCache().delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   @Override
    public long getTotalCacheSize() {
        if ( getDiskLruCache() != null ){
            return getDiskLruCache().size() ;
        }
        return 0;
    }
}
