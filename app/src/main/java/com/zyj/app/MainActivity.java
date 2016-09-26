package com.zyj.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zyj.app.util.Contans;

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
        Glide.get( MainActivity.this).clearMemory();
       // ImageLoad.get( MainActivity.this ).load(  Contans.imageUrls[0], imageView );

        mrecyclerView = (RecyclerView)findViewById(R.id.recycleview);
        mrecyclerView.setLayoutManager(new GridLayoutManager( MainActivity.this , 2)) ;   //gridView网格布局
        mimageAdapter = new ImageAdapter( MainActivity.this , Contans.imageUrls ) ;
        mrecyclerView.setAdapter( mimageAdapter );

    }
}
