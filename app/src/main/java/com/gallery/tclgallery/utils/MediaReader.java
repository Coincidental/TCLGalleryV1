package com.gallery.tclgallery.utils;

/**
 * Created by liddo on 2017/9/20.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumFile;
import com.gallery.tclgallery.bean.AlbumFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类为获取图片和视频多媒体文件的帮助类
 */
public class MediaReader {
    private static final String TAG = "MediaReader";
    private Context mContext;

    public MediaReader(Context context) {
        mContext = context;
    }

    /**
     *  Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_ID,
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
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.RESOLUTION
    };

    /**
     * Video thumb.
     */
    private static final String[] VIDEOS_THUMB = {
            MediaStore.Video.Thumbnails.DATA
    };

    /**
     * Scan for image File
     * @param albumFolderMap
     * @param allAlbumFolder
     */
    private void scanImageFiles(Map<String, AlbumFolder> albumFolderMap, AlbumFolder allAlbumFolder) {
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

                String name = cursor.getString(cursor.getColumnIndex(IMAGES[2]));       //
                String title = cursor.getString(cursor.getColumnIndex(IMAGES[3]));
                int bucketId = cursor.getInt(cursor.getColumnIndex(IMAGES[4]));
                String bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
                String mineType = cursor.getString(cursor.getColumnIndex(IMAGES[6]));   // mime_type (image/jpeg)
                String type = mineType.split("\\/")[1];                                 // type (jpeg)
                long addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));        // created_at
                long modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[8]));     // generated_at taken_at
                float latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[9]));     // latitude
                float longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[10]));   // longitude
                long size = cursor.getLong(cursor.getColumnIndex(IMAGES[11]));          // size
                int width = cursor.getInt(cursor.getColumnIndex(IMAGES[12]));           // width
                int height = cursor.getInt(cursor.getColumnIndex(IMAGES[13]));          // height
                int orientation = cursor.getInt(cursor.getColumnIndex(IMAGES[14]));     // orientation

                AlbumFile imageFile = new AlbumFile();
                imageFile.setmMediaType(AlbumFile.TYPE_IMAGE);
                imageFile.setId(id);
                imageFile.setmPath(path);
                imageFile.setmName(name);
                imageFile.setmTitle(title);
                imageFile.setmBucketId(bucketId);
                imageFile.setmBucketName(bucketName);
                imageFile.setmMimeType(mineType);
                imageFile.setmAddDate(addDate);
                imageFile.setmModifyDate(modifyDate);
                imageFile.setmLatitude(latitude);
                imageFile.setmLongitude(longitude);
                imageFile.setmSize(size);

                String thumbPath = null;
                Cursor thumbCursor = resolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                                                    IMAGE_THUMB,
                                                    MediaStore.Images.Thumbnails._ID + "=" + id,
                                                    null,
                                                    null);
                if (thumbCursor != null ) {
                    if (thumbCursor.moveToFirst()){
                        thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(IMAGE_THUMB[0]));      // thumbPath
                    }
                    thumbCursor.close();
                }
                imageFile.setmThumbPath(thumbPath);

                allAlbumFolder.addAlbumFile(imageFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);

                if (albumFolder != null) {
                    albumFolder.addAlbumFile(imageFile);
                } else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setId(bucketId);
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(imageFile);

                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
    }

    /**
     * scan for video file
     * @param albumFolderMap
     * @param allFileFolder
     */
    private void scanVideoFile(Map<String, AlbumFolder> albumFolderMap, AlbumFolder allFileFolder) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    VIDEOS,
                                    null,
                                    null,
                                    MediaStore.Video.Media.DATE_ADDED);

        if (cursor != null) {
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(VIDEOS[0]));
                String path = cursor.getString(cursor.getColumnIndex(VIDEOS[1]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex(VIDEOS[2]));
                String title = cursor.getString(cursor.getColumnIndex(VIDEOS[3]));
                int bucketId = cursor.getInt(cursor.getColumnIndex(VIDEOS[4]));
                String bucketName = cursor.getString(cursor.getColumnIndex(VIDEOS[5]));
                String mineType = cursor.getString(cursor.getColumnIndex(VIDEOS[6]));
                long addDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[7]));
                long modifyDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[8]));
                float latitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[9]));
                float longitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[10]));
                long size = cursor.getLong(cursor.getColumnIndex(VIDEOS[11]));
                long duration = cursor.getLong(cursor.getColumnIndex(VIDEOS[12]));
                String resolution = cursor.getString(cursor.getColumnIndex(VIDEOS[13]));

                AlbumFile videoFile = new AlbumFile();
                videoFile.setmMediaType(AlbumFile.TYPE_VIDEO);
                videoFile.setId(id);
                videoFile.setmPath(path);
                videoFile.setmName(name);
                videoFile.setmTitle(title);
                videoFile.setmBucketId(bucketId);
                videoFile.setmBucketName(bucketName);
                videoFile.setmMimeType(mineType);
                videoFile.setmAddDate(addDate);
                videoFile.setmModifyDate(modifyDate);
                videoFile.setmLatitude(latitude);
                videoFile.setmLongitude(longitude);
                videoFile.setmSize(size);
                videoFile.setmDuration(duration);
                videoFile.setmResolution(resolution);

                String thumbPath = null;
                Cursor thumbCursor = resolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                                                    VIDEOS_THUMB,
                                                    MediaStore.Video.Thumbnails._ID + "=" + id,
                                                    null,
                                                    null);
                if (thumbCursor != null) {
                    if (thumbCursor.moveToFirst()) {
                        thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(VIDEOS_THUMB[0]));
                    }
                    thumbCursor.close();
                }
                videoFile.setmThumbPath(thumbPath);

                int width = 0;
                int height = 0;
                if (!TextUtils.isEmpty(resolution) && resolution.contains("x")) {
                    String[] resolutionArray = resolution.split("x");
                    width = Integer.valueOf(resolutionArray[0]);
                    height = Integer.valueOf(resolutionArray[1]);
                }
                videoFile.setmWidth(width);
                videoFile.setmHeight(height);

                allFileFolder.addAlbumFile(videoFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);

                if (albumFolder != null) {
                    albumFolder.addAlbumFile(videoFile);
                } else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setId(bucketId);
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(videoFile);

                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
    }

    /**
     * scan the list of pictures in the library
     * @return
     */
    public ArrayList<AlbumFolder> getAllImage() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setName(mContext.getString(R.string.album_all_images));

        scanImageFiles(albumFolderMap, allFileFolder);

        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getmAlbumFile());
        albumFolders.add(allFileFolder);

        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()){
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getmAlbumFile());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }

    /**
     * scan the list of videos in the library
     * @return
     */
    public ArrayList<AlbumFolder> getAllVideo() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setName(mContext.getString(R.string.album_all_videos));

        scanVideoFile(albumFolderMap, allFileFolder);

        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getmAlbumFile());
        albumFolders.add(allFileFolder);

        for (Map.Entry<String, AlbumFolder> folderEntry: albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getmAlbumFile());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }


    /**
     * Get all the multimedia files, including videos and pictures.
     */
    public ArrayList<AlbumFolder> getAllMedia() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setName(mContext.getString(R.string.album_all_images_videos));

        scanImageFiles(albumFolderMap, allFileFolder);
        scanVideoFile(albumFolderMap, allFileFolder);

        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getmAlbumFile());
        albumFolders.add(allFileFolder);

        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getmAlbumFile());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }

}
