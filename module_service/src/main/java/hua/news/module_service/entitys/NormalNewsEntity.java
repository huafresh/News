package hua.news.module_service.entitys;

/**
 * 预览新闻bean
 *
 * @author hua
 * @date 2017/6/10
 */

public class NormalNewsEntity {

    /**
     * img_url : http://dmr.nosdn.127.net/inews-20170822-641a17e5fb10b126eaca04e49152e6de.jpg
     * news_id : CSDIDOCV051787KL
     * title : 脸蛋好身材辣就能吸引王宝强喜欢?张馨予醉翁之意或在李晨
     * source : 陆崕
     * is_photo_skip : 0
     * reply_count : 0
     * digest :
     * channel : T1348648517839
     * show_type : 1
     * imgextra_url1 :
     * imgextra_url2 :
     */

    private String img_url;
    private String news_id;
    private String title;
    private String source;
    private int is_photo_skip;
    private int reply_count;
    private String digest;
    private String channel;
    private int show_type;
    private String imgextra_url1;
    private String imgextra_url2;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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

    public int getIs_photo_skip() {
        return is_photo_skip;
    }

    public void setIs_photo_skip(int is_photo_skip) {
        this.is_photo_skip = is_photo_skip;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public String getImgextra_url1() {
        return imgextra_url1;
    }

    public void setImgextra_url1(String imgextra_url1) {
        this.imgextra_url1 = imgextra_url1;
    }

    public String getImgextra_url2() {
        return imgextra_url2;
    }

    public void setImgextra_url2(String imgextra_url2) {
        this.imgextra_url2 = imgextra_url2;
    }
}
