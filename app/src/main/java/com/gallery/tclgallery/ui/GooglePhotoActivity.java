package com.gallery.tclgallery.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


import com.gallery.tclgallery.R;
import com.gallery.tclgallery.anim.SwitchViewAnimHelper;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.bean.State;
import com.gallery.tclgallery.contract.GooglePhotoContract;
import com.gallery.tclgallery.listener.OnSwitchViewListener;
import com.gallery.tclgallery.presenter.GooglePhotoPresenter;
import com.gallery.tclgallery.utils.ImageLoader;
import com.gallery.tclgallery.viewholder.DayView;
import com.gallery.tclgallery.viewholder.MonthView;
import com.gallery.tclgallery.viewholder.MyListView;
import com.gallery.tclgallery.viewholder.YearView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 主 Activity
 */
public class GooglePhotoActivity extends AppCompatActivity implements GooglePhotoContract.View {

    /**
     * 视图类型
     */
    public enum ViewType {
        YEAR, MONTH, DAY, /*OTHER,*/COLLAGE, LIST
    }

    private ViewGroup mContainer;       // 视图容器
    private MyListView myListView;
    private YearView mYearView;         // 年视图
    private MonthView mMonthView;       // 月视图
    private DayView mDayView;           // 日视图
    //    private OtherView mOtherView;  // 其他文件夹视图
    private Toolbar toolbar;
    //    private PhotoFoldersAdapter mFoldersAdapter;
//    private BottomSheetBehavior mBottomSheetBehavior;
    private GooglePhotoContract.Presenter mPresenter;
    public static GooglePhotoActivity LongCickState = null;
    /**
     * 缩放操作回调
     */
    private OnSwitchViewListener mSwitchViewListener = new OnSwitchViewListener() {
        @Override
        public void onSwitchView(float scaleFactor) {
            switch (mPresenter.getViewType()) {
                case YEAR:
                    switchView(ViewType.MONTH, 0);
                    break;
                /*case COLLAGE:
                    switchView(ViewType.COLLAGE);*/
                case MONTH:
                    if (scaleFactor > 1) {
                        switchView(ViewType.DAY);
                    } else if (scaleFactor < 1) {
//                        switchView(ViewType.YEAR);
                        switchView(ViewType.COLLAGE);
                    }
                    break;
                case DAY:
                    if (1 < scaleFactor && scaleFactor < 2) {
                        switchView(ViewType.MONTH);
                    } else if (0 < scaleFactor && scaleFactor < 1) {
//                        switchView(ViewType.YEAR);
                        switchView(ViewType.COLLAGE);
                    }
            }
        }

        @Override
        public void onSwitchViewBySection(int section) {
            switchView(ViewType.MONTH, section);
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_photo);
        LongCickState = this;


        mPresenter = new GooglePhotoPresenter(this);

        setupToolbar();

        initDateViews();

//        initPhotoFolders();

        switchView(ViewType.YEAR);      // 默认显示月视图
    }

    /**
     * 初始化Toolbar
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * 初始化各个视图
     */
    private void initDateViews() {
        mContainer = (ViewGroup) findViewById(R.id.fl_container);
        mYearView = new YearView(this);
        mYearView.setSwitchViewListener(mSwitchViewListener);
        mMonthView = new MonthView(this);
        mMonthView.setSwitchViewListener(mSwitchViewListener);
        mMonthView.setPresenter(mPresenter);
        mDayView = new DayView(this);
        mDayView.setPresenter(mPresenter);
        mDayView.setSwitchViewListener(mSwitchViewListener);
        myListView =new MyListView(this);
//        myListView.setPresenter(mPresenter);
//        myListView.setSwitchViewListener(mSwitchViewListener);
        /*mCollageView= new CollageView(this);
        mCollageView.setPresenter(mPresenter);
        mCollageView.setSwitchViewListener(mSwitchViewListener);*/
//        mOtherView = new OtherView(this);
//        mOtherView.setPresenter(mPresenter);
    }

