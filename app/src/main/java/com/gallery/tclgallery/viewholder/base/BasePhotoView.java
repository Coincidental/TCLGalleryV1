package com.gallery.tclgallery.viewholder.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.ScaleGestureDetector;
import android.view.View;


import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.fastscroll.FastScroller;
import com.gallery.tclgallery.listener.DragSelectTouchListener;
import com.gallery.tclgallery.listener.OnSwitchViewListener;
import com.gallery.tclgallery.sectionedrecyclerviewadapter.SectionedSpanSizeLookup;
import com.gallery.tclgallery.ui.GooglePhotoActivity;
import com.gallery.tclgallery.ui.RolloutPreviewActivity;
import com.gallery.tclgallery.ui.adapter.base.BaseViewAdapter;
import com.gallery.tclgallery.utils.Format;
import com.gallery.tclgallery.utils.RolloutBDInfo;
import com.gallery.tclgallery.utils.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 相册视图基类
 * Created by jiaojie.jia on 2017/3/20.
 */

public abstract class BasePhotoView extends BaseHolder<LinkedHashMap<String, List<CameraItem>>> {
    protected RecyclerView mRecyclerView;           // 列表
    protected BaseViewAdapter mAdapter;
    private FastScroller fastScroller;              // 右侧快速导航
    private DragSelectTouchListener touchListener;              // 滑动选择 Listener
    protected OnSwitchViewListener mSwitchViewListener;         // 缩放手势 Listener
    private RolloutBDInfo bdInfo;
    private GooglePhotoContract.Presenter mPresenter;

    public BasePhotoView(Context context) {
        super(context);
    }

