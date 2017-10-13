package com.gallery.tclgallery.interfaces;

import com.gallery.tclgallery.bean.AlbumTag;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/10.
 */

public interface AlbumDao {
    void insertAlbumTag(AlbumTag albumTag);

    void deleteAlbumTagById(int tag_id);

    void deleteAlbumTag(AlbumTag albumTag);

    void updateAlbumTag(AlbumTag albumTag);

    ArrayList<AlbumTag> queryAllAlbumTag();

    AlbumTag queryAlbumTagById(int tag_id);

    ArrayList<AlbumTag> queryOtherAlbumTags();

}
