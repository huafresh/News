package hua.news.module_service.entitys;

/**
 * Author: hua
 * Created: 2017/9/30
 * Description:
 * 一个设置项通用实体类
 */

public class SettingCommonEntity extends TitleIconEntity {
    //右边描述
    private String description;
    //右边是否有选择开关
    private boolean is_has_switch;
    //右边选择开关是否打开
    private boolean is_switch_on;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean is_has_switch() {
        return is_has_switch;
    }

    public void setIs_has_switch(boolean is_has_switch) {
        this.is_has_switch = is_has_switch;
    }

    public boolean is_switch_on() {
        return is_switch_on;
    }

    public void setIs_switch_on(boolean is_switch_on) {
        this.is_switch_on = is_switch_on;
    }

}
