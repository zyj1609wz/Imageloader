package com.zyj.app.imageload;

import com.zyj.app.imageload.cache.DiskCacheFactory;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class RequestBuild {

    private boolean skipMemoryCache = false ;
    private DiskCacheFactory diskCacheFactory = null ;

     public RequestBuild skipMemoryCache( boolean b ){
         this.skipMemoryCache = b ;
         return this ;
     }

    public RequestBuild setDiskCacheFactory( DiskCacheFactory diskCacheFactory ){
        this.diskCacheFactory = diskCacheFactory ;
        return this ;
    }

}
