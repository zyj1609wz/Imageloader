# Imageloader
一个封装良好的图片加载框架，使用非常简单，

## `如何集成`
Android Studio集成
```
compile 'com.zyj.app:imageloader:1.2.0'

```
获取最新版本号 [Jcenter](http://jcenter.bintray.com/com/zyj/app/imageloader/)
## `使用方法`

### 1、如何加载图片
```
 ImageLoad.get( Context context).load(  String url , ImageView imageView );
 
```

### 2、如何获取缓存大小？
```
long diskCachesize = ImageLoad.get( Context context ).getDiskCacheSize() ;  //获取磁盘缓存大小
long memorycacheSize = ImageLoad.get( Context context ).getMemoryCacheSize() ;  //获取内存缓存大小

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

### 3、如何清除磁盘缓存？

#### 3.1 清除磁盘缓存
```
ImageLoad.get( Context context ).clearDiskCache();
```
注意这个方法是耗时操作，需要在异步中操作。

#### 3.2 清除内存缓存
```
ImageLoad.get( Context context ).clearMemoryCache();
```
注意这个方法需要在Android UI 线程调用

## `更新日志`
### `2016/9/26`

1、1.2.0发布

2、新增获取内存缓存大小的方法

3、新增清除内存缓存的方法

4、重构内存缓存逻辑


### `2016/9/23`
 
 1、修改磁盘缓存逻辑
 
 2、增加获取磁盘缓存大小接口
 
 3、增加清除磁盘缓存接口 
 

### `2016/9/22`
 
   1、完成Imageloader的第一次开发
   
   2、支持图片3级缓存，加载速度更快

