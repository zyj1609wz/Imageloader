# Imageloader  
 
一个封装良好的图片加载框架，使用非常简单

## 效果图
![](gif/GIF.gif) 

## `如何集成`
Android Studio集成
```
compile 'com.zyj.app:imageloader:1.7.0'

```
获取最新版本号 [Jcenter](http://jcenter.bintray.com/com/zyj/app/imageloader/)
## `使用方法`

### 1、如何加载图片
```
//加载一个图片
ImageLoad.get( Context context).load(  String url , ImageView imageView );
 
//加载一个图片，在加载之前设置一个默认图片
ImageLoad.get( Context context ).load( String url , ImageView imageView , int drawable );
 
```

### 2、如何获取缓存大小？
```
long diskCachesize = ImageLoad.get( Context context ).getDiskCacheSize() ;  //获取磁盘缓存大小
long memorycacheSize = ImageLoad.get( Context context ).getMemoryCacheSize() ;  //获取内存缓存大小

```

返回值是缓存大小字节数，提供一个方法进行大小转化
```
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

### 4、如何开启/关闭日志?
```
 //true：开启日志  false:关闭日志
 ImageLoad.get( Context context ).setLogEnable( Boolean log );
```


## `更新日志`
### `2016/10/19`
1、1.7.0发布

2、新增Log开启，关闭API


#### `2016/10/18`
1、1.6.0发布

2、修改图片磁盘缓存逻辑


#### `2016/10/9`
1、1.5.0发布

2、修改缓存大小计算方法，缓存保留两位小数

3、重构一些代码


#### `2016/9/28`
1、1.4.0发布

2、修改图片压缩方案，有效解决内存溢出的问题

3、增加预加载图片的方法。


#### `2016/9/27`
1、1.3.0发布

2、重构http下载模块

3、解决图片url连接可能出现的重定向问题

4、优化图片压缩方案

5、优化内存占用问题


#### `2016/9/26`

1、1.2.0发布

2、新增获取内存缓存大小的方法

3、新增清除内存缓存的方法

4、重构内存缓存逻辑


#### `2016/9/23`
 
1、1.1.0发布
 
2、修改磁盘缓存逻辑
 
3、增加获取磁盘缓存大小接口
 
4、增加清除磁盘缓存接口 
 

#### `2016/9/22`

1、1.0.0发布
    
2、完成Imageloader的第一次开发
   
3、支持图片3级缓存，加载速度更快

