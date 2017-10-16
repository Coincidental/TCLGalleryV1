package com.gallery.tclgallery.model;

import android.content.ContentValues;
import android.content.Context;
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

    }

    @Override
    public ArrayList<LocalMedia_AlbumTag> queryAllLocalMediaAlbumTag() {
        return null;
    }

    @Override
    public LocalMedia_AlbumTag queryMediaAlbumTagBylocal_id(int local_id) {
        return null;
    }

    @Override
    public ArrayList<LocalMedia_AlbumTag> queryMediaAlbumTagByTag_id(int tag_id) {
        return null;
    }
}
