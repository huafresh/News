package hua.news.module_news.ifengcommn;

import java.util.List;

/**
 * 对应一条凤凰新闻
 *
 * @author hua
 * @version 2018/4/3 14:03
 */

public class IFengNewsEntity {
    /**
     * type : doc
     * thumbnail : http://d.ifengimg.com/w230_h152_q100_aix0_aiy0_aiw1023_aih675/p0.ifengimg.com/web/2017_47/7e7e311211454ed_w1024_h710.jpg
     * title : 特朗普频频“出牌”，美国对华战略向何处去
     * showType : 0
     * source : 新京报
     * subscribe : {"cateid":"新京报","type":"source","catename":"新京报","description":""}
     * updateTime : 2018/04/03 07:22:36
     * id : http://api.3g.ifeng.com/ipadtestdoc?aid=cmpp_030210057274625&channelKey=&channelid=SYLB10NEW_UP&category=%E6%97%B6%E6%94%BF
     * documentId : cmpp_030210057274625
     * staticId : cmpp_030210057274625
     * style : {"backreason":["不感兴趣","不想看:新京报","旧闻、看过了","内容质量差"],"view":"titleimg"}
     * commentsUrl : http://pl.ifeng.com/a/20180403/57274625_0.shtml
     * comments : 3
     * commentsall : 3
     * link : {"type":"doc","url":"http://api.3g.ifeng.com/ipadtestdoc?aid=cmpp_030210057274625&channelKey=&channelid=SYLB10NEW_UP&category=%E6%97%B6%E6%94%BF","weburl":"http://m.ifeng.com/sharenews.f?ch=qd_sdk_dl1&aid=cmpp_030210057274625"}
     * simId : clusterId_43042727
     * reftype : ai||
     * recomToken : 93790a29-2523-4f91-a319-f4525c13ebd7
     * payload : eyJkb2NpZCI6IjU5NzAwMjk2In0=
     */

    private String type;
    private String thumbnail;
    private String title;
    private String showType;
    private String source;
    private SubscribeBean subscribe;
    private String updateTime;
    private String id;
    private String documentId;
    private String staticId;
    private StyleBean style;
    private String commentsUrl;
    private String comments;
    private String commentsall;
    private LinkBean link;
    private String simId;
    private String reftype;
    private String recomToken;
    private String payload;
    private int itemType;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public SubscribeBean getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(SubscribeBean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStaticId() {
        return staticId;
    }

    public void setStaticId(String staticId) {
        this.staticId = staticId;
    }

    public StyleBean getStyle() {
        return style;
    }

    public void setStyle(StyleBean style) {
        this.style = style;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCommentsall() {
        return commentsall;
    }

    public void setCommentsall(String commentsall) {
        this.commentsall = commentsall;
    }

    public LinkBean getLink() {
        return link;
    }

    public void setLink(LinkBean link) {
        this.link = link;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getReftype() {
        return reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }

    public String getRecomToken() {
        return recomToken;
    }

    public void setRecomToken(String recomToken) {
        this.recomToken = recomToken;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public static class SubscribeBean {
        /**
         * cateid : 新京报
         * type : source
         * catename : 新京报
         * description :
         */

        private String cateid;
        private String type;
        private String catename;
        private String description;

        public String getCateid() {
            return cateid;
        }

        public void setCateid(String cateid) {
            this.cateid = cateid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCatename() {
            return catename;
        }

        public void setCatename(String catename) {
            this.catename = catename;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class StyleBean {
        /**
         * backreason : ["不感兴趣","不想看:新京报","旧闻、看过了","内容质量差"]
         * view : titleimg
         */

        private String view;
        private List<String> backreason;
        private List<String> images;

        public String getView() {
            return view;
        }

        public void setView(String view) {
            this.view = view;
        }

        public List<String> getBackreason() {
            return backreason;
        }

        public void setBackreason(List<String> backreason) {
            this.backreason = backreason;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class LinkBean {
        /**
         * type : doc
         * url : http://api.3g.ifeng.com/ipadtestdoc?aid=cmpp_030210057274625&channelKey=&channelid=SYLB10NEW_UP&category=%E6%97%B6%E6%94%BF
         * weburl : http://m.ifeng.com/sharenews.f?ch=qd_sdk_dl1&aid=cmpp_030210057274625
         */

        private String type;
        private String url;
        private String weburl;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWeburl() {
            return weburl;
        }

        public void setWeburl(String weburl) {
            this.weburl = weburl;
        }
    }
}
