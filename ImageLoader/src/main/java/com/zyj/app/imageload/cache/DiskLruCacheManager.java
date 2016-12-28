package com.zyj.app.imageload.cache;

import android.graphics.Bitmap;

import com.zyj.app.imageload.bean.ImageSize;
import com.zyj.app.imageload.cache.disklrucache.DiskLruCache;
import com.zyj.app.imageload.load.HttpLoader;
import com.zyj.app.imageload.util.MD5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/9/17.
 */
public class DiskLruCacheManager {

    public static void setCacheBitmap( DiskCache diskCache , String urlString ){
        try {
            String key = MD5.stringToMD5( urlString ) ;
            DiskLruCache diskLruCacheS = diskCache.getDiskLruCache() ;
            if ( diskLruCacheS == null ) return;
            DiskLruCache.Editor editor = diskLruCacheS.edit(key);
            if ( editor == null ) return;
            if ( downloadUrlToStream( urlString , editor.newOutputStream( 0 ))){
                editor.commit();
            }else {
                editor.abort();
            }

        }catch ( Exception e){

        }
    }

    public static void setCacheBitmap( DiskCache diskCache , String urlString , Bitmap bitmap ){
        try {
            String key = MD5.stringToMD5( urlString ) ;
            DiskLruCache diskLruCacheS = diskCache.getDiskLruCache() ;
            if ( diskLruCacheS == null ) return;
            DiskLruCache.Editor editor = diskLruCacheS.edit(key);
            if ( editor == null ) return;
            if ( download( getInputStreamFromBitmap( bitmap )  , editor.newOutputStream( 0 ))){
                editor.commit();
            }else {
                editor.abort();
            }
        }catch ( Exception e){
        }
    }

    public static InputStream getInputStreamFromBitmap( Bitmap bitmap ){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 , baos );  //100 是压缩率，表示压缩0%; 如果不压缩是100，表示压缩率为0
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        return inputStream ;
    }

    public static Bitmap getCacheBitmap(DiskCache diskCache  , String urlString , ImageSize imageSize ){
        InputStream inputStream = null ;
        try {
            String key = MD5.stringToMD5( urlString ) ;
            DiskLruCache diskLruCacheS = diskCache.getDiskLruCache() ;
            if ( diskLruCacheS == null ) return null ;

            DiskLruCache.Snapshot snapShot = diskLruCacheS.get(key);
            if (snapShot != null) {
                inputStream = snapShot.getInputStream( 0 ) ;
                return HttpLoader.load( inputStream , imageSize  ) ;
            }
        } catch (IOException e) {
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
        return null ;
    }

    private static boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            if ( download(urlConnection.getInputStream() , outputStream )){
                return true ;
            }
            return false ;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null  ) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean download(InputStream inputStream , OutputStream outputStream) {
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream( inputStream , 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
