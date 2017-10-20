package com.gallery.tclgallery.viewholder.item;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.DefaultLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.listener.OnGridViewClickListener;
import com.gallery.tclgallery.utils.DateUtil;
import com.gallery.tclgallery.utils.ImageLoader;
import com.gallery.tclgallery.utils.UIUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 年视图Item（包含一个文字和一个GridView）
 * Created by jiaojie.jia on 2017/3/21.
 */

public class YearViewItemHolder extends RecyclerView.ViewHolder {
    public static final String TAG = "666";
    private VirtualLayoutManager layoutManager;
    private TextView mTextView;
    //    List<CameraItem> photos;
    private RecyclerView myRecyclerView;
    protected Context context;
    private MyAdapter mAdapter;
    boolean isToDay;
    int number;
    protected LinkedHashMap<String, List<CameraItem>> mAllPhotos;     // key-日期（月或日), value-该日期下的所有照片
    protected List<CameraItem> items;                       // 把上面照片集合转成一维集合，方便取值
    protected List<List<CameraItem>> mSectionPhotos;      // 照片集合
    protected List<String> mTitles;
    protected ImageView imgItem;        // 照片图片
    protected ImageView imgSelect;      // 右上角选中标识图片
    protected ImageView imageVideo;
    protected TextView duration;

    private OnGridViewClickListener mClickListener;

    private View.OnClickListener mChildClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mClickListener.onGridViewClick(getAdapterPosition());
        }
    };


    public void setClickListener(OnGridViewClickListener clickListener) {
        mClickListener = clickListener;
    }

    public YearViewItemHolder(final View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv_month);
        myRecyclerView = (RecyclerView) itemView.findViewById(R.id.gv_photos);
        layoutManager = new VirtualLayoutManager(context);
        myRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(layoutManager);
//        final ScrollFixLayoutHelper scrollFixLayoutHelper = new ScrollFixLayoutHelper(FixLayoutHelper.TOP_RIGHT, 100, 100);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                myRecyclerView.scrollToPosition(7);
                myRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }, 6000);
        myRecyclerView.setAdapter(mAdapter);
    }


    public void removeView() {
        if (itemView != null) {
            ((ViewGroup) itemView).removeView(myRecyclerView);
        }
    }

    public void setData(String month, List<CameraItem> photos) {
        mTextView.setText(month.substring(5));
        mTextView.setVisibility(View.GONE);
        if (((ViewGroup) itemView).getChildCount() == 1) {
            ((ViewGroup) itemView).addView(myRecyclerView);
        }
        mAdapter.setPhotos(photos);
    }


    private void setData1(CameraItem cameraItem) {
        if (State.SelectState) {
            imgSelect.setVisibility(View.VISIBLE);
            imgSelect.setSelected(cameraItem.isSelected());
        } else {
            imgSelect.setVisibility(View.GONE);
        }
        String type = cameraItem.getMime_type();
        if (type.indexOf("video") != -1) {
            imageVideo.setVisibility(View.VISIBLE);
            duration.setVisibility(View.VISIBLE);
            duration.setText(DateUtil.convertDuration(cameraItem.getDuration()));
        } else {
            imageVideo.setVisibility(View.GONE);
            duration.setVisibility(View.GONE);
        }
        String path = TextUtils.isEmpty(cameraItem.getThumbnail()) ? cameraItem.getPath() : cameraItem.getThumbnail();
        ImageLoader.loadGalleryImage(imgItem.getContext(), path, imgItem);
    }


    class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.iv_photo);
            imgSelect = (ImageView) itemView.findViewById(R.id.iv_select);
            imageVideo = (ImageView) itemView.findViewById(R.id.iv_video);
            duration = (TextView) itemView.findViewById(R.id.video_duration);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(UIUtils.getScreenWidth(), UIUtils.getScreenWidth() /5*3);
            itemView.setLayoutParams(lp);

        }
    }

    private class MyAdapter extends VirtualLayoutAdapter {
        List<CameraItem> photos;

        public void setPhotos(List<CameraItem> photos) {
            this.photos = photos;
            notifyDataSetChanged();
            myRecyclerView.setMinimumHeight((photos.size()/3+(photos.size()%3!=0?1:0))*(UIUtils.getScreenWidth()/5*3));
            final List<LayoutHelper> helpers = new LinkedList<>();
            int a = photos.size() % 3;
            if (a == 1) {
                final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
                gridLayoutHelper.setItemCount(photos.size() - 1);
                helpers.add(gridLayoutHelper);
                helpers.add(DefaultLayoutHelper.newHelper(1));
            } else if (a == 2) {
                final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
                gridLayoutHelper.setItemCount(photos.size() - 2);
                helpers.add(gridLayoutHelper);
                final ColumnLayoutHelper columnLayoutHelper = new ColumnLayoutHelper();
                columnLayoutHelper.setWeights(new float[]{60f, 38f});
                columnLayoutHelper.setItemCount(2);
                helpers.add(columnLayoutHelper);
            } else if (a == 0) {
                final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
                gridLayoutHelper.setItemCount(photos.size());
                helpers.add(gridLayoutHelper);
            }
            layoutManager.setLayoutHelpers(helpers);
        }

        public MyAdapter(@NonNull VirtualLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.holder_month_item, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder.itemView==null){
                holder.itemView.setTag(photos.get(position));
            }else {
                holder.itemView.getTag();
            }
            VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(
                    UIUtils.getScreenWidth() / 3, UIUtils.getScreenWidth() /5*3);
            if (photos.size() % 3 == 1) {
                if ((photos.size() - 1) == position)
                    layoutParams.width = UIUtils.getScreenWidth();
                layoutParams.height= UIUtils.getScreenWidth() /5*3;
            } else if (photos.size() % 3 == 2) {
                if ((photos.size() - 2) == position)
                    layoutParams.width = UIUtils.getScreenWidth() / 3 * 2;
                layoutParams.height= UIUtils.getScreenWidth() /5*3;
                if ((photos.size() - 1) == position)
                    layoutParams.width = UIUtils.getScreenWidth() / 3;
                layoutParams.height= UIUtils.getScreenWidth() /5*3;
            }
            Log.d("cccc", "onBindViewHolder: "+photos.get(position).getPath());

            setData1(photos.get(position));
            myRecyclerView.requestLayout();
        }

        @Override
        public int getItemCount() {
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

        @Override
        public int getItemViewType(int position) {
            position=photos.size()%3;
            return position;
        }
    }
}