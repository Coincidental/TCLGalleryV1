package com.gallery.tclgallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liddo on 2017/10/12.
 * 多媒体文件对应相册的关系类
 */

public class LocalMedia_AlbumTag implements Parcelable{
    /**
     * id
     */
    private int id;
    /**
     * local_media id
     */
    private int local_id;
    /**
     * album_tag id
     */
    private int album_id;

    public LocalMedia_AlbumTag() {}

    public LocalMedia_AlbumTag(int local_id,int album_id) {
        this.local_id = local_id;
        this.album_id = album_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    protected LocalMedia_AlbumTag(Parcel in) {
        id = in.readInt();
        local_id = in.readInt();
        album_id = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(local_id);
        parcel.writeInt(album_id);
    }

    public static final Creator<LocalMedia_AlbumTag> CREATOR = new Creator<LocalMedia_AlbumTag>() {
        @Override
        public LocalMedia_AlbumTag createFromParcel(Parcel parcel) {
            return new LocalMedia_AlbumTag(parcel);
        }

        @Override
        public LocalMedia_AlbumTag[] newArray(int i) {
            return new LocalMedia_AlbumTag[i];
        }
    };
}
