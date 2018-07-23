package com.example.hua.framework.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * 图片工具
 * @Deprecated 用glide库就够了
 *
 * @author hua
 * @date 2017/7/17
 */
@Deprecated
public class PictureUtil {

    /**
     * 获取圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap roundPicture(Bitmap bitmap) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, min, min, false), 0, 0, paint);
        return target;
    }

    public static Bitmap roundPicture(Drawable drawable) {
        return roundPicture(Drawable2Bitmap(drawable));
    }

    public static Drawable roundPicture(Context context, Bitmap bitmap) {
        return Bitmap2Drawable(context, roundPicture(bitmap));
    }

    public static Drawable roundPicture(Context context, Drawable drawable) {
        return Bitmap2Drawable(context, roundPicture(drawable));
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap Drawable2Bitmap(Drawable drawable) {
        if (drawable != null) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    /**
     * bitmap转drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable Bitmap2Drawable(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(context.getResources(), bitmap);
        }
        return null;
    }

}
