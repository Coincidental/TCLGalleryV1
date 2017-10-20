package com.gallery.tclgallery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.fastscroll.SectionTitleProvider;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.item.OtherViewItemHolder;

import java.util.List;

/**
 * 除相册文件夹的其他Adapter
 * Created by jiaojie.jia on 2017/3/23.
 */

public class OtherViewAdapter extends RecyclerView.Adapter implements SectionTitleProvider {

    private List<CameraItem> mCameraItems;             // 照片集合（除相册文件夹，其他不分组）

    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

    private GooglePhotoContract.Presenter mPresenter;

    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setData(List<CameraItem> items) {
        mCameraItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_month_item, parent, false);
        view.setOnLongClickListener(longClickListener);
        view.setOnClickListener(clickListener);
        return new OtherViewItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OtherViewItemHolder itemHolder = (OtherViewItemHolder) holder;
        itemHolder.setData(mCameraItems.get(position));
    }

    @Override
    public int getItemCount() {
        return Format.isEmpty(mCameraItems) ? 0 : mCameraItems.size();
    }

    public void setLongClickListener(View.OnLongClickListener clickListener) {
        this.longClickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /** 单选 */
    public void setSelected(int position) {
        CameraItem cameraItem = mCameraItems.get(position);
        boolean status = cameraItem.isSelected();
        cameraItem.setSelected(!status);
        mPresenter.selectPhoto(cameraItem);
        notifyItemChanged(position);
    }

    /** 滑动选择 */
    public void selectRangeChange(int start, int end, boolean selected) {
        if (start < 0 || end >= mCameraItems.size()) {
            return;
        }
        for (int i = start; i <= end; i++) {
            CameraItem model = getItem(i);
            model.setSelected(selected);
            mPresenter.selectPhoto(model);
            notifyItemChanged(i);
        }
    }

    /** 获取数据 */
    public CameraItem getItem(int i) {
        return mCameraItems.get(i);
    }

    @Override
    public String getSectionTitle(int position) {
        return null;
    }
}
