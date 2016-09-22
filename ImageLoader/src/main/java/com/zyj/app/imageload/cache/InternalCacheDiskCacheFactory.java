package com.zyj.app.imageload.cache;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by ${zyj} on 2016/9/21.
 */

public class InternalCacheDiskCacheFactory extends DiskCacheFactory  {

    Context mcontext ;
    String mcacheDirectory ;
    int mcacheSize  ;

    public InternalCacheDiskCacheFactory( Context context ){
        this( context ,  null ) ;
    }

    public InternalCacheDiskCacheFactory( Context context , String cacheDirectory ){
        this( context , cacheDirectory , 0  ) ;
    }

    public InternalCacheDiskCacheFactory( Context context ,  int cacheSize ){
        this( context ,  null , cacheSize  ) ;
    }

    public InternalCacheDiskCacheFactory( Context context , String cacheDirectory , int cacheSize ){
        this.mcontext = context ;

        if (TextUtils.isEmpty( cacheDirectory )){
            this.mcacheDirectory = DEFAULT_DISK_CACHE_DIR ;
        }else {
            this.mcacheDirectory = cacheDirectory ;
        }

        if ( cacheSize <=1 ){
            this.mcacheSize = DEFAULT_DISK_CACHE_SIZE ;
        }else {
            this.mcacheSize = cacheSize ;
        }
    }

    @Override
    public File getCacheDirectory() {
        return new File( mcontext.getCacheDir().getPath() , mcacheDirectory ) ;
    }

    @Override
    public int getCacheSize() {
        return mcacheSize ;
    }
}
