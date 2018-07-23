package hua.news.module_service.entitys;

import java.util.List;

/**
 * 用户信息bean
 *
 * @author hua
 * @date 2017/6/27
 */

public final class LoginUserInfo {
    //用户id，唯一标识用户
    private String user_id;
    //用户昵称
    private String nick_name;
    //用户性别
    private boolean is_man;
    //绑定的手机号
    private String bind_phone;
    //账号
    private String mail;
    //绑定的第三方平台（目前没有入口让用户自己绑定第三方平台，因此不可能
    //会出现一个账号对应多个第三方平台的情况）
    private String third_plat;
    //绑定的第三方平台的唯一码
    private String third_unique_id;
    //头像存储路径
    private String avatar_path;
    //收藏信息
    private String collect;
    //跟帖的等级，可取值 : 科员、副科长、科长、副处长、处长、副局长、副书记、局长、书记、长老、元老
    private String follow_level;
    //金币的数量
    private String gold_sum;
    //我的关注数量
    private String my_notice_sum;
    //我的粉丝数量
    private String my_fans_sum;
    //订阅数量
    private String my_subscribe_sum;
    //收藏数量
    private String my_collect_sum;
    //跟帖数量
    private String my_follow_sum;
    //阅读数量
    private String my_read_sum;
    //跟帖历史记录
    private List<FollowHistoryInfo> follow_history_info_list;

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public boolean isIs_man() {
        return is_man;
    }

    public void setIs_man(boolean is_man) {
        this.is_man = is_man;
    }

    public String getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(String bind_phone) {
        this.bind_phone = bind_phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getThird_plat() {
        return third_plat;
    }

    public void setThird_plat(String third_plat) {
        this.third_plat = third_plat;
    }

    public String getThird_unique_id() {
        return third_unique_id;
    }

    public void setThird_unique_id(String third_unique_id) {
        this.third_unique_id = third_unique_id;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getFollow_level() {
        return follow_level;
    }

    public void setFollow_level(String follow_level) {
        this.follow_level = follow_level;
    }

    public String getGold_sum() {
        return gold_sum;
    }

    public void setGold_sum(String gold_sum) {
        this.gold_sum = gold_sum;
    }

    public String getMy_notice_sum() {
        return my_notice_sum;
    }

    public void setMy_notice_sum(String my_notice_sum) {
        this.my_notice_sum = my_notice_sum;
    }

    public String getMy_fans_sum() {
        return my_fans_sum;
    }

    public void setMy_fans_sum(String my_fans_sum) {
        this.my_fans_sum = my_fans_sum;
    }

    public String getMy_subscribe_sum() {
        return my_subscribe_sum;
    }

    public void setMy_subscribe_sum(String my_subscribe_sum) {
        this.my_subscribe_sum = my_subscribe_sum;
    }

    public String getMy_collect_sum() {
        return my_collect_sum;
    }

    public void setMy_collect_sum(String my_collect_sum) {
        this.my_collect_sum = my_collect_sum;
    }

    public String getMy_follow_sum() {
        return my_follow_sum;
    }

    public void setMy_follow_sum(String my_follow_sum) {
        this.my_follow_sum = my_follow_sum;
    }

    public String getMy_read_sum() {
        return my_read_sum;
    }

    public void setMy_read_sum(String my_read_sum) {
        this.my_read_sum = my_read_sum;
    }

    public List<FollowHistoryInfo> getFollow_history_info_list() {
        return follow_history_info_list;
    }

    public void setFollow_history_info_list(List<FollowHistoryInfo> follow_history_info_list) {
        this.follow_history_info_list = follow_history_info_list;
    }

    //用户跟帖历史记录信息bean
    public static final class FollowHistoryInfo {
        //新闻标题
        private String title;
        //评论内容
        private String content;
        //点赞数量
        private String like_sum;
        //发表时间
        private String time;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLike_sum() {
            return like_sum;
        }

        public void setLike_sum(String like_sum) {
            this.like_sum = like_sum;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
