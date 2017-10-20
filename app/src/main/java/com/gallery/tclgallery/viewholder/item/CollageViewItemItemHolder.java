package com.gallery.tclgallery.viewholder.item;

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


/**
 * Created by Administrator on 2017/9/27.
 */


public class CollageViewItemItemHolder extends  RecyclerView.ViewHolder {
private final ImageView imgItem;        // 照片图片
    private final ImageView imgSelect;      // 右上角选中标识图片
    private final ImageView imageVideo;
    private final TextView duration;

    public CollageViewItemItemHolder(View itemView) {
        super(itemView);
        imgItem = (ImageView) itemView.findViewById(R.id.iv_photo);
        imgSelect = (ImageView) itemView.findViewById(R.id.iv_select);
        imageVideo = (ImageView) itemView.findViewById(R.id.iv_video);
        duration = (TextView) itemView.findViewById(R.id.video_duration);
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

}
