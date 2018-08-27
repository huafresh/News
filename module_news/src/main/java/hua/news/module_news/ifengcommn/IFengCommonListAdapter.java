package hua.news.module_news.ifengcommn;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.example.hua.framework.wrapper.recyclerview.MultiItemRvAdapter;
import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;

import java.util.Iterator;
import java.util.List;

import hua.news.module_common.constants.IFengConstant;
import hua.news.module_news.R;

/**
 * 适配凤凰网图文新闻
 *
 * @author hua
 * @version 2018/4/3 13:42
 */

class IFengCommonListAdapter extends MultiItemRvAdapter<IFengNewsEntity> {

    /**
     * 新闻：标题模式
     */
    public static final int TYPE_DOC_TITLEIMG = 0;

    /**
     * 新闻：滑动图片模式
     */
    public static final int TYPE_DOC_SLIDEIMG = 1;

    /**
     * 广告：标题模式
     */
    public static final int TYPE_ADVERT_TITLEIMG = 2;

    /**
     * 广告：多图
     */
    public static final int TYPE_ADVERT_SLIDEIMG = 3;

    /**
     * 广告：单图
     */
    public static final int TYPE_ADVERT_LONGIMG = 4;

    /**
     * 顶部banner
     */
    public static final int TYPE_BANNER = 5;


    private OnCancelClickListener mOnCancelClickListener;

    IFengCommonListAdapter(Context context) {
        super(context);
    }

    public void setOnCancelClickListener(OnCancelClickListener listener) {
        mOnCancelClickListener = listener;
    }

    @Override
    public void setDataList(List<IFengNewsEntity> list) {
        //解析出新闻item的类型，并把不支持的类型从列表去掉
        resolveItemType(list);
        super.setDataList(list);
    }

    private void resolveItemType(List<IFengNewsEntity> list) {
        if (list != null) {
            Iterator<IFengNewsEntity> iterator = list.iterator();
            while (iterator.hasNext()) {
                IFengNewsEntity entity = iterator.next();
                int itemType;
                if ((itemType = getItemTypeFromBean(entity)) != -1) {
                    entity.setItemType(itemType);
                } else {
                    iterator.remove();
                }
            }
        }
    }

