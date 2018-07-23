package hua.news.module_news.home;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author hua
 * @date 2017/6/5
 */
public class IFengColumnEntity implements Parcelable {
    private String name;
    private String channel_id;
    private boolean is_add;
    private boolean is_removable;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel_id() {
        return channel_id == null ? "" : channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public boolean isIs_add() {
        return is_add;
    }

    public void setIs_add(boolean is_add) {
        this.is_add = is_add;
    }

    public boolean isIs_removable() {
        return is_removable;
    }

    public void setIs_removable(boolean is_removable) {
        this.is_removable = is_removable;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.channel_id);
        dest.writeByte(this.is_add ? (byte) 1 : (byte) 0);
        dest.writeByte(this.is_removable ? (byte) 1 : (byte) 0);
    }

    public IFengColumnEntity() {
    }

    protected IFengColumnEntity(Parcel in) {
        this.name = in.readString();
        this.channel_id = in.readString();
        this.is_add = in.readByte() != 0;
        this.is_removable = in.readByte() != 0;
    }

    public static final Creator<IFengColumnEntity> CREATOR = new Creator<IFengColumnEntity>() {
        @Override
        public IFengColumnEntity createFromParcel(Parcel source) {
            return new IFengColumnEntity(source);
        }

        @Override
        public IFengColumnEntity[] newArray(int size) {
            return new IFengColumnEntity[size];
        }
    };
}
