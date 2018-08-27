package hua.news.module_common.loadviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hua.framework.wrapper.loadlayout.LoadLayout;

import com.example.hua.framework.wrapper.loadlayout.LoadView;

import hua.news.module_common.R;


/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */

public class LoadingView extends LoadView {

    public static final String ID = "LoadingView";

    @Override
    protected View getContentView(Context context, LoadLayout container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.view_load_ing, container, false);
        ImageView loading = (ImageView) contentView.findViewById(R.id.iv_loading);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
//        animationDrawable.run();
        return contentView;
    }

    @Override
    protected String createLoadViewId() {
        return ID;
    }
}
