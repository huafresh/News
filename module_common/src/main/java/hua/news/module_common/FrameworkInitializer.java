package hua.news.module_common;

import android.content.Context;

/**
 * Created by hua on 2017/6/12.
 * 框架初始化，使用框架前必须调用{@link #init(Context)}方法
 */
@Deprecated
public class FrameworkInitializer {

    private static Context mContext;

    private FrameworkInitializer() {

    }

    public static FrameworkInitializer getInstance() {
        return FrameworkInitializer.HOLDER.sInstance;
    }

    private static final class HOLDER {
        private static final FrameworkInitializer sInstance = new FrameworkInitializer();
    }

    public static void init(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }


}
