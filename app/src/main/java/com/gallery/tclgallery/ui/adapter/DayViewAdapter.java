package com.gallery.tclgallery.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.ui.adapter.base.BaseViewAdapter;
import com.gallery.tclgallery.viewholder.item.DayViewItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 日视图Adapter
 * Created by jiaojie.jia on 2017/3/20.
 */

public class DayViewAdapter extends BaseViewAdapter<DayViewItemHolder> {

    private List<String> mMonths;       // 月集合

    @Override
    protected DayViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_month_item, parent, false);
        view.setOnLongClickListener(longClickListener);
        view.setOnClickListener(clickListener);
        return new DayViewItemHolder(view);
    }

    @Override
    protected void onBindItemViewHolder(DayViewItemHolder holder, int section, int position) {
        CameraItem cameraItem = mSectionPhotos.get(section).get(position);
        holder.setData(cameraItem);
    }

    @Override
    public void initOther() {
        mMonths = new ArrayList<>();
        List<Float> percents = new ArrayList<>(mTitles.size());
        for (String title : mTitles) {
            String month = title.substring(0, 8);
            if (!mMonths.contains(month)) {
                mMonths.add(month);
            }
        }
        mPresenter.setTimelineData(percents, mTitles);
    }

    /**
     * 由日视图中的 position 得到在月视图中所属 Section
     *
     * @param position
     * @return
     */
    public int getSectionInMonthView(int position) {
        String title = getSectionTitle(position);
        int section = 0;
        for (String month : mMonths) {
            if (title.startsWith(month)) {
                break;
            }
            section++;
        }
        return section;
    }
}