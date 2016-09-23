# Imageloader
一个封装良好的图片加载框架，使用非常简单，

## `如何集成`
Android Studio集成
```
compile 'com.zyj.app:imageloader:1.1.0'

```

## `使用方法`

### 1、如何加载图片
```
 ImageLoad.get( Context context).load(  String url , ImageView imageView );
 
```

### 2、如何获取缓存大小？
```
long size = ImageLoad.get( Context context ).getCacheSize() ;

```

返回值是缓存大小字节数，提供一个方法进行大小转化
```
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
```

### 3、如何清除缓存？
```
ImageLoad.get( SettingActivity.this).clearDiskCache();
```
注意这个方法是耗时操作，需要在异步中操作。


## `更新日志`
 `2016/9/23`
 
 1、修改磁盘缓存逻辑
 
 2、增加获取磁盘缓存接口
 
 3、增加清除磁盘缓存接口 

 `2016/9/22`
 
   1、完成Imageloader的第一次开发
   
   2、支持图片3级缓存，加载速度更快

