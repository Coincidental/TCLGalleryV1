package com.gallery.tclgallery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumFile;
import com.gallery.tclgallery.bean.AlbumFolder;
import com.gallery.tclgallery.bean.LocalMediaBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liddo on 2017/10/11.
 */

public class MediaReader2 {

    private static final String TAG = "MediaReader2";
    private Context mContext;

    public MediaReader2(Context context) {
        mContext = context;
    }

    /**
     *  Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.ORIENTATION
    };

    /**
     * Image Thumb.
     */
    private static final String[] IMAGE_THUMB = {
            MediaStore.Images.Thumbnails.DATA
    };

    /**
     * Videos attribute.
     */
    private static final String[] VIDEOS = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT
    };

    /**
     * Video thumb.
     */
    private static final String[] VIDEOS_THUMB = {
            MediaStore.Video.Thumbnails.DATA
    };

    /**
     * Scan for image File
     * @param localMediaList
     */
    private void scanImageFiles(List<LocalMediaBean> localMediaList) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(IMAGES[0]));               // local_id
                String path = cursor.getString(cursor.getColumnIndex(IMAGES[1]));       // local_path
                File file = new File(path);
                if(!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
                String bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[3]));
                String mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[4]));   // mime_type (image/jpeg)
                String type = mimeType.split("\\/")[1];                                 // type (jpeg)
                int addDate = cursor.getInt(cursor.getColumnIndex(IMAGES[5]));        // created_at
                int modifyDate = cursor.getInt(cursor.getColumnIndex(IMAGES[6]));     // generated_at taken_at
                float latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[7]));     // latitude
                float longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[8]));   // longitude
                int size = cursor.getInt(cursor.getColumnIndex(IMAGES[9]));          // size
                int width = cursor.getInt(cursor.getColumnIndex(IMAGES[10]));           // width
                int height = cursor.getInt(cursor.getColumnIndex(IMAGES[11]));          // height
                int orientation = cursor.getInt(cursor.getColumnIndex(IMAGES[12]));     // orientation
                int duration = 0;                                                       // duration Image默认为0
                String location = "";                                                   // location 暂无Location
                int secret = 0;                                                         // secret   secret默认为0
                int visible = 1;                                                        // visible  visible默认为1

                LocalMediaBean localMedia = new LocalMediaBean();
                localMedia.setLocal_id(id);
                localMedia.setName(name);
                localMedia.setBucketName(bucketName);
                localMedia.setType(type);
                localMedia.setMime_type(mimeType);
                localMedia.setCreated_at(addDate);
                localMedia.setGenerated_at(modifyDate);
                localMedia.setLocal_path(path);
                localMedia.setSize(size);
                localMedia.setTaken_at(modifyDate);
                localMedia.setLatitude(latitude);
                localMedia.setLongitude(longitude);
                localMedia.setWidth(width);
                localMedia.setHeight(height);
                localMedia.setOrientation(orientation);
                localMedia.setDuration(duration);
                localMedia.setLocation(location);
                localMedia.setSecret(secret);
                localMedia.setVisible(visible);

                Cursor thumbCursor = resolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                        IMAGE_THUMB,
                        MediaStore.Images.Thumbnails.IMAGE_ID + "=" + id,
                        null,
                        null);
                if (thumbCursor != null ) {
                    if (thumbCursor.moveToFirst()){
                        String thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(IMAGE_THUMB[0]));      // thumbPath
                        localMedia.setThumbPath(thumbPath);
                    }
                    thumbCursor.close();
                }
                localMediaList.add(localMedia);
            }
            cursor.close();
        }
    }

    /**
     * scan for video file
     * @param localMediaList
     */
    private void scanVideoFile(List<LocalMediaBean> localMediaList) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEOS,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(VIDEOS[0]));               //local_id
                String path = cursor.getString(cursor.getColumnIndex(VIDEOS[1]));       //local_path

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex(VIDEOS[2]));
                String bucketName = cursor.getString(cursor.getColumnIndex(VIDEOS[3]));
                String mimeType = cursor.getString(cursor.getColumnIndex(VIDEOS[4]));   // mimeType (video/avi)
                String type = mimeType.split("\\/")[1];                                 // type (avi)
                int addDate = cursor.getInt(cursor.getColumnIndex(VIDEOS[5]));          // created_at
                int modifyDate = cursor.getInt(cursor.getColumnIndex(VIDEOS[6]));       // generated_at taken_at
                float latitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[7]));     // latitude
                float longitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[8]));   // longitude
                String location = "";                                                   // location
                int size = cursor.getInt(cursor.getColumnIndex(VIDEOS[9]));            // size
                int duration = cursor.getInt(cursor.getColumnIndex(VIDEOS[10]));        // duration
                int width = cursor.getInt(cursor.getColumnIndex(VIDEOS[11]));           // width
                int height = cursor.getInt(cursor.getColumnIndex(VIDEOS[12]));          // height
                int orientation = width > height ? 0 : 1;                                // orientation
                int secret = 0;                                                         // secret
                int visible = 1;                                                        // visible

                LocalMediaBean localMedia = new LocalMediaBean();
                localMedia.setLocal_id(id);
                localMedia.setName(name);
                localMedia.setBucketName(bucketName);
                localMedia.setType(type);
                localMedia.setMime_type(mimeType);
                localMedia.setCreated_at(addDate);
                localMedia.setGenerated_at(modifyDate);
                localMedia.setLocal_path(path);
                localMedia.setSize(size);
                localMedia.setTaken_at(modifyDate);
                localMedia.setLatitude(latitude);
                localMedia.setLongitude(longitude);
                localMedia.setWidth(width);
                localMedia.setHeight(height);
                localMedia.setOrientation(orientation);
                localMedia.setDuration(duration);
                localMedia.setLocation(location);
                localMedia.setSecret(secret);
                localMedia.setVisible(visible);

                Cursor thumbCursor = resolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        VIDEOS_THUMB,
                        MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id,
                        null,
                        null);
                if (thumbCursor != null) {
                    if (thumbCursor.moveToFirst()) {
                        String thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(VIDEOS_THUMB[0]));
                        localMedia.setThumbPath(thumbPath);
                    }
                    thumbCursor.close();
                }
                localMediaList.add(localMedia);
            }
            cursor.close();
        }
    }

    /**
     * Get all the multimedia files, including videos and pictures.
     */
    public ArrayList<LocalMediaBean> getAllMedia() {
        ArrayList<LocalMediaBean> localMediaList = new ArrayList<>();
        scanImageFiles(localMediaList);
        scanVideoFile(localMediaList);
        Collections.sort(localMediaList, new Comparator<LocalMediaBean>() {
            @Override
            public int compare(LocalMediaBean localMediaBean, LocalMediaBean t1) {
                if (localMediaBean.getLocal_id() > t1.getLocal_id()){
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        return localMediaList;
    }
}
