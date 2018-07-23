package hua.news.module_news.runalone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import hua.news.module_news.PictureNewsFragmentManager;
import hua.news.module_news.R;
import hua.news.module_news.ifengcommn.IFengNewsListFragment;

/**
 * @author hua
 * @version 2018/3/31 16:21
 */

public class TestNewsMainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_test);
        button = findViewById(R.id.show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setVisibility(View.GONE);

                PictureNewsFragmentManager pictureNewsFragmentManager = new PictureNewsFragmentManager();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, pictureNewsFragmentManager.getNewsHomeFragment())
                        .commit();
            }
        });

//        button2 = findViewById(R.id.fun);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                button2.setVisibility(View.GONE);
//
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, IFengNewsListFragment.newInstance("SYLB10,SYDT10"))
//                        .commit();
//            }
//        });

    }
}
