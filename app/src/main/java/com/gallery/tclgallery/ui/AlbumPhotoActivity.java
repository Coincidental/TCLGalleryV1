package com.gallery.tclgallery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumTag;
import com.gallery.tclgallery.bean.LocalMediaBean;
import com.gallery.tclgallery.ui.adapter.AlbumPhotoAdapter;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/18.
 */

public class AlbumPhotoActivity extends AppCompatActivity {
    public static final String TAG = "AlbumPhotoActivity";

    private Toolbar albumPhotoToolBar;
    private AlbumTag album;
    private ArrayList<LocalMediaBean> localMedia;
    private GridView gridView;
    private AlbumPhotoAdapter photoAdapter;
    private Context mContext;
    private int photoCheckedCount;
    private boolean photoNoChecked = true;
    private boolean photoCheckedAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photo);

        mContext = getApplicationContext();
        Intent intent = getIntent();
        album = intent.getParcelableExtra("albumTag");
        localMedia = new ArrayList<>();
        if (album.getItem_count()>0){
            localMedia.addAll(album.getMediaBeans());
        }
        albumPhotoToolBar = (Toolbar)findViewById(R.id.album_photo_toolbar);
        gridView = (GridView)findViewById(R.id.album_photo_gridView);
        initToolBar();
        initGridView();
    }

    private void initToolBar(){
        albumPhotoToolBar.setTitle(album.getName());
        albumPhotoToolBar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_black_24dp));
        albumPhotoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumPhotoActivity.this.finish();
            }
        });
        setSupportActionBar(albumPhotoToolBar);
    }

    private void initGridView() {
        photoAdapter = new AlbumPhotoAdapter(mContext);
        photoAdapter.setLocalMediaBeen(localMedia);
        gridView.setAdapter(photoAdapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!photoAdapter.isAlbumSelected()) {
                    if (localMedia.size() != 0) {
                        localMedia.get(i).setChecked(true);
                        photoAdapter.setAlbumSelected(true);
                        photoCheckedCount = 1;
                        albumPhotoToolBar.setTitle(photoCheckedCount + "");
                        albumPhotoToolBar.setNavigationIcon(R.drawable.ic_close_24px);
                        albumPhotoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                photoAdapter.setAlbumSelected(false);
                                for (LocalMediaBean mediaBean : localMedia) {
                                    mediaBean.setChecked(false);
                                }
                                photoAdapter.notifyDataSetChanged();
                                albumPhotoToolBar.setTitle(album.getName());
                                albumPhotoToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                                albumPhotoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlbumPhotoActivity.this.finish();
                                    }
                                });
                                invalidateOptionsMenu();
                            }
                        });
                        invalidateOptionsMenu();
                        photoAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (photoAdapter.isAlbumSelected()) {
                    //界面是选择状态
                    boolean isSelected = localMedia.get(i).isChecked();
                    localMedia.get(i).setChecked(!isSelected);
                    photoAdapter.notifyDataSetChanged();
                    checkPhotosNoChecked();
                    if (photoNoChecked) {
                        albumPhotoToolBar.setTitle(getString(R.string.album_photo_no_selected_toolbar_title));
                    } else {
                        albumPhotoToolBar.setTitle(photoCheckedCount + "");
                    }
                    if (photoCheckedCount == photoAdapter.getCount()) {
                        photoCheckedAll = true;
                    } else {
                        photoCheckedAll = false;
                    }
                    invalidateOptionsMenu();
                } else {
                    // 进入画廊
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.album_photo_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(photoAdapter.isAlbumSelected()){
            if(photoNoChecked) {
                // 没有选中
                menu.findItem(R.id.album_menu_photo_share).setVisible(false);
                menu.findItem(R.id.album_menu_photo_delete).setVisible(false);
                menu.findItem(R.id.album_menu_select_items).setVisible(false);
                menu.findItem(R.id.album_menu_slide_show).setVisible(false);
                menu.findItem(R.id.album_menu_rename_album).setVisible(false);
                menu.findItem(R.id.album_menu_delete_album).setVisible(false);
                menu.findItem(R.id.album_menu_select_all).setVisible(true);
                menu.findItem(R.id.album_menu_unselect_all).setVisible(false);
                menu.findItem(R.id.album_menu_copy_to_album).setVisible(false);
                menu.findItem(R.id.album_menu_move_to_album).setVisible(false);
                menu.findItem(R.id.album_menu_set_as_private).setVisible(false);
            } else {
                if (photoCheckedAll) {
                    // 全选中
                    menu.findItem(R.id.album_menu_photo_share).setVisible(true);
                    menu.findItem(R.id.album_menu_photo_delete).setVisible(true);
                    menu.findItem(R.id.album_menu_select_items).setVisible(false);
                    menu.findItem(R.id.album_menu_slide_show).setVisible(false);
                    menu.findItem(R.id.album_menu_rename_album).setVisible(false);
                    menu.findItem(R.id.album_menu_delete_album).setVisible(false);
                    menu.findItem(R.id.album_menu_select_all).setVisible(false);
                    menu.findItem(R.id.album_menu_unselect_all).setVisible(true);
                    menu.findItem(R.id.album_menu_copy_to_album).setVisible(true);
                    menu.findItem(R.id.album_menu_move_to_album).setVisible(true);
                    menu.findItem(R.id.album_menu_set_as_private).setVisible(true);
                } else {
                    // 有选中，非全选中
                    menu.findItem(R.id.album_menu_photo_share).setVisible(true);
                    menu.findItem(R.id.album_menu_photo_delete).setVisible(true);
                    menu.findItem(R.id.album_menu_select_items).setVisible(false);
                    menu.findItem(R.id.album_menu_slide_show).setVisible(false);
                    menu.findItem(R.id.album_menu_rename_album).setVisible(false);
                    menu.findItem(R.id.album_menu_delete_album).setVisible(false);
                    menu.findItem(R.id.album_menu_select_all).setVisible(true);
                    menu.findItem(R.id.album_menu_unselect_all).setVisible(false);
                    menu.findItem(R.id.album_menu_copy_to_album).setVisible(true);
                    menu.findItem(R.id.album_menu_move_to_album).setVisible(true);
                    menu.findItem(R.id.album_menu_set_as_private).setVisible(true);
                }
            }
        } else {
            // 非选择界面
            menu.findItem(R.id.album_menu_photo_share).setVisible(false);
            menu.findItem(R.id.album_menu_photo_delete).setVisible(false);
            menu.findItem(R.id.album_menu_select_items).setVisible(true);
            menu.findItem(R.id.album_menu_slide_show).setVisible(true);
            menu.findItem(R.id.album_menu_rename_album).setVisible(true);
            menu.findItem(R.id.album_menu_delete_album).setVisible(true);
            menu.findItem(R.id.album_menu_select_all).setVisible(false);
            menu.findItem(R.id.album_menu_unselect_all).setVisible(false);
            menu.findItem(R.id.album_menu_copy_to_album).setVisible(false);
            menu.findItem(R.id.album_menu_move_to_album).setVisible(false);
            menu.findItem(R.id.album_menu_set_as_private).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void checkPhotosNoChecked () {
        photoCheckedCount = 0;
        for (LocalMediaBean mediaBean : localMedia){
            if (mediaBean.isChecked()) {
                photoCheckedCount++;
            }
        }
        photoNoChecked = photoCheckedCount > 0? false:true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.album_menu_select_items:
                // 进入选择界面
                photoAdapter.setAlbumSelected(true);
                photoCheckedAll = false;
                photoCheckedCount = 0;
                photoNoChecked = true;
                albumPhotoToolBar.setTitle(getString(R.string.album_toolbar_select_item));
                photoAdapter.notifyDataSetChanged();
                albumPhotoToolBar.setNavigationIcon(R.drawable.ic_close_24px);
                albumPhotoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoAdapter.setAlbumSelected(false);
                        for (LocalMediaBean mediaBean : localMedia) {
                            mediaBean.setChecked(false);
                        }
                        photoAdapter.notifyDataSetChanged();
                        albumPhotoToolBar.setTitle(album.getName());
                        albumPhotoToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                        albumPhotoToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlbumPhotoActivity.this.finish();
                            }
                        });
                        invalidateOptionsMenu();
                    }
                });
                invalidateOptionsMenu();
                return true;
            case R.id.album_menu_slide_show:
                // 幻灯片播放相册
                return true;
            case R.id.album_menu_rename_album:
                // 重命名相册
                return true;
            case R.id.album_menu_delete_album:
                // 删除相册
                return true;
            case R.id.album_menu_select_all:
                // 全选
                photoCheckedCount = photoAdapter.getCount();
                for (LocalMediaBean mediaBean:localMedia){
                    mediaBean.setChecked(true);
                }
                photoAdapter.notifyDataSetChanged();
                albumPhotoToolBar.setTitle(photoCheckedCount + "");
                photoNoChecked = false;
                photoCheckedAll = true;
                invalidateOptionsMenu();
                return true;
            case R.id.album_menu_unselect_all:
                // 全不选
                photoCheckedCount = photoAdapter.getCount();
                for (LocalMediaBean mediaBean:localMedia){
                    mediaBean.setChecked(false);
                }
                photoAdapter.notifyDataSetChanged();
                albumPhotoToolBar.setTitle(getString(R.string.album_toolbar_select_item));
                photoNoChecked = true;
                photoCheckedAll = false;
                invalidateOptionsMenu();
                return true;
            case R.id.album_menu_copy_to_album:
                // 复制到相册
                return true;
            case R.id.album_menu_move_to_album:
                // 移动到相册
                return true;
            case R.id.album_menu_set_as_private:
                // 设置为私有
                return true;
            case R.id.album_menu_photo_share:
                // 图片分享
                return true;
            case R.id.album_menu_photo_delete:
                // 图片删除
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
