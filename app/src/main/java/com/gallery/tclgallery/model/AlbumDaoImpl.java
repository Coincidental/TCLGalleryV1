package com.gallery.tclgallery.model;

import android.content.Context;

import com.gallery.tclgallery.bean.AlbumTag;
import com.gallery.tclgallery.interfaces.AlbumDao;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/13.
 */

public class AlbumDaoImpl implements AlbumDao {
    private Context mContext;

    public AlbumDaoImpl(Context context) {
        mContext = context;
    }

    @Override
    public void insertAlbumTag(AlbumTag albumTag) {
        
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
