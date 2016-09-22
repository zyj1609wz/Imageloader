package com.zyj.app.imageload.cache;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by ${zyj} on 2016/9/21.
 */

public class ExternalSDCardCacheDiskCacheFactory extends DiskCacheFactory {
    Context mcontext ;
    String mcacheDirectory ;
    int mcacheSize  ;

    public ExternalSDCardCacheDiskCacheFactory(Context context ){
        this( context ,  null ) ;
    }

    public ExternalSDCardCacheDiskCacheFactory(Context context , String cacheDirectory ){
        this( context , cacheDirectory , 0  ) ;
    }

    public ExternalSDCardCacheDiskCacheFactory(Context context , int cacheSize ){
        this( context ,  null , cacheSize  ) ;
    }

    public ExternalSDCardCacheDiskCacheFactory(Context context , String cacheDirectory , int cacheSize ){
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
        if ( mcontext == null ) return null ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            String sdroot = Environment.getExternalStorageDirectory().getPath()  ;
            return new File( sdroot , mcacheDirectory ) ;
        }
        return new InternalCacheDiskCacheFactory( mcontext , mcacheDirectory ).getCacheDirectory() ;
    }

    @Override
    public int getCacheSize() {
        return mcacheSize ;
    }
}
