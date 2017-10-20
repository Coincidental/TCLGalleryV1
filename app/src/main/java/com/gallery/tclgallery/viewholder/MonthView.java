package com.gallery.tclgallery.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.ScaleGestureDetector;

import com.gallery.tclgallery.ui.adapter.MonthViewAdapter;
import com.gallery.tclgallery.ui.adapter.base.BaseViewAdapter;
import com.gallery.tclgallery.viewholder.base.BasePhotoView;


/**
 * Google相册月视图
 * Created by jiaojie.jia on 2017/3/15.
 */

public class MonthView extends BasePhotoView {

    public static  int CLUMN_COUNT = 6;

    public MonthView(Context context) {
        super(context);
    }

    @Override
    protected ScaleGestureDetector getScaleDetector() {
        return new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener(){
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mSwitchViewListener.onSwitchView(detector.getScaleFactor());
                return true;
            }
        });
    }

 @Override
    protected GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(context, CLUMN_COUNT);
    }

    @Override
    protected BaseViewAdapter getAdapter() {
        return new MonthViewAdapter();
    }
}
