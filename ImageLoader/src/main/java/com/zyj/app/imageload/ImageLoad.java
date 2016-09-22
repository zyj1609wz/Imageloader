package com.zyj.app.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.zyj.app.imageload.bean.ImageHolder;
import com.zyj.app.imageload.cache.DiskCacheFactory;
import com.zyj.app.imageload.cache.DiskLruCacheManager;
import com.zyj.app.imageload.cache.ExternalSDCardCacheDiskCacheFactory;
import com.zyj.app.imageload.util.MD5;
import com.zyj.app.imageload.util.MyTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ImageLoad {

    private static volatile ImageLoad instance ;
    private LruCache<String, Bitmap> mlruCache = null  ;
    private ExecutorService mexecutors = null ;
    private int mThreadCount = 10 ;
    private DiskCacheFactory diskCacheFactory ;
    private ImageLoad(Context context ){

        //获取我们应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8 ;

        mlruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        } ;

        //创建线程池
        mexecutors  = Executors.newFixedThreadPool( mThreadCount ) ;

        if ( diskCacheFactory == null ){
           // diskCacheFactory = new ExternalCacheDiskCacheFactory( context ) ;
            diskCacheFactory = new ExternalSDCardCacheDiskCacheFactory( context ) ;
        }
    }

    public static ImageLoad get(Context context) {
        if ( instance == null) {
            synchronized (ImageLoad.class) {
                if (instance == null) {
                    instance = new ImageLoad( context ) ;
                }
            }
        }
        return instance ;
    }


    public void load( Context context , String urlString , ImageView imageView ){
        if ( context == null ) return;
        if ( urlString == null ) return;
        if ( imageView == null ) return;
        Log.d("image" , "url--  " + urlString ) ;
        imageView.setTag( urlString );

        //首先从内存中获取图片
        Bitmap bitmap = getBitmapFromLruCache( urlString ) ;
        if ( bitmap != null  ){
            ImageHolder imageHolder = new ImageHolder() ;
            imageHolder.imageView = imageView ;
            imageHolder.bitmap = bitmap ;
            imageHolder.urlString = urlString ;
            setBitmapToImage( imageHolder );
            Log.d( "image" , "从内存中获取的bitmap 不为空" ) ;
        }else {
            //从磁盘获取图片
            Log.d( "image" , "从内存中获取的bitmap 为空" ) ;
            getBitmapFromDisk( context , urlString , imageView );
        }
    }

    public void setBitmapToImage( ImageHolder imageHolder ){
        if ( imageHolder.imageView.getTag().toString().equals( imageHolder.urlString )){
            imageHolder.imageView.setImageBitmap( imageHolder.bitmap );
        }
    }


    private void getBitmapFromDisk(final Context context , final String urlString , final ImageView imageView ){
        new MyTask<>().setTaskListener(new MyTask.TaskListener() {
            @Override
            public void start() {

            }

            @Override
            public void update(Object o) {

            }

            @Override
            public Object doInBackground(Object o) {
                return  DiskLruCacheManager.getCacheBitmap( diskCacheFactory ,  urlString ) ;
            }

            @Override
            public void result(Object o) {
                Bitmap bitmap = (Bitmap) o;
                setBitmapToLruCache( urlString , bitmap );

                if ( bitmap != null ){
                    ImageHolder imageHolder = new ImageHolder() ;
                    imageHolder.imageView = imageView ;
                    imageHolder.bitmap = bitmap ;
                    imageHolder.urlString = urlString ;
                    setBitmapToImage( imageHolder );
                    Log.d( "image" , "从磁盘获取的bitmap 不为空" ) ;
                }else {
                    //从网络中获取
                    Log.d( "image" , "从磁盘获取的bitmap 为空" ) ;
                    getBitmapFromNet(  context , urlString , imageView );
                }

            }
        }).executeOnExecutor( mexecutors , "") ;
    }

    private void getBitmapFromNet(final Context context , final String urlString , final ImageView imageView ){
        new MyTask<>().setTaskListener(new MyTask.TaskListener<String,Integer,Bitmap>() {
            @Override
            public void start() {

            }

            @Override
            public void update(Integer integer) {

            }

            @Override
            public Bitmap doInBackground(String netUrl) {
                Bitmap bitmap = downLoadBitmapFromNet( netUrl );

                //把缓存写入磁盘
                setBitmapToDiskCache( context , urlString );

                return bitmap ;
            }

            @Override
            public void result(Bitmap bitmap) {
                if ( bitmap != null ){
                    //把缓存写入内存
                    setBitmapToLruCache( urlString , bitmap );

                    ImageHolder imageHolder = new ImageHolder() ;
                    imageHolder.imageView = imageView ;
                    imageHolder.bitmap = bitmap ;
                    imageHolder.urlString = urlString ;
                    setBitmapToImage( imageHolder );
                    Log.d( "image" , "从网络获取的bitmap 成功" ) ;
                }else {
                    Log.d( "image" , "从网络获取的bitmap 为空" ) ;
                }
            }
        }).executeOnExecutor( mexecutors  , urlString ) ;

    }


    private void setBitmapToLruCache(String url , Bitmap bitmap ){
        if ( !TextUtils.isEmpty( url ) && bitmap != null ){
            String urlMD5 = MD5.stringToMD5( url ) ;
            if ( mlruCache.get( urlMD5 ) == null ){
                mlruCache.put( urlMD5 , bitmap ) ;
            }
            Log.d( "image" , "LruCache 存入成功 成功" ) ;
        }else {
            Log.d( "image" , "LruCache 存入失败 失败" ) ;
        }
    }

    private Bitmap getBitmapFromLruCache(String url){
        if (TextUtils.isEmpty( url )){
            Log.d( "image" , "LruCache  getBitmapFromLruCache url 为空" ) ;
            return null ;
        }
        return mlruCache.get( MD5.stringToMD5( url ) ) ;
    }


    //从网络下载bitmap
    private Bitmap downLoadBitmapFromNet( String urlString ){
        InputStream inputStream = null ;
        Bitmap bitmap ;
        try {
            URL url = new URL( urlString ) ;
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream( httpURLConnection.getInputStream() ) ;
            inputStream.mark( inputStream.available());

            BitmapFactory.Options options = new BitmapFactory.Options() ;
            options.inJustDecodeBounds = true ;
            options.inSampleSize = 4 ;
            BitmapFactory.decodeStream( inputStream , null , options ) ;
            options.inJustDecodeBounds = false ;
            inputStream.reset();
            bitmap = BitmapFactory.decodeStream( inputStream , null , options ) ;

            httpURLConnection.disconnect();
            return  bitmap ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if ( inputStream != null ){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  null ;
    }

    private void setBitmapToDiskCache(final Context context , final String urlString ){
        new MyTask<>().setTaskListener(new MyTask.TaskListener() {
            @Override
            public void start() {

            }

            @Override
            public void update(Object o) {

            }

            @Override
            public Object doInBackground(Object o) {
                DiskLruCacheManager.setCacheBitmap( diskCacheFactory , urlString );
                return null;
            }

            @Override
            public void result(Object o) {

            }
        }).executeOnExecutor( mexecutors  , "") ;
    }

    public Bitmap compressBitmap(){
        BitmapFactory.Options options = new BitmapFactory.Options() ;
        //如果我们把它设为true，那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，
        // 它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。
        options.inJustDecodeBounds = true ;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;   //设置图片的宽高都为原来的1/4 , 那么整个bitmap为原来 1/16
        return null ;
    }

    public static BitmapFactory.Options getCompressOptions(){
        BitmapFactory.Options options = new BitmapFactory.Options() ;
        //如果我们把它设为true，那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，
        // 它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。
        options.inJustDecodeBounds = true ;
        //   options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;   //设置图片的宽高都为原来的1/4 , 那么整个bitmap为原来 1/16

        return options ;
    }

}
