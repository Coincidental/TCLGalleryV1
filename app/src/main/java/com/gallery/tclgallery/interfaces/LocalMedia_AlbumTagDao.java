package com.gallery.tclgallery.interfaces;

import com.gallery.tclgallery.bean.LocalMedia_AlbumTag;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/13.
 */

public interface LocalMedia_AlbumTagDao {
    void insertLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag);

    void deleteLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag);

    void deleteLocalMediaAlbumTagByLocalId(int local_id);

    void deleteLocalMediaAlbumTagByTagId(int tag_id);

    void updateLocalMediaAlbumTag(LocalMedia_AlbumTag media_albumTag);

    ArrayList<LocalMedia_AlbumTag> queryAllLocalMediaAlbumTag();

    ArrayList<LocalMedia_AlbumTag> queryMediaAlbumTagBylocal_id(int local_id);

    ArrayList<LocalMedia_AlbumTag> queryMediaAlbumTagByTag_id(int tag_id);
}
