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
import com.gallery.tclgallery.ui.adapter.AlbumOtherAdapter;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/19.
 */

public class AlbumOtherActivity extends AppCompatActivity{
    private ArrayList<AlbumTag> invisibleAlbums;
    private GridView gridView;
    private Toolbar other_toolbar;
    private AlbumOtherAdapter adapter;
    private Context mContext;
    private boolean folderNoChecked = true;
    private int folderCheckedCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_album_other);
        invisibleAlbums = new ArrayList<>();
        Intent intent = getIntent();
        ArrayList<AlbumTag> arrayList = intent.getParcelableArrayListExtra("invisible_album");
        invisibleAlbums.addAll(arrayList);
        other_toolbar = (Toolbar)findViewById(R.id.other_toolbar);
        gridView = (GridView) findViewById(R.id.other_grid);
        mContext = getApplicationContext();
        initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initGridview();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        other_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        other_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumOtherActivity.this.finish();
            }
        });
        other_toolbar.setTitle(getString(R.string.album_other_toolbar_title));
        setSupportActionBar(other_toolbar);
    }

    /**
     * 初始化gridView
     */
    private void initGridview() {
        adapter = new AlbumOtherAdapter(mContext);
        gridView.setAdapter(adapter);
        adapter.setArrayList(invisibleAlbums);
        adapter.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!adapter.isAlbumSelected()) {
                    if (invisibleAlbums.size() != 0) {
                        invisibleAlbums.get(i).setChecked(true);
                        adapter.setAlbumSelected(true);
                        folderCheckedCount = 1;
                        other_toolbar.setTitle(folderCheckedCount + "");
                        other_toolbar.setNavigationIcon(R.drawable.ic_close_24px);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        other_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adapter.setAlbumSelected(false);
                                for (AlbumTag album:invisibleAlbums) {
                                    album.setChecked(false);
                                }
                                adapter.notifyDataSetChanged();
                                other_toolbar.setTitle(getString(R.string.default_album_others));
                                other_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                                other_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlbumOtherActivity.this.finish();
                                    }
                                });
                                invalidateOptionsMenu();
                            }
                        });
                        invalidateOptionsMenu();
                        adapter.notifyDataSetChanged();
                    }
                }
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.isAlbumSelected()) {
                    boolean isSelected = invisibleAlbums.get(i).isChecked();
                    invisibleAlbums.get(i).setChecked(!isSelected);
                    adapter.notifyDataSetChanged();
                    checkAlbumFoldersNoChecked();
                    if (folderNoChecked) {
                        other_toolbar.setTitle(getString(R.string.album_other_toolbar_select_albums_title));
                        invalidateOptionsMenu();
                    } else {
                        other_toolbar.setTitle(folderCheckedCount + "");
                        invalidateOptionsMenu();
                    }
                } else {
                    Intent intent = new Intent(AlbumOtherActivity.this, AlbumPhotoActivity.class);
                    intent.putExtra("albumTag", invisibleAlbums.get(i));
                    startActivity(intent);
                }
            }
        });
    }

    private void checkAlbumFoldersNoChecked () {
        folderCheckedCount = 0;
        for (AlbumTag album : invisibleAlbums){
            if (album.isChecked()) {
                folderCheckedCount++;
            }
        }
        folderNoChecked = folderCheckedCount > 0? false:true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(adapter.isAlbumSelected()){
            if(folderNoChecked) {
                menu.findItem(R.id.menu_unarchive_album).setVisible(false);
                menu.findItem(R.id.menu_delete_other_album).setVisible(false);
                menu.findItem(R.id.menu_select_other_album).setVisible(false);
            } else {
                menu.findItem(R.id.menu_unarchive_album).setVisible(true);
                menu.findItem(R.id.menu_delete_other_album).setVisible(true);
                menu.findItem(R.id.menu_select_other_album).setVisible(false);
            }
        } else {
            menu.findItem(R.id.menu_unarchive_album).setVisible(false);
            menu.findItem(R.id.menu_delete_other_album).setVisible(false);
            menu.findItem(R.id.menu_select_other_album).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.other_album_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_unarchive_album:
                // 移除other相册
                return true;
            case R.id.menu_select_other_album:
                adapter.setAlbumSelected(true);
                other_toolbar.setTitle(getString(R.string.album_other_toolbar_select_albums_title));
                other_toolbar.setNavigationIcon(R.drawable.ic_close_24px);
                other_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.setAlbumSelected(false);
                        for (AlbumTag album:invisibleAlbums) {
                            album.setChecked(false);
                        }
                        adapter.notifyDataSetChanged();
                        other_toolbar.setTitle(getString(R.string.default_album_others));
                        other_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                        other_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlbumOtherActivity.this.finish();
                            }
                        });
                        invalidateOptionsMenu();
                    }
                });
                invalidateOptionsMenu();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.menu_delete_other_album:
                // 删除相册
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
