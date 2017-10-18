package com.gallery.tclgallery.ui;

import android.content.Context;
import android.os.Bundle;
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
import com.gallery.tclgallery.controller.AlbumController;
import com.gallery.tclgallery.ui.adapter.AlbumFolderAdapter;

import java.util.ArrayList;


/**
 * Created by liddo on 2017/9/16.
 */

public class GalleryHomeActivity extends AppCompatActivity {

    private final static String TAG = "GalleryHomeActivity";
    private Toolbar toolbar;
    private GridView albumGridv;
    private ArrayList<AlbumTag> albumFolders;
    private ArrayList<AlbumTag> invisibleAlbums;
    private ArrayList<AlbumTag> visibleAlbums;
    private ArrayList<AlbumTag> showAlbums;
    private AlbumFolderAdapter albumFolderAdapter;
    private boolean folderNoChecked = true;
    private int folderCheckedCount = 0;

    private AlbumController albumController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.gallery_toolbar);
        mContext = getApplicationContext();
        albumController = new AlbumController(mContext);
        // 初始化Toolbar
        initToolBar();
        setSupportActionBar(toolbar);
        // 初始化AlbumGridView
        initAlbumGridv();
        // 初始化相册数据
        loadAlbumFolderDate();
    }

    private void initToolBar(){
        toolbar.setTitle("Album");
        toolbar.setNavigationIcon(null);
    }

    private void initAlbumGridv() {
        albumGridv = (GridView)findViewById(R.id.album_gridview);
        albumFolderAdapter = new AlbumFolderAdapter(this);
        albumGridv.setAdapter(albumFolderAdapter);
        albumGridv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!albumFolderAdapter.isAlbumSelected()) {
                    if (showAlbums.size() != 0) {
                        showAlbums.get(i).setChecked(true);
                        albumFolderAdapter.setAlbumSelected(true);
                        folderCheckedCount = 1;
                        toolbar.setTitle(folderCheckedCount + "");
                        toolbar.setNavigationIcon(R.drawable.ic_close_24px);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                albumFolderAdapter.setAlbumSelected(false);
                                for (AlbumTag album:showAlbums) {
                                    album.setChecked(false);
                                }
                                albumFolderAdapter.notifyDataSetChanged();
                                toolbar.setTitle("Album");
                                toolbar.setNavigationIcon(null);
                                invalidateOptionsMenu();
                            }
                        });
                        invalidateOptionsMenu();
                        albumFolderAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });
        albumGridv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (albumFolderAdapter.isAlbumSelected()){
                    boolean isSelected = showAlbums.get(i).isChecked();
                    showAlbums.get(i).setChecked(!isSelected);
                    albumFolderAdapter.notifyDataSetChanged();
                    checkAlbumFoldersNoChecked();
                    if (folderNoChecked){
                        toolbar.setTitle("");
                        invalidateOptionsMenu();
                    } else {
                        toolbar.setTitle(folderCheckedCount+"");
                        invalidateOptionsMenu();
                    }
                }
            }
        });
    }

    private void checkAlbumFoldersNoChecked () {
        folderCheckedCount = 0;
        for (AlbumTag album : showAlbums){
            if (album.isChecked()) {
                folderCheckedCount++;
            }
        }
        folderNoChecked = folderCheckedCount > 0? false:true;
    }

    private void loadAlbumFolderDate() {
        albumFolders = new ArrayList<>();
        visibleAlbums = new ArrayList<>();
        invisibleAlbums = new ArrayList<>();
        showAlbums = new ArrayList<>();
        // 所有相册
        albumFolders.addAll(albumController.getAlbum());
        for(AlbumTag album:albumFolders) {
            ArrayList<LocalMediaBean> localMediaBeen = albumController.getLocalMediaByAlbum(album);
            album.setMediaBeans(localMediaBeen);
            if (album.getVisible()==1) {
                // 可见相册
                visibleAlbums.add(album);
            } else {
                // 不可见相册 other
                invisibleAlbums.add(album);
            }
        }
        showAlbums.addAll(showAlbumList(visibleAlbums,invisibleAlbums));
        albumFolderAdapter.setArrayList(showAlbums);
        albumFolderAdapter.notifyDataSetChanged();
    }

    private ArrayList<AlbumTag> showAlbumList(ArrayList<AlbumTag> visibleAlbums,ArrayList<AlbumTag> invisibleAlbums){
        ArrayList<AlbumTag> showAlbums = new ArrayList<>();
        for (AlbumTag folder: visibleAlbums) {
            if (folder.isChecked()) {
                Log.i("dongdong",folder.getName() + "is selected");
                albumFolderAdapter.setAlbumSelected(true);
            }
        }
        showAlbums.addAll(visibleAlbums);
        AlbumTag othersAlbum = new AlbumTag();
        othersAlbum.setName(mContext.getString(R.string.default_album_others));
        othersAlbum.setDisplay_name(mContext.getString(R.string.default_album_others));
        othersAlbum.setItem_count(invisibleAlbums.size());
        showAlbums.add(othersAlbum);
        return showAlbums;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(albumFolderAdapter.isAlbumSelected()){
            if(folderNoChecked) {
                menu.findItem(R.id.menu_add_album).setVisible(false);
                menu.findItem(R.id.menu_select_album).setVisible(false);
                menu.findItem(R.id.menu_setting_album).setVisible(false);
                menu.findItem(R.id.menu_setting_album).setVisible(false);
                menu.findItem(R.id.menu_sort_album).setVisible(false);
                menu.findItem(R.id.menu_delete_album).setVisible(false);
                menu.findItem(R.id.menu_archive_album).setVisible(false);
            } else {
                menu.findItem(R.id.menu_add_album).setVisible(false);
                menu.findItem(R.id.menu_select_album).setVisible(false);
                menu.findItem(R.id.menu_setting_album).setVisible(false);
                menu.findItem(R.id.menu_setting_album).setVisible(false);
                menu.findItem(R.id.menu_sort_album).setVisible(false);
                menu.findItem(R.id.menu_delete_album).setVisible(true);
                menu.findItem(R.id.menu_archive_album).setVisible(true);
            }
        } else {
            menu.findItem(R.id.menu_add_album).setVisible(true);
            menu.findItem(R.id.menu_select_album).setVisible(true);
            menu.findItem(R.id.menu_setting_album).setVisible(true);
            menu.findItem(R.id.menu_setting_album).setVisible(true);
            menu.findItem(R.id.menu_sort_album).setVisible(true);
            menu.findItem(R.id.menu_delete_album).setVisible(false);
            menu.findItem(R.id.menu_archive_album).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_album:
                Log.i(TAG,"menu add album");
                return true;
            case R.id.menu_select_album:
                Log.i(TAG,"menu select album");
                albumFolderAdapter.setAlbumSelected(true);
                toolbar.setTitle("");
                toolbar.setNavigationIcon(R.drawable.ic_close_24px);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        albumFolderAdapter.setAlbumSelected(false);
                        for (AlbumTag album:showAlbums) {
                            album.setChecked(false);
                        }
                        albumFolderAdapter.notifyDataSetChanged();
                        toolbar.setTitle("Album");
                        toolbar.setNavigationIcon(null);
                        invalidateOptionsMenu();
                    }
                });
                invalidateOptionsMenu();
                albumFolderAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_sort_album:
                Log.i(TAG,"menu sort album");
                return true;
            case R.id.menu_setting_album:
                Log.i(TAG,"menu album settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (albumFolderAdapter.isAlbumSelected()) {
            albumFolderAdapter.setAlbumSelected(false);
            for (AlbumTag album:showAlbums) {
                album.setChecked(false);
            }
            albumFolderAdapter.notifyDataSetChanged();
            toolbar.setTitle("Album");
            toolbar.setNavigationIcon(null);
            invalidateOptionsMenu();
            toolbar.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }
}
