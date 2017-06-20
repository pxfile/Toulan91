package com.fast.library.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fast.library.handler.UIHandler;
import com.fast.library.utils.FileUtils;
import com.fast.library.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * 说明：Glide工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/2/22 14:44
 * <p/>
 * 版本：verson 1.0
 */
public class GlideLoader {

    //全局占位图
    private static int sGloblePlaceHolder,sGlobleErrorHolder;

    private GlideLoader(Context context){
        if (context == null){
            throw new NullPointerException("context is null");
        }
    }

    /////////////////////////////////////////////////////

    /**
     * 说明：设置全局占位图，错误图
     * @param placeHolder
     * @param errorHolder
     */
    public static void setGlobleHolder(int placeHolder,int errorHolder){
        sGloblePlaceHolder = placeHolder;
        sGlobleErrorHolder = errorHolder;
    }

    /////////////////////////////////////////////////////

    /**
     * 说明：url->bitmap
     * @param context
     * @param url
     * @param listener
     */
    public static void bitmap(Context context,final String url, final BitmapListener listener){
        if (TextUtils.isEmpty(url) || listener == null){
            return;
        }
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                UIHandler.async(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(resource);
                    }
                });
            }

            @Override
            public void onLoadFailed(final Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                UIHandler.async(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFail(e);
                    }
                });
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                UIHandler.async(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart();
                    }
                });
            }
        };
        Glide.with(context).load(url)
                .asBitmap()
                .into(target);
    }

    /**
     * 说明：url->bitmap
     * @param context
     * @param url
     * @param width
     * @param height
     * @param listener
     */
    public static void bitmap(final Context context,final String url, final int width, final int height,final BitmapListener listener){
        if (TextUtils.isEmpty(url) || listener == null){
            return;
        }
        UIHandler.subThread(new Runnable() {
            @Override
            public void run() {
                try {
                    UIHandler.sync(new Runnable() {
                        @Override
                        public void run() {
                            listener.onStart();
                        }
                    });
                    final Bitmap bitmap = Glide.with(context)
                            .load(url)
                            .asBitmap()
                            .into(width, height)
                            .get();
                    UIHandler.sync(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(bitmap);
                        }
                    });
                } catch (final InterruptedException e) {
                    UIHandler.sync(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(e);
                        }
                    });
                    listener.onFail(e);
                } catch (final ExecutionException e) {
                    UIHandler.sync(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(e);
                        }
                    });
                }
            }
        });
    }

    /**
     * 说明：获取request
     * @param context
     * @param resId
     * @return
     */
    public static DrawableTypeRequest load(Context context,int resId){
        return load(context,resId,0,0);
    }

    /**
     * 说明：获取request
     * @param context
     * @param url
     * @return
     */
    public static DrawableTypeRequest load(Context context,String url){
        return load(context,url,0,0);
    }

    /**
     * 说明：获取request
     * @param context
     * @param uri
     * @return
     */
    public static DrawableTypeRequest load(Context context,Uri uri){
        return load(context,uri,0,0);
    }

    /**
     * 说明：获取request
     * @param context
     * @param url
     * @param placeHolder [-1:使用默认，0不使用，>0替换默认]
     * @param errorHolder
     * @return
     */
    public static DrawableTypeRequest load(Context context,String url,int placeHolder,int errorHolder){
        DrawableTypeRequest request = Glide.with(context).load(url);
        int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
        int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGloblePlaceHolder : 0;
        if (place != 0) request.placeholder(place);
        if (error != 0) request.error(error);
        return request;
    }

    /**
     * 说明：获取request
     * @param context
     * @param uri
     * @param placeHolder [-1:使用默认，0不使用，>0替换默认]
     * @param errorHolder
     * @return
     */
    public static DrawableTypeRequest load(Context context,Uri uri,int placeHolder,int errorHolder){
        DrawableTypeRequest request = Glide.with(context).load(uri);
        int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
        int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGloblePlaceHolder : 0;
        if (place != 0) request.placeholder(place);
        if (error != 0) request.error(error);
        return request;
    }

    /**
     * 说明：获取request
     * @param context
     * @param resId
     * @param placeHolder [-1:使用默认，0不使用，>0替换默认]
     * @param errorHolder
     * @return
     */
    public static DrawableTypeRequest load(Context context,int resId,int placeHolder,int errorHolder){
        DrawableTypeRequest request = Glide.with(context).load(resId);
        int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
        int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGloblePlaceHolder : 0;
        if (place != 0) request.placeholder(place);
        if (error != 0) request.error(error);
        return request;
    }

    /**
     * 说明：显示网络图片
     * @param url
     * @param view
     */
    public static void into(String url,View view){
        into(url,view,-1,-1);
    }

    /**
     * 说明：显示资源文件
     * @param resId
     * @param view
     */
    public static void into(int resId,View view){
        into(resId,view,-1,-1);
    }

    /**
     * 说明：显示资源文件
     * @param resId
     * @param view
     */
    public static void into(int resId, View view,int placeHolder,int errorHolder){
        int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
        int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGloblePlaceHolder : 0;
        if (view instanceof ImageView){
            load(view.getContext(),resId,place,error);
        }else {
            view.setBackgroundResource(resId);
        }
    }
    /**
     * 说明：显示资源文件
     * @param uri
     * @param view
     */
    public static void into(Uri uri,ImageView view){
        into(uri,view,-1,-1);
    }

    /**
     * 说明：显示资源文件
     * @param uri
     * @param view
     */
    public static void into(Uri uri, ImageView view,int placeHolder,int errorHolder){
        int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
        int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGloblePlaceHolder : 0;
        load(view.getContext(),uri,place,error).into(view);
    }

    /**
     * 说明：显示网络图片
     * @param url
     * @param view
     * @param placeHolder 0:不使用
     * @param errorHolder 0：不使用
     */
    public static void into(String url,final View view, final int placeHolder, final int errorHolder){
        if (view == null){
            return;
        }
        Context context = view.getContext();
        if (context == null){
            throw new RuntimeException(view + "获取不到Context");
        }
        if (view instanceof ImageView){
            load(context,url, placeHolder, errorHolder).into((ImageView) view);
        }else {
            bitmap(context,url, new BitmapListener() {
                @Override
                public void onStart() {
                    int place = placeHolder > 0 ? placeHolder : placeHolder <= 0 ? sGloblePlaceHolder : 0;
                    if (place > 0){
                        view.setBackgroundResource(place);
                    }
                }

                @Override
                public void onSuccess(Bitmap bitmap) {
                    view.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }

                @Override
                public void onFail(Exception e) {
                    int error = errorHolder > 0 ? errorHolder : errorHolder <= 0 ? sGlobleErrorHolder : 0;
                    if (error > 0){
                        view.setBackgroundResource(error);
                    }
                }
            });
        }
    }

    /**
     * 说明：保存为PNG
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void savePNG(Bitmap bitmap,String path,SaveImageListener listener){
        save(bitmap, path, Bitmap.CompressFormat.PNG, 100, listener);
    }

    /**
     * 说明：保存为PNG
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void saveJPG(Bitmap bitmap,String path,SaveImageListener listener){
        save(bitmap, path, Bitmap.CompressFormat.JPEG, 100, listener);
    }

    /**
     * 说明：保存为WEBP
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void saveWEBP(Bitmap bitmap,String path,SaveImageListener listener){
        save(bitmap, path, Bitmap.CompressFormat.WEBP, 100, listener);
    }

    /**
     * 说明：保存图片-格式PNG
     * @param context
     * @param url
     * @param path
     * @param listener
     */
    public static void saveImage(Context context,final String url,final String path,final SaveImageListener listener){
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(path)){
            return;
        }
        bitmap(context, url, new BitmapListener() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                String type = url.substring(url.lastIndexOf('c'));
                if (StringUtils.isEqualsIgnoreCase(type, "jpg")) {
                    saveJPG(bitmap, path, listener);
                } else if (StringUtils.isEqualsIgnoreCase(type, "webp")) {
                    saveWEBP(bitmap, path, listener);
                } else {
                    savePNG(bitmap, path, listener);
                }
            }

            @Override
            public void onFail(Exception e) {
                if (listener != null) {
                    listener.onFail(e);
                }
            }
        });

    }

    /**
     * 说明：保存图片-格式PNG
     * @param context
     * @param url
     * @param path
     * @param listener
     */
    public static void saveImage(Context context,final String url,final String path,int width,int height,final SaveImageListener listener){
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(path)){
            return;
        }
        bitmap(context,url, width, height, new BitmapListener() {
            @Override
            public void onStart() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public void onSuccess(Bitmap bitmap) {
                String type = url.substring(url.lastIndexOf('c'));
                if (StringUtils.isEqualsIgnoreCase(type, "jpg")) {
                    saveJPG(bitmap, path, listener);
                } else if (StringUtils.isEqualsIgnoreCase(type, "webp")) {
                    saveWEBP(bitmap, path, listener);
                } else {
                    savePNG(bitmap, path, listener);
                }
            }

            @Override
            public void onFail(Exception e) {
                if (listener != null) {
                    listener.onFail(e);
                }
            }
        });

    }

    /**
     * 说明：保存图片
     * @param bitmap 位图
     * @param path 图片路径
     * @param format 图片格式
     * @param quality 图片质量
     * @param listener 监听器
     */
    public static void save(Bitmap bitmap,String path,Bitmap.CompressFormat format,int quality,SaveImageListener listener){
        if (bitmap == null || StringUtils.isEmpty(path)){
            return;
        }
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (file.isDirectory()){
                throw new IllegalArgumentException(path + "---保存图片路径有问题！");
            }
            if (file.exists()){
                file.exists();
            }

            fos = new FileOutputStream(file);
            bitmap.compress(format, quality, fos);
            fos.flush();
            if (listener != null){
                listener.onSuccess(file);
            }
        }catch (Exception e){
            if (listener != null){
                listener.onFail(e);
            }
        }finally {
            FileUtils.closeIO(fos);
        }
    }

    /**
     * 说明：清理缓存文件
     * @param filePath
     */
    public static long clean(String filePath){
        if (TextUtils.isEmpty(filePath)){
            return 0;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()){
            return 0;
        }else {
            long size = file.length();
            FileUtils.deleteAllFile(file);
            return size;
        }
    }

    /**
     * 说明：回收资源
     * @param bitmaps
     */
    public static void recycle(Bitmap... bitmaps){
        if (bitmaps == null){
            return;
        }
        for (Bitmap bitmap : bitmaps){
            if (bitmap != null){
                bitmap.recycle();
            }
        }
    }

    /////////////////////////////////////////////////////

    public interface GlideListener{
        void onStart();
        void onFail(Exception e);
    }

    /**
     * 说明：Bitmap监听器
     */
    public interface BitmapListener extends GlideListener{
        void onSuccess(Bitmap bitmap);
    }

    /**
     * 说明：保存图片监听器
     */
    public interface SaveImageListener extends GlideListener{
        void onSuccess(File file);
    }

}
