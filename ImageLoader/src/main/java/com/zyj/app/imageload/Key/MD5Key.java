package com.zyj.app.imageload.Key;

import android.text.TextUtils;

import com.zyj.app.imageload.util.MD5;

/**
 * Created by ${zyj} on 2016/9/23.
 */

public class MD5Key implements Key {

    @Override
    public String getKey(String url) {
        if (TextUtils.isEmpty( url )){
            return null ;
        }
        return MD5.stringToMD5( url );
    }
}
