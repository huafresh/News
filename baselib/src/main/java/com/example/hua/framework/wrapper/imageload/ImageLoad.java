package com.example.hua.framework.wrapper.imageload;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * 图片加载
 *
 * @author hua
 * @date 2017/8/13
 */

public class ImageLoad {

    /**
     * 加载普通图片
     *
     * @param imageView
     * @param url
     */
    public static void loadNormalImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param url
     */
    public static void loadRoundImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

    }

}
