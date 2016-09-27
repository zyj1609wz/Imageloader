package com.zyj.app.imageload.cache;

import android.graphics.Bitmap;

import com.zyj.app.imageload.disklrucache.DiskLruCache;
import com.zyj.app.imageload.load.HttpLoader;
import com.zyj.app.imageload.util.MD5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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

    public static Bitmap getCacheBitmap(  DiskCache diskCache  , String urlString ){
        InputStream inputStream = null ;
        try {
            String key = MD5.stringToMD5( urlString ) ;
            DiskLruCache diskLruCacheS = diskCache.getDiskLruCache() ;
            if ( diskLruCacheS == null ) return null ;

            DiskLruCache.Snapshot snapShot = diskLruCacheS.get(key);
            if (snapShot != null) {
                inputStream = snapShot.getInputStream( 0 ) ;
                return HttpLoader.load( inputStream ) ;
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
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
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
