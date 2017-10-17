package com.gallery.tclgallery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gallery.tclgallery.bean.LocalMedia_AlbumTag;
import com.gallery.tclgallery.interfaces.LocalMedia_AlbumTagDao;
import com.gallery.tclgallery.utils.DateBaseHelper;

import java.util.ArrayList;


/**
 * Created by liddo on 2017/10/13.
 */

public class LocalMedia_AlbumTagDaoImpl implements LocalMedia_AlbumTagDao{
    public static final String TAG = "LocalMedia_AlbumTagDao";

    private Context mContext;

    public static final String[] LOCAL_MEDIA_ALBUM_TAG_COLUMN = {DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_ID, DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_LOCAL_ID, DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_ALBUM_TAG_ID};

    public LocalMedia_AlbumTagDaoImpl(Context context) {
        mContext = context;
    }

    @Override
    public void insertLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("local_id",media_albumTag.getLocal_id());
        cv.put("tag_id",media_albumTag.getAlbum_id());
        long result = sqLiteDatabase.insert(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE,null,cv);
        if(result>0) {
            Log.i(TAG,"insert localMedia_AlbumTag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE,"local_id = ? and tag_id = ?",new String[]{media_albumTag.getLocal_id()+"",media_albumTag.getAlbum_id()+""});
        if (result>0) {
            Log.i(TAG,"delete localMedia_AlbumTag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteLocalMediaAlbumTagByLocalId(int local_id) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE,"local_id = ?",new String[]{"" + local_id});
        if (result>0) {
            Log.i(TAG,"delete localMedia_AlbumTag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteLocalMediaAlbumTagByTagId(int tag_id) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int result = sqLiteDatabase.delete(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE,"tag_id = ?",new String[]{"" + tag_id});
        if (result>0) {
            Log.i(TAG,"delete localMedia_AlbumTag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void updateLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("local_id",media_albumTag.getLocal_id());
        cv.put("tag_id",media_albumTag.getAlbum_id());
        int result = sqLiteDatabase.update(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE,cv,"id = ?", new String[]{""+media_albumTag.getId()});
        if (result > 0) {
            Log.i(TAG,"update local media album tag succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public ArrayList<LocalMedia_AlbumTag> queryAllLocalMediaAlbumTag() {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<LocalMedia_AlbumTag> media_tagList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE, LOCAL_MEDIA_ALBUM_TAG_COLUMN,null,null,null,null,null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[0]));
                int local_id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[1]));
                int tag_id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[2]));

                LocalMedia_AlbumTag media_tag = new LocalMedia_AlbumTag();
                media_tag.setId(id);
                media_tag.setLocal_id(local_id);
                media_tag.setAlbum_id(tag_id);

                media_tagList.add(media_tag);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return media_tagList;
    }

    @Override
    public ArrayList<LocalMedia_AlbumTag> queryMediaAlbumTagBylocal_id(int local_id) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<LocalMedia_AlbumTag> media_tagList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE, LOCAL_MEDIA_ALBUM_TAG_COLUMN,"local_id = ?",new String[]{local_id+""},null,null,null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[0]));
                int tag_id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[2]));

                LocalMedia_AlbumTag media_tag = new LocalMedia_AlbumTag();
                media_tag.setId(id);
                media_tag.setLocal_id(local_id);
                media_tag.setAlbum_id(tag_id);

                media_tagList.add(media_tag);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return media_tagList;
    }

    @Override
    public ArrayList<LocalMedia_AlbumTag> queryMediaAlbumTagByTag_id(int tag_id) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ArrayList<LocalMedia_AlbumTag> media_tagList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DateBaseHelper.LOCAL_MEDIA_ALBUM_TAG_TABLE, LOCAL_MEDIA_ALBUM_TAG_COLUMN,"tag_id = ?",new String[]{tag_id+""},null,null,null);
        if (cursor != null) {
            while(cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[0]));
                int local_id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA_ALBUM_TAG_COLUMN[1]));

                LocalMedia_AlbumTag media_tag = new LocalMedia_AlbumTag();
                media_tag.setId(id);
                media_tag.setLocal_id(local_id);
                media_tag.setAlbum_id(tag_id);

                media_tagList.add(media_tag);
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        dbHelper.close();
        return media_tagList;
    }
}
