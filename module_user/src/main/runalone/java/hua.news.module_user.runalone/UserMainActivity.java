package hua.news.module_user.runalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_user.R;
import hua.news.module_user.collect.CollectDetailActivity;
import hua.news.module_user.usercenter.UserCenterActivity;

/**
 * @author hua
 * @version 2018/3/31 13:54
 */

public class UserMainActivity extends AppCompatActivity {

    @BindView(R.id.collect)
    Button collect;
    @BindView(R.id.user_center)
    Button userCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.collect, R.id.user_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.collect:
                Intent intent = new Intent(UserMainActivity.this, CollectDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.user_center:
                Intent intent1 = new Intent(UserMainActivity.this, UserCenterActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
