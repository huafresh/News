package hua.news.module_news.ifengcommn;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hua.framework.interfaces.IWindow;
import com.example.hua.framework.utils.CommonUtil;
import com.example.hua.framework.wrapper.popupwindow.CommonPopupWindow;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapterNew;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hua.news.module_news.R;

/**
 * 不感兴趣弹框
 *
 * @author hua
 * @version 2018/4/4 15:15
 */

public class NewsDelPopContent implements IWindow.IContentView {
    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_nolike)
    TextView tvNolike;
    @BindView(R.id.iv_down)
    ImageView ivDown;

    private View contentView;
    private boolean isUp;
    private List<String> backReasons;
    private List<Integer> selectedPos = new ArrayList<>();
    private Unbinder unbinder;
    private Context context;
    private Adapter adapter;
    private IWindow.IContainer container;
    private OnConfirmListener mOnConfirmListener;

    public void setBackReasons(List<String> backReasons) {
        this.backReasons = backReasons;
        if (adapter != null) {
            adapter.setDataList(backReasons);
            adapter.notifyDataSetChanged();
        }
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public int getPopupHeight(Context context) {
        View contentView = getContentView(context);
        //因为缓存，导致ivTop和ivDown都有可能是gone的状态，这时候计算就不准确了
        setArrowPos();
        contentView.measure(0, 0);
        return contentView.getMeasuredHeight();
    }

    @Override
    public View getContentView(Context context) {
        if (contentView == null) {
            this.context = context;
            contentView = LayoutInflater.from(context).inflate(R.layout.popup_newsdel, null);
            unbinder = ButterKnife.bind(this, contentView);
            tvNolike.setText(context.getResources().getString(R.string.cancel));
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            adapter = new Adapter(context, R.layout.item_newsdelpop_del);
            if (backReasons != null) {
                adapter.setDataList(backReasons);
            }
            recyclerView.setAdapter(adapter);
            setListeners();
        }
        return contentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        this.container = container;
        setArrowPos();

    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        container = null;
        selectedPos.clear();
    }

    private void setArrowPos() {
        if (isUp) {
            ivTop.setVisibility(View.VISIBLE);
            ivDown.setVisibility(View.GONE);
        } else {
            ivTop.setVisibility(View.GONE);
            ivDown.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        adapter.setOnItemClickListener(new MultiItemRvAdapterNew.OnItemClickListener<Object>() {
            @Override
            public void onClick(View view, Object data, int position) {
                if (selectedPos.contains(position)) {
                    selectedPos.remove(selectedPos.indexOf(position));
                } else {
                    selectedPos.add(position);
                }

                if (selectedPos.isEmpty()) {
                    tvNolike.setText(context.getResources().getString(R.string.cancel));
                } else {
                    tvNolike.setText(context.getResources().getString(R.string.confirm));
                }

                adapter.notifyItemChanged(position);
            }
        });
    }

    @OnClick(R.id.tv_nolike)
    public void onViewClicked() {
        String text = tvNolike.getText().toString();
        if (context.getResources().getString(R.string.confirm).equals(text)) {
            if (mOnConfirmListener != null) {
                mOnConfirmListener.onConfirm(selectedPos);
            }
        }
        container.removeContentView(this);

    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        mOnConfirmListener = listener;
    }

    public interface OnConfirmListener {
        void onConfirm(List<Integer> selectedPos);
    }

    private class Adapter extends SingleRvAdapter<String> {

        public Adapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void convert(MyViewHolder holder, String data, int position) {
            holder.setText(R.id.tv_backreason, data);
            if (selectedPos.contains(position)) {
                holder.setTextColor(R.id.tv_backreason,
                        CommonUtil.getColor(context, android.R.color.holo_red_light, null));
                holder.setBackgroundRes(R.id.tv_backreason, R.drawable.delpop_tv_bg_selected);
            } else {
                holder.setTextColor(R.id.tv_backreason,
                        CommonUtil.getColor(context, android.R.color.black, null));
                holder.setBackgroundRes(R.id.tv_backreason, R.drawable.delpop_tv_bg);
            }
        }

    }
}
