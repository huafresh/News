package hua.news.module_news.common;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hua.framework.wrapper.imageload.ImageLoad;
import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import java.util.List;

import hua.news.module_service.entitys.NormalNewsEntity;
import hua.news.module_news.R;

/**
 * 图文新闻列表通用适配器。
 * 请求自家服务器。
 * <p>
 * Created by hua on 2017/8/18.
 */

public class CommonListAdapter extends MultiItemRvAdapter<NormalNewsEntity> {
    private static final int ITEM_TYPE_BANNER = 100; //banner广告
    private static final int ITEM_TYPE_ONE = 200; //一图 + 文字
    private static final int ITEM_TYPE_THREE = 300; //三图 + 文字
    private static final int ITEM_TYPE_ONE_BIG = 400; //一大图 + 文字

    public CommonListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        List<NormalNewsEntity> dataList = getDataList();
        if (dataList != null) {
            NormalNewsEntity bean = dataList.get(position);
            if (bean != null) {
                return getItemViewTypeFromBean(bean);
            }
        }
        return super.getItemViewType(position);
    }

    private int getItemViewTypeFromBean(NormalNewsEntity bean) {
        int viewType = 0;
        switch (bean.getShow_type()) {
            case 1:
                viewType = ITEM_TYPE_ONE;
                break;
            case 2:
                viewType = ITEM_TYPE_THREE;
                break;
            case 3:
                viewType = ITEM_TYPE_ONE_BIG;
                break;
            default:
                break;
        }
        return viewType;
    }

    @Override
    protected int getLayoutId(ViewGroup parent, int viewType) {
        return getLayoutIdByViewType(viewType);
    }

    private int getLayoutIdByViewType(int viewType) {
        int layoutId = -1;
        switch (viewType) {
            case ITEM_TYPE_ONE:
                layoutId = R.layout.item_recycler_news_one;
                break;
            case ITEM_TYPE_THREE:
                layoutId = R.layout.item_recycler_news_three;
                break;
            case ITEM_TYPE_ONE_BIG:
                break;
            default:
                break;
        }
        return layoutId;
    }

    @Override
    protected void multiConvert(MyViewHolder holder, NormalNewsEntity data, int position) {
        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case ITEM_TYPE_BANNER:
                break;
            case ITEM_TYPE_ONE:
                ViewTypeOneBind(holder, data, position);
                break;
            case ITEM_TYPE_THREE:
                ViewTypeThreeBind(holder, data, position);
                break;
            case ITEM_TYPE_ONE_BIG:

                break;
            default:
                break;
        }
    }

    private void ViewTypeThreeBind(MyViewHolder holder, Object data, int position) {
        NormalNewsEntity bean = (NormalNewsEntity) data;
        ImageLoad.loadNormalImage((ImageView) holder.getView(R.id.iv_thumbnail_one), bean.getImg_url());
        ImageLoad.loadNormalImage((ImageView) holder.getView(R.id.iv_thumbnail_two), bean.getImgextra_url1());
        ImageLoad.loadNormalImage((ImageView) holder.getView(R.id.iv_thumbnail_three), bean.getImgextra_url2());
        holder.setText(R.id.tv_title, bean.getTitle());
        holder.setText(R.id.tv_source, bean.getSource());
        int replyCount = bean.getReply_count();
        if (replyCount != 0) {
            holder.setText(R.id.tv_comment_suffix, replyCount);
        } else {
            holder.setText(R.id.tv_comment_suffix, "");
        }

    }

    private void ViewTypeOneBind(MyViewHolder holder, Object data, int position) {
        NormalNewsEntity bean = (NormalNewsEntity) data;
//        ImageView imageView = holder.getView(R.id.iv_thumbnail);
//        ImageLoad.loadNormalImage(imageView, bean.getImg_url());
        holder.loadImage(R.id.iv_thumbnail, "");
        holder.setText(R.id.tv_title, bean.getTitle());
        holder.setText(R.id.tv_source, bean.getSource());
        int replyCount = bean.getReply_count();
        if (replyCount != 0) {
            holder.setText(R.id.tv_comment, replyCount);
        } else {
            holder.setText(R.id.tv_comment, "");
        }
    }

}
