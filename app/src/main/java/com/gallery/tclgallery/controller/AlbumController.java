package com.gallery.tclgallery.controller;

import android.content.Context;

import com.gallery.tclgallery.bean.AlbumTag;
import com.gallery.tclgallery.bean.LocalMediaBean;
import com.gallery.tclgallery.bean.LocalMedia_AlbumTag;
import com.gallery.tclgallery.interfaces.AlbumDao;
import com.gallery.tclgallery.interfaces.LocalMedia_AlbumTagDao;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/13.
 */

public class AlbumController {

    private Context mContext;
    private AlbumDao albumDao;
    private LocalMedia_AlbumTagDao media_albumTagDao;

    public AlbumController(Context context, AlbumDao albumDao, LocalMedia_AlbumTagDao media_albumTagDao) {
        mContext = context;
        this.albumDao = albumDao;
        this.media_albumTagDao = media_albumTagDao;
    }

    /**
     * 创建相册
     * @param name 相册名
     * @param localMediaBeen 选择的数据
     * @param type move / copy
     * @param originAlbum 原先的相册
     */
    public void createAlbum(String name, ArrayList<LocalMediaBean> localMediaBeen,int type, AlbumTag originAlbum){
        AlbumTag newAlbum = new AlbumTag();
        int system_time = (int)System.currentTimeMillis();
        newAlbum.setTag_id(system_time);
        newAlbum.setName(name);
        newAlbum.setDisplay_name(name);
        newAlbum.setType(100); // 自建相册类型
        newAlbum.setLocal_path("");
        newAlbum.setLast_update_time(system_time);
        newAlbum.setItem_count(0);
        newAlbum.setVisible(1);
        newAlbum.setDefault_album(0);
        albumDao.insertAlbumTag(newAlbum);

        if (type == 0) {
            // copy to album
            copyToAlbum(localMediaBeen,newAlbum,originAlbum);
        } else {
            // move to album
            moveToAlbum(localMediaBeen,newAlbum,originAlbum);
        }
    }

    /**
     *  Copy到相册
     * @param localMediaBeen 选择的数据
     * @param album 相册
     */
    public void copyToAlbum(ArrayList<LocalMediaBean> localMediaBeen, AlbumTag album, AlbumTag originAlbum){
        //直接创建关系类，写入数据库
        for (LocalMediaBean localMediaBean:localMediaBeen) {
            // 更新相册
            int item_count = album.getItem_count();
            album.setLast_update_time((int)System.currentTimeMillis());
            album.setItem_count(item_count+1);
            albumDao.updateAlbumTag(album);
            // 创建关系表
            LocalMedia_AlbumTag copyLocal_Tag = new LocalMedia_AlbumTag(localMediaBean.getLocal_id(),album.getTag_id());
            media_albumTagDao.insertLocalMediaAlbumTag(copyLocal_Tag);
        }
    }

    /**
     * Move到相册
     * @param localMediaBeen 选择的数据
     * @param album 相册
     */
    public void moveToAlbum(ArrayList<LocalMediaBean> localMediaBeen, AlbumTag album, AlbumTag originAlbum) {
        for (LocalMediaBean localMediaBean:localMediaBeen) {
            // 删除数据库中原来的关系
            LocalMedia_AlbumTag originLocal_Tag = new LocalMedia_AlbumTag(localMediaBean.getLocal_id(),originAlbum.getTag_id());
            media_albumTagDao.deleteLocalMediaAlbumTag(originLocal_Tag);
            // 更新原相册
            int item_count_origin= originAlbum.getItem_count();
            originAlbum.setItem_count(item_count_origin-1);
            originAlbum.setLast_update_time((int)System.currentTimeMillis());
            albumDao.updateAlbumTag(originAlbum);
            // 创建关系类，写入数据库
            LocalMedia_AlbumTag dstLocal_Tag = new LocalMedia_AlbumTag(localMediaBean.getLocal_id(),album.getTag_id());
            media_albumTagDao.insertLocalMediaAlbumTag(dstLocal_Tag);
            // 更新新相册
            int item_count_new = album.getItem_count();
            album.setLast_update_time((int)System.currentTimeMillis());
            album.setItem_count(item_count_new+1);
            albumDao.updateAlbumTag(album);
        }
    }

    /**
     * 将相册移动到Others相册中
     * @param album 要移动的相册
     */
    public void moveToOthers(AlbumTag album) {
        if (album.getDefault_album()==0) {
            album.setVisible(0);
            album.setLast_update_time((int) System.currentTimeMillis());
            albumDao.updateAlbumTag(album);
        }
    }

    /**
     * 从其他相册中移除
     * @param album
     */
    public void moveBackToAlbum(AlbumTag album) {
        album.setVisible(1);
        album.setLast_update_time((int)System.currentTimeMillis());
        albumDao.updateAlbumTag(album);
    }

    /**
     * 重命名相册
     * @param album
     * @param newName
     */
    public void renameAlbum(AlbumTag album, String newName) {
        album.setName(newName);
        album.setDisplay_name(newName);
        album.setLast_update_time((int)System.currentTimeMillis());
        albumDao.updateAlbumTag(album);
    }

    /**
     * 删除相册
     * @param album
     */
    public void deleteAlbum(AlbumTag album) {

    }
}
