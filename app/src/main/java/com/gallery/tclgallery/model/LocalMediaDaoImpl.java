package com.gallery.tclgallery.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.constraint.solver.ArrayRow;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumTag;
import com.gallery.tclgallery.bean.LocalMediaBean;
import com.gallery.tclgallery.bean.LocalMedia_AlbumTag;
import com.gallery.tclgallery.interfaces.AlbumDao;
import com.gallery.tclgallery.interfaces.LocalMediaDao;
import com.gallery.tclgallery.interfaces.LocalMedia_AlbumTagDao;
import com.gallery.tclgallery.utils.DateBaseHelper;
import com.gallery.tclgallery.utils.LocalMediaScanTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liddo on 2017/10/10.
 */

public class LocalMediaDaoImpl implements LocalMediaDao {
    private static final String TAG = "LocalMediaDao";

    private Context mContext;
    private ArrayList<LocalMediaBean> localMedia;
    private ArrayList<AlbumTag> albumTags;
    private ArrayList<LocalMedia_AlbumTag> localMedia_TagList;
    private AlbumDao albumDao;
    private LocalMedia_AlbumTagDao localMedia_albumTagDao;

    private static final String[] LOCAL_MEDIA = {
            DateBaseHelper.LOCAL_MEDIA_DB_ID,
            DateBaseHelper.LOCAL_MEDIA_DB_TYPE,
            DateBaseHelper.LOCAL_MEDIA_DB_MIME_TYPE,
            DateBaseHelper.LOCAL_MEDIA_DB_CREATE_AT,
            DateBaseHelper.LOCAL_MEDIA_DB_GENERATED_AT,
            DateBaseHelper.LOCAL_MEDIA_DB_LOCAL_PATH,
            DateBaseHelper.LOCAL_MEDIA_DB_THUMBNAIL_PATH,
            DateBaseHelper.LOCAL_MEDIA_DB_SIZE,
            DateBaseHelper.LOCAL_MEDIA_DB_TAKEN_AT,
            DateBaseHelper.LOCAL_MEDIA_DB_LATITUDE,
            DateBaseHelper.LOCAL_MEDIA_DB_LONGITUDE,
            DateBaseHelper.LOCAL_MEDIA_DB_LOCATION,
            DateBaseHelper.LOCAL_MEDIA_DB_DURATION,
            DateBaseHelper.LOCAL_MEDIA_DB_SECRET,
            DateBaseHelper.LOCAL_MEDIA_DB_WIDTH,
            DateBaseHelper.LOCAL_MEDIA_DB_HEIGHT,
            DateBaseHelper.LOCAL_MEDIA_DB_ORIENTATION,
            DateBaseHelper.LOCAL_MEDIA_DB_VISIBLE
    };

    public LocalMediaDaoImpl(Context context,AlbumDao albumDao, LocalMedia_AlbumTagDao localMedia_albumTagDao) {
        mContext = context;
        this.albumDao = albumDao;
        this.localMedia_albumTagDao = localMedia_albumTagDao;
    }

