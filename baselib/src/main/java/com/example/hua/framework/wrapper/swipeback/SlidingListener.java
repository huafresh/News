package com.example.hua.framework.wrapper.swipeback;

/**
 * @author hua
 * @version 2018/9/11 9:54
 */

public interface SlidingListener {
//    void onStartSlide(View view);

    void onSliding(ISlidingPage page, float offset);

    void onFinishSlide();
}
