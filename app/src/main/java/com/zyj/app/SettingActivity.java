package com.zyj.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zyj.app.imageload.ImageLoad;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final TextView textView = (TextView) findViewById( R.id.textView1 );

        //获取缓存大小
        findViewById( R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long diskCacheSize = ImageLoad.get( SettingActivity.this).getDiskCacheSize() ;
                long memorycacheSize = ImageLoad.get( SettingActivity.this).getMemoryCacheSize() ;

                textView.setText( "磁盘缓存为： " + sizeToChange( diskCacheSize ) + "  内存缓存为：" + sizeToChange( memorycacheSize )  );
            }
        }) ;

        //清除磁盘缓存
        findViewById( R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoad.get( SettingActivity.this).clearDiskCache();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        }) ;

        /**
         * 清除内存缓存
         */
        findViewById( R.id.clearMoneryCache).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoad.get( SettingActivity.this).clearMemoryCache();
                Toast.makeText(SettingActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
            }
        }) ;

    }

    private String sizeToChange( long size ){
        java.text.DecimalFormat df   =new   java.text.DecimalFormat("#.00");  //字符格式化，为保留小数做准备

        double G = size * 1.0 / 1024 / 1204 /1024 ;
        if ( G >= 1 ){
            return df.format( G ) + "GB";
        }

        double M = size * 1.0 / 1024 / 1204  ;
        if ( M >= 1 ){
            return df.format( M ) + "MB";
        }

        double K = size  * 1.0 / 1024   ;
        if ( K >= 1 ){
            return df.format( K ) + "KB";
        }

        return size + "Byte" ;
    }
}
