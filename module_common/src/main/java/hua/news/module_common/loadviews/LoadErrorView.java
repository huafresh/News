package hua.news.module_common.loadviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.wrapper.loadlayout.LoadService;
import com.example.hua.framework.wrapper.loadlayout.LoadView;

import hua.news.module_common.R;


/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */

public class LoadErrorView extends LoadView {
    @Override
    protected View getContentView(Context context, ViewGroup container, final LoadService loadService) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.view_load_error, container, false);
        contentView.findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadService.executeReloadListeners(LoadErrorView.class.getCanonicalName(), v);
            }
        });
        return contentView;
    }
}
