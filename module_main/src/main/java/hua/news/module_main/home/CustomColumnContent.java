package hua.news.module_main.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.hua.framework.interfaces.IOnBackPress;
import com.example.hua.framework.interfaces.IWindow;

import com.example.hua.framework.widget.DragOrderRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hua.news.module_service.entitys.ColumnEntity;
import hua.news.module_main.R;
import hua.news.module_main.R2;


/**
 * 自定义栏目页面。
 *
 * @author hua
 * @version 2017/10/17 19:55
 * @see IWindow
 */

public class CustomColumnContent implements IWindow.IContentView {

    @BindView(R2.id.draggable_list_view)
    DragOrderRecyclerView dragOrderRecyclerView;
    @BindView(R2.id.tv_notice)
    TextView tvNotice;
    @BindView(R2.id.tv_add_more)
    TextView tvAddMore;
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.image_bac)
    View imageBac;

    private List<ColumnEntity> mDataList;
    private View mContentView;
    private Context mContext;
    private IWindow.IContainer mContainer;
    private BackPressListener listener;
    private DragOrderRecyclerViewAdapter adapter;

    private boolean isShowing = false;

    public CustomColumnContent(List<ColumnEntity> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContext = context;
            LayoutInflater inflater = LayoutInflater.from(context);
            mContentView = inflater.inflate(R.layout.fragment_custom_column, null);
        }
        return mContentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        unbinder = ButterKnife.bind(this, mContentView);

        if (adapter == null) {
            adapter = new DragOrderRecyclerViewAdapter(mContext, mDataList);
            dragOrderRecyclerView.setDraggableAdapter(adapter);

            // TODO: 2017/10/22
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(adapter);
        }

        if (mContext instanceof IOnBackPress) {
            listener = new BackPressListener();
            ((IOnBackPress) mContext).addOnBackPressListener(listener);
        }

        mContainer = container;
        isShowing = true;
    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        if (mContext instanceof IOnBackPress) {
            if (listener != null) {
                ((IOnBackPress) mContext).removeOnBackPressListener(listener);
            }
        }
        unbinder.unbind();
        mContainer = null;
        isShowing = false;
    }

    private class BackPressListener implements IOnBackPress.OnBackPressListener {

        @Override
        public boolean onBackPress() {
            if (dragOrderRecyclerView.getState() == DragOrderRecyclerView.STATE_DRAGGABLE) {
                dragOrderRecyclerView.setState(DragOrderRecyclerView.STATE_NORMAL);
            } else {
                dismissSelves();
            }
            //当dismissSelves()隐藏自身时，此监听会被移除，
            // 因此只要此回调被调用，就应该返回true。嗯，没毛病。
            return true;
        }
    }

    @OnClick(R2.id.image_bac)
    public void onViewClicked() {
        dismissSelves();
    }

    private void dismissSelves() {
        if (mContainer != null) {
            mContainer.removeContentView(this);
        }
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    public boolean isShowing(){
        return isShowing;
    }

    private OnDismissListener mOnDismissListener;

    public interface OnDismissListener {
        /**
         * 页面关闭时调用
         */
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }


}