    /**
     * 初始化文件夹列表
     */
    /*private void initPhotoFolders() {
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        RecyclerView rvPhotoFolders = (RecyclerView) bottomSheet.findViewById(R.id.rv_filedir);
        rvPhotoFolders.setLayoutManager(new LinearLayoutManager(this));
        mFoldersAdapter = new PhotoFoldersAdapter();
        rvPhotoFolders.setAdapter(mFoldersAdapter);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mFoldersAdapter.setOnItemClickListener(new OnEditItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                ImageFolder folder = mFoldersAdapter.getItem(position);
                mOtherView.setData(folder);
                if (folder.isPhoto()) {
                    switchView(ViewType.MONTH);
                } else {
                    mContainer.removeAllViews();
                    mPresenter.setViewType(ViewType.OTHER);
                    mContainer.addView(mOtherView.getRootView());
                }
            }
        });
    }*/
    @Override
    public void fullData(LinkedHashMap<String, List<CameraItem>> sections) {
        switch (mPresenter.getViewType()) {
            case YEAR:
                mYearView.setData(sections);
                break;
            case MONTH:
                mMonthView.setData(sections);
                break;
            case DAY:
                mDayView.setData(sections);
                break;
            /*case COLLAGE:
                mCollageView.setData(sections);*/
        }
    }

