package com.zyj.app.imageload.cache;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.zyj.app.imageload.Key.Key;
import com.zyj.app.imageload.util.LogUtil;
import com.zyj.app.imageload.util.ThreadUtil;

/**
 * Created by ${zyj} on 2016/9/22.
 */

public class MemoryCacheFactory implements MemoryCache {

    private Key mkey ;
    private LruCache<String, Bitmap> mlruCache = null  ;

    public MemoryCacheFactory( Key key ){
        this.mkey = key ;

        //获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();

        //给mlruCache分配最大内存为 把八分之一
        int cacheMemory = maxMemory / 8 ;

        if ( mlruCache == null ){
            mlruCache = new LruCache<String,Bitmap>(cacheMemory){

                //必须重写此方法，来测量Bitmap的大小 .默认返回图片的数量
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }
            } ;
        }

    }

    @Override
    public Bitmap getBitmapFromCache(String key) {
        if (TextUtils.isEmpty( key )){
            LogUtil.d( "LruCache  getBitmapFromLruCache url 为空" ) ;
            return null ;
        }
        return mlruCache.get( mkey.getKey( key ) ) ;
    }

    @Override
    public void setBitmapToCache(String key, Bitmap bitmap) {
        if ( !TextUtils.isEmpty( key ) && bitmap != null ){
            String keyS = mkey.getKey( key ) ;
            if ( mlruCache.get( keyS ) == null ){
                mlruCache.put( keyS , bitmap ) ;
                LogUtil.d( "内存中已经有缓存了" ) ;
            }else {
                LogUtil.d( "内存缓存 成功" ) ;
            }
        }else {
            LogUtil.d( "内存缓存 失败" ) ;
        }
    }

    /**
     * 获取内存缓存大小
     * @return
     */
    @Override
    public int getMemoryCacheSize() {
        return mlruCache.size() ;
    }

    /**
     * 清除内存缓存
     *  在Android UI Thread Calling
     */
    @Override
    public void clearMemoryCache() {
        ThreadUtil.assertMainThread();
        mlruCache.evictAll();
    }
}
