package com.gallery.tclgallery.viewholder.item;

import android.view.View;

import com.gallery.tclgallery.viewholder.MonthView;
import com.gallery.tclgallery.viewholder.base.BasePhotoItemHolder;


/**
 * 月视图照片Item
 * Created by jiaojie.jia on 2017/3/16.
 */

public class MonthViewItemHolder extends BasePhotoItemHolder {

    public MonthViewItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getClumnCount() {
        return MonthView.CLUMN_COUNT;
    }
}
