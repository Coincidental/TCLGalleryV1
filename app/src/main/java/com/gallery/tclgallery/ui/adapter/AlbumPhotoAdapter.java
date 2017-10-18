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
import com.gallery.tclgallery.bean.LocalMediaBean;

import java.util.ArrayList;

/**
 * Created by liddo on 2017/10/18.
 */

public class AlbumPhotoAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<LocalMediaBean> localMediaBeen;
    private LayoutInflater inflater;
    private ArrayList<String> videoType;
    private boolean isAlbumSelected;

    public AlbumPhotoAdapter(Context context){
        mContext = context;
        localMediaBeen = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        videoType = new ArrayList<>();
        videoType.add("avi");
        videoType.add("rmvb");
        videoType.add("3gp");
        videoType.add("mp4");
    }

    @Override
    public int getCount() {
        return localMediaBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return localMediaBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.album_photo_item, viewGroup, false);
            holder.photo = (ImageView)view.findViewById(R.id.album_photo_image);
            holder.selectImage = (ImageView)view.findViewById(R.id.album_photo_selected);
            holder.videoIcon = (ImageView) view.findViewById(R.id.album_video_icon);
            holder.duration = (TextView)view.findViewById(R.id.video_duration);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (videoType.contains(localMediaBeen.get(i).getType())){
            holder.videoIcon.setVisibility(View.VISIBLE);
            holder.duration.setText("11:00");
        } else {
            holder.videoIcon.setVisibility(View.GONE);
            holder.duration.setText("");
        }

        if (isAlbumSelected){
            holder.selectImage.setVisibility(View.VISIBLE);
            if (localMediaBeen.get(i).isChecked()) {
                holder.selectImage.setImageLevel(1);
            } else {
                holder.selectImage.setImageLevel(0);
            }
        } else {
            holder.selectImage.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .load("file://" + localMediaBeen.get(i).getLocal_path())
                .thumbnail(0.1f)
                .into(holder.photo);
        return view;
    }

    class ViewHolder {
        ImageView photo;
        ImageView selectImage;
        ImageView videoIcon;
        TextView duration;
    }

    public ArrayList<LocalMediaBean> getLocalMediaBeen() {
        return localMediaBeen;
    }

    public void setLocalMediaBeen(ArrayList<LocalMediaBean> localMediaBeen) {
        this.localMediaBeen.clear();
        this.localMediaBeen.addAll(localMediaBeen);
    }

    public boolean isAlbumSelected() {
        return isAlbumSelected;
    }

    public void setAlbumSelected(boolean albumSelected) {
        isAlbumSelected = albumSelected;
    }
}
