package hua.news.module_common.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.hua.framework.network.HttpRequest;
import com.example.hua.framework.support.pullrefresh.IRefreshLayout;
import com.example.hua.framework.support.pullrefresh.SupportRefreshLayout;
import com.example.hua.framework.utils.FileUtil;
import com.example.hua.framework.wrapper.emoji.EmojiKeyBoard;
import com.example.hua.framework.wrapper.loadlayout.LoadLayoutManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import hua.news.module_common.FrameworkInitializer;
import hua.news.module_common.constants.UrlCosntant;
import hua.news.module_common.loadviews.LoadErrorView;
import hua.news.module_common.loadviews.LoadingView;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author hua
 * @version 2018/3/26 19:23
 */

public class BaseApplication extends Application {

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
    }

    private void init() {
        FrameworkInitializer.init(this);

        HttpRequest.newBuilder()
                .baseUrl(UrlCosntant.URL_BASE)
                //后续优化
                //.addInterceptor(new LogginInterceptor())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoadLayoutManager.createLoadViewPool()
                .addLoadView(new LoadErrorView())
                .addLoadView(new LoadingView())
                .register();

        // TODO: 2017/10/15 这里拷贝assets目录下的表情到file目录，后续应该删掉，因为表情应该从服务器
        // TODO: 2017/10/15 下载，然后直接保存在file目录
        copyAssetsEmoji();

        EmojiKeyBoard.getInstance().initialization(this);

        ARouter.openDebug();
        ARouter.openLog();
        ARouter.init(this);
    }

    private void copyAssetsEmoji() {
        try {
            String[] emojis = getAssets().list("emoji/normal");
            for (String emojiName : emojis) {
                InputStream in = getAssets().open("emoji/normal/" + emojiName);
                String destPath = getExternalFilesDir(null) + "/emoji/normal/" + emojiName;
                File destFile = FileUtil.getFile(destPath);
                FileOutputStream fos = new FileOutputStream(destFile);
                FileUtil.readFromSteamToStream(in, fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Context getContext() {
        return context;
    }
}
