package hua.news.module_video.fragments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 自定义LayoutManager实现SmoothScroller
 *
 * @author hua
 * @version 2017/11/27 10:08
 */

public class SmoothLinearLayoutManager extends LinearLayoutManager {

    MySmoothScroller smoothScroller;

    public SmoothLinearLayoutManager(Context context) {
        super(context);
    }

    public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SmoothLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        if (smoothScroller == null) {
            smoothScroller = new MySmoothScroller(recyclerView.getContext());
        }
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
