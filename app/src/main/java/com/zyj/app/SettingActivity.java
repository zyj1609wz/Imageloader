package com.zyj.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
              long size = ImageLoad.get( SettingActivity.this).getCacheSize() ;
                Log.d( "ccccccccc" , "" +  size ) ;

              textView.setText( "缓存为： " + sizeToChange( size ) );
            }
        }) ;

        //清除缓存
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

    }

    String sizeToChange( long size ){
        double G = size / 1024 / 1204 /1024 ;
        if ( G >= 1 ){
            return G + "GB";
        }

        double M = size / 1024 / 1204  ;
        if ( M >= 1 ){
            return M + "MB";
        }

        double K = size  / 1024   ;
        if ( K >= 1 ){
            return K + "KB";
        }

        return size + "Byte" ;
    }
}
