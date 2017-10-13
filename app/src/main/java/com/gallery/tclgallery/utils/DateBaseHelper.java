package com.gallery.tclgallery.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liddo on 2017/9/30.
 */

public class DateBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String GALLERY_TCL_DB_NAME = "gallery_tcl";

    public static final String LOCAL_MEDIA_DB_TABLE = "Local_media";
    public static final String LOCAL_MEDIA_DB_ID = "local_id";
    public static final String LOCAL_MEDIA_DB_NAME = "name";
    public static final String LOCAL_MEDIA_DB_BUCKET_ID = "bucket_name";
    public static final String LOCAL_MEDIA_DB_TYPE = "type";
    public static final String LOCAL_MEDIA_DB_MIME_TYPE = "mime_type";
    public static final String LOCAL_MEDIA_DB_CREATE_AT = "create_at";
    public static final String LOCAL_MEDIA_DB_GENERATED_AT = "generated_at";
    public static final String LOCAL_MEDIA_DB_LOCAL_PATH = "local_path";
    public static final String LOCAL_MEDIA_DB_THUMBNAIL_PATH = "thumbnail_path";
    public static final String LOCAL_MEDIA_DB_SIZE = "size";
    public static final String LOCAL_MEDIA_DB_TAKEN_AT = "taken_at";
    public static final String LOCAL_MEDIA_DB_LATITUDE = "latitude";
    public static final String LOCAL_MEDIA_DB_LONGITUDE = "longitude";
    public static final String LOCAL_MEDIA_DB_LOCATION = "location";
    public static final String LOCAL_MEDIA_DB_DURATION = "duration";
    public static final String LOCAL_MEDIA_DB_SECRET = "secret";
    public static final String LOCAL_MEDIA_DB_WIDTH = "width";
    public static final String LOCAL_MEDIA_DB_HEIGHT = "height";
    public static final String LOCAL_MEDIA_DB_ORIENTATION = "orientation";
    public static final String LOCAL_MEDIA_DB_VISIBLE = "visible";

    public static final String ALBUM_TAG_DB_TABLE = "Album_tag";
    public static final String ALBUM_TAG_DB_TAG_ID = "tag_id";
    public static final String ALBUM_TAG_DB_TYPE = "type";
    public static final String ALBUM_TAG_DB_NAME = "name";
    public static final String ALBUM_TAG_DB_DISPLAY_NAME = "display_name";
    public static final String ALBUM_TAG_DB_LOCAL_PATH = "local_path";
    public static final String ALBUM_TAG_DB_VISIBLE = "visible";
    public static final String ALBUM_TAG_DB_ITEM_COUNT = "item_count";
    public static final String ALBUM_TAG_DB_LAST_UPDATE_TIME = "last_update_time";

    public static final String LOCAL_MEDIA_ALBUM_TAG_TABLE = "local_media_album_tag";
    public static final String LOCAL_MEDIA_ALBUM_TAG_ID = "id";
    public static final String LOCAL_MEDIA_ALBUM_TAG_LOCAL_ID = "local_id";
    public static final String LOCAL_MEDIA_ALBUM_TAG_ALBUM_TAG_ID = "tag_id";

    public DateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DateBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public DateBaseHelper(Context context, String name) {
        this(context, name, null,VERSION);
    }

    public DateBaseHelper(Context context) {
        this(context, GALLERY_TCL_DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sbLocalMedia = new StringBuilder();
        sbLocalMedia.append("create table if not exists ");
        sbLocalMedia.append(LOCAL_MEDIA_DB_TABLE);
        sbLocalMedia.append("(");
        sbLocalMedia.append(LOCAL_MEDIA_DB_ID).append(" integer primary key not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_NAME).append(" text not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_BUCKET_ID).append(" text not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_TYPE).append(" text not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_MIME_TYPE).append(" text,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_CREATE_AT).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_GENERATED_AT).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_LOCAL_PATH).append(" text,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_THUMBNAIL_PATH).append(" text,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_SIZE).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_TAKEN_AT).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_LATITUDE).append(" real not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_LONGITUDE).append(" real not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_LOCATION).append(" text,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_DURATION).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_SECRET).append(" integer not null,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_WIDTH).append(" integer,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_HEIGHT).append(" integer,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_ORIENTATION).append(" integer,");
        sbLocalMedia.append(LOCAL_MEDIA_DB_VISIBLE).append(" integer not null");
        sbLocalMedia.append(")");

        db.execSQL(sbLocalMedia.toString());

        StringBuilder sbAlbumTag = new StringBuilder();
        sbAlbumTag.append("create table if not exists ");
        sbAlbumTag.append(ALBUM_TAG_DB_TABLE);
        sbAlbumTag.append("(");
        sbAlbumTag.append(ALBUM_TAG_DB_TAG_ID).append(" integer primary key not null,");
        sbAlbumTag.append(ALBUM_TAG_DB_TYPE).append(" integer not null,");
        sbAlbumTag.append(ALBUM_TAG_DB_NAME).append(" text,");
        sbAlbumTag.append(ALBUM_TAG_DB_DISPLAY_NAME).append(" text,");
        sbAlbumTag.append(ALBUM_TAG_DB_LOCAL_PATH).append(" text,");
        sbAlbumTag.append(ALBUM_TAG_DB_VISIBLE).append(" integer not null,");
        sbAlbumTag.append(ALBUM_TAG_DB_ITEM_COUNT).append(" integer not null,");
        sbAlbumTag.append(ALBUM_TAG_DB_LAST_UPDATE_TIME).append(" integer not null");
        sbAlbumTag.append(")");

        db.execSQL(sbAlbumTag.toString());

        StringBuilder sbLocalMediaAlbumTag = new StringBuilder();
        sbAlbumTag.append("create table if not exists ");
        sbAlbumTag.append(LOCAL_MEDIA_ALBUM_TAG_TABLE);
        sbAlbumTag.append("(");
        sbAlbumTag.append(LOCAL_MEDIA_ALBUM_TAG_ID).append(" integer primary key not null autoincrement,");
        sbAlbumTag.append(LOCAL_MEDIA_ALBUM_TAG_LOCAL_ID).append(" integer not null,");
        sbAlbumTag.append(LOCAL_MEDIA_ALBUM_TAG_ALBUM_TAG_ID).append(" integer not null");
        sbAlbumTag.append(")");

        //db.execSQL(sbLocalMediaAlbumTag.toString());

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
