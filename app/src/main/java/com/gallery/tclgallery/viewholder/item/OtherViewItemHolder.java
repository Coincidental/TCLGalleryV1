package com.gallery.tclgallery.viewholder.item;

import android.view.View;

import com.gallery.tclgallery.viewholder.OtherView;
import com.gallery.tclgallery.viewholder.base.BasePhotoItemHolder;


/**
 * 除相册外视图Item
 * Created by jiaojie.jia on 2017/3/23.
 */

public class OtherViewItemHolder extends BasePhotoItemHolder {

    public OtherViewItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getClumnCount() {
        return OtherView.CLUMN_COUNT;
    }
}
