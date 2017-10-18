package com.gallery.tclgallery.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumTag;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/9/22.
 */

public class AlbumFolderAdapter extends BaseAdapter{

    private final String TAG = "AlbumFolderAdapter";
    private ArrayList<AlbumTag> arrayList;
    private ArrayList<AlbumTag> visibleList;
    private ArrayList<AlbumTag> invisibleList;
    private AlbumTag othersAlbum;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean isAlbumSelected = false;
    private boolean isAlbumSelectedNull = false;

    public AlbumFolderAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        visibleList = new ArrayList<>();
        invisibleList = new ArrayList<>();
        arrayList = new ArrayList<>();
        othersAlbum = new AlbumTag();
        mContext = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
            return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view = inflater.inflate(R.layout.album_adapter_item, viewGroup, false);
            holder.image = (ImageView)view.findViewById(R.id.image);
            holder.selectImage = (ImageView)view.findViewById(R.id.album_selected);
            holder.name = (TextView)view.findViewById(R.id.album_name);
            holder.count = (TextView)view.findViewById(R.id.album_image_count);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        holder.name.setText(arrayList.get(i).getName());
        holder.count.setText(""+arrayList.get(i).getItem_count());
        if (isAlbumSelected){
            holder.selectImage.setVisibility(View.VISIBLE);
            if (arrayList.get(i).isChecked()) {
                holder.selectImage.setImageLevel(1);
            } else {
                holder.selectImage.setImageLevel(0);
            }
        } else {
            holder.selectImage.setVisibility(View.GONE);
        }
        if (arrayList.get(i).getItem_count() > 0 && i<getCount()-1 && arrayList.get(i).getMediaBeans().size()>0) {
            Glide.with(mContext)
                    .load("file://" + arrayList.get(i).getMediaBeans().get(0).getLocal_path())
                    .thumbnail(0.1f)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.album_default_loading_pic);
        }
        return view;
    }

    public class Holder {
        ImageView image;
        ImageView selectImage;
        TextView name;
        TextView count;
    }

    public void setArrayList(ArrayList<AlbumTag> arrayList) {
        this.arrayList.addAll(arrayList);
        for (AlbumTag folder: arrayList) {
            if (folder.isChecked()) {
                Log.i("dongdong",folder.getName() + "is selected");
                isAlbumSelected = true;
            }
        }
    }

    public boolean isAlbumSelected() {
        return isAlbumSelected;
    }

    public void setAlbumSelected(boolean albumSelected) {
        isAlbumSelected = albumSelected;
    }

    public boolean isAlbumSelectedNull() {
        return isAlbumSelectedNull;
    }

    public void setAlbumSelectedNull(boolean albumSelectedNull) {
        isAlbumSelectedNull = albumSelectedNull;
    }
}
