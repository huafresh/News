package hua.news.module_login.pages.mailregister;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.hua.framework.interfaces.IWindow;
import com.example.hua.framework.wrapper.dialog.CommonDialog;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import java.util.Arrays;
import java.util.List;

import hua.news.module_login.R;


/**
 * 选择邮箱后缀的对话框
 *
 * @author hua
 * @date 2017/6/29
 */

public class SelectMailContent implements IWindow.IContentView {

    private View mContentView;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private CommonDialog mCommonDialog;

    private OnDialogDimissListener mOnDialogDimissListener;


    @Override
    public View getContentView(Context context) {
        if (mContentView == null) {
            mContext = context;
            mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_mail_suffix, null);
            initViews();
        }
        return mContentView;
    }

    @Override
    public void onAttachToContainer(IWindow.IContainer container) {
        mCommonDialog = (CommonDialog) container;
    }

    @Override
    public void onDetachContainer(IWindow.IContainer container) {
        mCommonDialog = null;
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Adapter mAdapter = new Adapter(mContext, R.layout.item_select_mail_suffix);
        final List<String> mDataList = Arrays.asList("@163.com", "@126.com", "@yeah.net");
        mAdapter.setDataList(mDataList);
        mAdapter.setOnItemClickListener(new MultiItemRvAdapter.OnItemClickListener<Object>() {
            @Override
            public void onClick(View view, Object data, int position) {
                if (mCommonDialog != null) {
                    mCommonDialog.dismiss();
                    if (mOnDialogDimissListener != null) {
                        mOnDialogDimissListener.onDismiss(mDataList.get(position));
                    }
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private static class Adapter extends SingleRvAdapter<String> {

        public Adapter(Context context, @LayoutRes int layoutId) {
            super(context, layoutId);
        }

        @Override
        protected void convert(MyViewHolder holder, String data, int position) {
            holder.setText(R.id.text, data);
        }
    }

    public interface OnDialogDimissListener{
        /**
         * dialog隐藏时调用
         * @param suffix
         */
        void onDismiss(String suffix);
    }

    public void setOnDialogDimissListener(OnDialogDimissListener listener){
        mOnDialogDimissListener = listener;
    }


}
