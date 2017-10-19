package com.gallery.tclgallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gallery.tclgallery.R;
import com.gallery.tclgallery.bean.AlbumTag;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/19.
 */

public class AlbumOtherAdapter extends BaseAdapter {

    private ArrayList<AlbumTag> arrayList;
    private LayoutInflater inflater;
    private Context mContext;
    private boolean isAlbumSelected = false;

    public AlbumOtherAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
    }

    @Override
    public long getItemId(int i) {
        return i;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        AlbumOtherAdapter.Holder holder;
        if (view == null) {
            holder = new AlbumOtherAdapter.Holder();
            view = inflater.inflate(R.layout.album_adapter_item, viewGroup, false);
            holder.image = (ImageView)view.findViewById(R.id.image);
            holder.selectImage = (ImageView)view.findViewById(R.id.album_selected);
            holder.name = (TextView)view.findViewById(R.id.album_name);
            holder.count = (TextView)view.findViewById(R.id.album_image_count);
            view.setTag(holder);
        } else {
            holder = (AlbumOtherAdapter.Holder) view.getTag();
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
        if (arrayList.get(i).getItem_count() > 0) {
            Glide.with(mContext)
                    .load("file://" + arrayList.get(i).getMediaBeans().get(0).getLocal_path())
                    .asBitmap()
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

    public ArrayList<AlbumTag> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<AlbumTag> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }

    public boolean isAlbumSelected() {
        return isAlbumSelected;
    }

    public void setAlbumSelected(boolean albumSelected) {
        isAlbumSelected = albumSelected;
    }
}
