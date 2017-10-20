package com.gallery.tclgallery.ui.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.sectionedrecyclerviewadapter.HeaderViewHolder;
import com.gallery.tclgallery.ui.adapter.base.BaseViewAdapter;
import com.gallery.tclgallery.ui.adapter.base.VirtualLayoutAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.item.CollageViewItemHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/27.*/


public class CollageViewAdapter extends VirtualLayoutAdapter<CollageViewItemHolder> {
    private  ImageView imgItem;        // 照片图片
    private  ImageView imgSelect;      // 右上角选中标识图片
    private  ImageView imageVideo;
    private  TextView duration;
    private LinkedHashMap<String, List<CameraItem>> mAllPhotos;       // key-日期（月或日), value-该日期下的所有照片

    protected List<String> mTitles;                             // 日期集合
    protected List<List<CameraItem>> mSectionPhotos;      // 照片集合
    protected List<CameraItem> items;                       // 把上面照片集合转成一维集合，方便取值

    protected View.OnLongClickListener longClickListener;
    protected View.OnClickListener clickListener;

    protected GooglePhotoContract.Presenter mPresenter;
        private List<String> mMonths;       // 月集合
    View view;

    @NonNull
    protected VirtualLayoutManager mLayoutManager;

    public CollageViewAdapter(@NonNull VirtualLayoutManager layoutManager) {
        super();
        Log.d("666", "CollageViewAdapter: 4");
        this.mLayoutManager = layoutManager;
    }

    public void setLayoutHelpers(List<LayoutHelper> helpers) {
        Log.d("666", "CollageViewAdapter: 5");
        this.mLayoutManager.setLayoutHelpers(helpers);
    }

    @NonNull
    public List<LayoutHelper> getLayoutHelpers() {
        Log.d("666", "CollageViewAdapter: 6");
        return this.mLayoutManager.getLayoutHelpers();
    }




    public void setPresenter(GooglePhotoContract.Presenter presenter) {
        mPresenter = presenter;
    }

   /* public void setAllPhotos(LinkedHashMap<String, List<CameraItem>> allPhotos) {
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
        Log.d("333", "setAllPhotos: ");
        initOther();
        notifyDataSetChanged();
    }*/
        @Override
        protected CollageViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            Log.d("666", "onCreateItemViewHolder: 7");
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.holder_month_item, parent, false);
            view.setOnLongClickListener(longClickListener);
            view.setOnClickListener(clickListener);
            imgItem = (ImageView) view.findViewById(R.id.iv_photo);
            imgSelect = (ImageView) view.findViewById(R.id.iv_select);
            imageVideo = (ImageView) view.findViewById(R.id.iv_video);
            duration = (TextView) view.findViewById(R.id.video_duration);
            return new CollageViewItemHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(CollageViewItemHolder holder, int section, int position) {
           /* Log.d("666", "onBindItemViewHolder: 8");
            VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            holder.itemView.setLayoutParams(layoutParams);
            for (Integer a :CollageView.collageView.one){
                if(a==position){layoutParams.width=UIUtils.getScreenWidth();layoutParams.height=UIUtils.getScreenHeight()/4;}
            } for (Integer a :CollageView.collageView.two){
                if(a==position){layoutParams.width=UIUtils.getScreenWidth()*2/3;layoutParams.height=UIUtils.getScreenHeight()/4;}
            } for (Integer a :CollageView.collageView.three){
                if(a==position){layoutParams.width=UIUtils.getScreenWidth()/3;layoutParams.height=UIUtils.getScreenHeight()/4;}
            }
            Log.d("333", "onBindItemViewHolder: layoutParams.width  ==  "+layoutParams.width+"\tlayoutParams.height  ==  "+layoutParams.height);*/
            CameraItem cameraItem = mSectionPhotos.get(section).get(position);
            holder.setData(cameraItem);
        }
    @Override
    public int getItemCount() {
        Log.d("666", "CollageViewAdapter: 9");
        List<LayoutHelper> helpers = getLayoutHelpers();
        if (helpers == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0, size = helpers.size(); i < size; i++) {
            count += helpers.get(i).getItemCount();
        }
        return count;
    }

/*@Override
        public void initOther() {
            mMonths = new ArrayList<>();
            List<Float> percents = new ArrayList<>(mTitles.size());
            for(String title: mTitles) {
                String month = title.substring(0, 8);
                if(!mMonths.contains(month)) {
                    mMonths.add(month);
                }
            }
            mPresenter.setTimelineData(percents, mTitles);
        }*/


/**
         * 由日视图中的 position 得到在月视图中所属 Section
         * @param position
         * @return*/


        public int getSectionInMonthView(int position) {
            Log.d("666", "CollageViewAdapter: 10");
            String title = getSectionTitle(position);
            int section = 0;
            for(String month: mMonths) {
                if(title.startsWith(month)) {
                    break;
                }
                section++;
            }
            return section;
        }

    @Override
    public List<Float> getPercents() {
        return mPresenter.getPercents();
    }

    @Override
    public List<String> getTitles() {
        return mPresenter.getTimelineTags();
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
    public String getSectionTitle(int position) {
        return getSectionHeaderTitle(getSection(position));

    }
/**
     * 单选*/


    public void setSelected(int position) {
        CameraItem cameraItem = items.get(getDataPositionByView(position));
        boolean status = cameraItem.isSelected();
        cameraItem.setSelected(!status);
        mPresenter.selectPhoto(cameraItem);
        notifyItemChanged(position);
        mPresenter.loadPhotos();
    }

/**
     * 滑动选择*/


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
     * 获取数据*/


    public CameraItem getItem(int i) {
        return items.get(i);
    }

/**
     * 由列表位置得到数据位置*/


    private int getDataPositionByView(int position) {
        int dataPosition = position - getSection(position) - 1;
        return dataPosition < 0 ? 0 : dataPosition;
    }

/**
     * 根据列表物理位置返回此位置所属Section*/


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
     * 当前位置是否为header*/


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
     * 获取指定分组header的位置*/


    public int getHeaderPosition(int section) {
        int sum = 0;
        for (int i = 0; i < section; i++) {
            List<CameraItem> photoSection = mSectionPhotos.get(i);
            sum += photoSection.size() + 1;
        }
        return sum;
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
        if ( State.SelectState) {
            holder.textSelct.setVisibility(View.VISIBLE);
            if (BaseViewAdapter.baseViewAdapter.istextSelect(section)) holder.textSelct.setSelected(true);
            else holder.textSelct.setSelected(false);
        } else holder.textSelct.setVisibility(View.GONE);
        holder.textSelct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.textSelct.setSelected(!holder.textSelct.isSelected());
                BaseViewAdapter.baseViewAdapter.selectconctrol(holder.textSelct.isSelected(), section);
//                Log.d("whj", "onClick: section" + section + "\t" + !holder.textSelct.isSelected());
            }
        });
    }


}


