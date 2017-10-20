package com.gallery.tclgallery.presenter;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.data.GooglePhotoScanner;
import com.gallery.tclgallery.ui.GooglePhotoActivity;
import com.gallery.tclgallery.utils.Format;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaojie.jia on 2017/3/15.
 */

public class GooglePhotoPresenter implements GooglePhotoContract.Presenter {

    private GooglePhotoActivity.ViewType mViewType;         // 当前视图类型

    private GooglePhotoContract.View mView;                 // view

    private List<CameraItem> mSelectedPhotos;                // 选中的照片集合

    private List<Float> mPercents;
    private List<String> mTimelineTags;

    public GooglePhotoPresenter(GooglePhotoContract.View view) {
        mView = view;
        mSelectedPhotos = new ArrayList<>();
    }

    @Override
    public void setViewType(GooglePhotoActivity.ViewType viewType) {
        mViewType = viewType;
    }

    @Override
    public GooglePhotoActivity.ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void loadPhotos() {
        if (!Format.isEmpty(getPhotoData())/* && !Format.isEmpty(GooglePhotoScanner.getImageFloders())*/) {
            mView.fullData(getPhotoData());
//            mView.fullFolders(GooglePhotoScanner.getImageFloders());
        } else {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    GooglePhotoScanner.startScan();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    mView.fullData(getPhotoData());
//                    mView.fullFolders(GooglePhotoScanner.getImageFloders());
                }
            }.execute();
        }
    }

    public void scanPhotos() {
        clear();
        GooglePhotoScanner.startScan();
        mView.fullData(getPhotoData());
//        mView.fullFolders(GooglePhotoScanner.getImageFloders());
    }

    @Override
    public void selectPhoto(CameraItem cameraItem) {
        if (cameraItem.isSelected() && !mSelectedPhotos.contains(cameraItem)) {
            mSelectedPhotos.add(cameraItem);
        } else if (!cameraItem.isSelected() && mSelectedPhotos.contains(cameraItem)) {
            mSelectedPhotos.remove(cameraItem);
        }
    }

    /**
     * 全选图片
     */
    public void selectPhotoAll() {
//        Log.d("whj", "selectPhotoAll: start");
        List<List<CameraItem>> mPhotos;
        mPhotos = new ArrayList<>(getPhotoData().size());
        for (Map.Entry<String, List<CameraItem>> entry : getPhotoData().entrySet()) {
            mPhotos.add(entry.getValue());
        }
        for (List<CameraItem> photoSection : mPhotos) {
            for (CameraItem cameraItem : photoSection) {
                cameraItem.setSelected(true);
                selectPhoto(cameraItem);
            }
        }
    }

    /**
     * 遍历是否全选
     */
    public boolean isAllSelect() {
//        Log.d("whj", "isAllSelect: start");
        List<List<CameraItem>> mPhotos;
        mPhotos = new ArrayList<>(getPhotoData().size());
        for (Map.Entry<String, List<CameraItem>> entry : getPhotoData().entrySet()) {
            mPhotos.add(entry.getValue());
        }
        for (List<CameraItem> photoSection : mPhotos) {
            for (CameraItem cameraItem : photoSection) {
                if (!cameraItem.isSelected()) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isSelectedEmpty() {
        return Format.isEmpty(mSelectedPhotos);
    }

    @Override
    public void cancleAllSelected() {
        if (!Format.isEmpty(mSelectedPhotos)) {
            for (CameraItem cameraItem : mSelectedPhotos) {
                cameraItem.setSelected(false);
            }
            mSelectedPhotos.clear();
        }
    }

    @Override
    public void setTimelineData(List<Float> percents, List<String> timelineTags) {
        this.mPercents = percents;
        this.mTimelineTags = timelineTags;
    }

    @Override
    public List<Float> getPercents() {
        return mPercents;
    }

    @Override
    public List<String> getTimelineTags() {
        return mTimelineTags;
    }

    @Override
    public void clear() {
        mSelectedPhotos.clear();
        GooglePhotoScanner.clear();
    }

    /**
     * 获取与视图对应的数据
     */
    private LinkedHashMap<String, List<CameraItem>> getPhotoData() {
        return GooglePhotoScanner.getPhotoSections(mViewType);
    }

    /*选中的图片数量*/
    public int SelectPictrueNumber() {
        Log.d("www", "SelectPictrueNumber: " + mSelectedPhotos.size());
        return mSelectedPhotos.size();
    }

    public ArrayList<Uri> ShareFile() {
        ArrayList<Uri> filelist = new ArrayList<Uri>();
        for (CameraItem cameraItem : mSelectedPhotos) {
            File file1 = new File(cameraItem.getPath());
            Log.d("www", "ShareFile: path" + cameraItem.getPath().toString());
            filelist.add(Uri.fromFile(file1));
        }
        return filelist;
    }

    public ArrayList<String> DeleteFile() {
        Log.d("www", "DeleteFile: Start");
        ArrayList<String> filelist = new ArrayList<String>();
        for (CameraItem cameraItem : mSelectedPhotos) {
            File file = new File(cameraItem.getPath());
            Log.d("www", "DeleteFile: path" + cameraItem.getPath().toString());
            filelist.add(cameraItem.getPath());
            file.delete();
        }
        return filelist;
    }
}
