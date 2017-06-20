package com.fast.library.glide;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 说明：获取圆形，圆角，普通，位图
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/2/21 10:22
 * <p/>
 * 版本：verson 1.0
 */
public class MulBitmapTransformation extends BitmapTransformation {

    //普通位图
    public static final int TYPE_NORMAL = 0;
    //圆形位图
    public static final int TYPE_CIRCLE = 1;
    //圆角位图
    public static final int TYPE_CORNER = 2;

    // 位图类型
    private int type;
    // 圆角
    private int radius = 10;

    public MulBitmapTransformation(Context context,int type) {
        super(context);
        this.type = type;
    }

    public MulBitmapTransformation(BitmapPool bitmapPool,int type) {
        super(bitmapPool);
        this.type = type;
    }

    /**
     * 说明：设置圆角度数
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        switch (type) {
            case TYPE_NORMAL:
                return toTransform;
            case TYPE_CIRCLE:
                return BitmapUtils.circleBitmap(toTransform);
            case TYPE_CORNER:
                return BitmapUtils.roundCorners(toTransform, radius);
            default:
                return toTransform;
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