    @Override
    public List<LocalMediaBean> getAllLocalMedia() {
        List<LocalMediaBean> allLocalMedia = new ArrayList<>();
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String sql = "select * from Local_media";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        if (cursor != null) {
            while(cursor.moveToNext()){
                int local_id = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[0]));
                String type = cursor.getString(cursor.getColumnIndex(LOCAL_MEDIA[1]));
                String mime_type = cursor.getString(cursor.getColumnIndex(LOCAL_MEDIA[2]));
                int created_at = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[3]));
                int generated_at = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[4]));
                String local_path = cursor.getString(cursor.getColumnIndex(LOCAL_MEDIA[5]));
                String thumbnail_path = cursor.getString(cursor.getColumnIndex(LOCAL_MEDIA[6]));
                int size = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[7]));
                int taken_at = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[8]));
                float latitude = cursor.getFloat(cursor.getColumnIndex(LOCAL_MEDIA[9]));
                float longitude = cursor.getFloat(cursor.getColumnIndex(LOCAL_MEDIA[10]));
                String location = cursor.getString(cursor.getColumnIndex(LOCAL_MEDIA[11]));
                int duration = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[12]));
                int secret  = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[13]));
                int width = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[14]));
                int height = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[15]));
                int orientation = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[16]));
                int visible = cursor.getInt(cursor.getColumnIndex(LOCAL_MEDIA[17]));

                LocalMediaBean localMedia = new LocalMediaBean();
                localMedia.setLocal_id(local_id);
                localMedia.setType(type);
                localMedia.setMime_type(mime_type);
                localMedia.setCreated_at(created_at);
                localMedia.setGenerated_at(generated_at);
                localMedia.setLocal_path(local_path);
                localMedia.setThumbPath(thumbnail_path);
                localMedia.setSize(size);
                localMedia.setTaken_at(taken_at);
                localMedia.setLatitude(latitude);
                localMedia.setLongitude(longitude);
                localMedia.setLocation(location);
                localMedia.setDuration(duration);
                localMedia.setSecret(secret);
                localMedia.setWidth(width);
                localMedia.setHeight(height);
                localMedia.setOrientation(orientation);
                localMedia.setVisible(visible);

                allLocalMedia.add(localMedia);
            }
            cursor.close();
        }
        return allLocalMedia;
    }

    @Override
    public void initLocalMedia() {
        //检索系统原生数据库中的音乐视频文件
        localMedia = new ArrayList<>();
        LocalMediaScanTask scanTask = new LocalMediaScanTask(mContext, new LocalMediaScanTask.Callback() {
            @Override
            public void onScanCallback(ArrayList<LocalMediaBean> localMediaList) {
                localMedia = localMediaList;
                // 将检索到的数据写入到本地数据表Local_media中
                for (LocalMediaBean localMediaBean:localMedia) {
                    insertLocalMedia(localMediaBean);
                }
                initAlbum();
                // 相册表，（每个相册包含图片）
                for(AlbumTag albumTag:albumTags){
                    Log.i("dongdong",albumTag.getTag_id()+"  "+albumTag.getName()+"   "+albumTag.getLocal_path());
                    ArrayList<LocalMediaBean> medias = albumTag.getMediaBeans();
                    for (LocalMediaBean albumMedias:medias) {
                        Log.i("dongdong","    "+albumMedias.getName());
                    }
                }
                // 计算最后修改时间和相册显示
                for (AlbumTag albumTag:albumTags) {
                    ArrayList<LocalMediaBean> medias = albumTag.getMediaBeans();
                    albumTag.setLast_update_time(getLastUpdateTime(medias));
                    if (albumTag.getTag_id()>0) {
                        albumTag.setVisible(1);
                    } else {
                        albumTag.setVisible(0);
                    }
                    albumTag.setItem_count(medias.size());
                    albumDao.insertAlbumTag(albumTag);
                }

                // 关系表
                for (LocalMedia_AlbumTag media_albumTag:localMedia_TagList) {
                    Log.i("dongdongli", media_albumTag.getId()+"  "+media_albumTag.getLocal_id()+"   "+media_albumTag.getAlbum_id());
                    localMedia_albumTagDao.insertLocalMediaAlbumTag(media_albumTag);
                }
            }
        });
        scanTask.execute();
        /** 计算分类到不同的相册中（形成关联表）
         *      1. 生成相册表（）
         *
         *
         */
    }

    /**
     *  type: 1.表示实际存在的相册，4，表示虚拟的相册（Favourites,CinemaGraph），101.私密相册，100，自建相册（无实际路径）
     *
     */
    private void initAlbum(){

        int tag_id = 0;
        int localMedia_AlbumTag_id = 0;
        albumTags = new ArrayList<>();
        localMedia_TagList = new ArrayList<>();
        ArrayList<String> album_local_path_list = new ArrayList<>();
        String[] default_album_local_path = mContext.getResources().getStringArray(R.array.default_album);

        // Camera
        AlbumTag albumDefaultCamera = new AlbumTag();
        albumDefaultCamera.setTag_id(1);
        albumDefaultCamera.setType(1);
        albumDefaultCamera.setName(mContext.getString(R.string.default_album_camera));
        albumDefaultCamera.setDisplay_name(mContext.getString(R.string.default_album_camera));
        albumDefaultCamera.setLocal_path(default_album_local_path[0]);
        albumDefaultCamera.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultCamera.setDefalult(1);
        albumTags.add(albumDefaultCamera);
        album_local_path_list.add(default_album_local_path[0]);
        //Favourites
        AlbumTag albumDefaultFavourite = new AlbumTag();
        albumDefaultFavourite.setTag_id(2);
        albumDefaultFavourite.setType(4);
        albumDefaultFavourite.setName(mContext.getString(R.string.default_album_favourites));
        albumDefaultFavourite.setDisplay_name(mContext.getString(R.string.default_album_favourites));
        albumDefaultFavourite.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultFavourite.setLocal_path("");
        albumDefaultFavourite.setDefalult(1);
        albumTags.add(albumDefaultFavourite);
        //Selfies
        AlbumTag albumDefaultSelfies = new AlbumTag();
        albumDefaultSelfies.setTag_id(3);
        albumDefaultSelfies.setType(1);
        albumDefaultSelfies.setName(mContext.getString(R.string.default_album_self));
        albumDefaultSelfies.setDisplay_name(mContext.getString(R.string.default_album_self));
        albumDefaultSelfies.setLocal_path(default_album_local_path[1]);
        albumDefaultSelfies.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultSelfies.setDefalult(1);
        albumTags.add(albumDefaultSelfies);
        album_local_path_list.add(default_album_local_path[1]);
        //Videos
        AlbumTag albumDefaultVideos = new AlbumTag();
        albumDefaultVideos.setTag_id(4);
        albumDefaultVideos.setType(1);
        albumDefaultVideos.setName(mContext.getString(R.string.default_album_videos));
        albumDefaultVideos.setDisplay_name(mContext.getString(R.string.default_album_videos));
        albumDefaultVideos.setLocal_path(default_album_local_path[2]);
        albumDefaultVideos.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultVideos.setDefalult(1);
        albumTags.add(albumDefaultVideos);
        album_local_path_list.add(default_album_local_path[2]);
        //ScreenShots
        AlbumTag albumDefaultScreenshots = new AlbumTag();
        albumDefaultScreenshots.setTag_id(5);
        albumDefaultScreenshots.setType(1);
        albumDefaultScreenshots.setName(mContext.getString(R.string.default_album_screenshots));
        albumDefaultScreenshots.setDisplay_name(mContext.getString(R.string.default_album_screenshots));
        albumDefaultScreenshots.setLocal_path(default_album_local_path[3]);
        albumDefaultScreenshots.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultScreenshots.setDefalult(1);
        albumTags.add(albumDefaultScreenshots);
        album_local_path_list.add(default_album_local_path[3]);
        //MyCreation
        AlbumTag albumDefaultMyCreation = new AlbumTag();
        albumDefaultMyCreation.setTag_id(6);
        albumDefaultMyCreation.setType(1);
        albumDefaultMyCreation.setName(mContext.getString(R.string.default_album_my_creation));
        albumDefaultMyCreation.setDisplay_name(mContext.getString(R.string.default_album_my_creation));
        albumDefaultMyCreation.setLocal_path(default_album_local_path[4]);
        albumDefaultMyCreation.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultMyCreation.setDefalult(1);
        albumTags.add(albumDefaultMyCreation);
        album_local_path_list.add(default_album_local_path[4]);
        //Cinema Graph
        AlbumTag albumDefaultCinemaGraph = new AlbumTag();
        albumDefaultCinemaGraph.setTag_id(7);
        albumDefaultCinemaGraph.setType(4);
        albumDefaultCinemaGraph.setName(mContext.getString(R.string.default_album_cinema_graph));
        albumDefaultCinemaGraph.setDisplay_name(mContext.getString(R.string.default_album_cinema_graph));
        albumDefaultCinemaGraph.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultCinemaGraph.setLocal_path("");
        albumDefaultCinemaGraph.setDefalult(1);
        albumTags.add(albumDefaultCinemaGraph);
        //Private
        AlbumTag albumDefaultPrivate = new AlbumTag();
        albumDefaultPrivate.setTag_id(8);
        albumDefaultPrivate.setType(101);
        albumDefaultPrivate.setName(mContext.getString(R.string.default_album_private));
        albumDefaultPrivate.setDisplay_name(mContext.getString(R.string.default_album_private));
        albumDefaultPrivate.setMediaBeans(new ArrayList<LocalMediaBean>());
        albumDefaultPrivate.setLocal_path("");
        albumDefaultPrivate.setDefalult(1);
        albumTags.add(albumDefaultPrivate);

        //localMedia_TagList
        for (LocalMediaBean localMediaBean: localMedia){
            String[] local_path_dir = localMediaBean.getLocal_path().split("\\/");
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<local_path_dir.length-1;i++){
                if (i!=local_path_dir.length-2) {
                    sb.append(local_path_dir[i] + "/");
                }else {
                    sb.append(local_path_dir[i]);
                }
            }
            String local_path = sb.toString();
            if (album_local_path_list.contains(local_path)){
                // 已存在的相册
                AlbumTag existAlbum = getAlbumTagByLocalPath(local_path);
                albumTags.remove(existAlbum);
                ArrayList<LocalMediaBean> localMedias = new ArrayList<>();
                localMedias.addAll(existAlbum.getMediaBeans());
                localMedias.add(localMediaBean);
                existAlbum.setMediaBeans(localMedias);
                int item_count = existAlbum.getItem_count();
                albumTags.add(existAlbum);
                LocalMedia_AlbumTag localMedia_albumTag = new LocalMedia_AlbumTag();
                localMedia_albumTag.setId(++localMedia_AlbumTag_id);
                localMedia_albumTag.setLocal_id(localMediaBean.getLocal_id());
                localMedia_albumTag.setAlbum_id(existAlbum.getTag_id());
                localMedia_TagList.add(localMedia_albumTag);
            } else {
                // 新相册
                AlbumTag newAlbum = new AlbumTag();
                newAlbum.setTag_id(--tag_id);
                newAlbum.setType(1);
                newAlbum.setName(localMediaBean.getBucketName());
                newAlbum.setDisplay_name(localMediaBean.getBucketName());
                newAlbum.setLocal_path(local_path);
                ArrayList<LocalMediaBean> localMediaList = new ArrayList<>();
                localMediaList.add(localMediaBean);
                newAlbum.setMediaBeans(localMediaList);
                album_local_path_list.add(local_path);
                albumTags.add(newAlbum);
                LocalMedia_AlbumTag localMedia_albumTag = new LocalMedia_AlbumTag();
                localMedia_albumTag.setId(++localMedia_AlbumTag_id);
                localMedia_albumTag.setLocal_id(localMediaBean.getLocal_id());
                localMedia_albumTag.setAlbum_id(newAlbum.getTag_id());
                localMedia_TagList.add(localMedia_albumTag);
            }
        }
    }

    /**
     * 通过Local_path查询现存数据表中的AlbumTag对象
     * @param local_path
     * @return
     */
    private AlbumTag getAlbumTagByLocalPath(String local_path) {
        for (AlbumTag albumTag:albumTags) {
            if (albumTag.getLocal_path()!=null) {
                if (albumTag.getLocal_path().equals(local_path)) {
                    return albumTag;
                }
            }
        }
        return null;
    }

    private int getLastUpdateTime(ArrayList<LocalMediaBean> localMedias){
        int lastUpdateTime = 0;
        if (localMedias.size()!=0) {
            lastUpdateTime = localMedias.get(0).getGenerated_at();
            for (LocalMediaBean localMediaBean : localMedias) {
                int tmp = localMediaBean.getGenerated_at();
                if (lastUpdateTime<tmp) {
                    lastUpdateTime = tmp;
                }
            }
        }
        return lastUpdateTime;
    }

    @Override
    public void insertLocalMedia(LocalMediaBean localMediaBean) {
        DateBaseHelper dbHelper = new DateBaseHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("local_id",localMediaBean.getLocal_id());
        cv.put("name",localMediaBean.getName());
        cv.put("bucket_name",localMediaBean.getBucketName());
        cv.put("type",localMediaBean.getType());
        cv.put("mime_type",localMediaBean.getMime_type());
        cv.put("create_at",localMediaBean.getCreated_at());
        cv.put("generated_at",localMediaBean.getGenerated_at());
        cv.put("local_path",localMediaBean.getLocal_path());
        cv.put("thumbnail_path",localMediaBean.getThumbPath());
        cv.put("size",localMediaBean.getSize());
        cv.put("taken_at",localMediaBean.getTaken_at());
        cv.put("latitude",localMediaBean.getLatitude());
        cv.put("longitude",localMediaBean.getLongitude());
        cv.put("location",localMediaBean.getLocation());
        cv.put("duration",localMediaBean.getDuration());
        cv.put("secret",localMediaBean.getSecret());
        cv.put("width",localMediaBean.getWidth());
        cv.put("height",localMediaBean.getHeight());
        cv.put("orientation",localMediaBean.getOrientation());
        cv.put("visible",localMediaBean.getVisible());
        long result = sqLiteDatabase.insert(DateBaseHelper.LOCAL_MEDIA_DB_TABLE,null,cv);
        if (result>0){
            Log.i(TAG,"insert local media succeed");
        }
        sqLiteDatabase.close();
        dbHelper.close();
    }

    @Override
    public void deleteLocalMedia(LocalMediaBean localMediaBean) {

    }

    @Override
    public void updateLocalMedia(LocalMediaBean localMediaBean) {

    }

    @Override
    public LocalMediaBean getLocalMediaBeanByLocalId(int local_id) {
        return null;
    }

    @Override
    public LocalMediaBean getLocalMediaBeanByType(int local_id) {
        return null;
    }

    @Override
    public List<LocalMediaBean> getLocalMediaBeanByAlbumTagId(int tag_id) {
        return null;
    }

    @Override
    public List<LocalMediaBean> getLocalMediaBeanByAlbumName(String album_name) {
        return null;
    }
}
