package com.zyj.app.imageload.util;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zyj.app.imageload.bean.ImageSize;

import java.lang.reflect.Field;

/**
 * Created by ${zyj} on 2016/9/28.
 */

public class ImageUtil {

    public static ImageSize getImageViewSize(ImageView imageView ){

        DisplayMetrics displayMetrics  = imageView.getContext().getResources().getDisplayMetrics() ;

        ImageSize imageSize = new ImageSize() ;

        ViewGroup.LayoutParams lp = imageView.getLayoutParams() ;
        int width = imageView.getWidth() ;   //获取imageView的实际宽度
        if ( width <= 0 ){
            width = lp.width ;  //获取imageView在布局文件中申明的宽度
        }
        if ( width <= 0 ){
            //width = imageView.getMaxWidth() ;  //获取最大值
            width = getImageViewFieldValue( imageView , "mMaxWidth" ) ; //通过反射获取imageView的最大宽度,用反射的目的是兼容老版本
        }
        if ( width <= 0 ){
            width = displayMetrics.widthPixels ;  //获取屏幕的宽度
        }

        int height = imageView.getHeight() ;   //获取imageView的实际高度
        if ( height <= 0 ){
            height = lp.height ;  //获取imageView在布局文件中申明的高度
        }
        if ( height <= 0 ){
            //height = imageView.getMaxHeight() ;  //获取最大值
            height = getImageViewFieldValue( imageView , "mMaxHeight" ) ;  //通过反射获取imageView的最大高度,用反射的目的是兼容老版本
        }
        if ( height <= 0 ){
            height = displayMetrics.heightPixels ;  //获取屏幕的高度
        }

        imageSize.width = width ;
        imageSize.height = height ;

        return imageSize  ;
    }


    /**
     * 计算图片的缩放值
     * @param options options.inJustDecodeBounds = true时传入的option
     * @param reqWidth 需要按需压缩的宽度
     * @param reqHeight 需要按需压缩的高度
     * @return 采样率inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            float heightScale =  height * 1.0f / reqHeight ;  //高度的比例
            float widthScale = width * 1.0f / reqWidth ;     //宽度的比例
            float scale = Math.max( heightScale , widthScale ) ;  //得到最大的比例值
            inSampleSize = Math.round( scale ) ;
        }
        return inSampleSize;
    }

    /**
     * 通过放射获取对象的某个属性值
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue( Object object , String fieldName ){
        int value = 0 ;
        try {
            Field field = ImageView.class.getDeclaredField( fieldName ) ;
            field.setAccessible( true ) ;  //在java的反射使用中,如果字段是私有的,那么必须要对这个字段设置
            int fieldValue = field.getInt( object ) ;

            if ( fieldValue > 0 && fieldValue < Integer.MAX_VALUE ){
                value = fieldValue ;
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  value ;
    }










}
