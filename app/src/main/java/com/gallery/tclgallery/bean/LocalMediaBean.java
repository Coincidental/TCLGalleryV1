package com.gallery.tclgallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liddo on 2017/10/9.
 */

public class LocalMediaBean implements Parcelable {

    /**
     * local_id
     */
    private int local_id;

    /**
     * name
     */
    private String name;

    /**
     * bucketName
     */
    private String bucketName;

    /**
     * type
     */
    private String type;

    /**
     * mime_type
     */
    private String mime_type;

    /**
     * create_at
     */
    private int created_at;

    /**
     * generated_at
     */
    private int generated_at;

    /**
     * local_path
     */
    private String local_path;

    /**
     * thumbnail path
     */
    private String thumbPath;

    /**
     * size
     */
    private int size;

    /**
     * taken_at
     */
    private int taken_at;

    /**
     * latitude
     */
    private float latitude;

    /**
     * longitude
     */
    private float longitude;

    /**
     * location
     */
    private String location;

    /**
     * duration
     */
    private int duration;

    /**
     * secret
     */
    private int secret;

    /**
     * width
     */
    private int width;

    /**
     * height
     */
    private int height;

    /**
     * orientation
     * 0 横向
     * 1 纵向
     */
    private int orientation;

    /**
     * visible
     * 0 不显示
     * 1 显示
     */
    private int visible;

    /**
     * checked
     */
    private boolean isChecked;

    public LocalMediaBean() {}

    public int getLocal_id() {
        return local_id;
    }

    public void setLocal_id(int local_id) {
        this.local_id = local_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getGenerated_at() {
        return generated_at;
    }

    public void setGenerated_at(int generated_at) {
        this.generated_at = generated_at;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTaken_at() {
        return taken_at;
    }

    public void setTaken_at(int taken_at) {
        this.taken_at = taken_at;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSecret() {
        return secret;
    }

    public void setSecret(int secret) {
        this.secret = secret;
    }

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

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected LocalMediaBean(Parcel in) {
        local_id = in.readInt();
        name = in.readString();
        bucketName = in.readString();
        type = in.readString();
        mime_type = in.readString();
        created_at = in.readInt();
        generated_at = in.readInt();
        local_path = in.readString();
        thumbPath = in.readString();
        size = in.readInt();
        taken_at = in.readInt();
        latitude = in.readFloat();
        longitude = in.readFloat();
        location = in.readString();
        duration = in.readInt();
        secret = in.readInt();
        width = in.readInt();
        height = in.readInt();
        orientation = in.readInt();
        visible = in.readInt();
        isChecked = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(local_id);
        dest.writeString(name);
        dest.writeString(bucketName);
        dest.writeString(type);
        dest.writeString(mime_type);
        dest.writeInt(created_at);
        dest.writeInt(generated_at);
        dest.writeString(local_path);
        dest.writeString(thumbPath);
        dest.writeInt(size);
        dest.writeInt(taken_at);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(location);
        dest.writeInt(duration);
        dest.writeInt(secret);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeInt(orientation);
        dest.writeInt(visible);
        dest.writeByte((byte)(isChecked? 1 : 0));
    }

    public static final Creator<LocalMediaBean> CREATOR = new Creator<LocalMediaBean>() {
        @Override
        public LocalMediaBean createFromParcel(Parcel source) {
            return new LocalMediaBean(source);
        }

        @Override
        public LocalMediaBean[] newArray(int size) {
            return new LocalMediaBean[size];
        }
    };
}
