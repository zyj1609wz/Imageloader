package com.zyj.app.imageload.cache;
import com.zyj.app.imageload.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by ${zyj} on 2016/9/21.
 */

public abstract class DiskCacheFactory implements DiskCache {

    private DiskLruCache diskLruCache ;

    @Override
    public DiskLruCache getDiskLruCache() {
        File cache = getCacheDirectory() ;
        if ( !cache.exists() ){
            cache.mkdir() ;
        }

        if ( diskLruCache == null ){
            try {
                diskLruCache =  DiskLruCache.open( cache , APP_VERSION , VALUE_COUNT , getCacheSize() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return diskLruCache ;
    }

}
