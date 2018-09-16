package com.example.hua.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;

import hua.news.module_common.constants.RouterConstant;

/**
 * app作为整体时的入口activity，通过路由启动module_main组件
 *
 * @author hua
 * @version 2017/10/23
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        try {
            ARouter.getInstance().build(RouterConstant.MAIN).navigation();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
