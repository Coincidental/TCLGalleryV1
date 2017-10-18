package com.gallery.tclgallery.ui;

import android.Manifest;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bottomnavbar.BottomNavigationBar;
import com.gallery.tclgallery.bottomnavbar.BottomNavigationItem;
import com.gallery.tclgallery.ui.adapter.GalleryBaseViewAdapter;
import com.gallery.tclgallery.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liddo on 2017/9/27.
 */

public class GalleryBaseActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private BottomNavigationBar bottomNavigationBar;
    private ViewPager viewPager;

    private Context mContext;
    private LocalActivityManager localActivityManager;
    private GalleryBaseViewAdapter galleryBaseViewAdapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_base_activity);

        localActivityManager = new LocalActivityManager(this, true);
        localActivityManager.dispatchCreate(savedInstanceState);
        mContext = getApplicationContext();
        initBottomNavBar();
    }

    @Override
    protected void onResume() {
        if (checkAndRequestForGallery() == 0) {
            initViewPager();
        } else {
            // no read permission
            if (!PermissionUtils.hasStorageReadPermission(mContext)) {
                ActivityCompat.requestPermissions(GalleryBaseActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }

            // no write permission
            if (!PermissionUtils.hasStorageWritePermission(mContext)) {
                ActivityCompat.requestPermissions(GalleryBaseActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        super.onResume();
    }

    /**
     * 初始化BottomNavBar
     */
    private void initBottomNavBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.gallery_bottom_nav_bar);
        BottomNavigationItem navigationItem1 = new BottomNavigationItem(R.drawable.ic_moments_24px_blue, getString(R.string.tab_moments))
                .setInactiveIconResource(R.drawable.ic_moments_24px_gray).setActiveColorResource(R.color.nav_bar_active_color);
        BottomNavigationItem navigationItem2 = new BottomNavigationItem(R.drawable.ic_album_24px_blue, getString(R.string.tab_albums))
                .setInactiveIconResource(R.drawable.ic_album_24px_gray).setActiveColorResource(R.color.nav_bar_active_color);
        BottomNavigationItem navigationItem3 = new BottomNavigationItem(R.drawable.ic_create_24px_blue, getString(R.string.tab_create))
                .setInactiveIconResource(R.drawable.ic_create_24px_gray).setActiveColorResource(R.color.nav_bar_active_color);
        bottomNavigationBar.addItem(navigationItem1)
                .addItem(navigationItem2)
                .addItem(navigationItem3)
                .initialise();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void initViewPager(){
        viewPager = (ViewPager)findViewById(R.id.gallery_base_viewpager);
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationBar.selectTab(0);
                        break;
                    case 1:
                        bottomNavigationBar.selectTab(1);
                        break;
                    case 2:
                        bottomNavigationBar.selectTab(2);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        addActivitiesToViewPager();
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(onPageChangeListener);
    }

    /**
     *  向ViewPager添加Activity
     */
    private void addActivitiesToViewPager() {
        List<View> views = new ArrayList<>();
        Intent intent = new Intent();

        intent.setClass(this, GalleryHomeActivity.class);
        intent.putExtra("view_id",1);
        views.add(getView("Moments", intent));

        intent.setClass(this, GalleryHomeActivity.class);
        intent.putExtra("view_id",2);
        views.add(getView("Albums", intent));

        intent.setClass(this, MainActivity.class);
        intent.putExtra("view_id",3);
        views.add(getView("Create", intent));

        galleryBaseViewAdapter = new GalleryBaseViewAdapter(views);
        viewPager.setAdapter(galleryBaseViewAdapter);
    }

    /**
     * 通过activity获取视图
     *
     * @param id
     * @param intent
     * @return
     */
    private View getView(String id, Intent intent) {
        return localActivityManager.startActivity(id, intent).getDecorView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    if (checkAndRequestForGallery() == 0) {
                        initViewPager();
                    } else {
                        ActivityCompat.requestPermissions(GalleryBaseActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    Toast.makeText(mContext,R.string.denied_required_permission, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    if (checkAndRequestForGallery() == 0) {
                        initBottomNavBar();
                        initViewPager();
                    } else {
                        ActivityCompat.requestPermissions(GalleryBaseActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                } else {
                    Toast.makeText(mContext,R.string.denied_required_permission, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public int checkAndRequestForGallery() {
        // get permissions needed in current scenario
        ArrayList<String> permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // check status of permissions, get which permissions need to request
        ArrayList<String> permissionsNeedRequest = new ArrayList<String>();
        for (String permission : permissionsNeeded) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            permissionsNeedRequest.add(permission);
        }
        return permissionsNeedRequest.size();
    }
}
