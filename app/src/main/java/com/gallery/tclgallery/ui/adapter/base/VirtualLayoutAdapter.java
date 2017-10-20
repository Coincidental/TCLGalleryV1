package com.gallery.tclgallery.ui.adapter.base;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.fastscroll.SectionTitleProvider;
import com.gallery.tclgallery.listener.PhotoTimelineDataProvider;
import com.gallery.tclgallery.sectionedrecyclerviewadapter.HeaderViewHolder;
import com.gallery.tclgallery.sectionedrecyclerviewadapter.SimpleSectionedAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.item.CollageViewItemHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 月/日 视图的基类Adapter
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class VirtualLayoutAdapter<T extends CollageViewItemHolder> extends SimpleSectionedAdapter<T> implements SectionTitleProvider, PhotoTimelineDataProvider {
public  static VirtualLayoutAdapter baseViewAdapter;
    private LinkedHashMap<String, List<CameraItem>> mAllPhotos;       // key-日期（月或日), value-该日期下的所有照片

    protected List<String> mTitles;                             // 日期集合
    protected List<List<CameraItem>> mSectionPhotos;      // 照片集合
    protected List<CameraItem> items;                       // 把上面照片集合转成一维集合，方便取值

    protected View.OnLongClickListener longClickListener;
    protected View.OnClickListener clickListener;
    protected GooglePhotoContract.Presenter mPresenter;
    @NonNull
    protected VirtualLayoutManager mLayoutManager;

    public VirtualLayoutAdapter(@NonNull VirtualLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public VirtualLayoutAdapter() {

    }

    public void setLayoutHelpers(List<LayoutHelper> helpers) {
        this.mLayoutManager.setLayoutHelpers(helpers);
    }

    @NonNull
    public List<LayoutHelper> getLayoutHelpers() {
        return this.mLayoutManager.getLayoutHelpers();
    }


    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public void setAllPhotos(LinkedHashMap<String, List<CameraItem>> allPhotos) {
        mAllPhotos = allPhotos;
        mTitles = new ArrayList<>(mAllPhotos.size());
        mSectionPhotos = new ArrayList<>(mAllPhotos.size());
        items = new ArrayList<>();
        for (Map.Entry<String, List<CameraItem>> entry : mAllPhotos.entrySet()) {
            mTitles.add(entry.getKey());
            mSectionPhotos.add(entry.getValue());
        }
        for (List<CameraItem> photoSection : mSectionPhotos) {
            for (CameraItem cameraItem : photoSection) {
                items.add(cameraItem);
            }
        }
        initOther();
        notifyDataSetChanged();
    }

    public void initOther() {
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return mTitles.get(section);
    }

    @Override
    protected int getSectionCount() {
        return Format.isEmpty(mTitles) ? 0 : mTitles.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return Format.isEmpty(mSectionPhotos) ? 0 : mSectionPhotos.get(section).size();
    }

    public void setLongClickListener(View.OnLongClickListener clickListener) {
        this.longClickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public List<Float> getPercents() {
        return mPresenter.getPercents();
    }

    @Override
    public List<String> getTitles() {
        return mPresenter.getTimelineTags();
    }

    /**
     * 单选
     */
    public void setSelected(int position) {
        CameraItem cameraItem = items.get(getDataPositionByView(position));
        boolean status = cameraItem.isSelected();
        cameraItem.setSelected(!status);
        mPresenter.selectPhoto(cameraItem);
        notifyItemChanged(position);
        mPresenter.loadPhotos();
    }

    /**
     * 滑动选择
     */
    public void selectRangeChange(int start, int end, boolean selected) {
        if (start < 0 || end >= items.size() + mSectionPhotos.size()) {
            return;
        }
        for (int i = start; i <= end; i++) {
            if (isSectionItem(i)) {
                return;
            }
            CameraItem model = getItem(getDataPositionByView(i));
            model.setSelected(selected);
            mPresenter.selectPhoto(model);
            notifyItemChanged(i);
        }
    }

    /**
     * 获取数据
     */
    public CameraItem getItem(int i) {
        return items.get(i);
    }

    /**
     * 由列表位置得到数据位置
     */
    private int getDataPositionByView(int position) {
        int dataPosition = position - getSection(position) - 1;
        return dataPosition < 0 ? 0 : dataPosition;
    }

    /**
     * 根据列表物理位置返回此位置所属Section
     */
    private int getSection(int position) {
        int section = 0;
        int sum = 0;
        for (List<CameraItem> photoSection : mSectionPhotos) {
            sum += photoSection.size() + 1;
            if (position < sum) {
                break;
            }
            section++;
        }
        return section;
    }

    /**
     * 当前位置是否为header
     */
    private boolean isSectionItem(int position) {
        int sum = 0;
        for (List<CameraItem> photoSection : mSectionPhotos) {
            sum += photoSection.size() + 1;
            if (position == sum) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取指定分组header的位置
     */
    public int getHeaderPosition(int section) {
        int sum = 0;
        for (int i = 0; i < section; i++) {
            List<CameraItem> photoSection = mSectionPhotos.get(i);
            sum += photoSection.size() + 1;
        }
        return sum;
    }

    @Override
    public String getSectionTitle(int position) {
        return getSectionHeaderTitle(getSection(position));
    }

    /*判断当前部分是否全选*/
    public boolean istextSelect(int section) {
        List<CameraItem> selectSection = mSectionPhotos.get(section);
        boolean istextSelect = true;
        if (istextSelect) {
            for (CameraItem cameraItem : selectSection) {
                boolean status = cameraItem.isSelected();
                Log.d("whj", "istextSelect: status" + status);
                if (!status) istextSelect = false;
            }
        }
        return istextSelect;
    }

    /*标题选择的处理*/
    public void selectconctrol(boolean isselect, int section) {
        List<CameraItem> selectSection = mSectionPhotos.get(section);
        int sum = 0;//遍历开始的position
        int select = 0;//遍历的次数
//        Log.d("whj", "selectconctrol: START");
        for (int i = 0; i < section; i++) {
            sum += mSectionPhotos.get(i).size() + 1;
//            Log.d("whj", "selectconctrol: sum" + sum);
        }
        select = selectSection.size();
//        Log.d("whj", "selectconctrol: select" + select);
        while (select != 0) {
            ++sum;
            CameraItem cameraItem = items.get(getDataPositionByView(sum));
            cameraItem.setSelected(isselect);
            mPresenter.selectPhoto(cameraItem);
            notifyItemChanged(sum);
            select--;
//            Log.d("whj", "selectconctrol: sum" + sum + "\tselect" + select);
        }
    }
    /*判断当前部分是否全选*/
    public boolean istextSelect1(int section) {
        List<CameraItem> selectSection = mSectionPhotos.get(section);
        boolean istextSelect = true;
        if (istextSelect) {
            for (CameraItem cameraItem : selectSection) {
                boolean status = cameraItem.isSelected();
                Log.d("whj", "istextSelect: status" + status);
                if (!status) istextSelect = false;
            }
        }
        return istextSelect;
    }
    @Override
    protected HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(getLayoutResource(), parent, false);
        final HeaderViewHolder holder = new HeaderViewHolder(view, getTitleTextID(), getTitleTextSelectID());
        return holder;
    }

    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderViewHolder holder, final int section) {
        final String title = getSectionHeaderTitle(section);
        holder.render(title);
        if (State.SelectState) {
            holder.textSelct.setVisibility(View.VISIBLE);
            if (istextSelect1(section)) holder.textSelct.setSelected(true);
            else holder.textSelct.setSelected(false);
        } else holder.textSelct.setVisibility(View.GONE);
        holder.textSelct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textSelct.setSelected(!holder.textSelct.isSelected());
                selectconctrol(holder.textSelct.isSelected(), section);
//                Log.d("whj", "onClick: section" + section + "\t" + !holder.textSelct.isSelected());
            }
        });
    }

}