    private int getItemTypeFromBean(IFengNewsEntity entity) {
        try {
            String newsType = entity.getType();
            if (IFengConstant.DETAIL_TYPE_DOC.equals(newsType)) {
                if (entity.getStyle().getView() != null) {
                    if (IFengConstant.VIEW_TYPE_TITLEIMG.equals(entity.getStyle().getView())) {
                        return TYPE_DOC_TITLEIMG;
                    } else if(IFengConstant.VIEW_TYPE_SLIDEIMG.equals(entity.getStyle().getView())){
                        return TYPE_DOC_SLIDEIMG;
                    }
                }
            } else if (IFengConstant.DETAIL_TYPE_ADVERT.equals(newsType)) {
                if (entity.getStyle() != null) {
                    if (IFengConstant.VIEW_TYPE_TITLEIMG.equals(entity.getStyle().getView())) {
                        return TYPE_ADVERT_TITLEIMG;
                    } else if (IFengConstant.VIEW_TYPE_SLIDEIMG.equals(entity.getStyle().getView())) {
                        return TYPE_ADVERT_SLIDEIMG;
                    } else {
                        return TYPE_ADVERT_LONGIMG;
                    }
                }
            } else if (IFengConstant.DETAIL_TYPE_SLIDE.equals(newsType)) {
                //滑动类型的新闻，就是查看的时候是滑动图片然后底部是文字的新闻
                if (IFengConstant.DETAIL_TYPE_DOC.equals(entity.getLink().getType())) {
                    if (IFengConstant.VIEW_TYPE_SLIDEIMG.equals(entity.getStyle().getView())) {
                        return TYPE_DOC_SLIDEIMG;
                    } else {
                        return TYPE_DOC_TITLEIMG;
                    }
                } else {
                    return TYPE_DOC_TITLEIMG;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void multiConvert(MyViewHolder holder, IFengNewsEntity data, int position) {
        String comment = !TextUtils.isEmpty(data.getComments()) ? data.getComments() : "0";
        switch (holder.getItemViewType()) {
            case TYPE_DOC_TITLEIMG:
                holder.loadImage(R.id.iv_thumbnail, data.getThumbnail());
                holder.setText(R.id.tv_title, data.getTitle());
                holder.setText(R.id.tv_source, data.getSource());
                holder.setText(R.id.tv_comment, comment);
                setCancelListener(holder.itemView, R.id.view_cancel_bac, data);
                break;
            case TYPE_DOC_SLIDEIMG:
                holder.setVisibility(R.id.iv_thumbnail_one, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_two, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_three, View.GONE);
                try {
                    holder.loadImage(R.id.iv_thumbnail_one, data.getStyle().getImages().get(0));
                    holder.setVisibility(R.id.iv_thumbnail_one, View.VISIBLE);
                    holder.loadImage(R.id.iv_thumbnail_two, data.getStyle().getImages().get(1));
                    holder.setVisibility(R.id.iv_thumbnail_two, View.VISIBLE);
                    holder.loadImage(R.id.iv_thumbnail_three, data.getStyle().getImages().get(2));
                    holder.setVisibility(R.id.iv_thumbnail_three, View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.setText(R.id.tv_title, data.getTitle());
                holder.setText(R.id.tv_source, data.getSource());
                holder.setText(R.id.tv_comment, comment);
                setCancelListener(holder.itemView, R.id.ll_dislike_bac, data);
                break;
            case TYPE_ADVERT_TITLEIMG:
                holder.loadImage(R.id.iv_thumbnail, data.getThumbnail());
                holder.setText(R.id.tv_title, data.getTitle());
                setCancelListener(holder.itemView, R.id.view_cancel_bac, data);
                break;
            case TYPE_ADVERT_SLIDEIMG:
                holder.setVisibility(R.id.iv_thumbnail_one, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_two, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_three, View.GONE);
                try {
                    holder.loadImage(R.id.iv_thumbnail_one, data.getStyle().getImages().get(0));
                    holder.setVisibility(R.id.iv_thumbnail_one, View.VISIBLE);
                    holder.loadImage(R.id.iv_thumbnail_two, data.getStyle().getImages().get(1));
                    holder.setVisibility(R.id.iv_thumbnail_two, View.VISIBLE);
                    holder.loadImage(R.id.iv_thumbnail_three, data.getStyle().getImages().get(2));
                    holder.setVisibility(R.id.iv_thumbnail_three, View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.tv_title, data.getTitle());
                setCancelListener(holder.itemView, R.id.ll_dislike_bac, data);
                break;
            case TYPE_ADVERT_LONGIMG:
                holder.setVisibility(R.id.iv_thumbnail_one, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_two, View.GONE);
                holder.setVisibility(R.id.iv_thumbnail_three, View.GONE);
                holder.loadImage(R.id.iv_thumbnail_one, data.getThumbnail());
                holder.setVisibility(R.id.iv_thumbnail_one, View.VISIBLE);
                holder.setText(R.id.tv_title, data.getTitle());
                setCancelListener(holder.itemView, R.id.ll_dislike_bac, data);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutId(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DOC_TITLEIMG:
                return R.layout.item_recycler_news_one;
            case TYPE_DOC_SLIDEIMG:
                return R.layout.item_recycler_news_three;
            case TYPE_ADVERT_TITLEIMG:
                return R.layout.item_recycler_advert_one;
            case TYPE_ADVERT_SLIDEIMG:
                return R.layout.item_recycler_advert_three;
            case TYPE_ADVERT_LONGIMG:
                return R.layout.item_recycler_advert_three;
            default:
                break;
        }
        return super.getLayoutId(parent, viewType);
    }

    private void setCancelListener(View itemView, int id, final IFengNewsEntity data) {
        itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCancelClickListener != null) {
                    mOnCancelClickListener.onCancel(v, data);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        List<IFengNewsEntity> dataList = getDataList();
        if (dataList != null && dataList.size() > 0) {
            return dataList.get(position).getItemType();
        }
        return super.getItemViewType(position);
    }

    public interface OnCancelClickListener {
        void onCancel(View view, IFengNewsEntity data);
    }

}
