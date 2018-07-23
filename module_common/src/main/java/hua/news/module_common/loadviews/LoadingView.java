package hua.news.module_common.loadviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hua.framework.wrapper.loadlayout.LoadService;
import com.example.hua.framework.wrapper.loadlayout.LoadView;

import hua.news.module_common.R;


/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */

public class LoadingView extends LoadView {

    @Override
    protected View getContentView(Context context, ViewGroup container, LoadService loadService) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.view_load_ing, container, false);
        ImageView loading = (ImageView) contentView.findViewById(R.id.iv_loading);
//        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
//        animationDrawable.run();
        return contentView;
    }
}
