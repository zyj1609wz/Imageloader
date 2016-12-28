package com.zyj.app.imageload.cache;
import com.zyj.app.imageload.cache.disklrucache.DiskLruCache;
import java.io.File;

/**
 * Created by ${zyj} on 2016/9/21.
 */

public interface DiskCache {

    int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
    String DEFAULT_DISK_CACHE_DIR = "zlide_manager_disk_cache";
    int APP_VERSION = 1;
    int VALUE_COUNT = 1;

    File getCacheDirectory();

    int getCacheSize() ;

    DiskLruCache getDiskLruCache() ;

    void clearCache();

    long getTotalCacheSize() ;

}
