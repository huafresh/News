package hua.news.module_service.entitys;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 图文新闻详情
 *
 * @author hua
 * @date 2017/9/18
 */
public class NormalNewsDetailBean implements Parcelable{

    private String news_id;
    private String title;
    private String source;
    private String time;
    private String content;
    private String share_link;
    private int skip_type;
    private String relative_sys;
    private List<String> images;
    private int reply_count;

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public int getSkip_type() {
        return skip_type;
    }

    public void setSkip_type(int skip_type) {
        this.skip_type = skip_type;
    }

    public String getRelative_sys() {
        return relative_sys;
    }

    public void setRelative_sys(String relative_sys) {
        this.relative_sys = relative_sys;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.news_id);
        dest.writeString(this.title);
        dest.writeString(this.source);
        dest.writeString(this.time);
        dest.writeString(this.content);
        dest.writeString(this.share_link);
        dest.writeInt(this.skip_type);
        dest.writeString(this.relative_sys);
        dest.writeStringList(this.images);
        dest.writeInt(this.reply_count);
    }

    public NormalNewsDetailBean() {
    }

    protected NormalNewsDetailBean(Parcel in) {
        this.news_id = in.readString();
        this.title = in.readString();
        this.source = in.readString();
        this.time = in.readString();
        this.content = in.readString();
        this.share_link = in.readString();
        this.skip_type = in.readInt();
        this.relative_sys = in.readString();
        this.images = in.createStringArrayList();
        this.reply_count = in.readInt();
    }

    public static final Creator<NormalNewsDetailBean> CREATOR = new Creator<NormalNewsDetailBean>() {
        @Override
        public NormalNewsDetailBean createFromParcel(Parcel source) {
            return new NormalNewsDetailBean(source);
        }

        @Override
        public NormalNewsDetailBean[] newArray(int size) {
            return new NormalNewsDetailBean[size];
        }
    };
}
