package hua.news.module_service.entitys;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 首页栏目实体
 *
 * @author hua
 * @date 2017/6/5
 */
public class ColumnEntity implements Parcelable {

    /**
     * name : 栏目名称
     * isAdd : 是否在TabLayout中显示
     * column_id : 栏目唯一标识
     * type : 内容类型：图文-0、视频-1、直播-2
     * channel : 请求页面内容所需的频道id
     */

    private String name;
    private boolean isAdd;
    private String column_id;
    private int type;
    private String channel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getColumn_id() {
        return column_id;
    }

    public void setColumn_id(String column_id) {
        this.column_id = column_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isAdd ? (byte) 1 : (byte) 0);
        dest.writeString(this.column_id);
        dest.writeInt(this.type);
        dest.writeString(this.channel);
    }

    public ColumnEntity() {
    }

    protected ColumnEntity(Parcel in) {
        this.name = in.readString();
        this.isAdd = in.readByte() != 0;
        this.column_id = in.readString();
        this.type = in.readInt();
        this.channel = in.readString();
    }

    public static final Parcelable.Creator<ColumnEntity> CREATOR = new Parcelable.Creator<ColumnEntity>() {
        @Override
        public ColumnEntity createFromParcel(Parcel source) {
            return new ColumnEntity(source);
        }

        @Override
        public ColumnEntity[] newArray(int size) {
            return new ColumnEntity[size];
        }
    };
}
