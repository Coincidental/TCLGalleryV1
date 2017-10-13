package com.gallery.tclgallery.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by liddo on 2017/9/20.
 */

public class AlbumFile implements Comparable<AlbumFile>, Parcelable {
    public static final int TYPE_INVALID = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaType{
    }

    /**
     * id
     */
    private int id;
    /**
     * File path.
     */
    private String mPath;
    /**
     * File name.
     */
    private String mName;
    /**
     * File title
     */
    private String mTitle;
    /**
     * Folder id.
     */
    private int mBucketId;
    /**
     * Folder name
     */
    private String mBucketName;
    /**
     * MimeType
     */
    private String mMimeType;
    /**
     * Add date.
     */
    private long mAddDate;
    /**
     * Modify date
     */
    private long mModifyDate;
    /**
     * Latitude
     */
    private float mLatitude;
    /**
     * Longitude
     */
    private float mLongitude;
    /**
     * File size.
     */
    private long mSize;
    /**
     *  Duration
     */
    private long mDuration;
    /**
     * Resolution
     */
    private String mResolution;
    /**
     * thumbnail path
     */
    private String mThumbPath;
    /**
     * width
     */
    private int mWidth;
    /**
     * height
     */
    private int mHeight;
    /**
     * MediaType
     */
    private int mMediaType;
    /**
     * checked
     */
    private boolean isChecked;

    public AlbumFile() {
    }

    @Override
    public int compareTo(AlbumFile albumFile) {
        long time = albumFile.getmAddDate() - getmAddDate();
        if (time > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (time < -Integer.MAX_VALUE){
            return -Integer.MAX_VALUE;
        }
        return (int)time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AlbumFile) {
            AlbumFile o = (AlbumFile)obj;
            if (!TextUtils.isEmpty(mPath) && !TextUtils.isEmpty(o.getmPath())){
                return o.getmPath().equals(mPath);
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        String key = id + mBucketId + mPath;
        return key.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmBucketId() {
        return mBucketId;
    }

    public void setmBucketId(int mBucketId) {
        this.mBucketId = mBucketId;
    }

    public String getmBucketName() {
        return mBucketName;
    }

    public void setmBucketName(String mBucketName) {
        this.mBucketName = mBucketName;
    }

    public String getmMimeType() {
        return mMimeType;
    }

    public void setmMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public long getmAddDate() {
        return mAddDate;
    }

    public void setmAddDate(long mAddDate) {
        this.mAddDate = mAddDate;
    }

    public long getmModifyDate() {
        return mModifyDate;
    }

    public void setmModifyDate(long mModifyDate) {
        this.mModifyDate = mModifyDate;
    }

    public float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public String getmResolution() {
        return mResolution;
    }

    public void setmResolution(String mResolution) {
        this.mResolution = mResolution;
    }

    public String getmThumbPath() {
        return mThumbPath;
    }

    public void setmThumbPath(String mThumbPath) {
        this.mThumbPath = mThumbPath;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getmMediaType() {
        return mMediaType;
    }

    public void setmMediaType(int mMediaType) {
        this.mMediaType = mMediaType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected AlbumFile(Parcel in) {
        id = in.readInt();
        mPath = in.readString();
        mName = in.readString();
        mTitle = in.readString();
        mBucketId = in.readInt();
        mBucketName = in.readString();
        mMimeType = in.readString();
        mAddDate = in.readLong();
        mModifyDate = in.readLong();
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
        mSize = in.readLong();
        mDuration = in.readLong();
        mResolution = in.readString();
        mThumbPath = in.readString();
        mWidth = in.readInt();
        mHeight = in.readInt();
        mMediaType = in.readInt();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mPath);
        parcel.writeString(mName);
        parcel.writeString(mTitle);
        parcel.writeInt(mBucketId);
        parcel.writeString(mBucketName);
        parcel.writeString(mMimeType);
        parcel.writeLong(mAddDate);
        parcel.writeLong(mModifyDate);
        parcel.writeFloat(mLatitude);
        parcel.writeFloat(mLongitude);
        parcel.writeLong(mSize);
        parcel.writeLong(mDuration);
        parcel.writeString(mResolution);
        parcel.writeString(mThumbPath);
        parcel.writeInt(mWidth);
        parcel.writeInt(mHeight);
        parcel.writeInt(mMediaType);
        parcel.writeByte((byte)(isChecked? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFile> CREATOR = new Creator<AlbumFile>() {
        @Override
        public AlbumFile createFromParcel(Parcel parcel) {
            return new AlbumFile(parcel);
        }

        @Override
        public AlbumFile[] newArray(int i) {
            return new AlbumFile[i];
        }
    };
}
