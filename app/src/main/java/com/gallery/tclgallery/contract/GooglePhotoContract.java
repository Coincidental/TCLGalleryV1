package com.gallery.tclgallery.contract;


import android.net.Uri;


import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.ui.GooglePhotoActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by jiaojie.jia on 2017/3/15.
 */

public interface GooglePhotoContract {

    interface View {

        /**
         * 填充相册数据
         */
        void fullData(LinkedHashMap<String, List<CameraItem>> sections);

        /**
         * 填充文件夹数据
         *
         * @param folders
         */
//        void fullFolders(List<ImageFolder> folders);

    }

    interface Presenter {

        /**
         * 设置视图类型
         */
        void setViewType(GooglePhotoActivity.ViewType viewType);

        /**
         * 获取视图类型
         */
        GooglePhotoActivity.ViewType getViewType();

        /**
         * 开始加载数据
         */
        void loadPhotos();

        /**
         * 重新load数据
         */
        void scanPhotos();

        /**
         * 选中照片
         *
         * @param cameraItem
         */
        void selectPhoto(CameraItem cameraItem);

        /**
         * 全选图片
         */
        void selectPhotoAll();

        /**
         * 图片选择的数量
         */
        int SelectPictrueNumber();

        /**
         * 是否全选状态
         */
        boolean isAllSelect();

        /**
         * 删除的图片打包
         */
        ArrayList<String> DeleteFile();

        /**
         * 分享的图片Uri打包
         */
        ArrayList<Uri> ShareFile();

        /**
         * 是否选择了照片
         *
         * @return
         */
        boolean isSelectedEmpty();

        /**
         * 清楚所有选中的照片
         */
        void cancleAllSelected();


        /**
         * 设置相册时间线数据
         *
         * @param percents
         * @param timelineTags
         */
        void setTimelineData(List<Float> percents, List<String> timelineTags);

        /**
         * 获取时间线tag位置数据
         *
         * @return
         */
        List<Float> getPercents();

        /**
         * 获取时间线tag标题
         *
         * @return
         */
        List<String> getTimelineTags();

        /**
         * 清楚数据
         */
        void clear();
    }
}
