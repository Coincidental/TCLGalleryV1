package com.gallery.tclgallery.data;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.ImageFolder;
import com.gallery.tclgallery.ui.GooglePhotoActivity;
import com.gallery.tclgallery.utils.UIUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Google相册扫描器
 * Created by jiaojie.jia on 2017/3/15.
 */

public class GooglePhotoScanner extends Activity{

    private static final int MIN_SIZE = 1024 * 10;

    private static final long MIN_DATE = 1000000000;

    //扫描结果图片文件夹
    private static HashMap<String, ImageFolder> mGruopMap = new HashMap<>();

    private static LinkedHashMap<String, List<CameraItem>> mSectionsOfMonth = new LinkedHashMap<>();
    private static LinkedHashMap<String, List<CameraItem>> mSectionsOfDay = new LinkedHashMap<>();

    private static List<ImageFolder> imageFloders = new ArrayList<>();

    public static ImageFolder mDefaultFolder;                  // 默认图片文件夹

    private static final SimpleDateFormat mDataFormatOfMonth = new SimpleDateFormat("yyyy年MM月");
    private static final SimpleDateFormat mDataFormatOfDay = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * Video attribute.
     */
    private static final String[] VIDEOS = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_TAKEN
    };

    public static void startScan(){
        readSystemGallery(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        readSystemGallery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        readVideo(MediaStore.Video.Media.INTERNAL_CONTENT_URI);
        readVideo(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    private static void readSystemGallery(Uri uri){
        //获取ContentResolver
        ContentResolver contentResolver = UIUtils.getContext().getContentResolver();
        //查询字段
        String[] projection = new String[]{MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.LONGITUDE,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_TAKEN};
        // 条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
        // 条件值
        String[] selectionArgs = {"image/jpeg", "image/png", "image/gif", "image/webp"};
        // 排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        // 查询
        Cursor mCursor = MediaStore.Images.Media.query(contentResolver, uri, projection, selection, selectionArgs, sortOrder);

        while (mCursor != null && mCursor.moveToNext()) {
            //图片大小
            int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            //过滤掉10k以下的图片
            if(size < MIN_SIZE)
                continue;
            //修改日期
            long modified = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            if(modified < MIN_DATE)
                continue;
            //图片路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            if(TextUtils.isEmpty(path))
                continue;
            //图片Id
            int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
            //图片宽度
            int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            //图片高度
            int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            //拍摄日期
            int takendate = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            double longitude = mCursor.getDouble(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE));
            double latitude = mCursor.getDouble(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE));
            int orientation = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
            //类型
            String mime_type = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
            String parentName = new File(path).getParent();
            CameraItem cameraItem = new CameraItem(id, path, width, height, size, latitude, longitude, 0, orientation, takendate, modified,0,mime_type);

            // 查询缩略图非常消耗性能
//            Log.i("667", "readSystemGallery: tempid"+id);
            String temp =  getImageThumbnail(id);
            Log.i("667", "readSystemGallery: temp"+temp);
            cameraItem.setThumbnail(temp);

            //根据父路径名将图片放入到mGruopMap中
            if (!mGruopMap.containsKey(parentName)) {
                ImageFolder floder = new ImageFolder();
                floder.setDir(parentName);
                floder.setFirstImagePath(path);
                floder.setCount(floder.getCount() + 1);
                List<CameraItem> photoList = new ArrayList<>();
                photoList.add(cameraItem);
                if(floder.isPhoto()) {
                    mDefaultFolder = floder;
                    sortPhotosByMonth(cameraItem);
                    sortPhotosByDay(cameraItem);
                }
                floder.setList(photoList);
                mGruopMap.put(parentName, floder);
                imageFloders.add(floder);
                if(mDefaultFolder == null || !mDefaultFolder.isPhoto()) {
                    mDefaultFolder = floder;
                }
            } else {
                ImageFolder floder = mGruopMap.get(parentName);
                floder.setCount(floder.getCount() + 1);
                floder. getList().add(cameraItem);
                if(floder.isPhoto()) {
                    sortPhotosByMonth(cameraItem);
                    sortPhotosByDay(cameraItem);
                }
            }
        }
        if (mCursor != null) {
            mCursor.close();
        }
    }
private  static void readVideo(Uri uri){
        //获取ContentResolver
        ContentResolver contentResolver = UIUtils.getContext().getContentResolver();
    Log.d("wwww", "readVideo: Start");
        // 查询
        Cursor mCursor = contentResolver.query(uri, VIDEOS,null,null,MediaStore.Video.Media.DATE_ADDED);

        while (mCursor != null && mCursor.moveToNext()) {
            //图片大小
            int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.SIZE));
            //过滤掉10k以下的图片
            if(size < MIN_SIZE)
                continue;
            //修改日期
            long modified = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));
            if(modified < MIN_DATE)
                continue;
            //图片路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
            if(TextUtils.isEmpty(path))
                continue;
            //图片Id
            int id = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));
            //图片宽度
            int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.WIDTH));
            //图片高度
            int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.HEIGHT));
            //拍摄日期
            int takendate = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
            double longitude = mCursor.getDouble(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.LONGITUDE));
            double latitude = mCursor.getDouble(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.LATITUDE));
            //int orientation = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.ORIENTATION));
            //类型
            String mime_type = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
            long  duration =  mCursor.getLong(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            String parentName = new File(path).getParent();
            CameraItem cameraItem = new CameraItem(id, path, width, height, size, latitude, longitude, 0, 0, takendate, modified,duration,mime_type);
            Log.d("wwww", "readVideo: cameraItem.setThumbnail start");
            // 查询缩略图非常消耗性能
            cameraItem.setThumbnail(getVideoThumbnail(id,uri));
            Log.d("wwww", "readVideo: cameraItem.setThumbnail end" );
            //根据父路径名将图片放入到mGruopMap中
            if (!mGruopMap.containsKey(parentName)) {
                ImageFolder floder = new ImageFolder();
                floder.setDir(parentName);
                floder.setFirstImagePath(path);
                floder.setCount(floder.getCount() + 1);
                List<CameraItem> photoList = new ArrayList<>();
                photoList.add(cameraItem);
                if(floder.isPhoto()) {
                    mDefaultFolder = floder;
                    sortPhotosByMonth(cameraItem);
                    sortPhotosByDay(cameraItem);
                }
                floder.setList(photoList);
                mGruopMap.put(parentName, floder);
                imageFloders.add(floder);
                if(mDefaultFolder == null || !mDefaultFolder.isPhoto()) {
                    mDefaultFolder = floder;
                }
            } else {
                ImageFolder floder = mGruopMap.get(parentName);
                floder.setCount(floder.getCount() + 1);
                floder. getList().add(cameraItem);
                if(floder.isPhoto()) {
                    sortPhotosByMonth(cameraItem);
                    sortPhotosByDay(cameraItem);
                }
            }
        }
        if (mCursor != null) {
            mCursor.close();
        }
    }
    /** 获取照片缩略图 */
    private static String getImageThumbnail(int imageId) {
        Log.i("667", "readSystemGallery: do getImageThumbnail");
        String thumbnailPath = null;
        final String[] projection = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID};
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(UIUtils.getContext().getContentResolver(),
                imageId, MediaStore.Images.Thumbnails.MICRO_KIND, projection);
        if(/*cursor != null && */cursor.moveToFirst()) {
            thumbnailPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            cursor.close();
        }
        return thumbnailPath;
    }
    /** 获取视频缩略图*/
    private static String getVideoThumbnail(int imageId, Uri uri) {
        String thumbPath = null;
        ContentResolver contentResolver = UIUtils.getContext().getContentResolver();
        Cursor thumbCursor = contentResolver.query(uri,
                VIDEOS,
                MediaStore.Video.Thumbnails._ID + "=" + imageId,
                null,
                null);
        if (thumbCursor != null) {
            if (thumbCursor.moveToFirst()) {
                thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            }
            thumbCursor.close();
        }
        Log.d("wwww", "getVideoThumbnail: thumbPath"+thumbPath);
        return thumbPath;
    }
    /** 根据当前视图，返回对应数据 */
    public static LinkedHashMap<String, List<CameraItem>> getPhotoSections(GooglePhotoActivity.ViewType viewType) {
        switch (viewType) {
            case COLLAGE:
            case DAY:
            case YEAR:
                Log.i("666", "getPhotoSections: mSectionsOfDay in googlePhotoScanner :"+mSectionsOfDay.keySet());
                return mSectionsOfDay;
            case MONTH:
            default:
                return mSectionsOfMonth;
        }
    }

    /*public static List<ImageFolder> getImageFloders() {
        return imageFloders;
    }*/

    /** 把照片按月分类 */
    private static void sortPhotosByMonth(CameraItem photo) {
        Date date = new Date(photo.getModified() * 1000);
        String millisecond = mDataFormatOfMonth.format(date);
        if(!mSectionsOfMonth.containsKey(millisecond)) {
            List<CameraItem> section = new ArrayList<>();
            section.add(photo);
            mSectionsOfMonth.put(millisecond, section);
        } else {
            List<CameraItem> section = mSectionsOfMonth.get(millisecond);
            section.add(photo);
        }
    }

    /** 把照片按日分类 */
    private static void sortPhotosByDay(CameraItem photo) {
        Date date = new Date(photo.getModified() * 1000);
//        Date today = new Date(System.currentTimeMillis());//获取当前时间
//        String dayKey =mDataFormatOfDay.format(date);
        /*if(IsToday(dayKey)){dayKey="ToDay";}else if (IsYesterday(dayKey)){dayKey="YesterDay";}*/
        String detail = mDataFormatOfDay.format(date);
//        String week = DateUtil.getWeek(date);
//        String dayKey = detail + week;
        if(!mSectionsOfDay.containsKey(detail)) {
            List<CameraItem> section = new ArrayList<>();
            section.add(photo);
            mSectionsOfDay.put(detail, section);
        } else {
            List<CameraItem> section = mSectionsOfDay.get(detail);
            section.add(photo);
        }
    }
   /* public static boolean IsToday(String day){

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        try {
            Date date = getDateFormat().parse(day);
            cal.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }
    public static boolean IsYesterday(String day) {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        try {
            Date date = getDateFormat().parse(day);
            cal.setTime(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }
    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();*/
    public static void clear() {
        if(mGruopMap != null)
            mGruopMap.clear();
        if(imageFloders != null)
            imageFloders.clear();
        if(mSectionsOfMonth != null)
            mSectionsOfMonth.clear();
        if(mSectionsOfDay != null)
            mSectionsOfDay.clear();
        mDefaultFolder = null;
    }
}
