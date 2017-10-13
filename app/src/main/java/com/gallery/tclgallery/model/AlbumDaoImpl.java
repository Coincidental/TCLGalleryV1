package com.gallery.tclgallery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gallery.tclgallery.bean.AlbumTag;
import com.gallery.tclgallery.interfaces.AlbumDao;
import com.gallery.tclgallery.utils.DateBaseHelper;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/13.
 */

public class AlbumDaoImpl implements AlbumDao {
    public static final String TAG = "AlbumDaoImpl";

    private Context mContext;

    public AlbumDaoImpl(Context context) {
        mContext = context;
    }

    @Override
    public void insertAlbumTag(AlbumTag albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tag_id",albumTag.getTag_id());
        cv.put("type",albumTag.getType());
        cv.put("name",albumTag.getName());
        cv.put("display_name",albumTag.getDisplay_name());
        cv.put("local_path",albumTag.getLocal_path());
        cv.put("visible",albumTag.getVisible());
        cv.put("item_count",albumTag.getItem_count());
        cv.put("default_album",albumTag.getDefalult());
        cv.put("last_update_time",albumTag.getLast_update_time());
        long result = sqLiteDatabase.insert(DateBaseHelper.ALBUM_TAG_DB_TABLE,null,cv);
        if (result>0) {
            Log.i(TAG,"insert Album tag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteAlbumTagById(int tag_id) {

    }

    @Override
    public void deleteAlbumTag(AlbumTag albumTag) {

    }

    @Override
    public void updateAlbumTag(AlbumTag albumTag) {

    }

    @Override
    public ArrayList<AlbumTag> queryAllAlbumTag() {
        return null;
    }

    @Override
    public AlbumTag queryAlbumTagById(int tag_id) {
        return null;
    }

    @Override
    public ArrayList<AlbumTag> queryOtherAlbumTags() {
        return null;
    }
}
