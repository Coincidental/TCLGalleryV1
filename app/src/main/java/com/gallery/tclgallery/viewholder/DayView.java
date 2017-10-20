package com.gallery.tclgallery.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;
import com.gallery.tclgallery.ui.adapter.DayViewAdapter;
import com.gallery.tclgallery.ui.adapter.base.BaseViewAdapter;
import com.gallery.tclgallery.viewholder.base.BasePhotoView;

import java.util.LinkedList;
import java.util.List;


/**
 * Google相册日视图
 * Created by jiaojie.jia on 2017/3/20.
 */

public class DayView extends BasePhotoView {
    public static DayView dayview;
    public  List<LayoutHelper> helpers = new LinkedList<>();
    public Context mcontext=context;
    public RecyclerView recy =mRecyclerView;
    public static int CLUMN_COUNT = 4;

    public DayView(Context context) {
        super(context);
    }

    @Override
    protected ScaleGestureDetector getScaleDetector() {
        return new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (detector.getScaleFactor() < 1) {
                    View child = mRecyclerView.getChildAt(0);
                    if (child != null) {
                        int position = mRecyclerView.getLayoutManager().getPosition(child);
                        int section = ((DayViewAdapter) mAdapter).getSectionInMonthView(position);
                        mSwitchViewListener.onSwitchViewBySection(section);
                    }
                }
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
        return new DayViewAdapter();
    }
}
