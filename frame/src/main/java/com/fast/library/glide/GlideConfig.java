package com.fast.library.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.fast.library.utils.SDCardUtils;
import com.fast.library.utils.StringUtils;
import java.io.File;

/**
 * 说明：Glide配置
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/2/22 15:13
 * <p/>
 * 版本：verson 1.0
 */
public abstract class GlideConfig implements GlideModule {

    /**
     * 说明：存放缓存位置，0存放内部，1存放SD卡内部，2存放SD卡中（不删除）
     */
    private int currentTYPE = TYPE_INNER;

    /**
     * 说明：存放缓存位置，0存放内部
     */
    public static final int TYPE_INNER = 0;

    /**
     * 说明：存放缓存位置，1存放SD卡内部
     */
    public static final int TYPE_EXTERNAL_INNER = 1;

    /**
     * 说明：存放缓存位置，2存放SD卡中（不删除）
     */
    public static final int TYPE_EXTERNAL_STORAGE = 2;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 设置图片格式
        if (isRGB8888()){
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        }
        DiskCache.Factory factory;
        String cachePath = "";
        if (StringUtils.isEmpty(setDiskCacheName())){
            currentTYPE = TYPE_INNER;
        }else {
            currentTYPE = setDiskCacheType();
            cachePath = setDiskCacheName();
        }
        if (SDCardUtils.isAvailable()){
            switch (currentTYPE){
                case 1:
                    factory = getExternalCacheFactory(context,cachePath);
                    break;
                case 2:
                    factory = getExternalStorageFactory(cachePath);
                    break;
                case 0:
                default:
                    factory = getInnerCacheFactory(context,cachePath);
                    break;
            }
        }else {
            factory = getInnerCacheFactory(context,cachePath);
        }
        // 设置缓存目录
        builder.setDiskCache(factory);
    }

    /**
     * 说明：获得内部缓存
     * @param context
     * @param path
     * @return
     */
    private DiskCache.Factory getInnerCacheFactory(Context context,String path){
        return new InternalCacheDiskCacheFactory(
                context,
                path,
                DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    private DiskCache.Factory getExternalCacheFactory(Context context,String path){
        return new ExternalCacheDiskCacheFactory(context,path,DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    private DiskCache.Factory getExternalStorageFactory(String path){
        return new ExternalFactory(path,DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    class ExternalFactory extends DiskLruCacheFactory {
        public ExternalFactory(final String diskCacheName, int diskCacheSize) {
            super(new CacheDirectoryGetter() {
                @Override
                public File getCacheDirectory() {
                    File cacheDirectory = Environment.getExternalStorageDirectory();
                    if (cacheDirectory == null) {
                        return null;
                    }
                    if (diskCacheName != null) {
                        return new File(cacheDirectory, diskCacheName);
                    }
                    return cacheDirectory;
                }
            }, diskCacheSize);
        }
    }

    /**
     * 说明：设置缓存路径类型
     * @return
     */
    public abstract int setDiskCacheType();

    /**
     * 说明：设置缓存路径
     * @return
     */
    public abstract String setDiskCacheName();

    /**
     * 说明：设置保存图片格式为RGB8888
     * @return
     */
    public boolean isRGB8888(){
        return false;
    }

}

