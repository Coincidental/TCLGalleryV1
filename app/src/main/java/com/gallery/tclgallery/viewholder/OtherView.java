package com.gallery.tclgallery.viewholder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.ImageFolder;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.fastscroll.FastScroller;
import com.gallery.tclgallery.listener.DragSelectTouchListener;
import com.gallery.tclgallery.ui.adapter.OtherViewAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.base.BaseHolder;

/**
 * 除相册其他文件夹视图
 * Created by jiaojie.jia on 2017/3/23.
 */

public class OtherView extends BaseHolder<ImageFolder> {

    public static final int CLUMN_COUNT = 4;

    protected RecyclerView mRecyclerView;       // 列表
    private OtherViewAdapter mAdapter;
    private FastScroller fastScroller;          // 右侧快速导航

    private DragSelectTouchListener touchListener;          // 滑动选择 Listener

    private GooglePhotoContract.Presenter mPresenter;

    public OtherView(Context context) {
        super(context);
    }

    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
        mAdapter.setPresenter(presenter);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.holder_month_view, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_month);
        fastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mAdapter = new OtherViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        fastScroller.setRecyclerView(mRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter.setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                mAdapter.setSelected(position);
                touchListener.setStartSelectPosition(position);
                return false;
            }
        });
        mAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                mAdapter.setSelected(position);
            }
        });

        // 添加滑动监听
        touchListener = new DragSelectTouchListener();
        mRecyclerView.addOnItemTouchListener(touchListener);

        // 取消默认选中动画（闪烁）
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        touchListener.setSelectListener(new DragSelectTouchListener.onSelectListener() {
            @Override
            public void onSelectChange(int start, int end, boolean isSelected) {
                mAdapter.selectRangeChange(start, end, isSelected);
            }
        });

        return view;
    }

    @Override
    public void refreshView() {
        if (Format.isEmpty(data.getList())) return;
        mAdapter.setData(data.getList());
    }

    /**
     * 清除数据选中状态
     */
    public void clearSelectedStatus() {
        mAdapter.notifyDataSetChanged();
    }
}
