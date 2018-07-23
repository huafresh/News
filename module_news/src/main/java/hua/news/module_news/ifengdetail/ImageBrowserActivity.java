package hua.news.module_news.ifengdetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.hua.framework.utils.SimpleViewPagerAdapter;
import com.example.hua.framework.wrapper.imageload.ImageLoad;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hua.news.module_news.R;

/**
 * @author hua
 * @version 2018/4/10 9:44
 */

public class ImageBrowserActivity extends AppCompatActivity {

    public static final String KEY_AID = "aid";
    @BindView(R.id.btn_titlebar_left)
    ImageView btnTitlebarLeft;
    @BindView(R.id.tv_titlebar_name)
    TextView tvTitlebarName;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    private Adapter adapter;
    private String aid;
    private IFengDetailLiveData iFengDetailLiveData;


    public static void start(Activity activity, String aid) {
        //test
        //aid = "cmpp_010230043042460";

        if (!TextUtils.isEmpty(aid)) {
            Intent intent = new Intent(activity, ImageBrowserActivity.class);
            intent.putExtra(KEY_AID, aid);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "aid不能空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        ButterKnife.bind(this);

        adapter = new Adapter(this);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                setBottomText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        aid = getIntent().getStringExtra(KEY_AID);
        iFengDetailLiveData = new IFengDetailLiveData(aid);
        iFengDetailLiveData.observe(this, new Observer<IFengNewsDetail>() {
            @Override
            public void onChanged(@Nullable IFengNewsDetail iFengNewsDetail) {
                adapter.setDataList(iFengNewsDetail.getBody().getSlides());
                adapter.notifyDataSetChanged();

                tvTitlebarName.setText(iFengNewsDetail.getBody().getTitle());
                setBottomText(0);
            }
        });
        iFengDetailLiveData.getData();
    }

    @SuppressLint("SetTextI18n")
    private void setBottomText(int position) {
        List<IFengNewsDetail.BodyBean.SlidesBean> dataList = adapter.getDataList();
        tvInfo.setText(String.format(Locale.CHINA, "%d/%d  ", position+1, dataList.size())
                + dataList.get(position).getDescription());
    }

    @OnClick(R.id.btn_titlebar_left)
    public void onViewClicked() {
        finish();
    }

    private static class Adapter extends SimpleViewPagerAdapter<IFengNewsDetail.BodyBean.SlidesBean> {

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected View inflateView(IFengNewsDetail.BodyBean.SlidesBean o, ViewGroup container, int position) {
            View pageView = LayoutInflater.from(context)
                    .inflate(R.layout.pager_image_browser, container, false);
            ImageView imageView = pageView.findViewById(R.id.iv_image);
            final ProgressBar progressBar = pageView.findViewById(R.id.pb_progress);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(o.getImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
            return pageView;
        }
    }
}
