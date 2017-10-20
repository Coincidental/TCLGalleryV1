package com.gallery.tclgallery.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.listener.OnGridViewClickListener;
import com.gallery.tclgallery.sectionedrecyclerviewadapter.SimpleSectionedAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.viewholder.item.YearViewItemHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 年视图Adapter
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearViewAdapter extends SimpleSectionedAdapter<YearViewItemHolder> {

    private List<String> mMonths;
    private LinkedHashMap<String, List<String>> mDayOfMonths;
    private LinkedHashMap<String, LinkedHashMap<String, List<CameraItem>>> mMonthsPhotos;
    protected List<List<CameraItem>> mSectionPhotos;
    protected OnGridViewClickListener clickListener;

    public void setClickListener(OnGridViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setAllPhotos(LinkedHashMap<String, List<CameraItem>> allPhotos) {
        mMonths = new ArrayList<>();
        mDayOfMonths = new LinkedHashMap<>();
        mMonthsPhotos = new LinkedHashMap<>();
        mSectionPhotos=new LinkedList<>();
        for (Map.Entry<String, List<CameraItem>> entry : allPhotos.entrySet()) {
            mSectionPhotos.add(entry.getValue());
        }
        for(Map.Entry<String, List<CameraItem>> entry: allPhotos.entrySet()) {
            String key = entry.getKey();
            List<CameraItem> value = entry.getValue();
            String months = key.substring(0,10);
            if(!mMonthsPhotos.containsKey(months)) {
                List<String> day = new ArrayList<>();
                day.add(key);
                mDayOfMonths.put(months, day);
                LinkedHashMap<String, List<CameraItem>> oneMonths = new LinkedHashMap<>();
                oneMonths.put(key, value);
                mMonths.add(months);
                mMonthsPhotos.put(months, oneMonths);
            } else {
                List<String> day = mDayOfMonths.get(months);
                day.add(key);
                LinkedHashMap<String, List<CameraItem>> oneYear = mMonthsPhotos.get(months);
                oneYear.put(key, value);
            }
        }
        notifyDataSetChanged();
    }

    public int getMonthPosition(int position) {
        int section = 0;
        int sum = 0;
        for(Map.Entry<String, List<String>> photoSection: mDayOfMonths.entrySet()) {
            sum += photoSection.getValue().size() + 1;
            if(position < sum) {
                break;
            }
            section++;
        }
        int monthPosition = position - section - 1;
        monthPosition = monthPosition < 0 ? 0 : monthPosition;
        return monthPosition;
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return mMonths.get(section);//年的titile
    }

    @Override
    protected int getSectionCount() {
        return Format.isEmpty(mMonths) ? 0 : mMonths.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        String key = mMonths.get(section);
        int i = Format.isEmpty(mMonthsPhotos) ? 0 : mMonthsPhotos.get(key).size();
        Log.i("667", "getItemCountForSection: " + i);
        return 1;
    }

    @Override
    protected YearViewItemHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.holder_year_item, parent, false);
        YearViewItemHolder holder = new YearViewItemHolder(view);
        holder.setClickListener(clickListener);
        return holder;
    }

    @Override
    protected void onBindItemViewHolder(YearViewItemHolder holder, int section, int position) {
        if(holder.itemView==null){
            holder.itemView.setTag(position);
        }else {
            holder.itemView.getTag();
        }
        String MonthsKey = mMonths.get(section);
        String DayKey = mDayOfMonths.get(MonthsKey).get(position);
//        List<CameraItem> cameraItems = mMonthsPhotos.get(MonthsKey).get(DayKey);
        List<CameraItem> cameraItems = mSectionPhotos.get(section);
        Log.d("ccc", "onBindItemViewHolder: start");
        for (CameraItem cameraItem1:cameraItems){
            int i=0;
            Log.d("ccc", "onBindItemViewHolder: CameraItem"+i+"  =  "+cameraItem1.getPath());
            i++;
        }
        Log.d("ccc", "onBindItemViewHolder: end");
        holder.setData(DayKey, cameraItems);
        Log.d("fff", "onBindItemViewHolder: "+cameraItems.size()+"\tMonthsKey = "+MonthsKey+"\t DayKey = "+DayKey+"\tcameraItems = "+cameraItems.size()+"\tsection = "+section+"\tposition"+position+"\ncameraItems"+cameraItems);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof YearViewItemHolder) {
            YearViewItemHolder yearViewItemHolder = (YearViewItemHolder) holder;
            yearViewItemHolder.removeView();
        }
    }
}
