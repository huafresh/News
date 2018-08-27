package hua.news.module_common.loadviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.wrapper.loadlayout.LoadLayout;
import com.example.hua.framework.wrapper.loadlayout.LoadView;

import java.util.ArrayList;
import java.util.List;

import hua.news.module_common.R;


/**
 * Author: hua
 * Created: 2017/9/26
 * Description:
 */

public class LoadErrorView extends LoadView {

    public static final String ID = "LoadErrorView";

    @Override
    protected View getContentView(Context context, LoadLayout container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.view_load_error, container, false);
    }

    @Override
    protected List<Integer> clickableIds() {
        List<Integer> ids = new ArrayList<>();
        ids.add(R.id.error);
        return ids;
    }

    @Override
    protected String createLoadViewId() {
        return ID;
    }
}
