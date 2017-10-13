package com.gallery.tclgallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/9.
 */

public class AlbumTag implements Parcelable {

    /**
     * tag_id
     */
    private int tag_id;
    /**
     * type
     */
    private int type;
    /**
     * name
     */
    private String name;
    /**
     * display_name
     */
    private String display_name;
    /**
     * local_path
     */
    private String local_path;
    /**
     * visible
     */
    private int visible;
    /**
     * item_count
     */
    private int item_count;
    /**
     * last update time
     */
    private int last_update_time;
    /**
     * media list in album
     */
    private ArrayList<LocalMediaBean> mediaBeans;
    /**
     * checked
     */
    private boolean isChecked;

    public AlbumTag() {
        mediaBeans = new ArrayList<>();
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public int getLast_update_time() {
        return last_update_time;
    }

    public void setLast_update_time(int last_update_time) {
        this.last_update_time = last_update_time;
    }

    public ArrayList<LocalMediaBean> getMediaBeans() {
        if (mediaBeans==null) {
            mediaBeans = new ArrayList<>();
        }
        return mediaBeans;
    }

    public void setMediaBeans(ArrayList<LocalMediaBean> mediaBeans) {
        this.mediaBeans = new ArrayList<>();
        this.mediaBeans.addAll(mediaBeans);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected AlbumTag(Parcel in){
        tag_id = in.readInt();
        type = in.readInt();
        name = in.readString();
        display_name = in.readString();
        visible = in.readInt();
        item_count = in.readInt();
        last_update_time = in.readInt();
        mediaBeans = in.createTypedArrayList(LocalMediaBean.CREATOR);
        isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tag_id);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeString(display_name);
        dest.writeInt(visible);
        dest.writeInt(item_count);
        dest.writeInt(last_update_time);
        dest.writeTypedList(mediaBeans);
        dest.writeByte((byte)(isChecked ? 1:0));
    }

    public static final Creator<AlbumTag> CREATOR = new Creator<AlbumTag>() {
        @Override
        public AlbumTag createFromParcel(Parcel source) {
            return new AlbumTag(source);
        }

        @Override
        public AlbumTag[] newArray(int size) {
            return new AlbumTag[size];
        }
    };
}
