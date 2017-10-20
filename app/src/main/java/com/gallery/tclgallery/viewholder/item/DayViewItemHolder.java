package com.gallery.tclgallery.viewholder.item;

import android.view.View;

import com.gallery.tclgallery.viewholder.DayView;
import com.gallery.tclgallery.viewholder.base.BasePhotoItemHolder;


/**
 * 日视图照片Item
 * Created by jiaojie.jia on 2017/3/20.
 */

public class DayViewItemHolder extends BasePhotoItemHolder {


    public DayViewItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getClumnCount() {
        return DayView.CLUMN_COUNT;
    }
}
