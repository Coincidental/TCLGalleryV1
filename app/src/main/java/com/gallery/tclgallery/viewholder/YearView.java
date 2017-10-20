package com.gallery.tclgallery.viewholder;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.listener.DragSelectTouchListener;
import com.gallery.tclgallery.listener.OnGridViewClickListener;
import com.gallery.tclgallery.listener.OnSwitchViewListener;
import com.gallery.tclgallery.ui.adapter.YearViewAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.base.BaseHolder;
import com.gallery.tclgallery.viewholder.item.YearViewItemHolder;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearView extends BaseHolder<LinkedHashMap<String, List<CameraItem>>> {

    private RecyclerView mRecyclerView;                     // 列表
    private YearViewAdapter mAdapter;
    private YearViewItemHolder yearViewItemHolder;

    private DragSelectTouchListener touchListener;          // 滑动选择 Listener
    private OnSwitchViewListener mSwitchViewListener;       // 缩放手势 Listener

    public YearView(Context context) {
        super(context);
    }

    public void setSwitchViewListener(OnSwitchViewListener switchViewListener) {
        mSwitchViewListener = switchViewListener;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.holder_year_view, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_month);
        mAdapter = new YearViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(5)));

        touchListener = new DragSelectTouchListener();
        mRecyclerView.addOnItemTouchListener(touchListener);
        touchListener.setScaleGestureDetector(new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (detector.getScaleFactor() > 1) {
                    mSwitchViewListener.onSwitchView(detector.getScaleFactor());
                }
                return true;
            }
        }));

        mAdapter.setClickListener(new OnGridViewClickListener() {
            @Override
            public void onGridViewClick(int parentPosition) {
                int monthPosition = mAdapter.getMonthPosition(parentPosition);
                mSwitchViewListener.onSwitchViewBySection(monthPosition);
            }
        });

        return view;
    }

    @Override
    public void refreshView() {
        if (Format.isEmpty(data)) return;
        mAdapter.setAllPhotos(data);
//        yearViewItemHolder.setAllPhotos(data);
    }

    /**
     * 判断当前视图是否填充了数据
     */
    public boolean isEmpty() {
        return !(mAdapter != null && mAdapter.getItemCount() > 0);
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
    }
}
