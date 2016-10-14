package com.zyj.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;

import com.zyj.app.imageload.ImageLoad;
import com.zyj.app.imageload.util.LogUtil;
import com.zyj.app.util.Contans;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView ;
    private RecyclerView mrecyclerView ;
    private ImageAdapter mimageAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById( R.id.setting ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( MainActivity.this , SettingActivity.class ));
            }
        }); ;

        imageView = (ImageView) findViewById( R.id.image);
        ImageLoad.get( MainActivity.this ).load(  Contans.imageUrls[0], imageView );

        mrecyclerView = (RecyclerView)findViewById(R.id.recycleview);
        mrecyclerView.setLayoutManager(new GridLayoutManager( MainActivity.this , 2)) ;   //gridView网格布局
        mimageAdapter = new ImageAdapter( MainActivity.this , Contans.imageUrls ) ;
        mrecyclerView.setAdapter( mimageAdapter );

         //Glide.with( this).load( "").skipMemoryCache().into( imageView).;

        LogUtil.d( "uuuuu  " + getDeviceUUID( MainActivity.this ));

    }

    public static String getDeviceUUID(Context context){
        //android6.0权限判断
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        LogUtil.w(uniqueId);
        return uniqueId;
    }

}