    public void setSwitchViewListener(OnSwitchViewListener switchViewListener) {
        mSwitchViewListener = switchViewListener;
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
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        fastScroller.setRecyclerView(mRecyclerView);
        final GridLayoutManager layoutManager = getLayoutManager();
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter.setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                GooglePhotoActivity.LongCickState.invalidateOptionsMenu();
                State.SelectState=true;
                int position = mRecyclerView.getChildAdapterPosition(v);
                mAdapter.setSelected(position);
                touchListener.setStartSelectPosition(position);
                refreshView();
                return false;
            }
        });

        mAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (State.SelectState) {
                    int position = mRecyclerView.getChildAdapterPosition(v);
                    mAdapter.setSelected(position);
//                    Log.d("whj", "onClick: position"+position);

                }else {
                    LinkedHashMap<String, List<CameraItem>> mAllPhotos;       // key-日期（月或日), value-该日期下的所有照片

                    List<String> mTitles;                             // 日期集合
                    List<List<CameraItem>> mSectionPhotos;      // 照片集合
                    List<CameraItem> items;
                    mAllPhotos = data;
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

                    bdInfo=new RolloutBDInfo();
                    int position = mRecyclerView.getChildAdapterPosition(v);

                    int section = 0;
                    int sum = 0;
                    for (List<CameraItem> photoSection : mSectionPhotos) {
                        sum += photoSection.size() + 1;
                        if (position < sum) {
                            break;
                        }
                        section++;
                    }
                    int dataPosition = position -section - 1;
                    int Rposition=(dataPosition < 0 ? 0 : dataPosition);

                    View c=layoutManager.getChildAt(0);
                    int top=c.getTop();
                    int firstVisiblePosition=layoutManager.findFirstVisibleItemPosition();
                    int a, b;
                    int Rposition1=0;
                    int  size = 0;
                    int hang = 0;
                    //模拟位置
                    if (mPresenter.getViewType()== GooglePhotoActivity.ViewType.DAY) {
                    for (int i=0;i<section;i++){
                        Rposition1+=mSectionPhotos.get(i).size()+(4-mSectionPhotos.get(i).size()%4);
                        size+=mSectionPhotos.get(i).size();
                    }
                    Rposition1=Rposition-size+Rposition1;

                    //模拟行列划分3等分进行计算

                        a = Rposition1 / 4;
                        b = Rposition1 % 4;
                        bdInfo.width = (UIUtils.getScreenWidth() - 4 * UIUtils.dip2px(2)) / 4;
                        bdInfo.height = bdInfo.width;

                        //把屏幕划分成了行和列，采用行列估算方法，进行计算位置
                        bdInfo.x = UIUtils.dip2px(1) + b * bdInfo.width + b * UIUtils.dip2px(2);

                        //模拟实际firstVisiblePosition的行数
//                    int title=1;
//                    Log.d("mm", "onClick: 1 = "+firstVisiblePosition);
                        for (int a1 = 0; a1 < mSectionPhotos.size(); a1++) {
                            if (a1 != 0) {
                                firstVisiblePosition = firstVisiblePosition - 1;
//                            title++;
                            }
                            if (firstVisiblePosition > 0) {
                                if (((firstVisiblePosition - mSectionPhotos.get(a1).size())) >= 0) {
                                    firstVisiblePosition = firstVisiblePosition - mSectionPhotos.get(a1).size();
                                    hang += mSectionPhotos.get(a1).size() / 4 + (mSectionPhotos.get(a1).size() % 4 != 0 ? 1 : 0);
//                                Log.d("mm", "onClick: 2 = "+firstVisiblePosition+"\t\thang = "+hang);

                                } else {
                                    hang += firstVisiblePosition / 4 + (firstVisiblePosition % 4 == 0 ? 0 : 1);
//                                Log.d("mm", "onClick: 3 = "+firstVisiblePosition+"\t\thang = "+hang);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        hang = hang - 1;
//                    bdInfo.y = UIUtils.dip2px( 1) + bdInfo.height * (a - firstVisiblePosition) + top + (a - firstVisiblePosition) * UIUtils.dip2px( 2) + top - UIUtils.dip2px( 1)+section*UIUtils.dip2px(20);
                        bdInfo.y = UIUtils.dip2px(1) + bdInfo.height * (a - hang) + top + (a - hang) * UIUtils.dip2px(2) + layoutManager.getPaddingTop() - UIUtils.dip2px(1);
//                    Log.d("mm", "onClick: firstVisiblePosition = "+firstVisiblePosition+"\t\tbdInfo.y = "+bdInfo.y+"\t\tRposition1 = "+Rposition1+"\t\tRposition = "+Rposition);
//                    Log.d("nn", "bdInfo.height"+bdInfo.height+"\nbdInfo.height * (a - hang) = "+bdInfo.height * (a - hang)+"\ntop = "+top+"\nlayoutManager.getPaddingTop() = "
//                            +layoutManager.getPaddingTop()+"\ntitle*UIUtils.dip2px(20) = "+title*UIUtils.dip2px(5)+"\n(a - hang) * UIUtils.dip2px( 2) = "+(a - hang) * UIUtils.dip2px( title)+"\ntitle"+title+"\nbdInfo.y = "+bdInfo.y);
//                    Log.d("mm", "onClick: hang = "+hang+"\t\tbdInfo.y = "+bdInfo.y+"\t\tRposition1 = "+Rposition1+"\t\tRposition = "+Rposition+"\t\thang1 = "+hang1+"\t\tfirstVisiblePosition = "+firstVisiblePosition+"\t\ttop = "+top);
                    }else if (mPresenter.getViewType()== GooglePhotoActivity.ViewType.MONTH){
                        for (int i=0;i<section;i++){
                            Rposition1+=mSectionPhotos.get(i).size()+(6-mSectionPhotos.get(i).size()%6);
                            size+=mSectionPhotos.get(i).size();
                        }
                        Rposition1=Rposition-size+Rposition1;

                        //模拟行列划分6等分进行计算

                        a = Rposition1 / 6;
                        b = Rposition1 % 6;
                        bdInfo.width = (UIUtils.getScreenWidth() - 6 * UIUtils.dip2px(2)) / 6;
                        bdInfo.height = bdInfo.width;

                        //把屏幕划分成了行和列，采用行列估算方法，进行计算位置
                        bdInfo.x = UIUtils.dip2px(1) + b * bdInfo.width + b * UIUtils.dip2px(2);

                        //模拟实际firstVisiblePosition的行数
                        for (int a1 = 0; a1 < mSectionPhotos.size(); a1++) {
                            if (a1 != 0) {
                                firstVisiblePosition = firstVisiblePosition - 1;
                            }
                            if (firstVisiblePosition > 0) {
                                if (((firstVisiblePosition - mSectionPhotos.get(a1).size())) >= 0) {
                                    firstVisiblePosition = firstVisiblePosition - mSectionPhotos.get(a1).size();
                                    hang += mSectionPhotos.get(a1).size() / 6 + (mSectionPhotos.get(a1).size() % 6 != 0 ? 1 : 0);
                                } else {
                                    hang += firstVisiblePosition / 6 + (firstVisiblePosition % 6 == 0 ? 0 : 1);
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        hang = hang - 1;
                        bdInfo.y = UIUtils.dip2px(1) + bdInfo.height * (a - hang) + top + (a - hang) * UIUtils.dip2px(10) + layoutManager.getPaddingTop() - UIUtils.dip2px(1)+(section+1)*UIUtils.dip2px(20);
                    }
                    Intent intent = new Intent(context, RolloutPreviewActivity.class);
                    intent.putExtra("data",  (Serializable)items);
                    intent.putExtra("bdinfo", bdInfo);
                    intent.putExtra("index", Rposition);
                    intent.putExtra("type", 2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
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

        touchListener.setScaleGestureDetector(getScaleDetector());

        return view;
    }

    protected abstract ScaleGestureDetector getScaleDetector();

    protected abstract GridLayoutManager getLayoutManager();
    protected abstract BaseViewAdapter getAdapter();


    /**
     * 滚动到指定位置
     */
    public void scrollToPosition(int position) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    /**
     * 滚动到指定分组
     */
    public void scrollToSection(int section) {
        int position = mAdapter.getHeaderPosition(section);
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(position);
            int scrollY = 0;
            RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder != null) {        // 显示在屏幕中
                View view = viewHolder.itemView;
                scrollY = view.getTop();
            } else {                        // 没有显示
                View view = mRecyclerView.getChildAt(0);
                if (view != null) {
                    int firstVisable = mRecyclerView.getLayoutManager().getPosition(view);
                    if (firstVisable < position) {       // 目标位置在下方，需要滚动
                        scrollY = UIUtils.getScreenHeight() - UIUtils.dip2px(98) - UIUtils.getStatusBarHeight();
                    }
                }
            }
            final int finalScrollY = scrollY;
            if (finalScrollY != 0) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.scrollBy(0, finalScrollY);
                    }
                }, 100);
            }
        }
    }

    /**
     * 判断当前视图是否填充了数据
     */
    public boolean isEmpty() {
        return !(mAdapter != null && mAdapter.getItemCount() > 0);
    }

    @Override
    public void refreshView() {
        if (Format.isEmpty(data)) return;
        mAdapter.setAllPhotos(data);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                    fastScroller.initTimelineView();

            }
        }, 1000);
    }

    /**
     * 清除数据选中状态
     */
    public void clearSelectedStatus() {
        mAdapter.notifyDataSetChanged();
    }

}
