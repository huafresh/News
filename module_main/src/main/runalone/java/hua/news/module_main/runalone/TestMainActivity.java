package hua.news.module_main.runalone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import hua.news.module_main.MainActivity;
import hua.news.module_main.R;

/**
 * @author hua
 * @version 2018/4/2 16:13
 */

public class TestMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
