package com.zyj.app.imageload.engin;

import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ${zyj} on 2016/9/23.
 */

public class Decode {

    public static BitmapFactory.Options compressBitmap (InputStream inputStream ){
        BitmapFactory.Options options  = null ;
        try {
            inputStream.mark( inputStream.available());
            options = new BitmapFactory.Options() ;
            options.inJustDecodeBounds = true ;
            options.inSampleSize = 4 ;
            BitmapFactory.decodeStream( inputStream , null , options ) ;
            options.inJustDecodeBounds = false ;
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return options ;
    }

}
