package hua.news.module_main.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hua.framework.divider.LinearItemDecoration;

import java.util.ArrayList;
import java.util.List;

import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;

import hua.news.module_common.NewsPullToRefreshLayout;
import hua.news.module_main.R;

/**
 * Created by hua on 2017/11/18.
 */

public class TestPullToRefreshActivity extends Activity {

    private NewsPullToRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_for_refresh);

        pullToRefreshLayout = findViewById(R.id.pull_refresh);
        pullToRefreshLayout.setPullDownRefreshEnable(true);
        pullToRefreshLayout.setOnRefreshingListener(new NewsPullToRefreshLayout.OnRefreshingListener() {
            @Override
            public void onRefreshing() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pullToRefreshLayout.setHeaderRefreshComplete(0);
                    }
                }).start();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Adapter adapter = new Adapter(this, R.layout.test_for_refresh_item);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(i + "");
        }
        adapter.setDataList(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LinearItemDecoration(this));
        recyclerView.setAdapter(adapter);
    }

    private static class Adapter extends SingleRvAdapter<String> {

        public Adapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void convert(MyViewHolder holder, String data, int position) {
            holder.setText(R.id.text, data);
        }
    }

}
