package hua.news.module_common;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.hua.framework.wrapper.pullrefresh.PTRLayout;


/**
 * 实际上{@link PTRLayout}本身是可以直接作为控件使用的，但是考虑到一个app里，其实刷新的样式
 * 基本上是一样的，因此统一进行一波个性化的定制。这里下拉刷新的样式模仿的是腾讯新闻的下拉刷新
 *
 * @author hua
 * @date 2017/9/5
 */

public class NewsPullToRefreshLayout extends PTRLayout {
    private TextView mTvStateNotice;
    private AnimationDrawable mAnimationDrawable;
    private Context mContext;
    private LinearLayout mllRefreshComplete;
    private View headerView;


    public NewsPullToRefreshLayout(Context context) {
        this(context, null);
    }

    public NewsPullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewsPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        LayoutInflater mInflater = LayoutInflater.from(context);
        mContext = context;

        //初始化头部布局
        headerView = mInflater.inflate(R.layout.header_pulldown_refresh, this, false);
        mllRefreshComplete = (LinearLayout) headerView.findViewById(R.id.ll_refresh_complete);
        mllRefreshComplete.setVisibility(INVISIBLE);
        mTvStateNotice = (TextView) headerView.findViewById(R.id.tv_state_notice);
        ImageView refreshingImg = (ImageView) headerView.findViewById(R.id.refreshing_image);
        mAnimationDrawable = (AnimationDrawable) refreshingImg.getDrawable();

        setHeaderLayout(headerView);

        //监听头部布局状态的改变
        addStateChangeListener(new OnRefreshStateChangedListener() {
            @Override
            public void onStateChanged(View child, State oldState, State newState) {
                if (child == headerView) {
                    updateHeaderLayout(newState);
                }
            }
        });
    }

    private OnRefreshingListener mOnRefreshingListener;

    public void setOnRefreshingListener(OnRefreshingListener listener) {
        mOnRefreshingListener = listener;
    }

    public interface OnRefreshingListener {
        void onRefreshing();
    }

    private void updateHeaderLayout(PTRLayout.State newState) {
        switch (newState) {
            case NONE:
                mTvStateNotice.setText(getResources().getText(R.string.refresh_pull_down));
                mAnimationDrawable.stop();
                break;
            case PULLING_TO_REFRESH:
                mTvStateNotice.setText(getResources().getText(R.string.refresh_pull_down));
                mAnimationDrawable.stop();
                break;
            case RELEASE_TO_REFRESH:
                mTvStateNotice.setText(getResources().getText(R.string.refresh_release));
                mAnimationDrawable.stop();
                break;
            case REFRESHING:
                mTvStateNotice.setText(getResources().getText(R.string.refresh_refreshing));
                mAnimationDrawable.start();
                if (mOnRefreshingListener != null) {
                    mOnRefreshingListener.onRefreshing();
                } else {
                    setHeaderRefreshComplete(-1);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置下拉刷新结束
     *
     * @param newSum 新内容个数, -1表示不展示刷新完成布局
     */
    public void setHeaderRefreshComplete(final int newSum) {
        setRefreshComplete(LayoutParams.LAYOUT_TYPE_HEADER);
    }

}
