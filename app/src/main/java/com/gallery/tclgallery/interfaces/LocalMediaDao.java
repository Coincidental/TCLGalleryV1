package com.gallery.tclgallery.interfaces;

import com.gallery.tclgallery.bean.LocalMediaBean;

import java.util.List;

/**
 * Created by liddo on 2017/10/10.
 */

public interface LocalMediaDao {

    /**
     * get all localMedia
     * @return
     */
    List<LocalMediaBean> getAllLocalMedia();

    /**
     * init local media when the gallery app first open
     *
     */
    void initLocalMedia();

    /**
     * insert local media
     * @param localMediaBean
     */
    void insertLocalMedia(LocalMediaBean localMediaBean);

    /**
     * delete local media
     * @param localMediaBean
     */
    void deleteLocalMedia(LocalMediaBean localMediaBean);

    /**
     * update local media
     */
    void updateLocalMedia(LocalMediaBean localMediaBean);

    /**
     * get local media by local_id
     * @param local_id
     * @return
     */
    LocalMediaBean getLocalMediaBeanByLocalId(int local_id);

    /**
     * get local media by type
     * @param local_id
     * @return
     */
    LocalMediaBean getLocalMediaBeanByType(int local_id);

    /**
     * get local media by album tag id
     * @param tag_id
     * @return
     */
    List<LocalMediaBean> getLocalMediaBeanByAlbumTagId(int tag_id);

    /**
     * get local media by album name
     * @param album_name
     * @return
     */
    List<LocalMediaBean> getLocalMediaBeanByAlbumName(String album_name);
}
