package hua.news.module_video.data;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

/**
 * 通用的LiveData观察者，通过id区分不同的LiveData
 *
 * @author hua
 * @version 2017/11/23 15:44
 */

public class CommonObserver<T> implements Observer<T> {

    private int id;
    private OnChangedListener listener;

    public CommonObserver(int id, OnChangedListener listener) {
        this.id = id;
        this.listener = listener;
    }

    @Override
    public void onChanged(@Nullable T o) {
        if (listener != null) {
            listener.onChanged(id, o);
        }
    }

    public interface OnChangedListener {
        /**
         * 观察的LiveData数据改变时调用
         *
         * @param id 用于区分数据源
         * @param o  LiveData改变后的数据
         */
        void onChanged(int id, Object o);
    }

}