   /* @Override
    public void fullFolders(List<ImageFolder> folders) {
        mFoldersAdapter.setData(folders);
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
//        mPresenter.loadPhotos();
        //数据刷新
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        mPresenter.clear();
        ImageLoader.clearMemory();
    }

    @Override
    public void onBackPressed() {
       /* if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }*/
        if (!mPresenter.isSelectedEmpty()) {
            mPresenter.cancleAllSelected();
            mMonthView.clearSelectedStatus();
            mDayView.clearSelectedStatus();
//            mOtherView.clearSelectedStatus();
            return;
        }
        if (mPresenter.isSelectedEmpty() && State.SelectState) {
            State.SelectState = false;
            invalidateOptionsMenu();
            mPresenter.loadPhotos();
            return;
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_view_menu, menu);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mPresenter.getViewType() == ViewType.DAY) {
            menu.findItem(R.id.day_view).setChecked(false);
            menu.findItem(R.id.month_view).setChecked(true);
            menu.findItem(R.id.year_view).setChecked(true);
        } else if (mPresenter.getViewType() == ViewType.MONTH) {
            menu.findItem(R.id.day_view).setChecked(true);
            menu.findItem(R.id.year_view).setChecked(true);
            menu.findItem(R.id.month_view).setChecked(false);
        } else if (mPresenter.getViewType() == ViewType.YEAR) {
            menu.findItem(R.id.day_view).setChecked(true);
            menu.findItem(R.id.month_view).setChecked(true);
            menu.findItem(R.id.year_view).setChecked(false);
        }
        if (mPresenter.isAllSelect()) {
            menu.findItem(R.id.selectall).setTitle(R.string.unselectall);
        } else {
            menu.findItem(R.id.selectall).setTitle(R.string.selectall);
        }
        if (State.SelectState) {
            menu.findItem(R.id.share).setVisible(true);
            menu.findItem(R.id.delete).setVisible(true);
            menu.findItem(R.id.camera).setVisible(false);
            menu.findItem(R.id.selectall).setVisible(true);
        } else {
            menu.findItem(R.id.share).setVisible(false);
            menu.findItem(R.id.delete).setVisible(false);
            menu.findItem(R.id.camera).setVisible(true);
            menu.findItem(R.id.selectall).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.year_view:
//                switchView(ViewType.YEAR);
                switchView(ViewType.LIST);
                break;
            case R.id.month_view:
                switchView(ViewType.MONTH);
                break;
            case R.id.day_view:
                switchView(ViewType.DAY);
                break;
            /*case R.id.other_view:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;*/
            case R.id.delete:
                delete();
                break;
            case R.id.share:
                share();
                break;
            case R.id.camera:
                TakePicture();
                break;
            case R.id.selectall:
                if (!mPresenter.isAllSelect()) {
                    mPresenter.selectPhotoAll();
                    //item.setTitle(R.string.unselectall);
                } else {
                    mPresenter.cancleAllSelected();
                    //item.setTitle(R.string.selectall);
                }
                mPresenter.loadPhotos();
                break;
        }
        return true;
    }

    private void TakePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    private void share() {
        if (mPresenter.SelectPictrueNumber() == 1 && mPresenter.SelectPictrueNumber() != 0) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, mPresenter.ShareFile().get(0));
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "share image to ..."));
        } else {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mPresenter.ShareFile());
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share images to.."));
        }
    }

    /*删除dialog方法*/
    private void delete() {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setMessage(getDeleteString(mPresenter.SelectPictrueNumber()))
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                }).setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePicture();
                //数据刷新
                refresh();
            }
        }).show();

    }

    //数据刷新
    public void refresh() {
        mContainer.removeAllViews();
        mPresenter.scanPhotos();
        mPresenter.setViewType(mPresenter.getViewType());
        if (mPresenter.getViewType() == ViewType.DAY) {
            mContainer.addView(mDayView.getRootView());
        } else if (mPresenter.getViewType() == ViewType.MONTH) {
            mContainer.addView(mMonthView.getRootView());
        }
    }

    /*删除图片*/
    private void deletePicture() {
        ArrayList<String> filelist = mPresenter.DeleteFile();
        String[] filepath = new String[filelist.size()];
        for (int i = 0; i < filelist.size(); i++) {
            filepath[i] = filelist.get(i);
            if (isPic(filepath[i])) {
                GooglePhotoActivity.this.getContentResolver()
                        .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                MediaStore.Images.Media.DATA
                                        + " LIKE ?",
                                new String[]{filepath[i]});
            }
        }
    }

    /*判断是否为图片*/
    private boolean isPic(String path) {
        if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")
                || path.endsWith(".bmp") || path.endsWith(".gif")) {
            return true;
        }
        return false;
    }


    private int getDeleteString(int num) {
        switch (num) {
            case 0:
                break;
            case 1:
                return R.string.delete_photo;
            default:
                return R.string.delete_photos;
        }
        return R.string.app_name;
    }

    /**
     * 切换视图
     *
     * @param viewType 视图类型
     */
    public void switchView(ViewType viewType) {
        switchView(viewType, 0);
    }

    public void switchView(ViewType viewType, int section) {
        if ((mContainer.getChildCount() != 0 && mPresenter.getViewType() == viewType)
                || SwitchViewAnimHelper.getInstance().isAnimRunning()) {
            return;
        }
        switch (viewType) {
            case YEAR:
                toolbar.setTitle(R.string.year_view);
                break;
            case MONTH:
                toolbar.setTitle(R.string.month_view);
                break;
            case DAY:
                toolbar.setTitle(R.string.day_view);
                break;
            /*case COLLAGE:
                toolbar.setTitle("Collage View");*/
        }
        boolean load = true;
        mContainer.removeAllViews();
        switch (viewType) {
            case YEAR:
                mContainer.addView(mYearView.getRootView());
                SwitchViewAnimHelper.getInstance().toSmallView(mYearView.getRootView());

                // load = mYearView.isEmpty();
                break;
            case MONTH:
                mContainer.addView(mMonthView.getRootView());
                mMonthView.scrollToSection(section);
                if (mPresenter.getViewType() == ViewType.DAY) {
                    SwitchViewAnimHelper.getInstance().toSmallView(mMonthView.getRootView());
                } else if (mPresenter.getViewType() == ViewType.YEAR) {
                    SwitchViewAnimHelper.getInstance().toLargeView(mMonthView.getRootView());
                }

                // load = mMonthView.isEmpty();
                break;
            case DAY:
                mContainer.addView(mDayView.getRootView());
                SwitchViewAnimHelper.getInstance().toLargeView(mDayView.getRootView());
                // load = mDayView.isEmpty();

                break;
            /*case COLLAGE:
                mContainer.addView(mCollageView.getRootView());
                SwitchViewAnimHelper.getInstance().toLargeView(mCollageView.getRootView());*/

                // load = mYearView.isEmpty();
//                break;
            case  LIST:
                mContainer.addView(myListView.getRootView());
                break;
        }
        mPresenter.setViewType(viewType);
        if (load) {
            mPresenter.loadPhotos();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//横屏
            DayView.CLUMN_COUNT = 6;
            MonthView.CLUMN_COUNT = 10;
            State.orientation=2;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//竖屏
            State.orientation=1;
            DayView.CLUMN_COUNT = 4;
            MonthView.CLUMN_COUNT = 6;
        }
        super.onConfigurationChanged(newConfig);
    }

}
