package hua.news.module_service.entitys;


/**
 * 视频类型新闻实体
 *
 * @author hua
 * @version 2017/11/20 16:47
 */
public class VideoNewsEntity {

    private String video_id;
    private String title;
    private String _360p_url;
    private String _480p_url;
    private String _720p_url;
    private int duration;
    private int width;
    private int height;
    private String category_name;
    private int like_count;
    private int unlike_count;
    private int share_count;
    private int play_count;
    private int comment_count;
    private String date;
    private String user_id;

//    private LoginUserInfo author;
    private String comment_id;
    private String cover_path;


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_360p_url() {
        return _360p_url;
    }

    public void set_360p_url(String _360p_url) {
        this._360p_url = _360p_url;
    }

    public String get_480p_url() {
        return _480p_url;
    }

    public void set_480p_url(String _480p_url) {
        this._480p_url = _480p_url;
    }

    public String get_720p_url() {
        return _720p_url;
    }

    public void set_720p_url(String _720p_url) {
        this._720p_url = _720p_url;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getUnlike_count() {
        return unlike_count;
    }

    public void setUnlike_count(int unlike_count) {
        this.unlike_count = unlike_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

//    public LoginUserInfo getAuthor() {
//        return author;
//    }

//    public void setAuthor(LoginUserInfo author) {
//        this.author = author;
//    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }
}
