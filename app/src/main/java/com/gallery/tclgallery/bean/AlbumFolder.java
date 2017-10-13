package com.gallery.tclgallery.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/9/20.
 */

public class AlbumFolder implements Parcelable {
    public static final String DEFAULT_ALBUM_CAMERA = "Camera";
    public static final String DEFAULT_ALBUM_FAVOURITES = "Favourites";
    public static final String DEFAULT_ALBUM_SELFIES = "Selfies";
    public static final String DEFAULT_ALBUM_VIDEOS = "Videos";
    public static final String DEFAULT_ALBUM_SCREENSHOTS = "Screenshots";
    public static final String DEFAULT_ALBUM_MY_CREATIONS = "My Creations";
    public static final String DEFAULT_ALBUM_CINEMAGRAPH = "Cinemagraph";
    public static final String DEFAULT_ALBUM_PRIVATE = "Private";
    public static final String DEFAULT_ALBUM_USER_CREATED_ALBUM = "User Created Album";
    public static final String DEFAULT_ALBUM_OTHER = "Other";

    private int id;
    /**
     * Album name
     */
    private String name;
    /**
     * Image list in folder
     */
    private ArrayList<AlbumFile> mAlbumFile = new ArrayList<>();
    /**
     * checked
     */
    private boolean isChecked;

    private String typeName;

    public AlbumFolder() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AlbumFile> getmAlbumFile() {
        return mAlbumFile;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setmAlbumFile(ArrayList<AlbumFile> mAlbumFile) {
        this.mAlbumFile = mAlbumFile;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void addAlbumFile(AlbumFile albumFile){
        if (!mAlbumFile.contains(albumFile)){
            mAlbumFile.add(albumFile);
        }
    }

    protected AlbumFolder(Parcel in){
        id = in.readInt();
        name = in.readString();
        mAlbumFile = in.createTypedArrayList(AlbumFile.CREATOR);
        isChecked = in.readByte() != 0;
        typeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(mAlbumFile);
        parcel.writeByte((byte)(isChecked ? 1:0));
        parcel.writeString(typeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        @Override
        public AlbumFolder createFromParcel(Parcel parcel) {
            return new AlbumFolder(parcel);
        }

        @Override
        public AlbumFolder[] newArray(int i) {
            return new AlbumFolder[i];
        }
    };
}
