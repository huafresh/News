package com.example.hua.framework.wrapper.swipeback;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

/**
 * @author hua
 * @version 2018/9/10 18:31
 */

public class SlidingHelper {

    private static SparseArray<SlidingLayout> layoutArray = new SparseArray<>();

    public static SlidingLayout wrap(Activity activity) {
        return wrap(activity, layoutArray.size());
    }

    /**
     * Wrap activity's decorView with SlidingLayout. Must be called after
     * setContentView.
     *
     * @param activity Activity
     * @param id       an id can be used to find SlidingLayout instance
     */
    public static SlidingLayout wrap(final Activity activity, int id) {
        SlidingLayout slidingLayout = new SlidingLayout(activity);
        return slidingLayout.wrap(new ActivitySlidingPage(activity));
    }

    private SlidingLayout createSlidingLayout(View originView, int id) {
        SlidingLayout slidingLayout = new SlidingLayout(originView.getContext());
        return slidingLayout;

    }


}
