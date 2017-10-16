package com.gallery.tclgallery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        cv.put("default_album",albumTag.getDefault_album());
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
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(DateBaseHelper.ALBUM_TAG_DB_TABLE,"tag_id = ?",new String[]{tag_id+""});
        if (result > 0) {
            Log.i(TAG, "delete album succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteAlbumTag(AlbumTag albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(DateBaseHelper.ALBUM_TAG_DB_TABLE,"tag_id = ?",new String[]{albumTag.getTag_id()+""});
        if (result > 0) {
            Log.i(TAG, "delete album succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void updateAlbumTag(AlbumTag albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type",albumTag.getType());
        cv.put("name",albumTag.getName());
        cv.put("display_name",albumTag.getDisplay_name());
        cv.put("local_path",albumTag.getLocal_path());
        cv.put("visible",albumTag.getVisible());
        cv.put("item_count",albumTag.getItem_count());
        cv.put("default_album",albumTag.getDefault_album());
        cv.put("last_update_time",albumTag.getLast_update_time());
        int result = sqLiteDatabase.update(DateBaseHelper.ALBUM_TAG_DB_TABLE,cv,"tag_id = ?",new String[]{albumTag.getTag_id()+""});
        if (result > 0) {
            Log.i(TAG,"update album succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public ArrayList<AlbumTag> queryAllAlbumTag() {
        ArrayList<AlbumTag> albumList = new ArrayList<>();
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.ALBUM_TAG_DB_TABLE,DateBaseHelper.ALBUM_TAG_COLUMNS,null,null,null,null,DateBaseHelper.ALBUM_TAG_DB_TAG_ID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int tag_id = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[0]));
                int type = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[1]));
                String name = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[2]));
                String displayName = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[3]));
                String localPath = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[4]));
                int visible = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[5]));
                int itemCount = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[6]));
                int defaultAlbum = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[7]));
                int lastUpdateTime = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[8]));

                AlbumTag albumTag = new AlbumTag();
                albumTag.setTag_id(tag_id);
                albumTag.setType(type);
                albumTag.setName(name);
                albumTag.setDisplay_name(displayName);
                albumTag.setLocal_path(localPath);
                albumTag.setVisible(visible);
                albumTag.setItem_count(itemCount);
                albumTag.setDefault_album(defaultAlbum);
                albumTag.setLast_update_time(lastUpdateTime);

                albumList.add(albumTag);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return albumList;
    }

    @Override
    public AlbumTag queryAlbumTagById(int tag_id) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        AlbumTag albumTag = new AlbumTag();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.ALBUM_TAG_DB_TABLE,DateBaseHelper.ALBUM_TAG_COLUMNS,"tag_id = ?",new String[]{""+tag_id},null,null,null);
        if (cursor!=null) {
            if (cursor.moveToFirst()) {
                int type = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[1]));
                String name = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[2]));
                String displayName = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[3]));
                String localPath = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[4]));
                int visible = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[5]));
                int itemCount = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[6]));
                int defaultAlbum = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[7]));
                int lastUpdateTime = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[8]));

                albumTag.setTag_id(tag_id);
                albumTag.setType(type);
                albumTag.setName(name);
                albumTag.setDisplay_name(displayName);
                albumTag.setLocal_path(localPath);
                albumTag.setVisible(visible);
                albumTag.setItem_count(itemCount);
                albumTag.setDefault_album(defaultAlbum);
                albumTag.setLast_update_time(lastUpdateTime);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return albumTag;
    }

    @Override
    public ArrayList<AlbumTag> queryOtherAlbumTags() {
        ArrayList<AlbumTag> albumList = new ArrayList<>();
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.ALBUM_TAG_DB_TABLE,DateBaseHelper.ALBUM_TAG_COLUMNS,"visible = 0",null,null,null,null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int tag_id = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[0]));
                int type = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[1]));
                String name = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[2]));
                String displayName = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[3]));
                String localPath = cursor.getString(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[4]));
                int visible = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[5]));
                int itemCount = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[6]));
                int defaultAlbum = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[7]));
                int lastUpdateTime = cursor.getInt(cursor.getColumnIndex(DateBaseHelper.ALBUM_TAG_COLUMNS[8]));

                AlbumTag albumTag = new AlbumTag();
                albumTag.setTag_id(tag_id);
                albumTag.setType(type);
                albumTag.setName(name);
                albumTag.setDisplay_name(displayName);
                albumTag.setLocal_path(localPath);
                albumTag.setVisible(visible);
                albumTag.setItem_count(itemCount);
                albumTag.setDefault_album(defaultAlbum);
                albumTag.setLast_update_time(lastUpdateTime);

                albumList.add(albumTag);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return albumList;
    }
}
