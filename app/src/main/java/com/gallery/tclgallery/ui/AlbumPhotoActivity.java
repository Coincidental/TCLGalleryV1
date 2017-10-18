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

    private Toolbar albumPhotoToolBar;
    private AlbumTag album;
    private ArrayList<LocalMediaBean> localMedia;
    private GridView gridView;
    private AlbumPhotoAdapter photoAdapter;
    private Context mContext;
    private int photoCheckedCount;

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
        setSupportActionBar(albumPhotoToolBar);
    }

    private void initToolBar(){
        albumPhotoToolBar.setTitle(album.getName());
        albumPhotoToolBar.setNavigationIcon(getDrawable(R.drawable.ic_arrow_back_black_24dp));
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
                                for (LocalMediaBean mediaBean:localMedia) {
                                    mediaBean.setChecked(false);
                                }
                                photoAdapter.notifyDataSetChanged();
                                albumPhotoToolBar.setTitle(album.getName());
                                albumPhotoToolBar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.album_photo_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.album_menu_select_items:
                return true;
            case R.id.album_menu_slide_show:
                return true;
            case R.id.album_menu_rename_album:
                return true;
            case R.id.album_menu_delete_album:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
