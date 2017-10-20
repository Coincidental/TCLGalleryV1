package com.gallery.tclgallery.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.CameraItem;
import com.gallery.tclgallery.utils.RGlideUtil;
import com.gallery.tclgallery.utils.RolloutBDInfo;
import com.gallery.tclgallery.utils.UIUtils;
import com.gallery.tclgallery.viewholder.RolloutViewPager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class RolloutPreviewActivity extends RolloutBaseActivity implements ViewPager.OnPageChangeListener {
    public static RolloutPreviewActivity mRolloutPreviewActivity;
    private int index = 0;
    private int selectIndex = -1;

    private FrameLayout main_show_view;
    private LinearLayout btn_edit;
    private Button btn_Fiters, btn_Frame, btn_UcropAndRotate, btn_adjust;
    private ImageButton btn_Draw;
    private boolean visible = false;
    private Toolbar toolbar;
    private ViewPager viewpager;
    private SamplePagerAdapter pagerAdapter;

    private ArrayList<CameraItem> ImgList;

    private float moveheight;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRolloutPreviewActivity=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_rollout_preview);

        findID();
        setupToolbar();
        Listener();
        InData();
        getValue();

    }

    @Override
    public void findID() {
        super.findID();
        viewpager = (RolloutViewPager) findViewById(R.id.bi_viewpager);
        main_show_view = (FrameLayout) findViewById(R.id.main_show_view);
        btn_edit = (LinearLayout) findViewById(R.id.btn_edit);
        btn_adjust = (Button) findViewById(R.id.btn_adjust);
        btn_Draw = (ImageButton) findViewById(R.id.btn_Draw);
        btn_Fiters = (Button) findViewById(R.id.btn_Fiters);
        btn_Frame = (Button) findViewById(R.id.btn_Frame);
        btn_UcropAndRotate = (Button) findViewById(R.id.btn_UcropAndRotate);
    }

    /**
     * 初始化Toolbar
     */
    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        toolbar.bringToFront();
        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);
       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setVisibility(View.GONE);
                showimg.setVisibility(View.VISIBLE);
                if (selectIndex != -1) {
                    RGlideUtil.setImage(RolloutPreviewActivity.this, ImgList.get(selectIndex).getPath(), showimg);
                }
                setShowimage();
            }
        });*/

    }

    @Override
    public void Listener() {
        super.Listener();
        viewpager.setOnPageChangeListener(this);
    }

    @Override
    public void InData() {
        super.InData();

        index = getIntent().getIntExtra("index", 0);
        type = getIntent().getIntExtra("type", 0);
        ImgList = (ArrayList<CameraItem>) getIntent().getSerializableExtra("data");

        Log.e("1", ImgList.size() + "数量");

        imageInfo = ImgList.get(index);
        bdInfo = (RolloutBDInfo) getIntent().getSerializableExtra("bdinfo");

        pagerAdapter = new SamplePagerAdapter();
        viewpager.setAdapter(pagerAdapter);
        viewpager.setCurrentItem(index);

        if (type == 1) {
            moveheight = UIUtils.dip2px(70);
        } else if (type == 2) {
            moveheight = (Width - 4 * UIUtils.dip2px(2)) / 4;
        } else if (type == 3) {
            moveheight = (Width - UIUtils.dip2px(80) - UIUtils.dip2px(2)) / 4;
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        if (showimg == null) {
            return;
        }
        CameraItem info = ImgList.get(arg0);
        //单张
        if (type == 0) {
            RGlideUtil.setImage(RolloutPreviewActivity.this, info.getPath(), showimg);
        } else if (type == 1) {//listview
            selectIndex = arg0;
            int move_index = arg0 - index;
            to_y = move_index * moveheight;
        } else if (type == 2) {//gridview，计算图片原始的位置，某行某列
            selectIndex = arg0;
            int a = index / 4;
            int b = index % 4;
            int a1 = arg0 / 4;
            int b1 = arg0 % 4;
            to_y = (a1 - a) * moveheight + (a1 - a) * UIUtils.dip2px(2);
            to_x = (b1 - b) * moveheight + (b1 - b) * UIUtils.dip2px(2);
        } else if (type == 3) {//类似与朋友圈
            selectIndex = arg0;
            int a = index / 4;
            int b = index % 4;
            int a1 = arg0 / 4;
            int b1 = arg0 % 4;
            to_y = (a1 - a) * moveheight + (a1 - a) * UIUtils.dip2px(1);
            to_x = (b1 - b) * moveheight + (b1 - b) * UIUtils.dip2px(1);
        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ImgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String path = ImgList.get(position).getPath();
            Glide.with(RolloutPreviewActivity.this).load(path).into(photoView);
            // Now just add PhotoView to ViewPager and return it
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {

                    if (!visible) {
                        btn_edit.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                        visible = true;
                    } else {
                        btn_edit.setVisibility(View.GONE);
                        toolbar.setVisibility(View.GONE);
                        visible = false;
                    }
                }
            });

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewpager.setVisibility(View.GONE);
            showimg.setVisibility(View.VISIBLE);
            setShowimage();
        }
        return true;
    }

    @Override
    protected void EndSoring() {
        super.EndSoring();
        viewpager.setVisibility(View.VISIBLE);
        showimg.setVisibility(View.GONE);
    }

    @Override
    protected void EndMove() {
        super.EndMove();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (selectIndex != -1) {
            selectIndex = -1;
        }
        RGlideUtil.clearMemory(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_preview, menu);
        return true;
    }
public void  closeActivity(){
   /* EndMove();
    onDestroy();*/
    viewpager.setVisibility(View.GONE);
    showimg.setVisibility(View.VISIBLE);
    if (selectIndex != -1) {
        RGlideUtil.setImage(RolloutPreviewActivity.this, ImgList.get(selectIndex).getPath(), showimg);
    }
    setShowimage();
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewpager.setVisibility(View.GONE);
                showimg.setVisibility(View.VISIBLE);
                if (selectIndex != -1) {
                    RGlideUtil.setImage(RolloutPreviewActivity.this, ImgList.get(selectIndex).getPath(), showimg);
                }
                setShowimage();
                break;
            case R.id.menu_Slideshow:break;
            case R.id.menu_Set_as:break;
            case R.id.menu_Details:break;
            case R.id.menu_Print:break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
