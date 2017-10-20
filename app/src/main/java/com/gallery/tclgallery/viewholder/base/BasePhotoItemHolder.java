package com.gallery.tclgallery.viewholder.base;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.utils.DateUtil;
import com.gallery.tclgallery.utils.ImageLoader;
import com.gallery.tclgallery.utils.UIUtils;
import com.nineoldandroids.view.ViewPropertyAnimator;


/**
 * 照片item基类
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class   BasePhotoItemHolder extends RecyclerView.ViewHolder {

    private final ImageView imgItem;        // 照片图片
    private final ImageView imgSelect;      // 右上角选中标识图片
    private final ImageView imageVideo;
    private final TextView duration;

    private ViewPropertyAnimator mAnimator;

    public BasePhotoItemHolder(View itemView) {
        super(itemView);
        imgItem = (ImageView) itemView.findViewById(R.id.iv_photo);
        imgSelect = (ImageView) itemView.findViewById(R.id.iv_select);
        imageVideo = (ImageView) itemView.findViewById(R.id.iv_video);
        duration = (TextView) itemView.findViewById(R.id.video_duration);
        int width = UIUtils.getScreenWidth() / getClumnCount();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
        itemView.setLayoutParams(lp);
    }

    public void setData(CameraItem cameraItem) {
        if (/*System.getProperty("isLongSelect", "false").equals("true")*/State.SelectState) {
            imgSelect.setVisibility(View.VISIBLE);
            imgSelect.setSelected(cameraItem.isSelected());
//            startAnim(photoItem);

        } else {
            imgSelect.setVisibility(View.GONE);
        }
        String type = cameraItem.getMime_type();
        if (type.indexOf("video") != -1) {
            imageVideo.setVisibility(View.VISIBLE);
            duration.setVisibility(View.VISIBLE);
            duration.setText(DateUtil.convertDuration(cameraItem.getDuration()));
        }else {
            imageVideo.setVisibility(View.GONE);
            duration.setVisibility(View.GONE);
        }
        String path = TextUtils.isEmpty(cameraItem.getThumbnail()) ? cameraItem.getPath() : cameraItem.getThumbnail();
        ImageLoader.loadGalleryImage(imgItem.getContext(), path, imgItem);
    }


    public abstract int getClumnCount();

    /**
     * 选中动画
     *
     * @param cameraItem
     */
    private void startAnim(CameraItem cameraItem) {
        if (mAnimator == null) {
            mAnimator = ViewPropertyAnimator.animate(imgItem);
        }
        if (cameraItem.isSelected()) {
            mAnimator.scaleX(0.8f).scaleY(0.8f).setDuration(200);
        } else {
            mAnimator.scaleX(1.0f).scaleY(1.0f).setDuration(200);
        }
        mAnimator.start();
    }
}
