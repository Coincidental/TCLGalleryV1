package com.gallery.tclgallery.bean;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017/9/20.
 */
public class CameraItem implements Serializable {
    public static final int TYPE_INVALID = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_INVALID, TYPE_IMAGE, TYPE_VIDEO})
    public @interface MediaType {
    }

    private String mName;
    private String path;                    // 路径
    private String thumbnail;               // 缩略图
    private int Id;                    // ID
    private int Width;                 // 宽度
    private int Height;                // 高度
    private int size;                       // 图片大小
    private int tokenDate;                  // 拍摄时间
    private long modified;                  // 修改时间
    private int orientation;                // 照片方向
    private double latitude;                // 纬度
    private double longitude;               // 经度
    private double altitude;                // 海拔
    private long duration;                //时长
    private String mime_type;                     //类型
    private boolean isSelected;

    public void setId(int id) {
        this.Id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(int tokenDate) {
        this.tokenDate = tokenDate;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {

        return Id;
    }

    public CameraItem(/*String mName,*/int id, String path, int width, int height, int size,double latitude, double longitude, double altitude, int orientation,int tokenDate, long modified,  long duration, String mime_type) {
        this.mName = mName;
        this.path = path;
        Id = id;
        Width = width;
        Height = height;
        this.size = size;
        this.tokenDate = tokenDate;
        this.modified = modified;
        this.orientation = orientation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.duration = duration;
        this.mime_type = mime_type;
        this.isSelected = isSelected;
    }
}
