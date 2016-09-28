package com.zyj.app.imageload.load;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zyj.app.imageload.bean.ImageSize;
import com.zyj.app.imageload.load.data.HttpUrlFetcher;
import com.zyj.app.imageload.load.data.ZUrl;
import com.zyj.app.imageload.util.ImageUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ${zyj} on 2016/9/27.
 */

public class HttpLoader {

    private static final int MARK_POSITION = 5 * 1024 * 1024;

    private HttpLoader(){

    }

    public static Bitmap load( String url , ImageSize imageSize  ){
        HttpUrlFetcher httpUrlFetcher = null ;
        try {
            httpUrlFetcher = new HttpUrlFetcher( new ZUrl( url )) ;
            return loadBitmap( httpUrlFetcher.loadData() , imageSize ) ;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if ( httpUrlFetcher != null ){
                httpUrlFetcher.cleanup();
            }
        }
        return null ;
    }

    public static Bitmap load( InputStream inputStream  , ImageSize imageSize ){
        return loadBitmap( inputStream , imageSize ) ;
    }

    private static Bitmap loadBitmap(InputStream inputStream , ImageSize imageSize ){
        BufferedInputStream bufferedInputStream = new BufferedInputStream( inputStream ) ;
        try {
            if ( bufferedInputStream.available() <= 1){
                bufferedInputStream.mark( MARK_POSITION );
            }else {
                bufferedInputStream.mark( bufferedInputStream.available() );
            }

            BitmapFactory.Options options = new BitmapFactory.Options() ;
            options.inJustDecodeBounds = true ;
            BitmapFactory.decodeStream( bufferedInputStream , null , options ) ;

            options.inSampleSize = ImageUtil.calculateInSampleSize( options , imageSize.width , imageSize.height  );

            options.inPreferredConfig = Bitmap.Config.RGB_565 ;
            options.inJustDecodeBounds = false ;

            bufferedInputStream.reset();
            return  BitmapFactory.decodeStream( bufferedInputStream , null , options ) ;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if ( bufferedInputStream != null ){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null ;
    }

}
