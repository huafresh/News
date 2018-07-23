package hua.news.module_service.entitys;

import android.support.annotation.DrawableRes;

/**
 * Author: hua
 * Created: 2017/9/30
 * Description:
 * 图片 + 标题实体类
 */

public class TitleIconEntity {
    //标题
    private String title;
    //图片资源id
    private int icon;
    //扩展用
    private String extern1;
    private String extern2;
    private String extern3;
    private String extern4;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public String getExtern1() {
        return extern1;
    }

    public void setExtern1(String extern1) {
        this.extern1 = extern1;
    }

    public String getExtern2() {
        return extern2;
    }

    public void setExtern2(String extern2) {
        this.extern2 = extern2;
    }

    public String getExtern3() {
        return extern3;
    }

    public void setExtern3(String extern3) {
        this.extern3 = extern3;
    }

    public String getExtern4() {
        return extern4;
    }

    public void setExtern4(String extern4) {
        this.extern4 = extern4;
    }
}
