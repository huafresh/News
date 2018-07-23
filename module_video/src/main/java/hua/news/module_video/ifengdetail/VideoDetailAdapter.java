package hua.news.module_video.ifengdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.hua.framework.wrapper.recyclerview.MyViewHolder;
import com.example.hua.framework.wrapper.recyclerview.SingleRvAdapter;

import hua.news.module_service.entitys.VideoDetailBean;
import hua.news.module_video.R;

/**
 * @author hua
 * @version 2018/4/28 17:53
 */

public class VideoDetailAdapter extends SingleRvAdapter<VideoDetailBean> {

    public VideoDetailAdapter(Context context) {
        super(context, R.layout.item_detail_video);
    }

    @Override
    protected void convert(MyViewHolder holder, VideoDetailBean data, int position) {

    }

}
